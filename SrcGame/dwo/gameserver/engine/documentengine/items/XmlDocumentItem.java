/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package dwo.gameserver.engine.documentengine.items;

import dwo.gameserver.engine.documentengine.XmlDocumentBase;
import dwo.gameserver.model.items.base.L2Item;
import dwo.gameserver.model.skills.base.conditions.Condition;
import dwo.gameserver.model.skills.stats.StatsSet;
import javolution.util.FastList;
import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author mkizub, JIV
 */
public class XmlDocumentItem extends XmlDocumentBase
{
	private Item _currentItem;
	private List<L2Item> _itemsInFile = new FastList<>();

	public XmlDocumentItem(File file)
	{
		super(file);
	}

	@Override
	public int getCurrentId()
	{
		return _currentItem.id;
	}

	@Override
	protected void parseDocument(Document doc)
	{
		for(Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if("list".equalsIgnoreCase(n.getNodeName()))
			{
				for(Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if("item".equalsIgnoreCase(d.getNodeName()))
					{
						try
						{
							_currentItem = new Item();
							parseItem(d);
							_itemsInFile.add(_currentItem.item);
							resetTable();
						}
						catch(Exception e)
						{
							_log.log(Level.ERROR, "Cannot create item " + _currentItem.id, e);
						}
					}
				}
			}
		}
	}

	@Override
	protected StatsSet getStatsSet()
	{
		return _currentItem.set;
	}

	@Override
	protected String getTableValue(String name)
	{
		return _tables.get(name)[_currentItem.currentLevel];
	}

	@Override
	protected String getTableValue(String name, int idx)
	{
		return _tables.get(name)[idx - 1];
	}

	protected void parseItem(Node n) throws InvocationTargetException
	{
		int itemId = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
		String className = n.getAttributes().getNamedItem("type").getNodeValue();
		String itemName = n.getAttributes().getNamedItem("name").getNodeValue();

		_currentItem.id = itemId;
		_currentItem.name = itemName;
		_currentItem.type = className;
		_currentItem.set = new StatsSet();
		_currentItem.set.set("item_id", itemId);
		_currentItem.set.set("name", itemName);

		Node first = n.getFirstChild();
		for(n = first; n != null; n = n.getNextSibling())
		{
			if("table".equalsIgnoreCase(n.getNodeName()))
			{
				if(_currentItem.item != null)
				{
					throw new IllegalStateException("Item created but table node found! Item " + itemId);
				}
				parseTable(n);
			}
			else if("set".equalsIgnoreCase(n.getNodeName()))
			{
				if(_currentItem.item != null)
				{
					throw new IllegalStateException("Item created but set node found! Item " + itemId);
				}
				parseBeanSet(n, _currentItem.set, 1);
			}
			else if("for".equalsIgnoreCase(n.getNodeName()))
			{
				makeItem();
				parseTemplate(n, _currentItem.item);
			}
			else if("cond".equalsIgnoreCase(n.getNodeName()))
			{
				makeItem();
				Condition condition = parseCondition(n.getFirstChild(), _currentItem.item);
				Node msg = n.getAttributes().getNamedItem("msg");
				Node msgId = n.getAttributes().getNamedItem("msgId");
				if(condition != null && msg != null)
				{
					condition.setMessage(msg.getNodeValue());
				}
				else if(condition != null && msgId != null)
				{
					condition.setMessageId(Integer.decode(getValue(msgId.getNodeValue(), null)));
					Node addName = n.getAttributes().getNamedItem("addName");
					if(addName != null && Integer.decode(getValue(msgId.getNodeValue(), null)) > 0)
					{
						condition.addName();
					}
				}
				_currentItem.item.attach(condition);
			}
		}
		//bah! in this point item doesn't have to be still created
		makeItem();
	}

	private void makeItem() throws InvocationTargetException
	{
		if(_currentItem.item != null)
		{
			return; // item is already created
		}
		try
		{
			Constructor<?> c = Class.forName("dwo.gameserver.model.items.base.L2" + _currentItem.type).getConstructor(StatsSet.class);
			_currentItem.item = (L2Item) c.newInstance(_currentItem.set);
		}
		catch(Exception e)
		{
			throw new InvocationTargetException(e);
		}
	}

	/**
	 * @return _itemsInFile
	 */
	public List<L2Item> getItemList()
	{
		return _itemsInFile;
	}

	private class Item
	{
		public int id;

		public String type;

		public String name;

		public StatsSet set;

		public int currentLevel;

		public L2Item item;
	}
}

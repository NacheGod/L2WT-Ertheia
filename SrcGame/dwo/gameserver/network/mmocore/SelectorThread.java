/* This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package dwo.gameserver.network.mmocore;

import javolution.util.FastList;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author KenM<BR>
 *         Parts of design based on networkcore from WoodenGil
 */
public class SelectorThread<T extends MMOClient<?>> extends Thread
{
	protected static final Logger _log = LogManager.getLogger(SelectorThread.class);

	// default BYTE_ORDER
	private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
	// default HEADER_SIZE
	private static final int HEADER_SIZE = 2;
	// Selector
	private final Selector _selector;
	// Implementations
	private final IPacketHandler<T> _packetHandler;
	private final IMMOExecutor<T> _executor;
	private final IClientFactory<T> _clientFactory;
	private final IAcceptFilter _acceptFilter;
	// Configurations
	private final int HELPER_BUFFER_SIZE;
	private final int HELPER_BUFFER_COUNT;
	private final int MAX_SEND_PER_PASS;
	private final int MAX_READ_PER_PASS;
	private final long SLEEP_TIME;
	// Main Buffers
	private final ByteBuffer DIRECT_WRITE_BUFFER;
	private final ByteBuffer WRITE_BUFFER;
	private final ByteBuffer READ_BUFFER;
	// String Buffer
	private final NioNetStringBuffer STRING_BUFFER;
	// ByteBuffers General Purpose Pool
	private final FastList<ByteBuffer> _bufferPool;
	// Pending Close
	private final NioNetStackList<MMOConnection<T>> _pendingClose;
	public boolean TCP_NODELAY;
	private boolean _shutdown;

	public SelectorThread(SelectorConfig sc, IMMOExecutor<T> executor, IPacketHandler<T> packetHandler, IClientFactory<T> clientFactory, IAcceptFilter acceptFilter) throws IOException
	{
		setName("SelectorThread-" + getId());

		HELPER_BUFFER_SIZE = sc.HELPER_BUFFER_SIZE;
		HELPER_BUFFER_COUNT = sc.HELPER_BUFFER_COUNT;
		MAX_SEND_PER_PASS = sc.MAX_SEND_PER_PASS;
		MAX_READ_PER_PASS = sc.MAX_READ_PER_PASS;

		SLEEP_TIME = sc.SLEEP_TIME;
		TCP_NODELAY = sc.TCP_NODELAY;

		DIRECT_WRITE_BUFFER = ByteBuffer.allocateDirect(sc.WRITE_BUFFER_SIZE).order(BYTE_ORDER);
		WRITE_BUFFER = ByteBuffer.wrap(new byte[sc.WRITE_BUFFER_SIZE]).order(BYTE_ORDER);
		READ_BUFFER = ByteBuffer.wrap(new byte[sc.READ_BUFFER_SIZE]).order(BYTE_ORDER);

		STRING_BUFFER = new NioNetStringBuffer(64 << 10);

		_pendingClose = new NioNetStackList<>();
		_bufferPool = new FastList<>();

		for(int i = 0; i < HELPER_BUFFER_COUNT; i++)
		{
			_bufferPool.addLast(ByteBuffer.wrap(new byte[HELPER_BUFFER_SIZE]).order(BYTE_ORDER));
		}

		_acceptFilter = acceptFilter;
		_packetHandler = packetHandler;
		_clientFactory = clientFactory;
		_executor = executor;
		_selector = Selector.open();
	}

	public void openServerSocket(InetAddress address, int tcpPort) throws IOException
	{
		ServerSocketChannel selectable = ServerSocketChannel.open();
		selectable.configureBlocking(false);

		ServerSocket ss = selectable.socket();

		if(address == null)
		{
			ss.bind(new InetSocketAddress(tcpPort));
		}
		else
		{
			ss.bind(new InetSocketAddress(address, tcpPort));
		}

		selectable.register(_selector, SelectionKey.OP_ACCEPT);
	}

	ByteBuffer getPooledBuffer()
	{
		if(_bufferPool.isEmpty())
		{
			return ByteBuffer.wrap(new byte[HELPER_BUFFER_SIZE]).order(BYTE_ORDER);
		}

		return _bufferPool.removeFirst();
	}

	void recycleBuffer(ByteBuffer buf)
	{
		if(_bufferPool.size() < HELPER_BUFFER_COUNT)
		{
			buf.clear();
			_bufferPool.addLast(buf);
		}
	}

	@Override
	public void run()
	{
		int selectedKeysCount = 0;

		SelectionKey key;
		MMOConnection<T> con;

		Iterator<SelectionKey> selectedKeys;

		while(!_shutdown)
		{
			try
			{
				selectedKeysCount = _selector.selectNow();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

			if(selectedKeysCount > 0)
			{
				selectedKeys = _selector.selectedKeys().iterator();

				while(selectedKeys.hasNext())
				{
					key = selectedKeys.next();
					selectedKeys.remove();

					con = (MMOConnection<T>) key.attachment();

					switch(key.readyOps())
					{
						case SelectionKey.OP_CONNECT:
							finishConnection(key, con);
							break;
						case SelectionKey.OP_ACCEPT:
							acceptConnection(key, con);
							break;
						case SelectionKey.OP_READ:
							readPacket(key, con);
							break;
						case SelectionKey.OP_WRITE:
							writePacket(key, con);
							break;
						case SelectionKey.OP_READ | SelectionKey.OP_WRITE:
							writePacket(key, con);
							if(key.isValid())
							{
								readPacket(key, con);
							}
							break;
					}
				}
			}

			synchronized(_pendingClose)
			{
				while(!_pendingClose.isEmpty())
				{
					con = _pendingClose.removeFirst();
					writeClosePacket(con);
					closeConnectionImpl(con.getSelectionKey(), con);
				}
			}

			try
			{
				Thread.sleep(SLEEP_TIME);
			}
			catch(InterruptedException e)
			{
				_log.log(Level.ERROR, "Error while running Selector Thread", e);
			}
		}
		closeSelectorThread();
	}

	private void finishConnection(SelectionKey key, MMOConnection<T> con)
	{
		try
		{
			((SocketChannel) key.channel()).finishConnect();
		}
		catch(IOException e)
		{
			con.getClient().onForcedDisconnection();
			closeConnectionImpl(key, con);
		}

		// key might have been invalidated on finishConnect()
		if(key.isValid())
		{
			key.interestOps(key.interestOps() | SelectionKey.OP_READ);
			key.interestOps(key.interestOps() & ~SelectionKey.OP_CONNECT);
		}
	}

	private void acceptConnection(SelectionKey key, MMOConnection<T> con)
	{
		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		SocketChannel sc;

		try
		{
			while((sc = ssc.accept()) != null)
			{
				if(_acceptFilter == null || _acceptFilter.accept(sc))
				{
					sc.configureBlocking(false);
					SelectionKey clientKey = sc.register(_selector, SelectionKey.OP_READ);
					con = new MMOConnection<>(this, sc.socket(), clientKey, TCP_NODELAY);
					con.setClient(_clientFactory.create(con));
					clientKey.attach(con);
				}
				else
				{
					sc.socket().close();
				}
			}
		}
		catch(IOException e)
		{
			_log.log(Level.ERROR, "Error while acceptConnection!", e);
		}
	}

	private void readPacket(SelectionKey key, MMOConnection<T> con)
	{
		if(!con.isClosed())
		{
			ByteBuffer buf;
			if((buf = con.getReadBuffer()) == null)
			{
				buf = READ_BUFFER;
			}

			// if we try to to do a read with no space in the buffer it will
			// read 0 bytes
			// going into infinite loop
			if(buf.position() == buf.limit())
			{
				System.exit(0);
			}

			int result = -2;

			try
			{
				result = con.read(buf);
			}
			catch(IOException e)
			{
				// error handling goes bellow
			}

			if(result > 0)
			{
				buf.flip();

				T client = con.getClient();

				for(int i = 0; i < MAX_READ_PER_PASS; i++)
				{
					if(!tryReadPacket(key, client, buf, con))
					{
						return;
					}
				}

				// only reachable if MAX_READ_PER_PASS has been reached
				// check if there are some more bytes in buffer
				// and allocate/compact to prevent content lose.
				if(buf.remaining() > 0)
				{
					// did we use the READ_BUFFER ?
					if(buf.equals(READ_BUFFER))
					{
						// move the pending byte to the connections READ_BUFFER
						allocateReadBuffer(con);
					}

					else
					{
						// move the first byte to the beginning :)
						buf.compact();
					}
				}
			}
			else
			{
				switch(result)
				{
					case 0:
					case -1:
						closeConnectionImpl(key, con);
						break;
					case -2:
						con.getClient().onForcedDisconnection();
						closeConnectionImpl(key, con);
						break;
				}
			}
		}
	}

	private boolean tryReadPacket(SelectionKey key, T client, ByteBuffer buf, MMOConnection<T> con)
	{
		switch(buf.remaining())
		{
			case 0:
				// buffer is full
				// nothing to read
				return false;
			case 1:
				// we don`t have enough data for header so we need to read
				key.interestOps(key.interestOps() | SelectionKey.OP_READ);

				// did we use the READ_BUFFER ?
				if(buf.equals(READ_BUFFER))
				{
					// move the pending byte to the connections READ_BUFFER
					allocateReadBuffer(con);
				}
				else
				{
					// move the first byte to the beginning :)
					buf.compact();
				}
				return false;
			default:
				// data size excluding header size :>
				int dataPending = (buf.getShort() & 0xFFFF) - HEADER_SIZE;

				// do we got enough bytes for the packet?
				if(dataPending <= buf.remaining())
				{
					// avoid parsing dummy packets (packets without body)
					if(dataPending > 0)
					{
						int pos = buf.position();
						parseClientPacket(pos, buf, dataPending, client);
						buf.position(pos + dataPending);
					}

					// if we are done with this buffer
					if(!buf.hasRemaining())
					{
						if(buf.equals(READ_BUFFER))
						{
							READ_BUFFER.clear();
						}
						else
						{
							con.setReadBuffer(null);
							recycleBuffer(buf);
						}
						return false;
					}
					return true;
				}
				else
				{
					// we don`t have enough bytes for the dataPacket so we need
					// to read
					key.interestOps(key.interestOps() | SelectionKey.OP_READ);

					// did we use the READ_BUFFER ?
					if(buf.equals(READ_BUFFER))
					{
						// move it`s position
						buf.position(buf.position() - HEADER_SIZE);
						// move the pending byte to the connections READ_BUFFER
						allocateReadBuffer(con);
					}
					else
					{
						buf.position(buf.position() - HEADER_SIZE);
						buf.compact();
					}
					return false;
				}
		}
	}

	private void allocateReadBuffer(MMOConnection<T> con)
	{
		con.setReadBuffer(getPooledBuffer().put(READ_BUFFER));
		READ_BUFFER.clear();
	}

	private void parseClientPacket(int pos, ByteBuffer buf, int dataSize, T client)
	{
		boolean ret = client.decrypt(buf, dataSize);

		if(ret && buf.hasRemaining())
		{
			// apply limit
			int limit = buf.limit();
			buf.limit(pos + dataSize);
			ReceivablePacket<T> cp = _packetHandler.handlePacket(buf, client);

			if(cp != null)
			{
				cp._buf = buf;
				cp._stringBuffer = STRING_BUFFER;
				cp._client = client;

				if(cp.read())
				{
					_executor.execute(cp);
				}

				cp._buf = null;
				cp._stringBuffer = null;
			}
			buf.limit(limit);
		}
	}

	private void writeClosePacket(MMOConnection<T> con)
	{
		SendablePacket<T> sp;
		synchronized(con.getSendQueue())
		{
			if(con.getSendQueue().isEmpty())
			{
				return;
			}

			while((sp = con.getSendQueue().removeFirst()) != null)
			{
				WRITE_BUFFER.clear();

				putPacketIntoWriteBuffer(con.getClient(), sp);

				WRITE_BUFFER.flip();

				try
				{
					con.write(WRITE_BUFFER);
				}
				catch(IOException e)
				{
					// error handling goes on the if bellow
				}
			}
		}
	}

	protected void writePacket(SelectionKey key, MMOConnection<T> con)
	{
		if(!prepareWriteBuffer(con))
		{
			key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
			return;
		}

		DIRECT_WRITE_BUFFER.flip();

		int size = DIRECT_WRITE_BUFFER.remaining();

		int result = -1;

		try
		{
			result = con.write(DIRECT_WRITE_BUFFER);
		}
		catch(IOException e)
		{
			// error handling goes on the if bellow
		}

		// check if no error happened
		if(result >= 0)
		{
			// check if we written everything
			if(result == size)
			{
				// complete write
				synchronized(con.getSendQueue())
				{
					if(con.getSendQueue().isEmpty() && !con.hasPendingWriteBuffer())
					{
						key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
					}
				}
			}
			else
			{
				// incomplete write
				con.createWriteBuffer(DIRECT_WRITE_BUFFER);
			}
		}
		else
		{
			con.getClient().onForcedDisconnection();
			closeConnectionImpl(key, con);
		}
	}

	private boolean prepareWriteBuffer(MMOConnection<T> con)
	{
		boolean hasPending = false;
		DIRECT_WRITE_BUFFER.clear();

		// if there is pending content add it
		if(con.hasPendingWriteBuffer())
		{
			con.movePendingWriteBufferTo(DIRECT_WRITE_BUFFER);
			hasPending = true;
		}

		if(DIRECT_WRITE_BUFFER.remaining() > 1 && !con.hasPendingWriteBuffer())
		{
			NioNetStackList<SendablePacket<T>> sendQueue = con.getSendQueue();
			T client = con.getClient();
			SendablePacket<T> sp;

			for(int i = 0; i < MAX_SEND_PER_PASS; i++)
			{
				synchronized(con.getSendQueue())
				{
					sp = sendQueue.isEmpty() ? null : sendQueue.removeFirst();
				}

				if(sp == null)
				{
					break;
				}

				hasPending = true;

				// put into WriteBuffer
				putPacketIntoWriteBuffer(client, sp);

				WRITE_BUFFER.flip();

				if(DIRECT_WRITE_BUFFER.remaining() >= WRITE_BUFFER.limit())
				{
					DIRECT_WRITE_BUFFER.put(WRITE_BUFFER);
				}
				else
				{
					con.createWriteBuffer(WRITE_BUFFER);
					break;
				}
			}
		}
		return hasPending;
	}

	private void putPacketIntoWriteBuffer(T client, SendablePacket<T> sp)
	{
		WRITE_BUFFER.clear();

		// reserve space for the size
		int headerPos = WRITE_BUFFER.position();
		int dataPos = headerPos + HEADER_SIZE;
		WRITE_BUFFER.position(dataPos);

		// set the write buffer
		sp._buf = WRITE_BUFFER;
		// write content to buffer
		sp.write();
		// delete the write buffer
		sp._buf = null;

		// size (inclusive header)
		int dataSize = WRITE_BUFFER.position() - dataPos;
		WRITE_BUFFER.position(dataPos);
		client.encrypt(WRITE_BUFFER, dataSize);

		// recalculate size after encryption
		dataSize = WRITE_BUFFER.position() - dataPos;

		WRITE_BUFFER.position(headerPos);
		// write header
		WRITE_BUFFER.putShort((short) (dataSize + HEADER_SIZE));
		WRITE_BUFFER.position(dataPos + dataSize);
	}

	void closeConnection(MMOConnection<T> con)
	{
		synchronized(_pendingClose)
		{
			_pendingClose.addLast(con);
		}
	}

	private void closeConnectionImpl(SelectionKey key, MMOConnection<T> con)
	{
		try
		{
			// notify connection
			con.getClient().onDisconnection();
		}
		finally
		{
			try
			{
				// close socket and the SocketChannel
				con.close();
			}
			catch(IOException e)
			{
				// ignore, we are closing anyway
			}
			finally
			{
				con.releaseBuffers();
				// clear attachment
				key.attach(null);
				// cancel key
				key.cancel();
			}
		}
	}

	public void shutdown()
	{
		_shutdown = true;
	}

	protected void closeSelectorThread()
	{
		for(SelectionKey key : _selector.keys())
		{
			try
			{
				key.channel().close();
			}
			catch(IOException e)
			{
				// ignore
			}
		}

		try
		{
			_selector.close();
		}
		catch(IOException e)
		{
			// Ignore
		}
	}
}
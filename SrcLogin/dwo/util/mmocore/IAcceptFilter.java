package dwo.util.mmocore;

import java.nio.channels.SocketChannel;

/**
 * @author KenM
 *
 */
public interface IAcceptFilter
{
	boolean accept(SocketChannel sc);
}

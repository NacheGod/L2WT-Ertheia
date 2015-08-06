package dwo.log.filters;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

/**
 * Log4J filter that looks if there is exception present in the logging event and accepts event if present. Otherwise it
 * bLocationks filtring.
 *
 * @author SoulKeeper
 */
public class ThrowablePresentFilter extends Filter
{

	/**
	 * Decides what to do with logging event.<br>
	 * This method accepts only log events that contain exceptions.
	 *
	 * @param loggingEvent log event that is going to be filtred.
	 * @return {@link Filter#ACCEPT} if throwable present, {@link Filter#DENY}
	 *         otherwise
	 */
	@Override
	public int decide(LoggingEvent loggingEvent)
	{

		Object message = loggingEvent.getMessage();

		if(message instanceof Throwable)
		{
			return ACCEPT;
		}

		ThrowableInformation information = loggingEvent.getThrowableInformation();

		if(information != null && information.getThrowable() != null)
		{
			return ACCEPT;
		}
		return DENY;
	}
}
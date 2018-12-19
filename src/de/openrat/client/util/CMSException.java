package de.openrat.client.util;

public class CMSException extends RuntimeException
{
	public CMSException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public CMSException(String message)
	{
		super(message);
	}

	public CMSException(Throwable cause)
	{
		super(cause);
	}

}

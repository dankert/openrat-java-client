package de.openrat.client.util;

public class CMSConnectionException extends CMSException
{

	private static final long serialVersionUID = 3734310284809339317L;

	private String message;
	private String status;
	private String description;
	private String reason;

	public CMSConnectionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public CMSConnectionException(String message, String status, String description, String reason, Throwable cause)
	{
		super(message, cause);

		this.message = message;
		this.status = status;
		this.description = description;
		this.reason = reason;
	}

	public CMSConnectionException(String message, String status, String description, String reason)
	{
		super(message);

		this.message = message;
		this.status = status;
		this.description = description;
		this.reason = reason;
	}
}

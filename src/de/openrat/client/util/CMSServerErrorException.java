package de.openrat.client.util;

public class CMSServerErrorException extends CMSException
{

	private static final long serialVersionUID = 3734310284809339317L;

	private String message;
	private String status;
	private String description;
	private String reason;

	public CMSServerErrorException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public CMSServerErrorException(String message, String status, String description, String reason, Throwable cause)
	{
		super(message, cause);

		this.message = message;
		this.status = status;
		this.description = description;
		this.reason = reason;
	}

	public CMSServerErrorException(String message)
	{
		super(message);
		this.message = message;
	}

	public CMSServerErrorException(String message, String status, String description, String reason)
	{
		super(message);

		this.message = message;
		this.status = status;
		this.description = description;
		this.reason = reason;
	}
}

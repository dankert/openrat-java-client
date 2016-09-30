package de.openrat.client.util;

public class HttpStatus
{

	private int statusCode;
	private String serverMessage;

	public HttpStatus(int statusCode, String serverMessage)
	{
		super();
		this.statusCode = statusCode;
		this.serverMessage = serverMessage;
	}

	public boolean isSuccess()
	{
		return statusCode / 100 == 2;
	}

	public boolean isRedirect()
	{
		return statusCode / 100 == 3;
	}

	public boolean isClientError()
	{
		return statusCode / 100 == 4;
	}

	public boolean isServerError()
	{
		return statusCode / 100 == 5;
	}

	/**
	 * Inhalt des Feldes <code>statusCode</code>.
	 * 
	 * @return statusCode
	 */
	public int getStatusCode()
	{
		return statusCode;
	}

	/**
	 * Inhalt des Feldes <code>serverMessage</code>.
	 * 
	 * @return serverMessage
	 */
	public String getServerMessage()
	{
		return serverMessage;
	}

}

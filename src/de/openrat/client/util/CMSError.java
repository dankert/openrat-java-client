package de.openrat.client.util;

public class CMSError
{

	public static enum CMSErrorStatus
	{
		NOTICE, WARN, ERROR;
	}

	private CMSErrorStatus status;
	private String type;
	private String message;
	private String reason;

	/**
	 * Inhalt des Feldes <code>status</code>.
	 * 
	 * @return status
	 */
	public CMSErrorStatus getStatus()
	{
		return status;
	}

	/**
	 * Setzt das Feld <code>status</code>.
	 * 
	 * @param status
	 *            status
	 */
	public void setStatus(CMSErrorStatus status)
	{
		this.status = status;
	}

	/**
	 * Inhalt des Feldes <code>type</code>.
	 * 
	 * @return type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Setzt das Feld <code>type</code>.
	 * 
	 * @param type
	 *            type
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * Inhalt des Feldes <code>message</code>.
	 * 
	 * @return message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * Setzt das Feld <code>message</code>.
	 * 
	 * @param message
	 *            message
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * Inhalt des Feldes <code>reason</code>.
	 * 
	 * @return reason
	 */
	public String getReason()
	{
		return reason;
	}

	/**
	 * Setzt das Feld <code>reason</code>.
	 * 
	 * @param reason
	 *            reason
	 */
	public void setReason(String reason)
	{
		this.reason = reason;
	}

}

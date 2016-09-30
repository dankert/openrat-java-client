package de.openrat.client.util;

public class CMSNotice
{

	public static enum CMSErrorStatus
	{
		NOTICE, WARN, ERROR;
	}

	private CMSErrorStatus status;
	private String type;
	private String name;
	private String text;
	private String key;

	public CMSErrorStatus getStatus()
	{
		return status;
	}

	public void setStatus(CMSErrorStatus status)
	{
		this.status = status;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

}

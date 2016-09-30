package de.openrat.client.util;

public class CMSSession
{

	private String name;
	private String id;
	private String token;

	/**
	 * Inhalt des Feldes <code>name</code>.
	 * 
	 * @return name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Setzt das Feld <code>name</code>.
	 * 
	 * @param name
	 *            name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Inhalt des Feldes <code>id</code>.
	 * 
	 * @return id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Setzt das Feld <code>id</code>.
	 * 
	 * @param id
	 *            id
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * Inhalt des Feldes <code>token</code>.
	 * 
	 * @return token
	 */
	public String getToken()
	{
		return token;
	}

	/**
	 * Setzt das Feld <code>token</code>.
	 * 
	 * @param token
	 *            token
	 */
	public void setToken(String token)
	{
		this.token = token;
	}

}

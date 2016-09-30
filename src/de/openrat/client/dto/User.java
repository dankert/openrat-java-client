package de.openrat.client.dto;

public class User extends CMSObject
{
	private String name;

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

}

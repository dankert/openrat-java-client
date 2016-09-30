package de.openrat.client.dto;

import java.io.Serializable;

import de.openrat.client.util.Id;

public class CMSObject implements Serializable
{
	private static final long serialVersionUID = -7483013624561450027L;
	
	private Id id;

	/**
	 * Inhalt des Feldes <code>id</code>.
	 * 
	 * @return id
	 */
	public Id getId()
	{
		return id;
	}

	/**
	 * Setzt das Feld <code>id</code>.
	 * 
	 * @param id
	 *            id
	 */
	public void setId(Id id)
	{
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CMSObject other = (CMSObject) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return super.toString() + ": Id " + id.longValue();
	}
}

/*
OpenRat Java-Client
Copyright (C) 2009 Jan Dankert
 
This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Library General Public
License as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Library General Public License for more details.

You should have received a copy of the GNU Library General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
Boston, MA  02110-1301, USA.

 */
package de.openrat.client.util;


/**
 * @author Jan Dankert
 */
public class CMSResponse
{

	private CMSError[] error;
	private CMSNode output;
	private CMSControl control;
	private CMSSession session;
	private int httpStatus;

	private int api;
	private String version;

	/**
	 * Inhalt des Feldes <code>error</code>.
	 * 
	 * @return error
	 */
	public CMSError[] getError()
	{
		return error;
	}

	/**
	 * Setzt das Feld <code>error</code>.
	 * 
	 * @param error
	 *            error
	 */
	public void setError(CMSError[] error)
	{
		this.error = error;
	}

	/**
	 * Inhalt des Feldes <code>output</code>.
	 * 
	 * @return output
	 */
	public CMSNode getOutput()
	{
		return output;
	}

	/**
	 * Setzt das Feld <code>output</code>.
	 * 
	 * @param cmsNode
	 *            output
	 */
	public void setOutput(CMSNode cmsNode)
	{
		this.output = cmsNode;
	}

	/**
	 * Inhalt des Feldes <code>control</code>.
	 * 
	 * @return control
	 */
	public CMSControl getControl()
	{
		return control;
	}

	/**
	 * Setzt das Feld <code>control</code>.
	 * 
	 * @param control
	 *            control
	 */
	public void setControl(CMSControl control)
	{
		this.control = control;
	}

	/**
	 * Inhalt des Feldes <code>api</code>.
	 * 
	 * @return api
	 */
	public int getApi()
	{
		return api;
	}

	/**
	 * Setzt das Feld <code>api</code>.
	 * 
	 * @param api
	 *            api
	 */
	public void setApi(int api)
	{
		this.api = api;
	}

	/**
	 * Inhalt des Feldes <code>version</code>.
	 * 
	 * @return version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * Setzt das Feld <code>version</code>.
	 * 
	 * @param version
	 *            version
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

	/**
	 * Inhalt des Feldes <code>session</code>.
	 * 
	 * @return session
	 */
	public CMSSession getSession()
	{
		return session;
	}

	/**
	 * Setzt das Feld <code>session</code>.
	 * 
	 * @param session
	 *            session
	 */
	public void setSession(CMSSession session)
	{
		this.session = session;
	}

	/**
	 * Inhalt des Feldes <code>httpStatus</code>.
	 * 
	 * @return httpStatus
	 */
	public int getHttpStatus()
	{
		return httpStatus;
	}

	/**
	 * Setzt das Feld <code>httpStatus</code>.
	 * 
	 * @param httpStatus
	 *            httpStatus
	 */
	public void setHttpStatus(int httpStatus)
	{
		this.httpStatus = httpStatus;
	}
}

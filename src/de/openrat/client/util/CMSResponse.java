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

import de.openrat.client.Version;

import java.util.List;


/**
 * @author Jan Dankert
 */
public class CMSResponse
{

	private List<CMSNotice> notices;
	private List<String> validationErrors;
	private CMSNode output;
	private CMSSession session;
	private int httpStatus;

	private Version api;
	private Version version;

	/**
	 * Inhalt des Feldes <code>error</code>.
	 * 
	 * @return error
	 */
	public List<CMSNotice> getNotices()
	{
		return notices;
	}

	/**
	 * Setzt das Feld <code>error</code>.
	 * 
	 * @param error
	 *            error
	 */
	public void setNotices(List<CMSNotice> error)
	{
		this.notices = error;
	}

	
	/**
	 * List of input fields with validation errors.
	 * @return
	 */
	public List<String> getValidationErrors()
	{
		return validationErrors;
	}

	/**
	 * @param validationErrors
	 */
	public void setValidationErrors(List<String> validationErrors)
	{
		this.validationErrors = validationErrors;
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
	 * the API version.
	 * 
	 * @return api
	 */
	public Version getApi()
	{
		return api;
	}

	/**
	 * Setzt das Feld <code>api</code>.
	 *
	 * @param api
	 *            api
	 */
	public void setApi(Version api)
	{
		this.api = api;
	}

	/**
	 * Inhalt des Feldes <code>version</code>.
	 * 
	 * @return version
	 */
	public Version getVersion()
	{
		return version;
	}

	/**
	 * Setzt das Feld <code>version</code>.
	 *
	 * @param version
	 *            version
	 */
	public void setVersion(Version version)
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

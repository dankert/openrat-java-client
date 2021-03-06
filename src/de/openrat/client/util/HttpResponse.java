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
 * API-Request to the OpenRat Content Management System. <br>
 * <br>
 * The call to the CMS server is done via a (non-SSL) HTTP connection.<br>
 * <br>
 * Before a call you are able to set some key/value-pairs as parameters. After
 * calling the CMS a DOM-document is returned, which contains the server
 * response.<br>
 * Example <br>
 * 
 * <pre>
 * CMSRequest request = new CMSRequest(&quot;your.openrat.example.com&quot;);
 * // prints tracing information to stdout.
 * request.trace = true;
 * try
 * {
 * 	request.parameter.put(&quot;action&quot;, &quot;index&quot;);
 * 	request.parameter.put(&quot;subaction&quot;, &quot;showlogin&quot;); // login page
 * 	request.parameter.put(&quot;...&quot;, &quot;...&quot;);
 * 	Document response = request.call();
 * 	// now traverse through the dom tree and get your information.
 * }
 * catch (IOException e)
 * {
 * 	// your error handling.
 * }
 * </pre>
 * 
 * @author Jan Dankert
 */
public class HttpResponse
{

	private HttpHeaderMap responseHeader = new HttpHeaderMap();
	private String payload;

	private HttpStatus httpStatus;

	/**
	 * Inhalt des Feldes <code>payload</code>.
	 * 
	 * @return payload
	 */
	public String getPayload()
	{
		return payload;
	}

	/**
	 * Setzt das Feld <code>payload</code>.
	 * 
	 * @param payload
	 *            payload
	 */
	public void setPayload(String payload)
	{
		this.payload = payload;
	}

	/**
	 * Inhalt des Feldes <code>responseHeader</code>.
	 * 
	 * @return responseHeader
	 */
	public HttpHeaderMap getResponseHeader()
	{
		return responseHeader;
	}

	/**
	 * Inhalt des Feldes <code>httpStatus</code>.
	 * 
	 * @return httpStatus
	 */
	public HttpStatus getHttpStatus()
	{
		return httpStatus;
	}

	/**
	 * Setzt das Feld <code>httpStatus</code>.
	 * 
	 * @param httpStatus
	 *            httpStatus
	 */
	public void setHttpStatus(HttpStatus httpStatus)
	{
		this.httpStatus = httpStatus;
	}

}

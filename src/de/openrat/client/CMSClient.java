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
package de.openrat.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import de.openrat.client.action.LoginAction;
import de.openrat.client.util.CMSConnection;

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
public class CMSClient
{

	public final static int SUPPORTED_API_VERSION = 2;

	final CMSConnection connection;

	/**
	 * Constructs a CMS-Connection to the specified server.<br>
	 * Server-Path is "/", Server-Port is 80.
	 * 
	 * @param host
	 *            hostname
	 */
	public CMSClient(String host)
	{

		this.connection = new CMSConnection(host, 80, "/");
	}

	/**
	 * Constructs a CMS-Request to the specified server/path.<br>
	 * Server-Port is 80.
	 * 
	 * @param host
	 *            hostname
	 * @param path
	 *            path
	 */
	public CMSClient(String host, String path)
	{

		this.connection = new CMSConnection(host, 80, path);

	}

	/**
	 * Constructs a CMS-Request to the specified server/path/port.
	 * 
	 * @param host
	 *            hostname
	 * @param path
	 *            path
	 * @param port
	 *            port-number
	 */
	public CMSClient(String host, String path, int port)
	{

		this.connection = new CMSConnection(host, port, path);
	}

	public LoginAction getLoginAction()
	{
		return new LoginAction(connection);
	}

	public void setLogWriter(PrintWriter logWriter)
	{
		this.connection.setLogWriter(logWriter);
	}

	public CMSConnection getConnection()
	{
		return connection;
	}

	/**
	 * Setting a HTTP-Proxy.
	 * 
	 * @param host
	 *            hostname
	 * @param port
	 *            port
	 */
	public void setProxy(String host, int port)
	{

		setProxy(host, port, null, null);
	}

	/**
	 * Setting a HTTP-Proxy.
	 * 
	 * @param host
	 *            hostname
	 * @param port
	 *            port
	 */
	public void setProxy(String host, int port, String user, String password)
	{

		this.connection.setProxyHostname(host);
		this.connection.setProxyPort(port);
		this.connection.setProxyUser(user);
		this.connection.setProxyPassword(password);
	}

	public void setLocale(Locale locale)
	{
		connection.setLocale(locale);
	}

	public void setTimeout(int timeout)
	{
		connection.setTimeout(timeout);
	}

	public void setKeepAlive(boolean useKeepAlive)
	{
		connection.setKeepAlive(useKeepAlive);
	}

	@Override
	public String toString()
	{
		return super.toString() + ": " + String.valueOf(this.connection);
	}

	public void close()
	{
		if (connection.getSocket() != null)
		{
			try
			{
				connection.getSocket().close();
			}
			catch (IOException e)
			{
				;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable
	{
		close();
		super.finalize();
	}

}

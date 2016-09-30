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
 * Client for the OpenRat Content Management System. <br>
 * <br>
 * The call to the CMS server is done via a HTTP connection.<br>
 * <br>
 * Example <br>
 * 
 * <pre>
 * CMSClient client = new CMSClient(&quot;demo.openrat.de&quot;, &quot;/latest-snapshot/openrat/dispatcher.php&quot;, 80);
 * client.setLogWriter(new PrintWriter(System.out, true));
 * // client.setProxy(&quot;proxy.mycompany.exmaple&quot;, 8080, &quot;user&quot;, &quot;pass&quot;);
 * client.setLocale(Locale.GERMAN);
 * LoginAction loginAction = client.getLoginAction();
 * 
 * try
 * {
 * 	loginAction.login(&quot;admin&quot;, &quot;admin&quot;, &quot;db1&quot;);
 * }
 * catch (LoginException e)
 * {
 * 	fail(&quot;Login failed&quot; + e.getLocalizedMessage());
 * }
 * </pre>
 * 
 * @author Jan Dankert
 */
public class CMSClient
{
	// the api version which we are supporting
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
	 * Constructs a CMS-Connection to the specified server/path.<br>
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
	 * Constructs a CMS-Connection to the specified server/path/port.
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

	/**
	 * Action class for login methods.
	 * 
	 * @return
	 */
	public LoginAction getLoginAction()
	{
		return new LoginAction(connection);
	}

	/**
	 * you may want to set a LogWriter, in which log messages are written.
	 * 
	 * @param logWriter
	 */
	public void setLogWriter(PrintWriter logWriter)
	{
		this.connection.setLogWriter(logWriter);
	}

	/**
	 * CMS Connection
	 * 
	 * @return
	 */
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
	 * @param user
	 *            proxy username
	 * @param password
	 *            password
	 */
	public void setProxy(String host, int port, String user, String password)
	{

		this.connection.setProxyHostname(host);
		this.connection.setProxyPort(port);
		this.connection.setProxyUser(user);
		this.connection.setProxyPassword(password);
	}

	/**
	 * changing the {@link Locale}. Default is the Default-Locale.
	 * 
	 * @param locale
	 */
	public void setLocale(Locale locale)
	{
		connection.setLocale(locale);
	}

	/**
	 * Socket-Timeout in milliseconds. Default: 5000.
	 * 
	 * @param timeout
	 */
	public void setTimeout(int timeout)
	{
		connection.setTimeout(timeout);
	}

	/**
	 * Experimental Feature, DO <b>NOT</b> SET THIS TO TRUE. HTTP persistent
	 * connections are not supported!
	 * 
	 * @param useKeepAlive
	 */
	public void setKeepAlive(boolean useKeepAlive)
	{
		connection.setKeepAlive(useKeepAlive);
	}

	@Override
	public String toString()
	{
		return super.toString() + ": " + String.valueOf(this.connection);
	}

	/**
	 * closing all resources. Normally, you do not need to call this, because
	 * the sockets are closed after each call. this is only for future vesions
	 * wenn keep-alive is implemented.
	 * 
	 */
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
	 * Closes all resources before finalizing an instance of this class.
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

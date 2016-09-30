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

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

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
public class CMSConnection
{

	private String serverPath;
	private String serverHost;
	private int serverPort;
	private boolean secure;

	private String proxyHostname;
	private int proxyPort;

	private String proxyUser;
	private String proxyPassword;

	private PrintWriter logWriter;

	private CookieStoreMap cookieStore = new CookieStoreMap();
	private String token = "";

	private String paramActionName = "action";
	private String paramMethodName = "subaction";

	private Locale locale = Locale.getDefault();
	private int timeout = 5000;
	private boolean keepAlive;
	private Socket socket;

	/**
	 * Inhalt des Feldes <code>socket</code>.
	 * 
	 * @return socket
	 */
	public Socket getSocket()
	{
		return socket;
	}

	/**
	 * Setzt das Feld <code>socket</code>.
	 * 
	 * @param socket
	 *            socket
	 */
	public void setSocket(Socket socket)
	{
		this.socket = socket;
	}

	/**
	 * Inhalt des Feldes <code>timeout</code>.
	 * 
	 * @return timeout
	 */
	public int getTimeout()
	{
		return timeout;
	}

	/**
	 * Constructs a CMS-Request to the specified server/path/port.
	 * 
	 * @param host
	 *            hostname
	 * @param port
	 *            port-number
	 * @param path
	 *            path
	 */
	public CMSConnection(String host, int port, String path)
	{

		super();
		this.serverHost = host;
		this.serverPath = path;
		this.serverPort = port;

		if (this.serverHost.startsWith("https://"))
		{
			secure = true;
			if (port == 80)
			{
				this.serverPort = 443;
			}
			this.serverHost = this.serverHost.substring(8);
		}
		else if (this.serverHost.startsWith("http://"))
		{
			this.serverHost = this.serverHost.substring(7);
		}
		else if (port == 443)
		{
			secure = true;
		}
	}

	/**
	 * Inhalt des Feldes <code>serverPath</code>.
	 * 
	 * @return serverPath
	 */
	public String getServerPath()
	{
		return serverPath;
	}

	/**
	 * Setzt das Feld <code>serverPath</code>.
	 * 
	 * @param serverPath
	 *            serverPath
	 */
	public void setServerPath(String serverPath)
	{
		this.serverPath = serverPath;
	}

	/**
	 * Inhalt des Feldes <code>serverHost</code>.
	 * 
	 * @return serverHost
	 */
	public String getServerHost()
	{
		return serverHost;
	}

	/**
	 * Setzt das Feld <code>serverHost</code>.
	 * 
	 * @param serverHost
	 *            serverHost
	 */
	public void setServerHost(String serverHost)
	{
		this.serverHost = serverHost;
	}

	/**
	 * Inhalt des Feldes <code>serverPort</code>.
	 * 
	 * @return serverPort
	 */
	public int getServerPort()
	{
		return serverPort;
	}

	/**
	 * Setzt das Feld <code>serverPort</code>.
	 * 
	 * @param serverPort
	 *            serverPort
	 */
	public void setServerPort(int serverPort)
	{
		this.serverPort = serverPort;
	}

	/**
	 * Inhalt des Feldes <code>proxyHostname</code>.
	 * 
	 * @return proxyHostname
	 */
	public String getProxyHostname()
	{
		return proxyHostname;
	}

	/**
	 * Setzt das Feld <code>proxyHostname</code>.
	 * 
	 * @param proxyHostname
	 *            proxyHostname
	 */
	public void setProxyHostname(String proxyHostname)
	{
		this.proxyHostname = proxyHostname;
	}

	/**
	 * Inhalt des Feldes <code>proxyPort</code>.
	 * 
	 * @return proxyPort
	 */
	public int getProxyPort()
	{
		return proxyPort;
	}

	/**
	 * Setzt das Feld <code>proxyPort</code>.
	 * 
	 * @param proxyPort
	 *            proxyPort
	 */
	public void setProxyPort(int proxyPort)
	{
		this.proxyPort = proxyPort;
	}

	/**
	 * Setzt das Feld <code>logWriter</code>.
	 * 
	 * @param logWriter
	 *            logWriter
	 */
	public void setLogWriter(PrintWriter logWriter)
	{
		this.logWriter = logWriter;
	}

	/**
	 * Setzt das Feld <code>secure</code>.
	 * 
	 * @param secure
	 *            secure
	 */
	public void setSecure(boolean secure)
	{
		this.secure = secure;
	}

	/**
	 * Setzt das Feld <code>proxyUser</code>.
	 * 
	 * @param proxyUser
	 *            proxyUser
	 */
	public void setProxyUser(String proxyUser)
	{
		this.proxyUser = proxyUser;
	}

	/**
	 * Setzt das Feld <code>proxyPassword</code>.
	 * 
	 * @param proxyPassword
	 *            proxyPassword
	 */
	public void setProxyPassword(String proxyPassword)
	{
		this.proxyPassword = proxyPassword;
	}

	/**
	 * Inhalt des Feldes <code>proxyUser</code>.
	 * 
	 * @return proxyUser
	 */
	public String getProxyUser()
	{
		return proxyUser;
	}

	/**
	 * Inhalt des Feldes <code>proxyPassword</code>.
	 * 
	 * @return proxyPassword
	 */
	public String getProxyPassword()
	{
		return proxyPassword;
	}

	public PrintWriter getLogWriter()
	{
		return logWriter;
	}

	/**
	 * Inhalt des Feldes <code>cookieStore</code>.
	 * 
	 * @return cookieStore
	 */
	public CookieStoreMap getCookieStore()
	{
		return cookieStore;
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

	/**
	 * Inhalt des Feldes <code>secure</code>.
	 * 
	 * @return secure
	 */
	public boolean isSecure()
	{
		return secure;
	}

	/**
	 * Inhalt des Feldes <code>paramActionName</code>.
	 * 
	 * @return paramActionName
	 */
	public String getParamActionName()
	{
		return paramActionName;
	}

	/**
	 * Setzt das Feld <code>paramActionName</code>.
	 * 
	 * @param paramActionName
	 *            paramActionName
	 */
	public void setParamActionName(String paramActionName)
	{
		this.paramActionName = paramActionName;
	}

	/**
	 * Inhalt des Feldes <code>paramMethodName</code>.
	 * 
	 * @return paramMethodName
	 */
	public String getParamMethodName()
	{
		return paramMethodName;
	}

	/**
	 * Setzt das Feld <code>paramMethodName</code>.
	 * 
	 * @param paramMethodName
	 *            paramMethodName
	 */
	public void setParamMethodName(String paramMethodName)
	{
		this.paramMethodName = paramMethodName;
	}

	/**
	 * Inhalt des Feldes <code>locale</code>.
	 * 
	 * @return locale
	 */
	public Locale getLocale()
	{
		return this.locale;
	}

	/**
	 * Setzt das Feld <code>locale</code>.
	 * 
	 * @param locale
	 *            locale
	 */
	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	/**
	 * Setzt das Feld <code>timeout</code>.
	 * 
	 * @param timeout
	 *            timeout
	 */
	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	/**
	 * Using Keep-alive-sockets and HTTP-persistent-connections. we do NOT
	 * support this! DO NOT set this to true!
	 * 
	 * @param keepAlive
	 *            keepAlive
	 */
	public void setKeepAlive(boolean keepAlive)
	{
		this.keepAlive = keepAlive;
	}

	public boolean isKeepAlive()
	{
		return keepAlive;
	}

	@Override
	public String toString()
	{
		return super.toString() + ": " + serverHost + ":" + serverPort + serverPath;

	}
}

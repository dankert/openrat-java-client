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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.DatatypeConverter;

import de.openrat.client.util.HttpRequest.HttpMethod;

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
public class HttpClient
{
	private static final String HTTP_VERSION = "HTTP/1.0";

	private PrintWriter logWriter;

	private CMSConnection connection;

	private ParameterMap parameter = new ParameterMap();

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
	public HttpClient(CMSConnection connection)
	{

		super();
		this.connection = connection;
		this.logWriter = connection.getLogWriter();
	}

	/**
	 * Sends a request to the openrat-server and parses the response into a DOM
	 * tree document.
	 * 
	 * @return server response as a DOM tree
	 * @throws IOException
	 *             if server is unrechable or responds non-wellformed XML
	 */
	public HttpResponse execute(HttpRequest request) throws IOException
	{
		final Socket socket = this.createSocket();

		try
		{

			String httpUrl = this.connection.getServerPath();

			final PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);

			if (connection.getProxyHostname() != null)
				// See RFC 2616 Section 5.1.2 "Request-URI"
				// "The absolute URI form is REQUIRED when the request is being made to a proxy"
				httpUrl = "http://" + this.connection.getServerHost() + httpUrl;

			String queryString = parameter.toQueryString();
			if (HttpMethod.GET.equals(request.getMethod()))
				httpUrl = httpUrl + "?" + queryString;

			// using HTTP/1.0 as this is supported by all HTTP-servers and
			// proxys.
			// We have no need for HTTP/1.1 at the moment.
			String httpCommandLine = request.getMethod().name() + " " + httpUrl + " " + HTTP_VERSION + "\n";

			socketWriter.write(httpCommandLine);

			HttpHeaderMap requestHeader = new HttpHeaderMap();

			// Setting the HTTP Header
			requestHeader.put("Host", this.connection.getServerHost());
			requestHeader.put("Accept-Language", connection.getLocale().getLanguage());
			requestHeader.put("User-Agent", "Mozilla/5.0; compatible (" + HttpClient.class.getName() + ")");

			requestHeader.putAll(request.getRequestHeader());

			String connectionStatus = connection.isKeepAlive() ? "keep-alive" : "close";
			requestHeader.put("Connection", connectionStatus);

			if (this.connection.getProxyUser() != null)
			{
				final String userPass = DatatypeConverter.printBase64Binary((connection.getProxyUser() + ":" + connection
						.getProxyPassword()).getBytes());
				requestHeader.put("Proxy-Authorization", "Basic " + userPass);
			}

			String cookieHeader = connection.getCookieStore().getCookieRequestHeader();

			if (cookieHeader.length() > 0)
				requestHeader.put("Cookie", cookieHeader);

			if (HttpMethod.POST.equals(request.getMethod()))
			{
				requestHeader.put("Content-Type", "application/x-www-form-urlencoded");
				requestHeader.put("Content-Length", "" + queryString.length());

			}

			// write HTTP-request-headers to socket
			socketWriter.write(requestHeader.toHttpHeaderString());
			// empty line after HTTP headers
			socketWriter.write("\n");

			if (this.logWriter != null)
			{
				logWriter.println("--- HTTP-Request ---");
				logWriter.println(httpCommandLine);
				logWriter.println(requestHeader.toHttpHeaderString());
			}

			// POST-request have the payload in the body
			if (HttpMethod.POST.equals(request.getMethod()))
			{

				if (this.logWriter != null)
				{
					logWriter.println("\n" + queryString);
				}

				socketWriter.write(queryString);
			}

			socketWriter.flush();

			// now waiting for the answer...
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String readLine = bufferedReader.readLine();

			if (readLine == null)
			{
				throw new CMSException("Server response is empty");
			}

			final String httpServerResponse = readLine.trim();
			final String httpStatusCode = httpServerResponse.substring(9, 12);
			final String httpServerMessage = httpServerResponse.substring(13);

			if (this.logWriter != null)
			{

				logWriter.println("--- HTTP-Response ---");
				logWriter.println(httpServerResponse);
			}

			HttpHeaderMap responseHeader = new HttpHeaderMap();
			// Analyze the HTTP response headers
			while (true)
			{
				String responseHeaderString = bufferedReader.readLine().trim();

				if (responseHeaderString.equals(""))
					break;

				int pos = responseHeaderString.indexOf(": ");
				if (pos > 1)
				{
					String key = responseHeaderString.substring(0, pos);
					String value = responseHeaderString.substring(pos + 2);
					responseHeader.put(key, value);
				}
				else
				{
					throw new CMSException("Unknown HTTP response header:" + responseHeaderString);
				}

			}

			if (this.logWriter != null)
				logWriter.println(responseHeader.toHttpHeaderString());

			if (responseHeader.containsKey("Set-Cookie"))
			{
				this.parseRawCookie(responseHeader.get("Set-Cookie"));
			}

			StringBuffer responseString = new StringBuffer();
			// while (bufferedReader.ready())
			// {
			// responseString.append(bufferedReader.readLine() + "\n");
			// }

			String buffer;
			while ((buffer = bufferedReader.readLine()) != null)
			{
				responseString.append(buffer + "\n");
			}

			if (this.logWriter != null)
			{
				logWriter.println();
				logWriter.println(responseString + "\n\n\n");
				logWriter.flush();
			}

			final HttpResponse httpResponse = new HttpResponse();
			httpResponse.setPayload(responseString.toString());
			httpResponse.setHttpStatus(new HttpStatus(NumberUtils.toInt(httpStatusCode), httpServerMessage));

			return httpResponse;
		}
		finally
		{
			// always close the socket because sockets are not endless resources
			if (!connection.isKeepAlive())
				try
				{
					socket.close();
				}
				catch (Exception e)
				{
					; // we have done our very best to close the socket
				}
		}
	}

	public Socket createSocket() throws IOException
	{

		if (connection.getSocket() != null && connection.isKeepAlive())
		{
			if (logWriter != null)
			{
				logWriter.println("Reusing socket: " + connection.getSocket().toString());
			}

			return connection.getSocket();
		}

		// When a client uses a proxy, it typically sends all requests to that
		// proxy, instead
		// of to the servers in the URLs. Requests to a proxy differ from normal
		// requests in one
		// way: in the first line, they use the complete URL of the resource
		// being requested,
		// instead of just the path.

		final boolean useProxy = connection.getProxyHostname() != null;

		SocketFactory socketFactory;
		if (connection.isSecure())
		{
			socketFactory = SSLSocketFactory.getDefault();
		}
		else
		{
			socketFactory = SocketFactory.getDefault();
		}

		Socket socket = socketFactory.createSocket();

		SocketAddress socketAddress;
		if (useProxy)
		{
			socketAddress = new InetSocketAddress(connection.getProxyHostname(), connection.getProxyPort());
		}
		else
		{
			socketAddress = new InetSocketAddress(connection.getServerHost(), connection.getServerPort());
		}

		if (logWriter != null)
		{
			logWriter.println("Creating Socket: " + socketAddress.toString());
		}

		socket.setKeepAlive(connection.isKeepAlive());
		socket.setReuseAddress(false);

		try
		{
			socket.connect(socketAddress, connection.getTimeout());
		}
		catch (ConnectException e)
		{
			throw new CMSException("cannot connect to " + socketAddress.toString(), "", "", e.getMessage(), e);
		}

		if (connection.isKeepAlive())
		{
			connection.setSocket(socket);
		}

		return socket;
	}

	public void setLogWriter(PrintWriter logWriter)
	{
		this.logWriter = logWriter;
	}

	private void parseRawCookie(String rawCookie)
	{

		String[] rawCookieParams = rawCookie.split(";");
		String[] rawCookieNameAndValue = rawCookieParams[0].split("=");

		if (rawCookieNameAndValue.length != 2)
		{
			this.logWriter.println("Set-Cookie: " + rawCookie + " - Invalid cookie: missing name and value");
		}

		String cookieName = rawCookieNameAndValue[0].trim();
		String cookieValue = rawCookieNameAndValue[1].trim();

		connection.getCookieStore().put(cookieName, cookieValue);

		// Ignoring all cookie attributes, because we are only storing the
		// cookies for this session.
	}

	public ParameterMap getParameter()
	{
		return parameter;
	}

}

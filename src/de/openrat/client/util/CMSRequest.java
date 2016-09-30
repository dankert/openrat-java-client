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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.openrat.client.CMSClient;
import de.openrat.client.util.CMSNotice.CMSErrorStatus;
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
public class CMSRequest
{

	private ParameterMap parameter = new ParameterMap();

	private PrintWriter logWriter;

	private HttpMethod method = HttpMethod.GET;

	private CMSConnection connection;

	/**
	 * Set the HTTP Method. Default is "GET".
	 * 
	 * @param method
	 *            HTTP-method
	 */
	public void setMethod(HttpMethod method)
	{

		this.method = method;
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
	public CMSRequest(CMSConnection connection)
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
	public CMSResponse execute() throws IOException
	{

		parameter.put("token", connection.getToken());

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setMethod(method);

		httpRequest.getRequestHeader().put("User-Agent", "Mozilla/5.0; compatible (OpenRat CMS java-client)");
		httpRequest.getRequestHeader().put("Accept", "application/xml");
		httpRequest.getRequestHeader().put("Accept-Charset", "utf-8");

		final HttpClient httpClient = new HttpClient(connection);
		httpClient.getParameter().putAll(parameter);

		HttpResponse httpResponse = httpClient.execute(httpRequest);

		final CMSNode rootNode;
		try
		{
			// Try XML parsing
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.parse(new InputSource(new StringReader(httpResponse.getPayload())));
			rootNode = convertXMLNodeIntoCMSNode(document.getDocumentElement());
		}
		catch (ParserConfigurationException e)
		{
			if (logWriter != null)
			{
				e.printStackTrace(logWriter);
			}
			throw new CMSException("XML-Parser-Configuration invalid", "", "", e.getMessage(), e);
		}
		catch (SAXException e)
		{
			throw new CMSException("Server did not return a valid XML-document: " + httpResponse.getPayload(), ""
					+ httpResponse.getHttpStatus().getStatusCode(), httpResponse.getHttpStatus().getServerMessage(), e.getMessage(), e);
		}

		CMSResponse cmsResponse = createCMSResponse(httpResponse.getHttpStatus(), rootNode);

		cmsResponse.setHttpStatus(httpResponse.getHttpStatus().getStatusCode());
		connection.setToken(cmsResponse.getSession().getToken());
		return cmsResponse;
	}

	/**
	 * Erzeugt aus
	 * 
	 * @param httpStatus
	 * 
	 * @param rootNode
	 * @return
	 */
	private CMSResponse createCMSResponse(HttpStatus httpStatus, final CMSNode rootNode)
	{

		if (httpStatus.getStatusCode() == 204)
		{
			return null; // No content
		}

		if (httpStatus.isServerError())
		{
			if (rootNode.getName() == "error")
			{
				// Server reports an technical error.
				String error = rootNode.getChild("error").getValue();
				String status = rootNode.getChild("status").getValue();
				String description = rootNode.getChild("description").getValue();
				String reason = rootNode.getChild("reason").getValue();

				throw new CMSException(error, status, description, reason);

			}
			else
			{
				throw new CMSException(httpStatus.getServerMessage(), "" + httpStatus.getStatusCode(), "", "");
			}

		}

		if (httpStatus.getStatusCode() == 200)
		{

			if (rootNode.getName() == "server")
			{
				// Server reports an answer

				CMSResponse cmsResponse = createCMSReponse(rootNode);

				return cmsResponse;
			}
			else
			{
				// HTTP-Status 200 OK, but no XML-Element "server" found.
				throw new CMSException(httpStatus.getServerMessage(), "" + httpStatus.getStatusCode(), "", "no SERVER element found");
			}
		}
		else
		{
			// Unknown HTTP Status
			throw new CMSException(httpStatus.getServerMessage(), "" + httpStatus.getStatusCode(), "", "Unsupported HTTP Status");
		}
	}

	private CMSResponse createCMSReponse(final CMSNode rootNode)
	{

		CMSResponse cmsResponse = new CMSResponse();

		// Do we support the server api version?
		int apiVersion = Integer.parseInt(rootNode.getChild("api").getValue());

		if (apiVersion != CMSClient.SUPPORTED_API_VERSION)
		{
			// oh no, the server api is older or newer than our client
			// api.
			// there is nothing we can do.
			throw new CMSException("Only API Version 2 is supported. The server is using API Version " + rootNode.getChild("api"));
		}

		cmsResponse.setApi(apiVersion);
		cmsResponse.setVersion(rootNode.getChild("version").getValue());

		List<String> errorList = new ArrayList<String>();
		for (CMSNode errorNode : rootNode.getChild("errors").getChildren())
		{
			errorList.add(errorNode.getValue());
		}
		cmsResponse.setValidationErrors(errorList);

		List<CMSNotice> noticeList = new ArrayList<CMSNotice>();

		for (CMSNode noticeNode : rootNode.getChild("notices").getChildren())
		{
			CMSNotice error = new CMSNotice();
			error.setKey(noticeNode.getChild("key").getValue());
			error.setType(noticeNode.getChild("type").getValue());
			error.setName(noticeNode.getChild("name").getValue());
			error.setText(noticeNode.getChild("text").getValue());

			String status = noticeNode.getChild("status").getValue();
			if (status.equalsIgnoreCase("ok"))
				error.setStatus(CMSErrorStatus.NOTICE);
			else if (status.equalsIgnoreCase("warning"))
				error.setStatus(CMSErrorStatus.WARN);
			else
				error.setStatus(CMSErrorStatus.ERROR);
			noticeList.add(error);
		}
		cmsResponse.setNotices(noticeList);

		CMSNode sessionNode = rootNode.getChild("session");
		CMSSession session = new CMSSession();
		session.setName(sessionNode.getChild("name").getValue());
		session.setId(sessionNode.getChild("id").getValue());
		session.setToken(sessionNode.getChild("token").getValue());
		cmsResponse.setSession(session);

		cmsResponse.setOutput(rootNode.getChild("outpout"));
		return cmsResponse;
	}

	public void setLogWriter(PrintWriter logWriter)
	{
		this.logWriter = logWriter;
	}

	public static Iterable<Node> iterable(final NodeList n)
	{

		return new Iterable<Node>()
		{

			public Iterator<Node> iterator()
			{

				return new Iterator<Node>()
				{

					int index = 0;

					public boolean hasNext()
					{
						return index < n.getLength();
					}

					public Node next()
					{
						if (hasNext())
						{
							return n.item(index++);
						}
						else
						{
							throw new NoSuchElementException();
						}
					}

					public void remove()
					{
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	private static CMSNode convertXMLNodeIntoCMSNode(Node node)
	{

		Map<String, CMSNode> children = new HashMap<String, CMSNode>();

		for (Node nodex : iterable(node.getChildNodes()))
		{
			if (nodex.getNodeType() == Node.ELEMENT_NODE)
			{

				CMSNode childNode = convertXMLNodeIntoCMSNode(nodex);
				children.put(nodex.getNodeName(), childNode);
			}
		}

		return new CMSNode(node.getNodeName(), node.getTextContent(), children);
	}

	public ParameterMap getParameter()
	{
		return parameter;
	}
}

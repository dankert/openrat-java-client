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
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.openrat.client.Version;
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
public class CMSRequest {

	private String actionMethod;
	private String action;
	private ParameterMap parameter = new ParameterMap();

	private PrintWriter logWriter;

	private HttpMethod method = HttpMethod.GET;

	private CMSConnection connection;

	/**
	 * Marks this request for writing.
	 */
	public CMSRequest forWriting() {
		this.method = HttpMethod.POST;
		return this;
	}

	/**
	 */
	public CMSRequest forReading() {
		this.method = HttpMethod.GET;
		return this;
	}

	/**
	 * Constructs a CMS-Request to the specified server/path/port.
	 *
	 * @param connection Connection to server
	 */
	public CMSRequest(CMSConnection connection) {
		super();
		this.connection = connection;
		this.logWriter = connection.getLogWriter();
	}


	public CMSRequest(CMSConnection connection, String action, String method) {
		this(connection);
		this.action = action;
		this.actionMethod = method;
	}


	public CMSRequest addParameter(String name, CharSequence value) {
		this.parameter.put(name, value.toString());
		return this;
	}

	public CMSRequest addParameter(String name, long value) {
		this.parameter.put(name, Long.toString(value));
		return this;
	}


	/**
	 * Sends a request to the openrat-server and parses the response into a DOM
	 * tree document.
	 *
	 * @return server response as a DOM tree
	 * @throws IOException if server is unrechable or responds non-wellformed XML
	 */
	public CMSResponse execute() throws IOException {

		parameter.put("action",this.action);
		parameter.put("subaction",this.actionMethod);
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
		try {
			// Try XML parsing
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.parse(new InputSource(new StringReader(httpResponse.getPayload())));
			rootNode = convertXMLNodeIntoCMSNode(document.getDocumentElement());
		} catch (ParserConfigurationException e) {
			if (logWriter != null) {
				e.printStackTrace(logWriter);
			}
			throw new CMSException("XML-Parser-Configuration invalid: " + e.getMessage(), e);
		} catch (SAXException e) {
			throw new CMSServerErrorException("Server did not return a valid XML-document: " + httpResponse.getPayload(), ""
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
	 * @param rootNode
	 * @return
	 */
	private CMSResponse createCMSResponse(HttpStatus httpStatus, final CMSNode rootNode) {

		if (httpStatus.getStatusCode() == 204) {
			return null; // No content
		}

		if (httpStatus.isServerError()) {
			if (rootNode.getName().equals("server") || rootNode.getName().equals("error")) {
				// Server reports an technical error.
				String error = rootNode.getFirstChildByName("error").getValue();
				String status = rootNode.getFirstChildByName("status").getValue();
				String description = rootNode.getFirstChildByName("description").getValue();
				String reason = rootNode.getFirstChildByName("reason").getValue();

				ServerSideException cause = null;

				if (rootNode.getFirstChildByName("trace") != null)
					cause = createExceptionFromServerTrace(rootNode.getFirstChildByName("trace"));

				throw new CMSServerErrorException(error, status, description, reason, cause);

			} else {
				throw new CMSServerErrorException(httpStatus.getServerMessage(), "" + httpStatus.getStatusCode(), "", "");
			}

		}

		if (httpStatus.getStatusCode() == 200) {

			if (rootNode.getName() == "server") {
				// Server reports an answer
				CMSResponse cmsResponse = createCMSReponse(rootNode);

				return cmsResponse;
			} else {
				// HTTP-Status 200 OK, but no XML-Element "server" found.
				throw new CMSServerErrorException(httpStatus.getServerMessage(), "" + httpStatus.getStatusCode(), "", "no SERVER element found");
			}
		} else {
			// Unknown HTTP Status
			throw new CMSServerErrorException(httpStatus.getServerMessage(), "" + httpStatus.getStatusCode(), "", "Unsupported HTTP Status");
		}
	}

	private ServerSideException createExceptionFromServerTrace(CMSNode trace) {

		final List<StackTraceElement> traceElements = new ArrayList<>();

		for (CMSNode traceElementNode : trace.getChildren()) {

			String file = traceElementNode.getFirstChildValue("file");
			String line = traceElementNode.getFirstChildValue("line");

			int lineNumber = 0;
			if (line != null)
				lineNumber = Integer.parseInt(line);

			String fct = traceElementNode.getFirstChildValue("function");
			String cls = traceElementNode.getFirstChildValue("class");
			if (cls == null) cls = "";
			traceElements.add(new StackTraceElement(cls, fct, file, lineNumber));
		}

		String name = "Exception";
		String message = "server error";
		ServerSideException cause = new ServerSideException(message, name);

		cause.setStackTrace(traceElements.toArray(new StackTraceElement[]{}));

		return cause;
	}

	private CMSResponse createCMSReponse(final CMSNode rootNode) {

		CMSResponse cmsResponse = new CMSResponse();

		// Do we support the server api version?
		Version apiVersion = new Version(rootNode.getFirstChildByName("api").getValue());

		if (!apiVersion.equals(CMSClient.SUPPORTED_API_VERSION)) {
			// oh no, the server api is older or newer than our client api.
			// there is nothing we can do.
			throw new CMSServerErrorException("Only API Version " + CMSClient.SUPPORTED_API_VERSION +
					" is supported. The server is using API Version " + apiVersion);
		}

		cmsResponse.setApi(apiVersion);
		cmsResponse.setVersion(new Version(rootNode.getFirstChildByName("version").getValue()));

		List<String> errorList = new ArrayList<String>();
		for (CMSNode errorNode : rootNode.getFirstChildByName("errors").getChildren()) {
			errorList.add(errorNode.getValue());
		}
		cmsResponse.setValidationErrors(errorList);

		List<CMSNotice> noticeList = new ArrayList<CMSNotice>();

		for (CMSNode noticeNode : rootNode.getFirstChildByName("notices").getChildren()) {
			CMSNotice error = new CMSNotice();
			error.setKey(noticeNode.getFirstChildByName("key").getValue());
			error.setType(noticeNode.getFirstChildByName("type").getValue());
			error.setName(noticeNode.getFirstChildByName("name").getValue());
			error.setText(noticeNode.getFirstChildByName("text").getValue());

			String status = noticeNode.getFirstChildByName("status").getValue();

			if (status.equalsIgnoreCase("ok"))
				error.setStatus(CMSErrorStatus.NOTICE);
			else if (status.equalsIgnoreCase("warning"))
				error.setStatus(CMSErrorStatus.WARN);
			else if (status.equalsIgnoreCase("info"))
				error.setStatus(CMSErrorStatus.INFO);
			else
				error.setStatus(CMSErrorStatus.ERROR);
			noticeList.add(error);
		}
		cmsResponse.setNotices(noticeList);

		CMSNode sessionNode = rootNode.getFirstChildByName("session");
		CMSSession session = new CMSSession();
		session.setName(sessionNode.getFirstChildByName("name").getValue());
		session.setId(sessionNode.getFirstChildByName("id").getValue());
		session.setToken(sessionNode.getFirstChildByName("token").getValue());
		cmsResponse.setSession(session);

		cmsResponse.setOutput(rootNode.getFirstChildByName("output"));
		return cmsResponse;
	}

	public static Iterable<Node> iterable(final NodeList n) {

		return new Iterable<Node>() {

			public Iterator<Node> iterator() {

				return new Iterator<Node>() {

					int index = 0;

					public boolean hasNext() {
						return index < n.getLength();
					}

					public Node next() {
						if (hasNext()) {
							return n.item(index++);
						} else {
							throw new NoSuchElementException();
						}
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	private static CMSNode convertXMLNodeIntoCMSNode(Node node) {

		List<CMSNode> children = new ArrayList<CMSNode>();

		for (Node nodex : iterable(node.getChildNodes())) {
			if (nodex.getNodeType() == Node.ELEMENT_NODE) {

				CMSNode childNode = convertXMLNodeIntoCMSNode(nodex);
				children.add(childNode);
			}
		}

		return new CMSNode(node.getNodeName(), node.getTextContent(), children);
	}
}

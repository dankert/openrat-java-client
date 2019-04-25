package de.openrat.client.action;

import de.openrat.client.util.CMSConnection;
import de.openrat.client.util.CMSException;
import de.openrat.client.util.CMSRequest;
import de.openrat.client.util.CMSResponse;
import de.openrat.client.util.HttpRequest.HttpMethod;
import de.openrat.client.util.Id;
import de.openrat.client.util.ParameterMap;

/**
 * Action.
 *
 * This class is NOT threadsafe and should be used by one thread simultaneously.
 */
public abstract class Action
{

	/**
	 * Parameter map.
	 */
	private ParameterMap parameter = new ParameterMap();
	private Id id;

	private CMSConnection connection;

	/**
	 * Clear parameter values.
	 */
	public void clear()
	{
		parameter.clear();
	}

	protected Action(CMSConnection connection)
	{
		this.connection = connection;
	}

	/**
	 * Setting a parameter value.
	 * 
	 * @param paramName
	 *            name
	 * @param paramValue
	 *            value
	 */
	protected void setParameter(String paramName, String paramValue)
	{

		parameter.put(paramName, paramValue);
	}

	/**
	 * Setting a parameter value. <strong>DO NOT url-encode your values</strong>
	 * as this is done automatically inside this method!
	 * 
	 * @param paramName
	 *            name
	 * @param paramValue
	 *            value
	 */
	public void addParameter(String paramName, String paramValue)
	{

		if (paramName == null || paramValue == null || "" == paramName)
			throw new IllegalArgumentException("parameter name and value must have values");

		parameter.put(paramName, paramValue);
	}

	public void setId(Id id)
	{
		this.id = id;
	}

	public CMSConnection getConnection()
	{
		return connection;
	}

	protected CMSResponse executeView(String action, String method) {
		return execute(action,method,HttpMethod.GET);
	}

	protected CMSResponse executePost(String action, String method) {
		return execute(action,method,HttpMethod.POST);
	}

	private CMSResponse execute(String action, String method, HttpMethod httpMethod) throws CMSException
	{

		final ParameterMap parameter = new ParameterMap();

		parameter.put(connection.getParamActionName(), action);
		parameter.put(connection.getParamMethodName(), method);
		parameter.put("id", String.valueOf(id));
		parameter.putAll(this.parameter);

		CMSRequest request = new CMSRequest(this.connection);
		request.setMethod(httpMethod);
		request.getParameter().putAll(parameter);

		try
		{
			CMSResponse response = request.execute();
			return response;
		}
		catch (CMSException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new CMSException(e);
		}
	}
}

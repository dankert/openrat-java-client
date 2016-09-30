package de.openrat.client.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ParameterMap
{
	private Map<String, String> parameter = new HashMap<String, String>();

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		return parameter.size();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key)
	{
		return parameter.containsKey(key);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public String get(Object key)
	{
		return parameter.get(key);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public String put(String key, String value)
	{
		value = urlEncode(value);
		return parameter.put(key, value);
	}

	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	public Collection<String> values()
	{
		return parameter.values();
	}

	public String toQueryString()
	{

		return new MapConverter(parameter).convertMapToString("=", "&", false);
	}

	/**
	 * @param m
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(ParameterMap other)
	{
		parameter.putAll(other.parameter);
	}

	/**
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear()
	{
		parameter.clear();
	}

	/**
	 * URL-Encoder.
	 * 
	 * @param value
	 * @return url-encoded value
	 */
	private String urlEncode(String value)
	{

		String CHARSET_UTF8 = "UTF-8";

		try
		{
			return URLEncoder.encode(value, CHARSET_UTF8);
		}
		catch (UnsupportedEncodingException e)
		{
			// maybe... this would be strange
			throw new IllegalStateException(CHARSET_UTF8 + " ist not supported by this VM");
		}
	}

}

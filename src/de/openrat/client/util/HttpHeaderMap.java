package de.openrat.client.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HttpHeaderMap
{
	private Map<String, String> header = new HashMap<String, String>();

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		return header.size();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key)
	{
		return header.containsKey(key);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public String get(Object key)
	{
		return header.get(key);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public String put(String key, String value)
	{
		return header.put(key, value);
	}

	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	public Collection<String> values()
	{
		return header.values();
	}

	public String toHttpHeaderString()
	{

		return new MapConverter(header).convertMapToString(": ", "\n", true);
	}

	public void putAll(HttpHeaderMap requestHeader)
	{
		header.putAll(requestHeader.header);
	}
}

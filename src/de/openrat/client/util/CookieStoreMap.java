package de.openrat.client.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CookieStoreMap
{
	private Map<String, String> cookies = new HashMap<String, String>();

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		return cookies.size();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key)
	{
		return cookies.containsKey(key);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public String get(Object key)
	{
		return cookies.get(key);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public String put(String key, String value)
	{
		return cookies.put(key, value);
	}

	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	public Collection<String> values()
	{
		return cookies.values();
	}

	public String getCookieRequestHeader()
	{

		return new MapConverter(cookies).convertMapToString("=", "; ", false);
	}
}

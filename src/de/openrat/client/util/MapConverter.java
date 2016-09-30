package de.openrat.client.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapConverter
{

	private Map<String, String> map;

	public MapConverter(Map<String, String> map)
	{
		super();
		this.map = map;
	}

	public String convertMapToString(String keyValueSeparator, String entrySeparator, boolean withLast)
	{

		List<String> list = new ArrayList<String>();

		for (Entry<String, String> entry : map.entrySet())
		{
			list.add(entry.getKey() + keyValueSeparator + entry.getValue());
		}

		StringBuffer out = new StringBuffer();
		for (Iterator<String> i = list.iterator(); i.hasNext();)
		{
			out.append(i.next());
			if (withLast || i.hasNext())
			{
				out.append(entrySeparator);
			}
		}
		return out.toString();

	}
}

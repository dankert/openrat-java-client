package de.openrat.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CMSNode
{

	private static final CMSNode EMPTY_NODE = new CMSNode(null, null, null);
	private String name;
	private String value;
	private Map<String, CMSNode> children;

	public CMSNode(String name, String value, Map<String, CMSNode> children)
	{
		super();
		this.name = name;
		this.value = value;
		this.children = children;
	}

	public List<CMSNode> getChildren()
	{

		if (children != null)
			return new ArrayList<CMSNode>(children.values());
		else
			return new ArrayList<CMSNode>();
	}

	public CMSNode getChild(String name)
	{

		if (children == null)
			return EMPTY_NODE;

		CMSNode node = children.get(name);

		if (node != null)
			return node;
		else
			return EMPTY_NODE;
	}

	public String getValue()
	{
		return value;
	}

	public boolean isTrue()
	{
		return Boolean.valueOf(value).booleanValue();
	}

	public int toInt()
	{
		return NumberUtils.toInt(value);
	}

	public long toLong()
	{
		return NumberUtils.toLong(value);
	}


	public String getName()
	{
		return name;
	}

	public boolean isEmpty()
	{
		return name == null;
	}

	public boolean isNotEmpty()
	{
		return !isEmpty();
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + ": " + getName() + "=" + getValue() + " children:" + children.size() + " "
				+ children.toString();
	}
}

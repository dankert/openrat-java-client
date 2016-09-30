package de.openrat.client.util;

public class Id extends Number
{

	private static final long serialVersionUID = -1360182293567671578L;
	
	private final long value;

	Id(long id)
	{
		this.value = id;
	}

	Id(int id)
	{
		this.value = (long) id;
	}

	Id(short id)
	{
		this.value = (long) id;
	}

	@Override
	public int intValue()
	{
		return (int) value;
	}

	@Override
	public long longValue()
	{
		return value;
	}

	@Override
	public float floatValue()
	{
		throw new ArithmeticException("this id is never a float value");
	}

	@Override
	public double doubleValue()
	{
		throw new ArithmeticException("this id is never a double value");
	}

}

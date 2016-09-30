package de.openrat.client.util;

public class NumberUtils
{
	/**
	 * Null-safe and Exception-safe conversion from {@link String} to
	 * {@link Integer}.
	 * 
	 * @param number
	 *            Number
	 * @return int (0, if number is not a number)
	 */
	public static int toInt(String number)
	{
		try
		{
			return Integer.parseInt(number);
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}

	/**
	 * Null-safe and Exception-safe conversion from {@link String} to
	 * {@link Long}.
	 * 
	 * @param number
	 *            Number
	 * @return int (0, if number is not a number)
	 */
	public static long toLong(String number)
	{
		try
		{
			return Long.parseLong(number);
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}

}

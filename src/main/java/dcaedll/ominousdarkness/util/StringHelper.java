package dcaedll.ominousdarkness.util;

public class StringHelper
{
	public static boolean isBlank(String string)
	{
		for (int i = 0; i < string.length(); i++)
		{
			if (string.charAt(i) != ' ')
				return false;
		}
		return true;
	}
}
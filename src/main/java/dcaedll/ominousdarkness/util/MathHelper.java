package dcaedll.ominousdarkness.util;

public class MathHelper
{
	public static float clamp(float value, float min, float max)
	{
		if (min >= max)
			return min;
		
		if (value < min)
			return min;
		else if (value > max)
			return max;
		
		return value;
	}
}
package mamaPajama;

/**
 * Helper functions for MamaPajama
 * 
 * @author nyounse
 * 
 */
public class SchoolYard {
	/**
	 * This breaks up a color int into a float[]
	 * 
	 * @param in
	 *            int value of a color
	 * @return float[] of the shifted int value -- [alpha, red, green blue]
	 */
	public static float[] breakUpColorInt(int in) {
		float[] broken = new float[4];
		broken[0] = (in >> 24) & 0xFF;
		broken[1] = (in >> 16) & 0xFF;
		broken[2] = (in >> 8) & 0xFF;
		broken[3] = (in >> 0) & 0xFF;
		return broken;
	} // end breakUp

	/**
	 * This composes a float array into a color int
	 * 
	 * @param floatsIn
	 *            The float[] for the color -- [alpha, red, green, blue]
	 * @return The color int
	 */
	public static int composeColorInt(float[] floatsIn) {
		int redone = (int) floatsIn[0];
		redone = (int) ((redone << 8) + constrain(floatsIn[1], 0, 255));
		redone = (int) ((redone << 8) + constrain(floatsIn[2], 0, 255));
		redone = (int) ((redone << 8) + constrain(floatsIn[3], 0, 255));
		return redone;
	} // end compose

	/**
	 * Constrains a float between two values
	 * 
	 * @param floatIn The float in question
	 * @param low
	 *            The min value
	 * @param high
	 *            The max value
	 * @return Constrained float
	 * 
	 */
	public static float constrain(float floatIn, float low, float high) {
		if (high < low) {
			float temp = high;
			high = low;
			low = temp;
		}
		floatIn = floatIn >= low ? floatIn : low;
		floatIn = floatIn <= high ? floatIn : high;
		return floatIn;
	} // end constrain
} // end class SchoolYard

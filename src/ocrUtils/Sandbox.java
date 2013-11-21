package ocrUtils;

import java.util.ArrayList;

//import processing.core.PApplet;
import processing.core.PVector;

/**
 * Helper functions for MamaPajama
 * 
 * @author nyounse
 * 
 */
public class Sandbox extends OCRUtils{
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
	 * @param floatIn
	 *            The float in question
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

	/**
	 * This will just find the min and max of a double[]
	 * 
	 * @param numbers
	 *            The double[] of data
	 * @return A double[]. [0] = min [1] = max
	 */
	public static double[] findMinMax(double[] numbers) {
		double[] minMax = new double[2];
		minMax[0] = numbers[0];
		minMax[1] = minMax[0];
		for (double d : numbers) {
			minMax[0] = minMax[0] < d ? minMax[0] : d;
			minMax[1] = minMax[1] > d ? minMax[1] : d;
		}
		return minMax;
	} // end findMinMax	
	
	/**
	 * This will make two vectors defining the right and up vectors according to a plane vector
	 * @param normalIn PVector of the plane normal
	 * @return An ArrayList<PVector> [up, right] of the two vectors defining the plane. 
	 */
	  public static ArrayList<PVector> makePlaneVectors (PVector normalIn) {
		    ArrayList<PVector> planeVectors = new ArrayList<PVector>();
		    PVector right =   new PVector(-normalIn.y, normalIn.x, 0);
		    if (normalIn.y == 0 && normalIn.x ==0) right = new PVector(0, normalIn.z, 0);
		    PVector up = new PVector();
		    PVector.cross(normalIn, right, up);
		    right.normalize();
		    up.normalize();
		    planeVectors.add(up);
		    planeVectors.add(right);
		    return planeVectors;
		  } // end makePlaneVectors	
} // end class SchoolYard


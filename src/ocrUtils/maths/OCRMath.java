package ocrUtils.maths;

import ocrUtils.Sandbox;
import ocrUtils.Statistics;
import processing.core.PApplet;
import processing.core.PVector;

public class OCRMath {

	/**
	 * This will print out a graph of a double dataset in the console based on a
	 * sepcified number of steps
	 * 
	 * @param label
	 *            The label for the console output of this graph
	 * @param numbers
	 *            The double[] of the numbers
	 * @param stepAmount
	 *            The amount to step each grouping
	 */
	public static void makeQuickGraphBySteps(String label, double[] numbers,
			float stepAmount) {
		makeQuickGraph(label, numbers, 0, false, stepAmount);
	} // end makeQuickGraphByDivisions

	/**
	 * This will print out a graph of a double dataset in the console based on a
	 * sepcified number of steps
	 * 
	 * @param label
	 *            The label for the console output of this graph
	 * @param numbers
	 *            The double[] of the numbers
	 * @param steps
	 *            The number of groups to make
	 */
	public static void makeQuickGraphByDivisions(String label,
			double[] numbers, int steps) {
		makeQuickGraph(label, numbers, steps, true, 0f);
	} // end makeQuickGraphByDivisions

	private static void makeQuickGraph(String label, double[] numbers,
			int steps, boolean straightDivision, float stepAmount) {
		Statistics stat = new Statistics(numbers);
		double median = stat.getMedian();
		double mean = stat.getMean();
		double stdDev = stat.getStdDev();

		double[] minMax = Sandbox.findMinMax(numbers);
		double range = ((minMax[1] - minMax[0]) / (steps)); // for when using
															// straightDivision

		System.out.println("_");
		System.out.println("making quick graph for: " + label);
		System.out.println("  range: " + minMax[0] + "  <>  " + minMax[1]);
		System.out.println("  ...median: " + median + "    ===mean: " + mean
				+ "    ---stdDev: " + stdDev);

		int[] counts = new int[steps + 1];
		double[] stepNum = new double[counts.length];
		if (straightDivision) {
			for (int i = 0; i < steps + 1; i++) {
				stepNum[i] = minMax[0] + range * i;
			}
		} else {
			stepNum = new double[0];
			double marker = minMax[0];
			stepNum = (double[]) PApplet.append(stepNum, marker);
			while (true) {
				marker += stepAmount;
				stepNum = (double[]) PApplet.append(stepNum, marker);
				if (marker > minMax[1])
					break;
			}
			counts = new int[stepNum.length];
		}

		for (int i = 0; i < stepNum.length - 1; i++) {
			for (int j = 0; j < numbers.length; j++) {
				double d = numbers[j];
				if (d >= stepNum[i] && d < stepNum[i + 1]
						&& j < numbers.length - 1) {
					counts[i]++;
				} else if (j == numbers.length - 1) {
					if (d == stepNum[i + 1])
						counts[i]++;
				}
			}
		}
		int tempLength = 1;
		for (double d : stepNum) {
			String temp = PApplet.splitTokens("" + d, ".")[0];
			tempLength = tempLength > temp.length() ? tempLength : temp
					.length();
		}
		for (int i = 0; i < stepNum.length - 1; i++) {
			String[] startLabel = PApplet.splitTokens(stepNum[i] + "", ".");
			String[] endLabel = PApplet.splitTokens(stepNum[i + 1] + "", ".");
			for (int k = 0; k < tempLength - startLabel[0].length(); k++)
				System.out.print(" ");
			System.out.print(PApplet.nf((float) (stepNum[i]), 0, 3));
			System.out.print("  -  ");
			for (int k = 0; k < tempLength - endLabel[0].length(); k++)
				System.out.print(" ");

			System.out.print(PApplet.nf((float) (stepNum[i + 1]), 0, 3));
			String spacer = " ";
			// deal with stdDevs
			if (mean - stdDev >= stepNum[i] && mean - stdDev < stepNum[i + 1])
				spacer = "-";
			else if (mean + stdDev >= stepNum[i]
					&& mean + stdDev < stepNum[i + 1])
				spacer = "-";
			else if (median >= stepNum[i] && median < stepNum[i + 1])
				spacer = ".";
			// then mean
			if (mean >= stepNum[i] && mean < stepNum[i + 1])
				spacer = "=";
			int modifiedSpacerCount = (int) PApplet.map(counts[i], 0,
					PApplet.max(counts), 1, 100);
			for (int j = 0; j < modifiedSpacerCount; j++)
				System.out.print(spacer);
			System.out.println("X : " + counts[i]);
		}
		System.out.println("_");
	} // end makeQuickGraph

	/**
	 * Checks whether a PVector lies within the bounds of a outline defined by
	 * an array of PVectors
	 * 
	 * @param pos
	 *            A PVector that is to be tested
	 * @param verticesIn
	 *            The PVector[] that defines the planar polygon
	 * @return true if the PVector is in the outline
	 */
	public static boolean isInsidePolygon(PVector pos, PVector[] verticesIn) {
		int i, j = verticesIn.length - 1;
		int sides = verticesIn.length;
		boolean oddNodes = false;
		for (i = 0; i < sides; i++) {
			if ((verticesIn[i].y < pos.y && verticesIn[j].y >= pos.y || verticesIn[j].y < pos.y
					&& verticesIn[i].y >= pos.y)
					&& (verticesIn[i].x <= pos.x || verticesIn[j].x <= pos.x)) {
				oddNodes ^= (verticesIn[i].x + (pos.y - verticesIn[i].y)
						/ (verticesIn[j].y - verticesIn[i].y)
						* (verticesIn[j].x - verticesIn[i].x) < pos.x);
			}
			j = i;
		}
		return oddNodes;
	} // end isInsidePolygon
} // end class Maths

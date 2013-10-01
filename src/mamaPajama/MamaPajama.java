package mamaPajama;

import processing.core.*;

/**
 * MamaPajama is a collection of often used functions
 * 
 * @author Noa Younse
 * @version 1.0
 */
public class MamaPajama {
	public static PApplet parent;

	/**
	 * Required to use functions in MamaPajama
	 * 
	 * @param parent_
	 *            Needed to set the overall Processing parent
	 */
	public static void begin(PApplet parent_) {
		parent = parent_;
	} // end begin

	/**
	 * Will draw a grid on the XY plane using the default color
	 * 
	 * @param gridExtents
	 *            How far to make the grid in both directions
	 * @param gridSize
	 *            How many units each grid will be
	 */
	public static void drawGrid(float gridExtents, float gridSize) {
		float[] baseColor = { 55, 55, 55, 255 };
		drawGrid(gridExtents, gridSize, SchoolYard.composeColorInt(baseColor));
	} // end drawGrid

	/**
	 * Will draw a grid on the XY plane
	 * 
	 * @param gridExtents
	 *            How far to make the grid in both directions
	 * @param gridSize
	 *            How many units each grid will be
	 * @param baseColor
	 *            The base color for the grid [excluding the alpha]
	 */
	public static void drawGrid(float gridExtents, float gridSize, int baseColor) {
		float[] brokenColor = SchoolYard.breakUpColorInt(baseColor);
		parent.noFill();
		parent.stroke(brokenColor[1], brokenColor[2], brokenColor[3], 55);
		parent.strokeWeight(1);
		for (float i = -gridExtents; i <= gridExtents; i += gridSize) {
			parent.line(-gridExtents, i, gridExtents, i);
			parent.line(i, -gridExtents, i, gridExtents);
		}
	} // end drawGridLight

	/**
	 * This will draw some lines at the origin. Red for X axis, Green for Y
	 * axis, and Blue for Z axis. The longer side indicates the positive
	 * direction
	 */
	public static void drawOrigin() {
		parent.pushStyle();
		parent.colorMode(PConstants.HSB, 360);
		parent.strokeWeight(1);
		parent.stroke(0, 360, 360);
		parent.line(-30, 0, 0, 100, 0, 0);
		parent.stroke(107, 360, 360);
		parent.line(0, -30, 0, 0, 100, 0);
		parent.stroke(236, 360, 360);
		parent.line(0, 0, -30, 0, 0, 100);
		parent.popStyle();
	} // end drawOrigin

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

	/**
	 * Will printStatus with defaults
	 */
	public static void printStatus() {
		printStatus("");
	} // end printStatus

	/**
	 * Will printStatus with a functionName and the defaults
	 * 
	 * @param functionName
	 *            A String used for organizing the code. Just prints out the
	 *            name
	 */
	public static void printStatus(String functionName) {
		printStatus(functionName, "");
	} // end printStatus

	/**
	 * Will printStatus with a functionName, Notes, and the defaults - free &
	 * total memory (divided by 100000), frameRate, frameCount, and the last few
	 * stack trace elements
	 * 
	 * @param functionName
	 *            A String used for organizing the code. Just prints out the
	 *            name
	 * @param notes
	 *            A String used for a manual description
	 */
	public static void printStatus(String functionName, String notes) {
		int freeMemory = PApplet.round((float) Runtime.getRuntime()
				.freeMemory() / 10000) / 10;
		int totalMemory = PApplet.round((float) Runtime.getRuntime()
				.totalMemory() / 10000) / 10;
		String builder = "printStatus:  ";
		if (functionName.length() > 0)
			builder += "_function: " + functionName + "  " + builder;
		builder += "_frame: " + parent.frameCount + "  _frameRate: "
				+ PApplet.nf(parent.frameRate, 0, 2) + "  _free memory: "
				+ freeMemory + "  _totalMemory: " + totalMemory;
		if (notes.length() > 0)
			builder += "\n  _notes: " + notes;
		try {
			// print the last few stack traces
			final StackTraceElement[] ste = Thread.currentThread()
					.getStackTrace();
			builder += "\n  stack trace:";
			for (int i = 1; i < 5; i++) {
				if (i >= ste.length)
					break;
				builder += "\n  " + i + ": " + ste[i];
			}
		} catch (Exception e) {
		}
		System.out.println(builder);
	} // end printStatus

	/**
	 * This will take in a String and split it up into a String[] according to
	 * the maximum width specified. Note: Don't use the special characters, »
	 * (option+9) and ¦ (option+7) in the text. Also note: if a single word is
	 * too long this will NOT break it up.. aka it will extend beyond the
	 * lineWidthIn
	 * 
	 * @param stringIn
	 *            The single String that will be split up
	 * @param lineWidthIn
	 *            The maximum width, in pixels, for each line
	 * @param fontIn
	 *            The Processing font that will be used to determine the line
	 *            widths
	 * @return The String[] of the broken up line
	 */
	public static String[] splitStringIntoLines(String stringIn,
			float lineWidthIn, PFont fontIn) {
		String[] lines = new String[0];
		parent.textFont(fontIn);

		String modifiedText = stringIn.replace(" ", " » "); // option+9
		modifiedText = modifiedText.replace("\n", " ¦ "); // option+7

		String[] broken = PApplet.splitTokens(modifiedText, " ");
		String builder = "";

		for (int i = 0; i < broken.length; i++) {
			if (broken[i].equals("¦")) {
				lines = (String[]) PApplet.append(lines, builder);
				builder = broken[i];
			} else if (parent.textWidth(builder.replace("»", " ").replace("¦",
					"")
					+ broken[i].replace("»", "").replace("¦", "")) <= lineWidthIn) {
				builder += broken[i];
			} else {
				lines = (String[]) PApplet.append(lines, builder);
				builder = broken[i];
			}
		} // end i for
		lines = (String[]) PApplet.append(lines, builder);

		// clean
		for (int i = 0; i < lines.length; i++) {
			lines[i] = lines[i].replace("¦", "\n");
			lines[i] = lines[i].replace("»", " ");
		}
		return lines;
	} // end splitStringIntoLines

	/**
	 * This will rotate a point around an origin point in the x, y, and z
	 * 
	 * @param ptIn
	 *            The original point
	 * @param originIn
	 *            The origin point to rotate around
	 * @param rotX
	 *            How much to rotate around the x axis
	 * @param rotY
	 *            How much to rotate around the y axis
	 * @param rotZ
	 *            How much to rotate around the z axis
	 * @return The resultant PVector
	 */
	public static PVector rotateXYZ(PVector ptIn, PVector originIn,
			double rotX, double rotY, double rotZ) {
		double dx = ptIn.x - originIn.x;
		double dy = ptIn.y - originIn.y;
		double dz = ptIn.z - originIn.z;

		// see
		// http://www.siggraph.org/education/materials/HyperGraph/modeling/mod_tran/3drota.htm
		double xPrime = dx;
		double yPrime = dy;
		double zPrime = dz;
		double xStore = xPrime;
		double yStore = yPrime;
		double zStore = zPrime;

		// z rotation
		if (rotZ != 0) {
			xPrime = xStore * Math.cos(rotZ) - yStore * Math.sin(rotZ);
			yPrime = xStore * Math.sin(rotZ) + yStore * Math.cos(rotZ);
			zPrime = zStore;
		}
		// x rotation
		if (rotX != 0) {
			xStore = xPrime;
			yStore = yPrime;
			zStore = zPrime;
			xPrime = xStore;
			yPrime = yStore * Math.cos(rotX) - zStore * Math.sin(rotX);
			zPrime = yStore * Math.sin(rotX) + zStore * Math.cos(rotX);
		}
		// y rotation
		if (rotY != 0) {
			xStore = xPrime;
			yStore = yPrime;
			zStore = zPrime;
			xPrime = zStore * Math.sin(rotY) + xStore * Math.cos(rotY);
			yPrime = yStore;
			zPrime = zStore * Math.cos(rotY) - xStore * Math.sin(rotY);
		}

		return PVector.add(new PVector((float) xPrime, (float) yPrime,
				(float) zPrime), originIn);
	} // end rotateXYZ

	/**
	 * This will rotate a PVector around an axis for a given amount
	 * 
	 * @param segmentStart
	 *            PVector of the start of the segment
	 * @param segmentEnd
	 *            PVector of the end of the segment
	 * @param angle
	 *            The angle of rotation from the original point around the
	 *            segment axis
	 * @param ptIn
	 *            The original point to be rotated around the axis
	 * @return PVector of the resultant rotation
	 */
	public static PVector rotate3d(PVector segmentStart, PVector segmentEnd,
			float angle, PVector ptIn) {
		PVector directionVector = PVector.sub(segmentStart, segmentEnd);
		directionVector.normalize();
		directionVector.mult(100);

		// move to origin
		PVector transform1 = PVector.sub(segmentEnd, new PVector());
		PVector tempPoint1 = PVector.sub(ptIn, transform1);

		// rotate to goto zy plane
		double directionXY = Math.PI / 2;
		if (directionVector.x != 0)
			directionXY = Math.atan(directionVector.y / directionVector.x);
		if (directionVector.x < 0)
			directionXY += Math.PI;

		PVector tempPoint2 = rotateXYZ(tempPoint1, new PVector(), 0, 0,
				-directionXY);
		PVector directionVector2 = rotateXYZ(directionVector, new PVector(), 0,
				0, -directionXY);
		directionVector2.mult((float) 1.5);

		// rotate to goto x axis
		double directionXZ = Math.PI / 2;
		if (directionVector2.x != 0)
			directionXZ = Math.atan(directionVector2.z / directionVector2.x);

		PVector tempPoint3 = rotateXYZ(tempPoint2, new PVector(), 0,
				directionXZ, 0);
		// drawPoint(tempPoint3, color(0, 0, 255));
		PVector directionVector3 = rotateXYZ(directionVector2, new PVector(),
				0, directionXZ, 0);

		// now rotate around z
		PVector tempPoint4 = rotateXYZ(tempPoint3, new PVector(), angle, 0, 0);

		// bring it back..
		PVector tempPoint5 = rotateXYZ(tempPoint4, new PVector(), 0,
				-directionXZ, 0);
		PVector tempPoint6 = rotateXYZ(tempPoint5, new PVector(), 0, 0,
				directionXY);
		PVector tempPoint7 = PVector.add(tempPoint6, transform1);

		return tempPoint7;
	} // end rotate3d for rotating around a segment

	/**
	 * This will draw a little cross thing at a point location
	 * 
	 * @param p
	 *            The PVector point to be drawn
	 * @param length
	 *            The overall length of the line that will represent the point
	 */
	public static void drawPoint(PVector p, float length) {
		parent.pushMatrix();
		parent.translate(0, 0, p.z);
		parent.translate(p.x, p.y, 0);
		parent.line(-length / 2, 0, 0, length / 2, 0, 0);
		parent.line(0, -length / 2, 0, 0, length / 2, 0);
		parent.line(0, 0, -length / 2, 0, 0, length / 2);
		parent.popMatrix();
	} // end drawPoint

	/**
	 * This will draw a line between two points, and optionally draw the points
	 * too
	 * 
	 * @param segmentTop
	 *            The top of the segment
	 * @param segmentBottom
	 *            The bottom of the segment
	 * @param extendAmount
	 *            How much, if any, to extend the segment
	 * @param drawPoints
	 *            Whether or not to draw the points at the end of the segment.
	 *            Default point size will be the extendAmount / 2 or 5,
	 *            whichever is bigger
	 */
	public static void drawSegment(PVector segmentTop, PVector segmentBottom,
			float extendAmount, boolean drawPoints) {
		if (drawPoints) {
			float ptAmt = extendAmount / 2 > 5 ? extendAmount / 2 : 5;
			drawPoint(segmentTop, ptAmt);
			drawPoint(segmentBottom, ptAmt);
		}
		if (extendAmount > 0) {
			PVector dir = PVector.sub(segmentTop, segmentBottom);
			dir.normalize();
			dir.mult(extendAmount);
			segmentTop.add(dir);
			segmentBottom.sub(dir);
		}
		parent.line(segmentTop.x, segmentTop.y, segmentTop.z, segmentBottom.x,
				segmentBottom.y, segmentBottom.z);

	} // end drawSegment

	/**
	 * This will print out a graph of a double dataset in the console
	 * 
	 * @param label
	 *            The label for the console output of this graph
	 * @param numbers
	 *            The double[] of the numbers
	 * @param steps
	 *            The number of groups to make
	 */
	public static void makeQuickGraph(String label, double[] numbers, int steps) {
		Statistics stat = new Statistics(numbers);
		double median = stat.median();
		double mean = stat.getMean();
		double stdDev = stat.getStdDev();

		double[] minMax = SchoolYard.findMinMax(numbers);
		double range = ((minMax[1] - minMax[0]) / (steps));

		System.out.println("_");
		System.out.println("making quick graph for: " + label);
		System.out.println("  range: " + minMax[0] + "  <>  " + minMax[1]);
		System.out.println("  ...median: " + median + "    ===mean: " + mean
				+ "    ---stdDev: " + stdDev);

		int[] counts = new int[steps + 1];
		double[] stepNum = new double[counts.length];
		for (int i = 0; i < steps + 1; i++) {
			stepNum[i] = minMax[0] + range * i;
		}
		for (int i = 0; i < stepNum.length - 1; i++) {
			
			for (int j = 0; j < numbers.length;j ++) {
				double d = numbers[j];
				if (d >= stepNum[i] && d < stepNum[i + 1] && j < numbers.length - 1) {
					counts[i]++;
				}
				else if (j == numbers.length - 1) {
					if (d == stepNum[i + 1]) counts[i]++;
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
			if (mean - stdDev >= stepNum[i]
					&& mean - stdDev < stepNum[i + 1])
				spacer = "-";
			else if (mean + stdDev >= stepNum[i]
					&& mean + stdDev < stepNum[i + 1])
				spacer = "-";
			else if (median >= stepNum[i]
					&& median < stepNum[i + 1])
				spacer = ".";
			// then mean
			if (mean >= stepNum[i] && mean < stepNum[i + 1])
				spacer = "=";
			//int modifiedSpacerCount = (int) PApplet.map(counts[i], PApplet.min(counts), PApplet.max(counts), 1, 100);
			int modifiedSpacerCount = (int) PApplet.map(counts[i], 0, PApplet.max(counts), 1, 100);
			for (int j = 0; j < modifiedSpacerCount; j++)
				System.out.print(spacer);
			System.out.println("X : " + counts[i]);
		}
		System.out.println("_");
	} // end makeQuickGraph


} // end class MamaPajama

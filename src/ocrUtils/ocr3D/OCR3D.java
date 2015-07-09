package ocrUtils.ocr3D;

import java.util.ArrayList;
import java.awt.geom.*;

import ocrUtils.OCRUtils;
import ocrUtils.Sandbox;
import processing.core.*;

public class OCR3D extends OCRUtils {

	/**
	 * Will draw a grid on the XY plane using the default color
	 * 
	 * @param gridExtents
	 *            How far to make the grid in both directions
	 * @param gridSize
	 *            How many units each grid will be
	 */
	public static void drawGrid(PGraphics pg, float gridExtents, float gridSize) {
		float[] baseColor = { 55, 55, 55, 255 };
		drawGrid(pg, gridExtents, gridSize, Sandbox.composeColorInt(baseColor));
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
	public static void drawGrid(PGraphics pg, float gridExtents, float gridSize, int baseColor) {
		float[] brokenColor = Sandbox.breakUpColorInt(baseColor);
		pg.noFill();
		pg.stroke(brokenColor[1], brokenColor[2], brokenColor[3], 55);
		pg.strokeWeight(1);
		pg.strokeWeight(1);
		for (float i = -gridExtents; i <= gridExtents; i += gridSize) {
			pg.line(-gridExtents, i, gridExtents, i);
			pg.line(i, -gridExtents, i, gridExtents);
		}
	} // end drawGridLight

	/**
	 * This will draw some lines at the origin. Red for X axis, Green for Y
	 * axis, and Blue for Z axis. The longer side indicates the positive
	 * direction
	 */
	public static void drawOrigin(PGraphics pg) {
		pg.pushStyle();
		pg.colorMode(PConstants.HSB, 360);
		pg.strokeWeight(1);
		pg.stroke(0, 360, 360);
		pg.line(-30, 0, 0, 100, 0, 0);
		pg.stroke(107, 360, 360);
		pg.line(0, -30, 0, 0, 100, 0);
		pg.stroke(236, 360, 360);
		pg.line(0, 0, -30, 0, 0, 100);
		pg.popStyle();
	} // end drawOrigin

	/**
	 * This will draw a little cross thing at a point location
	 * 
	 * @param p
	 *            The PVector point to be drawn
	 * @param length
	 *            The overall length of the line that will represent the point
	 */
	public static void drawPoint(PGraphics pg, PVector p, float length) {
		pg.pushMatrix();
		pg.translate(0, 0, p.z);
		pg.translate(p.x, p.y, 0);
		pg.line(-length / 2, 0, 0, length / 2, 0, 0);
		pg.line(0, -length / 2, 0, 0, length / 2, 0);
		pg.line(0, 0, -length / 2, 0, 0, length / 2);
		pg.popMatrix();
	} // end drawPoint

	/**
	 * This will draw a vector with a simple arrowhead describing the direction
	 * in 2D
	 * 
	 * @param vecIn
	 *            The PVector to be drawn
	 * @param departurePoint
	 *            The PVector describing the start point of the PVector to be
	 *            drawn
	 * @param arrowSize
	 *            How big to make the arrowhead lines
	 */
	public static void drawVector2D(PGraphics pg, PVector vecIn, PVector departurePoint, float arrowSize) {
		drawVector(pg, vecIn, departurePoint, arrowSize, false);
	} // drawVector3D

	/**
	 * This will draw a vector with a simple arrowhead describing the direction
	 * in 3D
	 * 
	 * @param vecIn
	 *            The PVector to be drawn
	 * @param departurePoint
	 *            The PVector describing the start point of the PVector to be
	 *            drawn
	 * @param arrowSize
	 *            How big to make the arrowhead lines
	 */
	public static void drawVector3D(PGraphics pg, PVector vecIn, PVector departurePoint, float arrowSize) {
		drawVector(pg, vecIn, departurePoint, arrowSize, true);
	} // drawVector3D

	private static void drawVector(PGraphics pg, PVector vecIn, PVector departurePoint, float arrowSize, boolean is3d) {
		pg.pushMatrix();
		if (is3d)
			pg.translate(departurePoint.x, departurePoint.y, departurePoint.z);
		else
			pg.translate(departurePoint.x, departurePoint.y);
		if (is3d)
			pg.line(0, 0, 0, vecIn.x, vecIn.y, vecIn.z);
		else
			pg.line(0, 0, vecIn.x, vecIn.y);

		PVector pt1 = vecIn.get();
		PVector direction = vecIn.get();
		direction.normalize();
		direction.mult(-1 * arrowSize);
		pt1.add(direction);
		if (is3d) {
			ArrayList<PVector> planeVectors = Sandbox.makePlaneVectors(vecIn);
			planeVectors.get(0).mult(arrowSize);
			pt1.add(planeVectors.get(0));
			pg.line(vecIn.x, vecIn.y, vecIn.z, pt1.x, pt1.y, pt1.z);
			planeVectors.get(0).mult(-2);
			pt1.add(planeVectors.get(0));
			pg.line(vecIn.x, vecIn.y, vecIn.z, pt1.x, pt1.y, pt1.z);
		} else {
			PVector arrow = vecIn.get();
			arrow.normalize();
			arrow = rotateUnitVector2D(arrow, -3 * (float) Math.PI / 4);

			arrow.mult(arrowSize);
			arrow.add(vecIn);
			pg.line(arrow.x, arrow.y, vecIn.x, vecIn.y);
			arrow = vecIn.get();
			arrow.normalize();
			arrow = rotateUnitVector2D(arrow, 3 * (float) Math.PI / 4);
			arrow.mult(arrowSize);
			arrow.add(vecIn);
			pg.line(arrow.x, arrow.y, vecIn.x, vecIn.y);
		}
		pg.popMatrix();
	} // end drawVector

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
	public static void drawSegment(PGraphics pg, PVector segmentTop, PVector segmentBottom, float extendAmount, boolean drawPoints) {
		PVector segTop = segmentTop.get();
		PVector segBot = segmentBottom.get();
		if (drawPoints) {
			float ptAmt = extendAmount / 2 > 5 ? extendAmount / 2 : 5;
			drawPoint(pg, segTop, ptAmt);
			drawPoint(pg, segBot, ptAmt);
		}
		if (extendAmount > 0) {
			PVector dir = PVector.sub(segTop, segBot);
			dir.normalize();
			dir.mult(extendAmount);

			segTop.add(dir);
			segBot.sub(dir);
		}
		pg.line(segTop.x, segTop.y, segTop.z, segBot.x, segBot.y, segBot.z);
	} // end drawSegment

	/**
	 * Will draw a gradient line in 3d
	 * 
	 * @param pg
	 *            The PGraphic to draw on
	 * @param start
	 *            The PVector starting point
	 * @param end
	 *            The PVector ending point
	 * @param startColor
	 *            The starting color for the line
	 * @param endColor
	 *            The ending color for the line
	 * @param divisions
	 *            How many mini segments to break it up into. Should be 2 or
	 *            more
	 */
	public static void drawGradientLine3D(PGraphics pg, PVector start, PVector end, int startColor, int endColor, int divisions) {
		drawGradientLine(pg, start, end, startColor, endColor, divisions, true);
	} // end drawGradientLine3D

	/**
	 * Will draw a gradient line in 2d
	 * 
	 * @param pg
	 *            The PGraphic to draw on
	 * @param start
	 *            The PVector starting point
	 * @param end
	 *            The PVector ending point
	 * @param startColor
	 *            The starting color for the line
	 * @param endColor
	 *            The ending color for the line
	 * @param divisions
	 *            How many mini segments to break it up into. Should be 2 or
	 *            more
	 */
	public static void drawGradientLine2D(PGraphics pg, PVector start, PVector end, int startColor, int endColor, int divisions) {
		drawGradientLine(pg, start, end, startColor, endColor, divisions, false);
	} // end drawGradientLine3D

	//
	private static void drawGradientLine(PGraphics pg, PVector startIn, PVector end, int startColor, int endColor, int divisions, boolean is3D) {
		PVector start = startIn.get();
		pg.pushStyle();
		if (divisions < 1) {
			// nothing
		} else if (divisions == 1) {
			pg.stroke(startColor);
			if (is3D)
				pg.line(start.x, start.y, start.z, end.x, end.y, end.z);
			else
				pg.line(start.x, start.y, end.x, end.y);
		} else {
			// split it up
			PVector normal = PVector.sub(end, start);
			normal.div(divisions);
			for (int i = 1; i <= divisions; i++) {
				pg.stroke(parent.lerpColor(startColor, endColor, PApplet.map(i, 1, divisions, 0, 1f)));
				if (is3D)
					pg.line(start.x, start.y, start.z, start.x + normal.x, start.y + normal.y, start.z + normal.z);
				else
					pg.line(start.x, start.y, start.x + normal.x, start.y + normal.y);
				start.add(normal);
			}
		}
		pg.popStyle();
	} // end drawGradientLine

	// how about a dashed line!

	/**
	 * This will draw a dashed line in 3D
	 * 
	 * @param pg
	 *            The PGraphic to draw on
	 * @param start
	 *            The starting PVector
	 * @param end
	 *            The ending PVector
	 * @param segLength
	 *            How long to make the line segment
	 * @param spacingLength
	 *            How long to space between line segments
	 */
	public static void drawDashedLine3D(PGraphics pg, PVector start, PVector end, float segLength, float spacingLength) {
		drawDashedLine(pg, start, end, segLength, spacingLength, true);
	} // end drawDottedLine3D

	/**
	 * This will draw a dashed line in 2D
	 * 
	 * @param pg
	 *            The PGraphic to draw on
	 * @param start
	 *            The starting PVector
	 * @param end
	 *            The ending PVector
	 * @param segLength
	 *            How long to make the line segment
	 * @param spacingLength
	 *            How long to space between line segments
	 */
	public static void drawDashedLine2D(PGraphics pg, PVector start, PVector end, float segLength, float spacingLength) {
		drawDashedLine(pg, start, end, segLength, spacingLength, false);
	} // end drawDottedLine3D
		//

	private static void drawDashedLine(PGraphics pg, PVector start, PVector end, float segLength, float spacingLength, boolean is3D) {
		if (!is3D) {
			start.z = 0;
			end.z = 0;
		}
		float dist = start.dist(end);
		float x1 = start.x;
		float x2 = end.x;
		float y1 = start.y;
		float y2 = end.y;
		float z1 = start.z;
		float z2 = end.z;
		float distA = 0f;
		float distB = segLength;
		for (float x = 0; x < dist; x += spacingLength + segLength) {
			if (distB > dist)
				distB = dist;
			x1 = PApplet.map(distA, 0, dist, start.x, end.x);
			x2 = PApplet.map(distB, 0, dist, start.x, end.x);
			y1 = PApplet.map(distA, 0, dist, start.y, end.y);
			y2 = PApplet.map(distB, 0, dist, start.y, end.y);
			z1 = PApplet.map(distA, 0, dist, start.z, end.z);
			z2 = PApplet.map(distB, 0, dist, start.z, end.z);
			if (is3D)
				pg.line(x1, y1, z1, x2, y2, z2);
			else
				pg.line(x1, y1, x2, y2);
			distA += spacingLength + segLength;
			distB += spacingLength + segLength;
		}
	} // end drawDottedLine

	/**
	 * This will draw a band.. essentially a filled arc made of faceted edges. Note that it will
	 * always go clockwise. So mind the starting angle.
	 * 
	 * @param pg
	 *            The PGraphics to draw on
	 * @param x
	 *            The x center point for the band
	 * @param y
	 *            The y center point for the band
	 * @param innerRad
	 *            The inner radius of the band
	 * @param outerRad
	 *            The outer radius of the band
	 * @param startAngle
	 *            The starting angle for the band in radians
	 * @param endAngle
	 *            The ending angle in radians
	 * @param divisions
	 *            How many divisions to split up the entire circle by. More =
	 *            smoother/slower
	 */
	public static void drawBand(PGraphics pg, float x, float y, float innerRad, float outerRad, float startAngle, float endAngle, int divisions) {
		// divisions are for the entire circle, so this portion will just ceil
		// up to the required divisions
		// assume it always goes clockwise
		// adjust the end angle if it is before the startAngle
		startAngle %= 2 * (float) Math.PI;
		endAngle %= 2 * (float) Math.PI;
		if (endAngle < startAngle) {
			endAngle += 2 * (float) Math.PI;
		}

		float totalCircumference = outerRad * (float) Math.PI * 2;
		float outerArcLength = totalCircumference * (endAngle - startAngle) / (2 * (float) Math.PI);
		int divisionsToUse = PApplet.ceil(divisions * outerArcLength / totalCircumference);

		float ptx, pty, thisAngle;

		int manualBreakout = 100;
		int breakoutCount = 0;
		if (divisionsToUse <= 1) {
			while (true) {
				divisions = (int) (divisions * 1.05);
				divisionsToUse = PApplet.ceil(divisions * outerArcLength / totalCircumference);
				if (breakoutCount++ > manualBreakout || divisionsToUse > 1) {
					break;
				}
			}
		}
		if (divisionsToUse <= 1)
			System.err.println("Band not drawn, increase division count");

		pg.beginShape();
		// outerband
		for (int i = 0; i < divisionsToUse; i++) {
			thisAngle = PApplet.map(i, 0, divisionsToUse - 1, startAngle, endAngle);
			ptx = x + outerRad * (float) Math.cos(thisAngle);
			pty = y + outerRad * (float) Math.sin(thisAngle);
			pg.vertex(ptx, pty);
		}
		// innerband
		for (int i = divisionsToUse - 1; i >= 0; i--) {
			thisAngle = PApplet.map(i, 0, divisionsToUse - 1, startAngle, endAngle);
			ptx = x + innerRad * (float) Math.cos(thisAngle);
			pty = y + innerRad * (float) Math.sin(thisAngle);
			pg.vertex(ptx, pty);
		}
		pg.endShape(PConstants.CLOSE);
	} // end drawBand

	/*
	 * // do this later.... public static void drawRectangle3Pt(PVector a,
	 * PVector b, PVector c) { parent.beginShape(); vertex(a.x, a.y, a.z);
	 * vertex(upper.x, lower.y, lower.z); vertex(upper.x, upper.y, upper.z);
	 * parent.endShape(PApplet.CLOSE); } // end drawRectangle
	 */

	/**
	 * Assumes an orthogonal series of points.. both are on x, y, or z plane
	 * 
	 * @param a
	 *            First point
	 * @param b
	 *            Second point
	 */
	public static void drawRectXYZ(PGraphics pg, PVector a, PVector b) {
		pg.beginShape();
		if (a.x == b.x) {
			pg.vertex(a.x, a.y, a.z);
			pg.vertex(a.x, b.y, a.z);
			pg.vertex(a.x, b.y, b.z);
			pg.vertex(a.x, a.y, b.z);
		} else if (a.y == b.y) {
			pg.vertex(a.x, a.y, a.z);
			pg.vertex(b.x, a.y, a.z);
			pg.vertex(b.x, a.y, b.z);
			pg.vertex(a.x, a.y, b.z);
		} else if (a.z == b.z) {
			pg.vertex(a.x, a.y, a.z);
			pg.vertex(b.x, a.y, a.z);
			pg.vertex(b.x, b.y, a.z);
			pg.vertex(a.x, b.y, a.z);
		}
		pg.endShape(PApplet.CLOSE);
	} // end drawRectXYZ

	/**
	 * This will draw a box based on two corner points in space. By default it
	 * will be only the lines.
	 * 
	 * @param lower
	 *            One of the PVector corner points
	 * @param upper
	 *            The other PVector corner point
	 */
	public static void drawBoxXYZ(PGraphics pg, PVector lower, PVector upper) {
		drawBoxXYZ(pg, lower, upper, false);
	} // end drawBox

	/**
	 * This will draw a box based on two corner points in space
	 * 
	 * @param lower
	 *            One of the PVector corner points
	 * @param upper
	 *            The other PVector corner point
	 * @param fill
	 *            Boolean whether or not to use a fill on the box or just draw
	 *            the lines
	 */
	public static void drawBoxXYZ(PGraphics pg, PVector lower, PVector upper, boolean fill) {
		if (fill) {
			pg.beginShape();
			drawRectXYZ(pg, new PVector(lower.x, lower.y, lower.z), new PVector(upper.x, upper.y, lower.z));
			drawRectXYZ(pg, new PVector(lower.x, lower.y, upper.z), new PVector(upper.x, upper.y, upper.z));
			drawRectXYZ(pg, new PVector(lower.x, lower.y, lower.z), new PVector(upper.x, lower.y, upper.z));
			drawRectXYZ(pg, new PVector(lower.x, lower.y, lower.z), new PVector(lower.x, upper.y, upper.z));
			drawRectXYZ(pg, new PVector(upper.x, upper.y, upper.z), new PVector(lower.x, upper.y, lower.z));
			drawRectXYZ(pg, new PVector(upper.x, upper.y, upper.z), new PVector(upper.x, lower.y, lower.z));
			pg.endShape(PApplet.CLOSE);
		} else {
			pg.line(lower.x, lower.y, lower.z, upper.x, lower.y, lower.z);
			pg.line(lower.x, lower.y, lower.z, lower.x, upper.y, lower.z);
			pg.line(lower.x, lower.y, lower.z, lower.x, lower.y, upper.z);
			pg.line(upper.x, upper.y, upper.z, lower.x, upper.y, upper.z);
			pg.line(upper.x, upper.y, upper.z, upper.x, lower.y, upper.z);
			pg.line(upper.x, upper.y, upper.z, upper.x, upper.y, lower.z);
			pg.line(lower.x, lower.y, upper.z, upper.x, lower.y, upper.z);
			pg.line(lower.x, lower.y, upper.z, lower.x, upper.y, upper.z);
			pg.line(lower.x, upper.y, lower.z, lower.x, upper.y, upper.z);
			pg.line(upper.x, upper.y, lower.z, lower.x, upper.y, lower.z);
			pg.line(upper.x, upper.y, lower.z, upper.x, lower.y, lower.z);
			pg.line(upper.x, lower.y, upper.z, upper.x, lower.y, lower.z);
		}
	} // end drawBox with specifics

	/**
	 * Will draw a plane as specified without a fill color for the plane
	 * 
	 * @param planePoint
	 *            A PVector point on the plane
	 * @param planeNormal
	 *            The PVector of the plane normal
	 * @param planeSize
	 *            How big to draw the plane - overall
	 * @param outlineColor
	 *            The color used to outline the plane
	 */
	public static void drawPlane(PGraphics pg, PVector planePoint, PVector planeNormal, float planeSize, int outlineColor) {
		drawPlane(pg, planePoint, planeNormal, planeSize, outlineColor, -1);
	}

	/**
	 * Will draw a plane as specified
	 * 
	 * @param planePoint
	 *            A PVector point on the plane
	 * @param planeNormal
	 *            The PVector of the plane normal
	 * @param planeSize
	 *            How big to draw the plane - overall
	 * @param outlineColor
	 *            The color used to outline the plane
	 * @param fillColor
	 *            The color to fill the plane
	 */
	public static void drawPlane(PGraphics pg, PVector planePoint, PVector planeNormal, float planeSize, int outlineColor, int fillColor) {
		float[] brokenOutlineColor = Sandbox.breakUpColorInt(outlineColor);
		float[] brokenFillColor = Sandbox.breakUpColorInt(fillColor);
		pg.stroke(brokenOutlineColor[1], brokenOutlineColor[2], brokenOutlineColor[3], brokenOutlineColor[0]);
		float planePointLength = 5f;
		drawPoint(pg, planePoint, planePointLength);
		ArrayList<PVector> planeVectors = Sandbox.makePlaneVectors(planeNormal);
		PVector up = planeVectors.get(0);
		PVector rt = planeVectors.get(1);
		up.mult(planeSize / 2);
		rt.mult(planeSize / 2);
		PVector cornerA = planePoint.get();
		cornerA.sub(up);
		cornerA.sub(rt);
		drawPoint(pg, cornerA, planePointLength);
		PVector cornerB = planePoint.get();
		cornerB.add(up);
		cornerB.sub(rt);
		drawPoint(pg, cornerB, planePointLength);
		PVector cornerC = planePoint.get();
		cornerC.add(up);
		cornerC.add(rt);
		drawPoint(pg, cornerC, planePointLength);
		PVector cornerD = planePoint.get();
		cornerD.sub(up);
		cornerD.add(rt);
		drawPoint(pg, cornerD, planePointLength);
		if (fillColor != -1)
			pg.fill(brokenFillColor[1], brokenFillColor[2], brokenFillColor[3], brokenFillColor[0]);
		else
			pg.noFill();
		pg.stroke(brokenOutlineColor[1], brokenOutlineColor[2], brokenOutlineColor[3], brokenOutlineColor[0]);
		pg.beginShape();
		pg.vertex(cornerA.x, cornerA.y, cornerA.z);
		pg.vertex(cornerB.x, cornerB.y, cornerB.z);
		pg.vertex(cornerC.x, cornerC.y, cornerC.z);
		pg.vertex(cornerD.x, cornerD.y, cornerD.z);
		pg.endShape(PApplet.CLOSE);

		float normalLength = planeSize / 4;
		PVector otherPoint = planePoint.get();
		PVector normalizedNormal = planeNormal.get();
		normalizedNormal.normalize();
		normalizedNormal.mult(normalLength);
		otherPoint.add(normalizedNormal);
		pg.stroke(brokenOutlineColor[1], brokenOutlineColor[2], brokenOutlineColor[3], brokenOutlineColor[0]);
		pg.line(otherPoint.x, otherPoint.y, otherPoint.z, planePoint.x, planePoint.y, planePoint.z);
		pg.noStroke();
		pg.fill(brokenOutlineColor[1], brokenOutlineColor[2], brokenOutlineColor[3], brokenOutlineColor[0]);
		drawPoint(pg, otherPoint, planePointLength);
	} // end drawPlane

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
	public static PVector rotateXYZ(PVector ptIn, PVector originIn, double rotX, double rotY, double rotZ) {
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

		return PVector.add(new PVector((float) xPrime, (float) yPrime, (float) zPrime), originIn);
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
	public static PVector rotate3D(PVector segmentStart, PVector segmentEnd, float angle, PVector ptIn) {
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

		PVector tempPoint2 = rotateXYZ(tempPoint1, new PVector(), 0, 0, -directionXY);
		PVector directionVector2 = rotateXYZ(directionVector, new PVector(), 0, 0, -directionXY);
		directionVector2.mult((float) 1.5);

		// rotate to goto x axis
		double directionXZ = Math.PI / 2;
		if (directionVector2.x != 0)
			directionXZ = Math.atan(directionVector2.z / directionVector2.x);

		PVector tempPoint3 = rotateXYZ(tempPoint2, new PVector(), 0, directionXZ, 0);
		// drawPoint(tempPoint3, color(0, 0, 255));

		// now rotate around z
		PVector tempPoint4 = rotateXYZ(tempPoint3, new PVector(), angle, 0, 0);

		// bring it back..
		PVector tempPoint5 = rotateXYZ(tempPoint4, new PVector(), 0, -directionXZ, 0);
		PVector tempPoint6 = rotateXYZ(tempPoint5, new PVector(), 0, 0, directionXY);
		PVector tempPoint7 = PVector.add(tempPoint6, transform1);

		return tempPoint7;
	} // end rotate3d for rotating around a segment

	/**
	 * This will take in a unit vector and rotate it a specific amount
	 * 
	 * @param unitVector
	 *            The unit PVector
	 * @param angle
	 *            The amount to be rotated
	 * @return The new unit PVector
	 */
	public static PVector rotateUnitVector2D(PVector unitVector, float angle) {
		PVector rotated = unitVector.get();
		float currentAngle = getAdjustedRotation(rotated);
		currentAngle += angle;
		rotated.set((float) Math.cos(currentAngle), (float) Math.sin(currentAngle));
		return rotated;
	} // end rotateUnitVector2d

	/**
	 * This should return an adjusted rotation float for a vector
	 * 
	 * @param rotationIn
	 *            The vector to get the rotation float for
	 * @return The float of the adjusted rotation
	 */
	public static float getAdjustedRotation(PVector rotationIn) {
		float newRotationF = 0f;
		if (rotationIn.x != 0)
			newRotationF = (float) Math.atan(rotationIn.y / rotationIn.x);
		else
			newRotationF = -(float) Math.PI / 2;
		if (rotationIn.x <= 0) {
			if (rotationIn.x != 0)
				newRotationF += (float) Math.PI;
			else if (rotationIn.y >= 0)
				newRotationF += (float) Math.PI;
		}
		return newRotationF;
	} // end getAdjustedRoation

	/**
	 * This will find the signed angle between two 2d PVectors
	 * 
	 * @param a
	 *            The first PVector
	 * @param b
	 *            The second PVector
	 * @return the signed angle from the first to the second PVector. Positive =
	 *         clockwise
	 */
	public static float findSignedAngle2D(PVector a, PVector b) {
		return (float) Math.atan2(a.x * b.y - a.y * b.x, a.x * b.x + a.y * b.y);
	} // end findSignedAngle2D

	// for a parallel line it will return null? maybe
	// http://compgroups.net/comp.soft-sys.matlab/distance-between-2-lines-in-3d-space/872966
	/**
	 * This will find the closest connection between two nonparallel lines
	 * 
	 * @param p1
	 *            First point of segment 1
	 * @param p2
	 *            Second point of segment 1
	 * @param p3
	 *            First point of segment 2
	 * @param p4
	 *            Second point of segment 2
	 * @return An ArrayList of PVectors with the two connection points
	 */
	public static ArrayList<PVector> findNearestLineConnection(PVector p1, PVector p2, PVector p3, PVector p4) {
		ArrayList<PVector> pts = new ArrayList<PVector>();
		PVector test1 = PVector.sub(p2, p1);
		PVector test2 = PVector.sub(p4, p3);
		test1.normalize();
		test2.normalize();
		if (test1.x == test2.x && test1.y == test2.y && test1.z == test2.z)
			return null;
		PVector u = PVector.sub(p2, p1);
		PVector v = PVector.sub(p4, p3);
		PVector w = new PVector();
		PVector.cross(u, v, w);
		PVector x = p1.get();
		PVector p3p1Sub = PVector.sub(p3, p1);
		PVector p3p1CrossV = new PVector();
		PVector.cross(p3p1Sub, v, p3p1CrossV);
		PVector wwu = u.get();
		float p3p1DotVFun = PVector.dot(p3p1CrossV, w);
		float wwDot = PVector.dot(w, w);
		wwu.mult(p3p1DotVFun / wwDot);
		x.add(wwu);
		PVector y = p3.get();
		PVector p3p1CrossU = new PVector();
		PVector.cross(p3p1Sub, u, p3p1CrossU);
		float p3p1DotUFun = PVector.dot(p3p1CrossU, w);
		PVector wwv = v.get();
		wwv.mult(p3p1DotUFun / wwDot);
		y.add(wwv);
		pts.add(x);
		pts.add(y);
		return pts;
	} // end findNearestLineConnection

	/**
	 * This function will find the closest point on a line from a given point.
	 * It may not be the most efficient method, but eh..
	 * 
	 * @param targetPoint
	 *            The PVector in question
	 * @param p1
	 *            The first PVector point that defines the line
	 * @param p2
	 *            The second PVector point that defines the line
	 * @return The PVector of the closest point
	 */
	public static PVector findPointLineConnection(PVector targetPoint, PVector p1, PVector p2) {
		PVector closest = targetPoint.get();
		PVector ln = PVector.sub(p1, p2);
		if (ln.mag() == 0)
			return null;
		PVector right = (Sandbox.makePlaneVectors(ln)).get(1);
		PVector targetPoint2 = PVector.add(targetPoint, right);
		ArrayList<PVector> connection = findNearestLineConnection(p1, p2, targetPoint, targetPoint2);
		if (connection.get(0).x == targetPoint.x && connection.get(0).y == targetPoint.y && connection.get(0).z == targetPoint.z)
			closest = connection.get(1);
		else
			closest = connection.get(0);
		return closest;
	} // end findPointLineConnection

	/**
	 * Will find the intersection of a segment and segment in 2d space. This
	 * will return null if the segments do not intersect.
	 * 
	 * @param pt1
	 *            First point of the segment
	 * @param pt2
	 *            Second point of the segment
	 * @param pt3
	 *            First point of the segment
	 * @param pt4
	 *            Second point of the segment
	 * @return
	 */
	public static PVector find2DSegmentSegmentIntersection(PVector pt1, PVector pt2, PVector pt3, PVector pt4) {
		PVector intersection = null;
		Line2D.Float lnA = new Line2D.Float(pt1.x, pt1.y, pt2.x, pt2.y);
		Line2D.Float lnB = new Line2D.Float(pt3.x, pt3.y, pt4.x, pt4.y);
		if (lnA.intersectsLine(lnB)) {
			Point2D intersectionPoint = getIntersectionPoint(lnA, lnB);
			if (intersectionPoint != null) {
				intersection = new PVector((float) intersectionPoint.getX(), (float) intersectionPoint.getY());
			}
		}
		return intersection;
	}// end find2DSegmentSegmentIntersection

	// from https://community.oracle.com/thread/1264395?start=0&tstart=0
	private static Point2D.Float getIntersectionPoint(Line2D.Float line1, Line2D.Float line2) {
		if (!line1.intersectsLine(line2))
			return null;
		double px = line1.getX1(), py = line1.getY1(), rx = line1.getX2() - px, ry = line1.getY2() - py;
		double qx = line2.getX1(), qy = line2.getY1(), sx = line2.getX2() - qx, sy = line2.getY2() - qy;
		double det = sx * ry - sy * rx;
		if (det == 0) {
			return null;
		} else {
			double z = (sx * (qy - py) + sy * (px - qx)) / det;
			if (z == 0 || z == 1) {
				return null; // intersection at end point!

			}
			return new Point2D.Float((float) (px + z * rx), (float) (py + z * ry));
		}
	} // end getIntersectionPoint

	/**
	 * This will check whether or not a point is on a segment given a tolerance
	 * 
	 * @param pt1
	 *            The point in question
	 * @param pt2
	 *            The first point of the segment
	 * @param pt3
	 *            The second point of the segment
	 * @param tolerance
	 *            The amount of tolerance allowed.. because the world is not a
	 *            perfect place
	 * @return true if the point is on the segment
	 */
	public static boolean pointIsOnSegment(PVector pt1, PVector pt2, PVector pt3, float tolerance) {
		if (pointIsOnLine(pt1, pt2, pt3, tolerance)) {
			float x1, x2, y1, y2, z1, z2;
			if (pt2.x < pt3.x) {
				x1 = pt2.x - tolerance;
				x2 = pt3.x + tolerance;
			} else {
				x2 = pt2.x - tolerance;
				x1 = pt3.x + tolerance;
			}
			if (pt2.y < pt3.y) {
				y1 = pt2.y - tolerance;
				y2 = pt3.y + tolerance;
			} else {
				y2 = pt2.y - tolerance;
				y1 = pt3.y + tolerance;
			}
			if (pt2.z < pt3.z) {
				z1 = pt2.z - tolerance;
				z2 = pt3.z + tolerance;
			} else {
				z2 = pt2.z - tolerance;
				z1 = pt3.z + tolerance;
			}
			if (pt1.x >= x1 && pt1.x <= x2 && pt1.y >= y1 && pt1.y <= y2 && pt1.z >= z1 && pt1.z <= z2) {
				return true;
			}
		}
		return false;
	} // end pointIsOnSegment

	/**
	 * This will check whether or not a point is on a line given a tolerance
	 * 
	 * @param pt1
	 *            The point in question
	 * @param pt2
	 *            The first point of the line
	 * @param pt3
	 *            The second point of the line
	 * @param tolerance
	 *            Given a bit of tolerance if you want
	 * @return true if the point is on the line
	 */
	public static boolean pointIsOnLine(PVector pt1, PVector pt2, PVector pt3, float tolerance) {
		PVector dirA = PVector.sub(pt1, pt3);
		PVector dirB = PVector.sub(pt1, pt2);
		PVector cross = new PVector();
		PVector.cross(dirA, dirB, cross);
		if (cross.mag() <= tolerance)
			return true;
		return false;
	} // end pointIsOnLine

	/**
	 * Will find the intersection of a line and segment in 2d space
	 * 
	 * @param pt1
	 *            First point of the ray
	 * @param pt2
	 *            Second point of the ray
	 * @param pt3
	 *            First point of the segment
	 * @param pt4
	 *            Second point of the segment
	 * @param projectionOk
	 *            Whether or not it is ok to project from the line. If not it
	 *            will return null if the segments do not intersect
	 * @return
	 */
	public static PVector find2DRaySegmentIntersection(PVector pt1, PVector pt2, PVector pt3, PVector pt4) {
		PVector intersection = null;
		Line2D.Float lnA = new Line2D.Float(pt1.x, pt1.y, pt2.x, pt2.y);
		Line2D.Float lnB = new Line2D.Float(pt3.x, pt3.y, pt4.x, pt4.y);

		ArrayList<PVector> intersectionTest = findNearestLineConnection(pt1, pt2, pt3, pt4);

		if (intersectionTest != null && intersectionTest.size() == 2) {
			PVector tempIntersection = intersectionTest.get(0);
			float x1, x2, y1, y2;
			if (pt3.x < pt4.x) {
				x1 = pt3.x;
				x2 = pt4.x;
			} else {
				x2 = pt3.x;
				x1 = pt4.x;
			}
			if (pt3.y < pt4.y) {
				y1 = pt3.y;
				y2 = pt4.y;
			} else {
				y2 = pt3.y;
				y1 = pt4.y;
			}

			if (tempIntersection.x >= x1 && tempIntersection.x <= x2 && tempIntersection.y >= y1 && tempIntersection.y <= y2) {
				intersection = tempIntersection;
			}

		}

		return intersection;
	} // end find2DRaySegmentIntersection

	/**
	 * This function will find the point where a segment/line intersects with a
	 * plane
	 * 
	 * @param pt1
	 *            PVector of the first point of the segment
	 * @param pt2
	 *            PVector of the second point of the segment
	 * @param planePt
	 *            PVector point on the plane to be intersected
	 * @param norm
	 *            PVector defining the normal of the plane
	 * @param projectionOk
	 *            boolean of whether or not projection of the segment to the
	 *            plane is ok if they don't intersect, [in other words, treating
	 *            the segment as an infinite line]
	 * @return null if the segment does not intersect the plane and projectionOk
	 *         passed in as false, otherwise passes back the intersection point
	 */
	public static PVector findPlaneSegmentIntersection(PVector pt1, PVector pt2, PVector planePt, PVector norm, boolean projectionOk) {
		PVector intersection = new PVector();
		PVector sub1 = PVector.sub(planePt, pt1);
		PVector sub2 = PVector.sub(pt2, pt1);
		float top = norm.dot(sub1);
		float bot = norm.dot(sub2);
		float u = top / bot;
		if (!projectionOk && (u <= 0 || u >= 1))
			return null;
		PVector dir = PVector.sub(pt2, pt1);
		float dist = pt1.dist(pt2) * u;
		dir.normalize();
		dir.mult(dist);
		intersection = pt1.get();
		intersection.add(dir);
		return intersection;
	} // end findPLaneSegmentIntersection

	/**
	 * This will find the AVERAGE of all PVectors in a list
	 * 
	 * @param pointsIn
	 *            The ArrayList of PVectors to be averaged
	 * @return The PVector average
	 */
	public static PVector findGroupCenter(ArrayList<PVector> pointsIn) {
		PVector newCenter = new PVector();
		if (pointsIn.size() > 0) {
			for (PVector p : pointsIn)
				newCenter.add(p);
			newCenter.div(pointsIn.size());
		}
		return newCenter;
	} // end getGroupCenter

	/**
	 * This will find the center of the XYZ extents of an ArrayList of PVectors
	 * 
	 * @param groupIn
	 *            The ArrayList of PVector objects
	 * @return A PVector in the middle of the XYZ extents
	 */
	public static PVector findExtentsCenter(ArrayList<PVector> groupIn) {
		if (groupIn.size() == 0)
			return null;
		ArrayList<PVector> extents = findExtents(groupIn);
		return new PVector((extents.get(0).x + extents.get(1).x) / 2, (extents.get(0).y + extents.get(1).y) / 2, (extents.get(0).z + extents.get(1).z) / 2);
	} // end findExtentsCenter

	/**
	 * This will find the XYZ extents of an ArrayList of PVectors
	 * 
	 * @param groupIn
	 *            The ArrayList of PVectors
	 * @return An ArrayList of PVectors the lowerLeft and upperRight
	 */
	public static ArrayList<PVector> findExtents(ArrayList<PVector> groupIn) {
		if (groupIn.size() == 0)
			return null;
		ArrayList<PVector> extents = new ArrayList<PVector>();
		float minX = groupIn.get(0).x;
		float maxX = minX;
		float minY = groupIn.get(0).y;
		float maxY = minY;
		float minZ = groupIn.get(0).z;
		float maxZ = minZ;
		for (int i = 0; i < groupIn.size(); i++) {
			minX = minX < groupIn.get(i).x ? minX : groupIn.get(i).x;
			maxX = maxX > groupIn.get(i).x ? maxX : groupIn.get(i).x;
			minY = minY < groupIn.get(i).y ? minY : groupIn.get(i).y;
			maxY = maxY > groupIn.get(i).y ? maxY : groupIn.get(i).y;
			minZ = minZ < groupIn.get(i).z ? minZ : groupIn.get(i).z;
			maxZ = maxZ > groupIn.get(i).z ? maxZ : groupIn.get(i).z;
		}
		PVector lowerLeft = new PVector(minX, minY, minZ);
		PVector upperRight = new PVector(maxX, maxY, maxZ);
		extents.add(lowerLeft);
		extents.add(upperRight);
		return extents;
	} // end findExtents

	/**
	 * This will project a PVector onto a plane which is defined by a point and
	 * a normal
	 * 
	 * @param ptIn
	 *            The original point
	 * @param projectionNormal
	 *            The direction which the pt should be projected with
	 * @param planePt
	 *            A point on the projection plane
	 * @param planeNormal
	 *            The normal of the projection plane
	 * @return Returns the PVector of the projection point
	 */
	public static PVector projectPVectorToPlane(PVector ptIn, PVector projectionNormal, PVector planePt, PVector planeNormal) {
		PVector secondSegmentPt = PVector.add(ptIn, projectionNormal);
		return findPlaneSegmentIntersection(ptIn, secondSegmentPt, planePt, planeNormal, true);
	} // end projectPVectorToPlane

	/**
	 * This will project a series of points onto a plane
	 * 
	 * @param ptsIn
	 *            The PVector[] of the points to be projected
	 * @param projectionNormal
	 *            The direction which the pt should be projected with
	 * @param planePt
	 *            A point on the projection plane
	 * @param planeNormal
	 *            The normal of the projection plane * @return A PVector[] of
	 *            the projected points
	 */
	public static PVector[] projectPVectorToPlane(PVector[] ptsIn, PVector projectionNormal, PVector planePt, PVector planeNormal) {
		PVector[] results = new PVector[ptsIn.length];
		for (int i = 0; i < ptsIn.length; i++) {
			results[i] = projectPVectorToPlane(ptsIn[i], projectionNormal, planePt, planeNormal);
		}
		return results;
	} // end projectPVectorToPlane

} // end class OCR3D

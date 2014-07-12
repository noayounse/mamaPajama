package ocrUtils;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import processing.data.JSONObject;

public class Colors extends OCRUtils {
	public static boolean messagesOn = true;
	public String name = "unnamed scheme";
	public LinkedHashMap<String, Integer> colors = new LinkedHashMap();
	public boolean valid = false;

	/*
	 * // public static void begin(PApplet _parent) { parent = _parent; } // end
	 * begin
	 */

	/**
	 * This will return the color, as an int, by name
	 * 
	 * @param colorName
	 *            String of the name of the color.. assuming it has been loaded
	 *            via readColors
	 * @return the target color
	 */
	public int getColor(String colorName) {
		try {
			return (Integer) colors.get(colorName);
		} catch (Exception e) {
			if (messagesOn)
				System.out.println("could not find color " + colorName
						+ " in scheme: " + name);
		}
		return 0;
	} // end getColor

	/**
	 * This will manually add a color to the colors hashmap
	 * 
	 * @param colorName
	 *            String of the name of the color to add
	 * @param c
	 *            int of the actual color to match the name
	 */
	public void addColor(String colorName, int c) {
		colors.put(colorName, c);
		reorganizeList();
	} // end addColor

	/**
	 * This will read in a json file and try to create a new Colors object with
	 * these colors
	 * 
	 * @param file
	 *            String of the location of the color json file
	 */
	public void readColors(String file) {
		try {
			JSONObject json = parent.loadJSONObject(file);
			Iterator i = json.keyIterator();
			Color c = new Color(0);

			while (i.hasNext()) {
				String key = (String) i.next();
				if (key.equals("name"))
					name = json.getString("name");
				else {
					String colorString = json.getString(key);
					if (colorString.contains(",")) {
						// println("splitting up an rgb color: " + colorString);
						String[] split = parent.split(colorString, ",");
						float r = Float.parseFloat(split[0]);
						float g = Float.parseFloat(split[1]);
						float b = Float.parseFloat(split[2]);
						c = new Color(r / 255f, g / 255f, b / 255f);
					} else if (colorString.contains("#")
							|| colorString.length() == 6
							|| colorString.length() == 3) {
						// println("splitting up a hex color: " + colorString);
						colorString = colorString.replace("#", "");
						if (colorString.length() == 3) {
							colorString = "" + colorString.charAt(0)
									+ colorString.charAt(0)
									+ colorString.charAt(1)
									+ colorString.charAt(1)
									+ colorString.charAt(2)
									+ colorString.charAt(2);
						}
						colorString = "FF" + colorString;
						c = new Color(parent.unhex(colorString));
					}

					colors.put(key, c.getRGB());

				}
			}
			reorganizeList();
			valid = true;
		} catch (Exception e) {
			System.out.print("could not read file: " + file);
		}
		if (messagesOn)
			System.out.println("read in " + colors.size() + " colors for "
					+ name);
	} // end readColors

	/**
	 * This simple function will write out a template that can be manually
	 * edited Note that each json file needs to include its own name field
	 */
	public static void writeColorTemplate() {
		// manual.. to preserve the order
		PrintWriter output = parent.createWriter("template.json");
		output.println("{");
		output.println(" \"name\": \"unnamed scheme\",");
		output.println(" \"p1\": \"#40f03f\",");
		output.println(" \"p2\": \"255, 0, 13\",");
		output.println(" \"p3\": \"255, 250, 113\",");
		output.println(" \"s1\": \"#aa33aa\",");
		output.println(" \"s2\": \"#aa33aa\",");
		output.println(" \"s3\": \"#aa33aa\",");
		output.println("}");
		output.flush();
		output.close();
	} // end writeColorTemplate

	private void reorganizeList() {
		String[] keyReference = new String[0];
		for (Map.Entry me : colors.entrySet())
			keyReference = (String[]) parent.append(keyReference,
					(String) me.getKey());
		keyReference = parent.sort(keyReference);
		LinkedHashMap<String, Integer> sortedColors = new LinkedHashMap();
		for (String s : keyReference)
			sortedColors.put(s, (Integer) colors.get(s));
		colors = sortedColors;
	} // end reorganizeList

	/**
	 * This will return the color directly across from the input color see
	 * http://edtech2.boisestate.edu/eckela/images/Color-Schemes1.gif for color
	 * stuff
	 * 
	 * @param c
	 * @return
	 */
	public static int getComplimentary(int c) {
		parent.pushStyle();
		parent.colorMode(parent.HSB, 360);
		int cc = c;
		float newHue = (parent.hue(c) + 180) % 360;
		float newSat = parent.saturation(c);
		float newBrightness = parent.brightness(c);
		cc = Color.HSBtoRGB(newHue / 360f, newSat / 360f, newBrightness / 360f);
		parent.popStyle();
		return cc;
	} // end getComplimentary

	/**
	 * This will find the four colors evenly spaced around the color wheel
	 * 
	 * @param c
	 *            The input color
	 * @return a color array with the input color as the first element
	 */
	public static int[] getSquare(int c) {
		return getTetradic(c, 90f);
	} // end getSquare

	/**
	 * This will essentially find two complimentary pairs spaced by the input
	 * angle
	 * 
	 * @param c
	 *            The input color
	 * @param angle
	 *            The angle [out of 360] from the input color
	 * @return An array of four colors, the first being the input color
	 */
	public static int[] getTetradic(int c, float angle) {
		int[] cc = new int[4];
		cc[0] = c;
		parent.pushStyle();
		parent.colorMode(parent.HSB, 360);
		float h = parent.hue(c);
		float s = parent.saturation(c);
		float b = parent.brightness(c);
		float newHue = (h + angle) % 360;
		cc[1] = Color.HSBtoRGB(newHue / 360f, s / 360f, b / 360f);
		newHue = (h + 180) % 360;
		cc[2] = Color.HSBtoRGB(newHue / 360f, s / 360f, b / 360f);
		newHue = (newHue + angle) % 360;
		cc[3] = Color.HSBtoRGB(newHue / 360f, s / 360f, b / 360f);
		parent.popStyle();
		return cc;
	} // end getTetradic

	/**
	 * This will find three colors evenly spaced around the color wheel
	 * 
	 * @param c
	 *            The input color
	 * @return An array of three colors, the first being the input color
	 */
	public static int[] getTriadic(int c) {
		return getSplitComplementary(c, 60f);
	} // end getTriadic

	/**
	 * This is like the compliment, but will split it into two colors spaced by
	 * the splitAngle
	 * 
	 * @param c
	 *            The input color
	 * @param splitAngle
	 *            The angle between the two far end colors
	 * @return An array of three colors, the first being the input color
	 */
	public static int[] getSplitComplementary(int c, float splitAngle) {
		int[] cc = new int[3];
		cc[0] = c;
		parent.pushStyle();
		parent.colorMode(parent.HSB, 360);
		float h = parent.hue(c);
		float s = parent.saturation(c);
		float b = parent.brightness(c);
		float newHue = (h + 180f - splitAngle / 2) % 360;
		cc[1] = Color.HSBtoRGB(newHue / 360f, s / 360f, b / 360f);
		newHue = (newHue + splitAngle) % 360;
		cc[2] = Color.HSBtoRGB(newHue / 360f, s / 360f, b / 360f);
		parent.popStyle();
		return cc;
	} // end getSplitComplementary

	/**
	 * This will simply split the color wheel into even increments starting with
	 * the input color
	 * 
	 * @param c
	 *            The input color
	 * @param divisionCount
	 *            The number of colors to divide the color wheel into
	 * @return An array of specified size of colors
	 */
	public static int[] getEvenDivisions(int c, int divisionCount) {
		return getEvenDivisions(c, divisionCount, 360f);
	} // end getEvenDivisions

	/**
	 * This will split up a portion of the color wheel into the specified number
	 * of colors
	 * 
	 * @param c
	 *            The input color
	 * @param divisionCount
	 *            The number of colors to divide the color wheel into
	 * @param angle
	 *            The amount of degrees that this division should take up
	 * @return An array of specified size of colors
	 */
	public static int[] getEvenDivisions(int c, int divisionCount, float angle) {
		int[] cc = new int[divisionCount];
		cc[0] = c;
		parent.pushStyle();
		parent.colorMode(parent.HSB, 360);
		float h = parent.hue(c);
		float s = parent.saturation(c);
		float b = parent.brightness(c);
		for (int i = 1; i < divisionCount; i++) {
			float newHue = (h + parent.map(i, 0, (angle == 360 ? divisionCount
					: divisionCount - 1), 0f, angle)) % 360;
			cc[i] = Color.HSBtoRGB(newHue / 360f, s / 360f, b / 360f);
		}
		parent.popStyle();
		return cc;
	} // end getEvenDivisions

	/**
	 * This will simply return Strings for the input color as RGB, HSB, and hex
	 * 
	 * @param c
	 *            The input color
	 * @return A String[] with the values of the input color. RGB, HSB, and hex
	 */
	public static String[] getColorValue(int c) {
		String[] awesome = new String[3];
		awesome[0] = getColorRGB(c);
		awesome[1] = getColorHSB(c);
		awesome[2] = getColorHex(c);
		return awesome;
	} // end getColorValue

	/**
	 * Get the input color as RGB
	 * 
	 * @param c
	 *            The input color
	 * @return String in RGB
	 */
	public static String getColorRGB(int c) {
		return parent.nf(parent.red(c), 0, 2) + ","
				+ parent.nf(parent.green(c), 0, 2) + ","
				+ parent.nf(parent.blue(c), 0, 2);
	} // end getColorRGB

	/**
	 * Get the input color as HSB 360
	 * 
	 * @param c
	 *            The input color
	 * @return String in HSB 360
	 */
	public static String getColorHSB(int c) {
		parent.pushStyle();
		parent.colorMode(parent.HSB, 360);
		String builder = parent.nf(parent.hue(c), 0, 2) + ","
				+ parent.nf(parent.saturation(c), 0, 2) + ","
				+ parent.nf(parent.brightness(c), 0, 2);
		parent.popStyle();
		return builder;
	} // end getColorHSB

	/**
	 * Get the input color as hex code
	 * 
	 * @param c
	 *            The input color
	 * @return String in hex
	 */
	public static String getColorHex(int c) {
		return "#" + parent.hex(c, 6);
	} // end getColorHex

	/**
	 * This will draw the colors in the colors HashMap and also show their names
	 * 
	 * @param x
	 *            float x coord of where to draw it
	 * @param y
	 *            float y coord of where to draw it
	 * @param txtColor
	 *            the color of the text
	 */
	public void drawColors(float x, float y, int txtColor) {
		parent.fill(txtColor);
		parent.textAlign(parent.CENTER, parent.TOP);
		parent.text(name, x, y - 20);
		for (Map.Entry me : colors.entrySet()) {
			parent.fill((Integer) me.getValue());
			parent.rect(x, y, 20, 20);
			parent.fill(0);
			parent.textAlign(parent.RIGHT, parent.TOP);
			parent.text((String) me.getKey(), x - 4, y);
			y += 20;
		}
	} // end drawColors
} // end class Colors

//
//
//
//
//
//
//

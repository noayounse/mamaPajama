package ocrUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.io.File;
import java.lang.reflect.*;
import java.util.Map;
import java.awt.Color;

import processing.core.*;
import processing.data.IntDict;

/**
 * OCRUtil is a collection of often used functions and such
 * 
 * @author OCR
 * @version 1.0
 */
public class OCRUtils {
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
		int freeMemory = PApplet.round((float) Runtime.getRuntime().freeMemory() / 10000) / 10;
		int totalMemory = PApplet.round((float) Runtime.getRuntime().totalMemory() / 10000) / 10;
		String builder = "printStatus:  ";
		if (functionName.length() > 0)
			builder += "_function: " + functionName + "  " + builder;
		builder += "_frame: " + parent.frameCount + "  _frameRate: " + PApplet.nf(parent.frameRate, 0, 2) + "  _free memory: " + freeMemory + "  _totalMemory: " + totalMemory;
		if (notes.length() > 0)
			builder += "\n  _notes: " + notes;
		try {
			// print the last few stack traces
			final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
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
	 * the maximum width specified. Note: Don't use the special characters, ~
	 * (option+9) and ` (option+7) in the text. Also note: if a single word is
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
	public static String[] splitStringIntoLines(String stringIn, float lineWidthIn, PFont fontIn) {
		String[] lines = new String[0];
		parent.textFont(fontIn);

		String modifiedText = stringIn.replace(" ", " ~ "); // option+9
		modifiedText = modifiedText.replace("\n", " ` "); // option+7

		String[] broken = PApplet.splitTokens(modifiedText, " ");
		String builder = "";

		for (int i = 0; i < broken.length; i++) {
			if (broken[i].equals("`")) {
				lines = (String[]) PApplet.append(lines, builder);
				builder = broken[i];
			} else if (parent.textWidth(builder.replace("~", " ").replace("`", "") + broken[i].replace("~", "").replace("`", "")) <= lineWidthIn) {
				builder += broken[i];
			} else {
				lines = (String[]) PApplet.append(lines, builder);
				builder = broken[i];
			}
		} // end i for
		lines = (String[]) PApplet.append(lines, builder);

		// clean
		for (int i = 0; i < lines.length; i++) {
			lines[i] = lines[i].replace("`", "\n");
			lines[i] = lines[i].replace("~", " ");
		}
		return lines;
	} // end splitStringIntoLines

	/**
	 * This will return a timestamp with the date
	 * 
	 * @return Something like 2013-10-15_17-41-23
	 */
	public static String getTimeStampWithDate() {
		String stamp = PApplet.nf(PApplet.year(), 4) + "-" + PApplet.nf(PApplet.month(), 2) + "-" + PApplet.nf(PApplet.day(), 2) + "_" + getTimeStamp();
		return stamp;
	} // end getTimeStamp

	/**
	 * This will return a timestamp
	 * 
	 * @return Something like 17-41-23
	 */
	public static String getTimeStamp() {
		String stamp = PApplet.nf(PApplet.hour(), 2) + "-" + PApplet.nf(PApplet.minute(), 2) + "-" + PApplet.nf(PApplet.second(), 2);
		return stamp;
	} // end getTimeStamp

	/**
	 * This will return an ArrayList of type Integer for a specified count
	 * 
	 * @param count
	 *            The upper limit of the shuffled list
	 * @return The ArrayList of type Integer
	 */
	public static ArrayList<Integer> generateRandomIndexArray(int count) {
		ArrayList<Integer> newNums = new ArrayList<Integer>();
		for (int i = 0; i < count; i++) {
			int randomIndex = (int) parent.random(newNums.size() + 1);
			newNums.add(randomIndex, i);
		}
		return newNums;
	} // end generateRandomIndexArray

	/**
	 * This will shuffle an ArrayList of objects
	 * 
	 * @param listIn
	 *            An ArrayList of objects to be shuffled
	 * @return the shuffled ArrayList of type Object
	 */
	public static ArrayList shuffleArrayList(ArrayList listIn) {
		ArrayList<Integer> newOrder = generateRandomIndexArray(listIn.size());
		ArrayList newList = new ArrayList();
		for (int i : newOrder) {
			newList.add(listIn.get(i));
		}
		return newList;
	} // end shuffleArrayList

	public static void sortObjectArrayListsSimple(ArrayList masterList, String paramName, ArrayList... listsIn) {
		int count = masterList.size();
		for (ArrayList al : listsIn) {
			if (al.size() != count || count == 0) {
				System.out.println("counts of lists are not the same, did not sort");
				return;
			}
		}
		ArrayList<ArrayList> result = sortObjectArrayListSimpleMaster(masterList, paramName);
		masterList.clear();
		for (Object o : result.get(0)) masterList.add(o);
		//masterList = result.get(0);
		ArrayList[] orderedLists = new ArrayList[listsIn.length];
		for (int i = 0; i < listsIn.length; i++)
			orderedLists[i] = new ArrayList();
		for (int i = 0; i < result.get(1).size(); i++) {
			int index = (Integer) result.get(1).get(i);
			for (int j = 0; j < listsIn.length; j++) {
				orderedLists[j].add(listsIn[j].get(index));
			}
		}
		
		
		for (int i = 0; i < listsIn.length; i++) {
			listsIn[i].clear();
			for (int j = 0; j < orderedLists[i].size(); j++) {
				listsIn[i].add(orderedLists[i].get(j));
			}
		}
	} // sortObjectArrayListsSimple

	// see http://www.javapractices.com/topic/TopicAction.do?Id=207
	/**
	 * This will sort an ArrayList of Objects by a specified field.
	 * 
	 * @param listIn
	 *            The ArrayList of Objects
	 * @param paramName
	 *            String name of the field - note now works with int, long,
	 *            float, double, char, and String
	 * @return The ascending ArrayList of Objects
	 */
	public static ArrayList sortObjectArrayListSimple(ArrayList listIn, String paramName) {
		return sortObjectArrayListSimpleMaster(listIn, paramName).get(0);
	} // end sortObjectArrayListSimple

	private static ArrayList<ArrayList> sortObjectArrayListSimpleMaster(ArrayList listIn, String paramName) {
		ArrayList<ArrayList> answer = new ArrayList<ArrayList>();
		ArrayList newList = new ArrayList();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		try {
			if (listIn.size() > 0) {
				Class<?> c = listIn.get(0).getClass();
				Field f = c.getDeclaredField(paramName);
				f.setAccessible(true);
				Class<?> t = f.getType();
				Double dd = new Double(14);
				Float ff = new Float(14);
				Integer ii = new Integer(14);
				Map sortedPos = new LinkedHashMap();
				Map sortedNeg = new LinkedHashMap();
				Map unsorted = new LinkedHashMap();
				int indexCount = 0;
				long count = 0;
				if (t.isPrimitive()) {
					for (Object thisObj : listIn) {
						Object o = f.get(thisObj);
						double d = 0;
						if (t.getName().equals("char")) {
							d = (int) ((Character) o);
						} else if (t.isInstance(dd))
							d = (Double) o;
						else if (t.isInstance(ff))
							d = (Float) o;
						else if (t.isInstance(ii))
							d = (Integer) o;
						else
							d = new Double(o.toString());

						boolean isNegative = false;

						if (d < 0) {
							isNegative = true;
							d = Math.abs(d);
						}

						String format = "%1$30f";
						String newKey = String.format(format, d);
						String format2 = "%1$20d";
						String countString = String.format(format2, count);
						newKey += "-" + countString;
						if (isNegative) {
							sortedNeg.put(newKey, thisObj);
						} else {
							sortedPos.put(newKey, thisObj);
						}
						unsorted.put(thisObj, indexCount);
						count++;
						indexCount++;
					}
					TreeMap<String, Object> resultPos = new TreeMap();
					resultPos.putAll(sortedPos);
					sortedPos = resultPos;
					TreeMap<String, Object> resultNeg = new TreeMap();
					resultNeg.putAll(sortedNeg);
					sortedNeg = resultNeg;
				} else if (t.isInstance(paramName)) {
					// System.out.println("is a string with value " + o);
					for (Object thisObj : listIn) {
						String key = (String) (f.get(thisObj));
						sortedPos.put(key + "-" + count, thisObj);
						unsorted.put(thisObj, indexCount);
						count++;
						indexCount++;
					}
					TreeMap<String, Object> result = new TreeMap(String.CASE_INSENSITIVE_ORDER);
					result.putAll(sortedPos);
					sortedPos = result;
				}

				Iterator itNeg = sortedNeg.entrySet().iterator();
				while (itNeg.hasNext()) {
					Map.Entry pairs = (Map.Entry) itNeg.next();
					newList.add(pairs.getValue());
					itNeg.remove();
				}

				Collections.reverse(newList);

				Iterator itPos = sortedPos.entrySet().iterator();
				while (itPos.hasNext()) {
					Map.Entry pairs = (Map.Entry) itPos.next();
					Object obj = pairs.getValue();
					newList.add(obj);
					indices.add((Integer) unsorted.get(obj));
					itPos.remove();
				}
			}
		} catch (Exception e) {
			System.out.println("problem sorting list.  listIn.size(): " + listIn.size() + " and param: " + paramName);
			answer.add(newList);
			answer.add(indices);
			return answer;
		}
		answer.add(newList);
		answer.add(indices);
		return answer;
	} // end sortObjectArrayListSimpleMaster

	/**
	 * This will simply reverse an ArrayList
	 * 
	 * @param listIn
	 *            The ArrayList to be reversed
	 * @return
	 */
	public static ArrayList reverseArrayList(ArrayList listIn) {
		ArrayList reversed = (ArrayList) listIn.clone();
		Collections.reverse(reversed);
		return reversed;
	} // end reverseArrayList

	/**
	 * This will count the number of occurrences of a variable in an ArrayList
	 * of objects
	 * 
	 * @param listIn
	 *            The ArrayList of objects
	 * @param paramName
	 *            The String name of the param to be sorted
	 * @return An IntDict in ascending order by param occurrence
	 */
	public static IntDict countOccurrences(ArrayList listIn, String paramName) {
		IntDict intDict = new IntDict();
		try {
			if (listIn.size() > 0) {
				Class<?> c = listIn.get(0).getClass();
				Field f = c.getDeclaredField(paramName);

				f.setAccessible(true);
				Class<?> t = f.getType();
				for (Object thisObj : listIn) {
					String key = "";
					Object o = f.get(thisObj);
					if (t.isPrimitive())
						key = o.toString();
					else
						key = (String) o;

					if (intDict.hasKey(key)) {
						intDict.add(key, 1);
					} else {
						intDict.set(key, 1);
					}
				}
				intDict.sortValues();
			}
		} catch (Exception e) {
			System.out.println("problem getting count for param: " + paramName + " does param exist?");
		}
		return intDict;
	} // end countOccurrences

	/**
	 * this function will get the file names for things that aren't directories
	 * 
	 * @param fileDirectory
	 *            .. use an actual path, most likely sketchPath("") + relative
	 *            directory
	 * @param goDeep
	 *            Whether or not to go deeper into the system or stay at this
	 *            folder level
	 * @return String array of files
	 */
	public static String[] getFileNames(String fileDirectory, boolean goDeep) {
		return getDirectoryObjectNames(fileDirectory, goDeep, true);
	} // end getFileNames

	/**
	 * this function will get the file names for things that are directories
	 * 
	 * @param fileDirectory
	 *            .. use an actual path, most likely sketchPath("") + relative
	 *            directory
	 * @param goDeep
	 *            Whether or not to go deeper into the system or stay at this
	 *            folder level
	 * @return String array of files
	 */
	public static String[] getDirectoryNames(String fileDirectory, boolean goDeep) {
		return getDirectoryObjectNames(fileDirectory, goDeep, false);
	} // end getFileNames

	private static String[] getDirectoryObjectNames(String fileDirectory, boolean goDeep, boolean filesOnly) {
		fileDirectory = fileDirectory.trim();
		if (fileDirectory.charAt(fileDirectory.length() - 1) != '/')
			fileDirectory += "/";
		String[] validFiles = new String[0];
		try {
			File file = new File(fileDirectory);
			if (file.isDirectory()) {
				String allFiles[] = file.list();
				for (String thisFile : allFiles) {
					if (thisFile.length() > 0 && thisFile.toLowerCase().charAt(0) != '.') { // ignore
																							// hidden
																							// files
						File child = new File(fileDirectory + thisFile);
						if ((filesOnly && !child.isDirectory()) || (!filesOnly && child.isDirectory())) {
							validFiles = (String[]) parent.append(validFiles, fileDirectory + thisFile);
							if (!filesOnly && goDeep) { // when it is only
														// looking for
														// directories, goDeep
														// if necessary
								String[] childFiles = getDirectoryObjectNames(fileDirectory + thisFile + "/", goDeep, filesOnly);
								for (String s : childFiles)
									validFiles = (String[]) parent.append(validFiles, s);
							}
						} else { // when it is only looking for files, then
									// goDeep if necessary
							if (goDeep) {
								String[] childFiles = getDirectoryObjectNames(fileDirectory + thisFile + "/", goDeep, filesOnly);
								for (String s : childFiles)
									validFiles = (String[]) parent.append(validFiles, s);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("error getting file names for directory: " + fileDirectory);
		}
		return validFiles;
	} // end getDirectoryObjectNames

} // end class OCRUtils

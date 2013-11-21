package ocrUtils;

//from http://stackoverflow.com/questions/7988486/how-do-you-calculate-the-variance-median-and-standard-deviation-in-c-or-java
import java.util.Arrays;

//import processing.core.PApplet;

/**
 * from http://stackoverflow.com/questions/7988486/how-do-you-calculate-the-
 * variance-median-and-standard-deviation-in-c-or-java
 * 
 * 
 */
public class Statistics {
	double[] data;
	double size;

	/**
	 * Constructor for the Statistics object
	 * 
	 * @param data
	 *            A double[]
	 */
	public Statistics(double[] data) {
		this.data = data;
		size = data.length;
	}

	/**
	 * Will find the min and max of the double array
	 * 
	 * @return double array with min and max
	 */
	public double[] getMaxMin() {
		double[] b = new double[data.length];
		double[] maxMin = new double[2];
		if (b.length > 0) {
			System.arraycopy(data, 0, b, 0, b.length);
			Arrays.sort(b);

			maxMin[0] = b[0];
			maxMin[1] = b[b.length - 1];
		} else {
			maxMin[0] = maxMin[1] = 0;
		}
		return maxMin;
	} // end getMaxMin

	/**
	 * Get the mean for the data double[]
	 * 
	 * @return The mean
	 */
	public double getMean() {
		double sum = 0.0;
		for (double a : data)
			sum += a;
		return sum / size;
	}

	/**
	 * Get the variance for the data double[]
	 * 
	 * @return The variance
	 */
	public double getVariance() {
		double mean = getMean();
		double temp = 0;
		for (double a : data)
			temp += (mean - a) * (mean - a);
		return temp / size;
	}

	/**
	 * Get the standard deviation for the data double[]
	 * 
	 * @return The standard deviation
	 */
	public double getStdDev() {
		return Math.sqrt(getVariance());
	}

	/**
	 * Calculates the median for the data double[]. note that this is a bit slow
	 * according to the comments from the above link
	 * 
	 * @return The median
	 */
	public double getMedian() {
		double[] b = new double[data.length];
		System.arraycopy(data, 0, b, 0, b.length);
		Arrays.sort(b);

		if (data.length % 2 == 0) {
			return (b[(b.length / 2) - 1] + b[b.length / 2]) / 2.0;
		} else {
			return b[b.length / 2];
		}
	}

	// other fun

} // end class Statistics


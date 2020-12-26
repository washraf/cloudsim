package org.cloudbus.cloudsim.util;

public class RandomSelector {

	public static int SelectItem(double[] items) {
		double p = Math.random();
		double cumulativeProbability = 0.0;
		for (int i = 0; i < items.length; i++) {
			cumulativeProbability += items[i];
			if (p <= cumulativeProbability) {
				return i;
			}
		}
		return 0;
	}
}

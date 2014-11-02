package com.digiturtle.common;

public class FastRand {

	private long seed;

	public FastRand(long seed) {
		this.seed = seed;
	}

	public FastRand() {
		this.seed = getSeed();
	}

	protected static long getSeed() {
		return System.currentTimeMillis() + System.nanoTime();
	}

	/**
	 * <p>Use this with care, and only if you know what
	 * you are doing!</p>
	 * <p>Setting the seed again, will make the RNG
	 * "jump" to a position in the random number sequence,
	 * which means, if you set the seed to 0, then the
	 * exactly same numbers will be generated, if you
	 * set the seed to 0 again.</p>
	 * @param seed the seed.
	 */
	public void setSeed(long seed) {
		this.seed = seed;
	}

	/* RANDOM INT AND LONG: */

	public long randLong() {
		seed ^= (seed << 21);
		seed ^= (seed >>> 35);
		seed ^= (seed << 4);
		return seed;
	}

	public int randInt() {
		return (int) randLong();
	}

	public long randAbsLong() {
		return Math.abs(randLong());
	}

	public int randAbsInt() {
		return Math.abs((int) randLong());
	}

	public long randLong(long max) {
		return randAbsLong() % max;
	}

	public int randInt(int max) {
		return randAbsInt() % max;
	}

	public long randLong(long min, long max) {
		return randLong(max - min) + min;
	}

	public int randInt(int min, int max) {
		return randInt(max - min) + min;
	}

	/* RANDOM DOUBLE AND FLOAT */

	public double randDouble() {
		return randLong() / (Long.MAX_VALUE - 1d);
	}

	public float randFloat() {
		return randLong() / (Long.MAX_VALUE - 1f);
	}

	public double randAbsDouble() {
		return (randDouble() + 1.0) / 2.0;
	}

	public float randAbsFloat() {
		return (randFloat() + 1.0f) / 2.0f;
	}

	public double randDouble(double min, double max) {
		return randAbsDouble() * (max - min) + min;
	}

	public float randFloat(float min, float max) {
		return randAbsFloat() * (max - min) + min;
	}

	/* OTHERS */

	public boolean randBool() {
		return randLong() > 0;
	}

}
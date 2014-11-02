package com.digiturtle.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Timed {

	public Timed() {
		this(new Date());
	}
	public Timed(Date date) {
		setHours(getInt(date, "hh"));
		setAM(getBoolean(date, "aa", "AM"));
		setMinutes(getInt(date, "mm"));
		setSeconds(getInt(date, "ss"));
		setMillis(getDouble(date, "SS"));
	}
	
	private int getInt(Date date, String data) {
		return Integer.parseInt(new SimpleDateFormat(data).format(date));
	}
	private double getDouble(Date date, String data) {
		return Double.parseDouble(new SimpleDateFormat(data).format(date));
	}
	private boolean getBoolean(Date date, String data, String test) {
		return new SimpleDateFormat(data).format(date).equalsIgnoreCase(test);
	}
	
	// HOURS
	private int hours;
	public void setHours(int hours) {
		this.hours= hours;
	}
	public int getHours() {
		return hours;
	}
	
	// AM/PM
	private boolean AM;
	public void setAM(boolean AM) {
		this.AM = AM;
	}
	public boolean getAM() {
		return AM;
	}
	
	// MINUTES
	private int minutes;
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getMinutes() {
		return minutes;
	}
	
	// SECONDS
	private int seconds;
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	public int getSeconds() {
		return seconds;
	}
	
	// MILLISECONDS
	private double millis;
	public void setMillis(double millis) {
		this.millis = millis;
	}
	public double getMillis() {
		return millis;
	}
	
}

package com.digiturtle.common;

import java.util.ArrayList;

public class ClockThread implements Runnable {
	
	private static ClockThread thread = new ClockThread();
	public static ClockThread getInstance() {
		return thread;
	}
	
	// INSTANCE

	private ArrayList<Timed> timed = new ArrayList<Timed>();
	
	public ClockThread() {
		
	}
	
	public void start() {
		lastMillis = System.currentTimeMillis();
		new Thread(this).start();
	}
	
	public void addTimed(Timed timed) {
		this.timed.add(timed);
	}
	
	private boolean running;
	public void stop() {
		running = false;
	}
	
	private long lastMillis;
	public void run() {
		running = true;
		while (running) {
			thread = this;
			long nMillis = System.currentTimeMillis();
			for (int index = 0; index < timed.size(); index++) {
				Timed timedTicker = timed.get(index);
				tick(timedTicker, (int)(nMillis - lastMillis));
			}
			lastMillis = nMillis;
			Sync.sync(250);
		}
	}
	
	private void tick(Timed timed, double _millis) {
		double millis = timed.getMillis();
		if (millis < (1000 - _millis)) {
			timed.setMillis(millis + _millis);
		} else {
			timed.setMillis(0);
			if (timed.getSeconds() < 59) { // Move up a second
				timed.setSeconds(timed.getSeconds() + 1);
			} else {
				timed.setSeconds(0);
				if (timed.getMinutes() < 59) { // Move up a minute
					timed.setMinutes(timed.getMinutes() + 1);
				} else {
					timed.setMinutes(0);
					if (timed.getHours() < 12) { // Move up an hour
						timed.setHours(timed.getHours() + 1);
					} else { // Move from AM to PM
						timed.setHours(1);
						timed.setAM(!timed.getAM());
					}
				}
			}
			
		}
	}
	
}

package com.yimei.multiobjective.core;

/***
 * The speed for each period of the day
 * @author e04499
 *
 */

public class PeriodSpeed {

	private double fromTime; // inclusive
	private double toTime; // exclusive
	private double speed;
	
	public PeriodSpeed(double fromTime, double toTime, double speed) {
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.speed = speed;
	}
	
	public double fromTime() {
		return fromTime;
	}
	
	public double toTime() {
		return toTime;
	}
	
	public double speed() {
		return speed;
	}
}

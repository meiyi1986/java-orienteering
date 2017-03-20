package com.yimei.singleobjective.core;

import java.util.ArrayList;
import java.util.List;

/***
 * A period of the day
 * @author yi
 *
 */

public class Period {
	
	private double fromTime;
	private double toTime;
	
	public Period(double fromTime, double toTime) {
		this.fromTime = fromTime;
		this.toTime = toTime;
	}
	
	public double fromTime() {
		return fromTime;
	}
	
	public double toTime() {
		return toTime;
	}
	
	public boolean contains(double time) {
		return (time >= fromTime && time < toTime);
	}

	
	// an universal period definition
	public static List<Period> universalDefine() {
		List<Period> periods = new ArrayList<Period>(); 
		Period period1 = new Period(7.0, 9.0); // period 1: 7:00am - 9:00am
		Period period2 = new Period(9.0, 17.0); // period 2: 9:00am - 5:00pm
		Period period3 = new Period(17.0, 19.0); // period 3: 5:00pm - 7:00pm
		Period period4 = new Period(19.0, 24.0); // period 4: 7:00pm - 12:00am
		
		periods.add(period1); 
		periods.add(period2); 
		periods.add(period3);
		periods.add(period4);
		
		return periods;
	}
}

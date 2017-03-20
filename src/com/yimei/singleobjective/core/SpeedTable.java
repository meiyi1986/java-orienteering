package com.yimei.singleobjective.core;

import java.util.ArrayList;
import java.util.List;

/***
 * A list of PeriodSpeeds, sorted from early to late
 * @author yi
 *
 */

public class SpeedTable {

	private List<PeriodSpeed> table;
	
	public SpeedTable() {
		table = new ArrayList<PeriodSpeed>();
	}
	
	public SpeedTable(List<PeriodSpeed> table) {
		this.table = table;
	}
	
	public void add(PeriodSpeed periodSpeed) {
		table.add(periodSpeed);
	}
	
	public int numOfPeriods() {
		return table.size();
	}
	
	public PeriodSpeed get(int index) {
		return table.get(index);
	}
	
	public int periodOf(double time) {
		int idx = -1;
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).toTime() > time) {
				idx = i;
				break;
			}
		}
		
		return idx;
	}
	
	public double speedOfPeriod(int index) {
		return table.get(index).speed();
	}
	
	public double speedWhen(double time) {
		double speed = -1;
		for (PeriodSpeed ps : table) {
			if (ps.toTime() > time) {
				speed = ps.speed();
				break;
			}
		}
		
		return speed;
	}
}

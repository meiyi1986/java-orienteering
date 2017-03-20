package com.yimei.multiobjective.core;

/***
 * An arc (directed edge) in the graph
 * arc category 0: Always busy
 * arc category 1: Morning peak
 * arc category 2: Two peak
 * arc category 3: Evening peak
 * arc category 4: Seldom traveled 
 * 
 * @author yi
 *
 */

public class Arc {

	private Vertex fromV; // from which vertex
	private Vertex toV; // to which vertex
	private double length; // Euclidean distance between fromV and toV
	private int category; // the category of the arc
	
	private SpeedTable speedTable; // the speed table of this arc
	
	public Arc(Vertex fromV, Vertex toV) {
		this.fromV = fromV;
		this.toV = toV;
		speedTable = new SpeedTable();
	}
	
	public Arc(Vertex fromV, Vertex toV, double length, int category, SpeedTable speedTable) {
		this.fromV = fromV;
		this.toV = toV;
		this.length = length;
		this.category = category;
		this.speedTable = speedTable;
	}
	
	public Vertex fromVertex() {
		return fromV;
	}
	
	public Vertex toVertex() {
		return toV;
	}
	
	public double length() {
		return length;
	}
	
	public int category() {
		return category;
	}
	
	public SpeedTable speedTable() {
		return speedTable;
	}
	
	public void setLength(double length) {
		this.length = length;
	}
	
	public void setCategory(int category) {
		this.category = category;
	}
	
	public void setSpeedTable(SpeedTable speedTable) {
		this.speedTable = speedTable;
	}
	
	public void printMe() {
		System.out.println("Arc (" + fromV.id() + ", " + toV.id() + "), length = "
				+ length + ", category = " + category);
	}
}

package com.yimei.singleobjective.core;

import java.util.List;

/***
 * The vertex on the graph
 * @author yi
 *
 */

public class Vertex {

	private int id;
	private double x;
	private double y;
	private List<Double> scores;
	
	public Vertex(int id, double x, double y, List<Double> scores) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.scores = scores;
	}
	
	public int id() {
		return id;
	}
	
	public double xcord() {
		return x;
	}
	
	public double ycord() {
		return y;
	}
	
	public List<Double> scores() {
		return scores;
	}
	
	public double score(int index) {
		return scores.get(index);
	}
	
	public int numScores() {
		return scores.size();
	}
	
	public double distanceTo(Vertex vertex) {
		double distance = Math.pow(this.x - vertex.xcord(), 2.0) +
				Math.pow(this.y - vertex.ycord(), 2.0);
		distance = Math.sqrt(distance);
		
		return distance;
	}
	
	public void printMe() {
		System.out.println("Vertex " + id + ": coordinates: (" + x + ", " + y + "), scores: " + scores);
	}
}

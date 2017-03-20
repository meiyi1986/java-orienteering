package com.yimei.multiobjective.routing.ant.pheromone;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.problem.Problem;

public class PheromoneInfo {
	
	private Problem problem; // the graph of the problem is to be used
	private Map<Arc, Double> map; // the pheromone of each arc
	
	public PheromoneInfo(Problem problem) {
		this.problem = problem;
		map = new HashMap<Arc, Double>();
	}

	public Set<Arc> keySet() {
		return map.keySet();
	}
	
	public Arc getArc(Vertex fromV, Vertex toV) {
		return problem.getArc(fromV, toV);
	}
	
	public double get(Arc arc) {
		return map.get(arc);
	}
	
	public double get(Vertex fromV, Vertex toV) {
		Arc arc = getArc(fromV, toV);
		return get(arc);
	}

	public void put(Arc arc, Double value) {
		map.put(arc, value);
	}
	
	public void put(Vertex fromV, Vertex toV, Double value) {
		Arc arc = getArc(fromV, toV);
		map.put(arc, value);
	}
	
	
	public void printMe() {
		for (Arc arc : map.keySet()) {
			arc.printMe();
			System.out.println("pheromone = " + map.get(arc));
		}
	}
}

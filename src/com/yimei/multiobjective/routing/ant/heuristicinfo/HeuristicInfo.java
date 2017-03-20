package com.yimei.multiobjective.routing.ant.heuristicinfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.problem.Problem;

public class HeuristicInfo {
	
	protected Problem problem; // the graph of the problem is to be used
	protected Map<Arc, Double> map; // the information of each arc
	
	public HeuristicInfo(Problem problem) {
		this.problem = problem;
		map = new HashMap<Arc, Double>();
	}

	
	public Arc getArc(Vertex fromV, Vertex toV) {
		return problem.getArc(fromV, toV);
	}
	
	public Set<Arc> keySet() {
		return map.keySet();
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
}

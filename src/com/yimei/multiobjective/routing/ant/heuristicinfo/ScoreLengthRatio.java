package com.yimei.multiobjective.routing.ant.heuristicinfo;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.problem.Problem;

public class ScoreLengthRatio extends HeuristicInfo {
	
	int index;

	public ScoreLengthRatio(Problem problem, int index) {
		super(problem);
		this.index = index;
		
		for (Arc arc : problem.arcs()) {
			this.map.put(arc, value(arc));
		}
	}
	
	private double value(Arc arc) {
		return arc.toVertex().score(index) / arc.length();
	}
}

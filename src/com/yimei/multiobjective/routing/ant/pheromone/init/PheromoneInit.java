package com.yimei.multiobjective.routing.ant.pheromone.init;

import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.ant.pheromone.PheromoneInfo;

abstract public class PheromoneInit {
	
	Problem problem;

	public PheromoneInit(Problem problem) {
		this.problem = problem;
	}
	
	abstract public PheromoneInfo execute();
}

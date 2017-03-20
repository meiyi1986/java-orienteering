package com.yimei.multiobjective.routing.ant.pheromone.update;

import com.yimei.multiobjective.routing.ant.pheromone.PheromoneInfo;

abstract public class PheromoneUpdate {
	
	public PheromoneUpdate() {
	}
	
	// local update
	abstract public void execute(PheromoneInfo pheromoneInfo);
}

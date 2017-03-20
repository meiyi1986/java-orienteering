package com.yimei.multiobjective.routing.ant.pheromone.init;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.ant.pheromone.PheromoneInfo;

/***
 * Fixed initial pheromone
 * @author e04499
 *
 */

public class FixedPheromoneInit extends PheromoneInit {

	private double value;
	
	public FixedPheromoneInit(Problem problem, double value) {
		super(problem);
		this.value = value;
	}

	@Override
	public PheromoneInfo execute() {
		// TODO Auto-generated method stub
		PheromoneInfo pheromoneInfo = new PheromoneInfo(problem);
		for (Arc arc : problem.arcs()) {
			pheromoneInfo.put(arc, value);
		}
		
		return pheromoneInfo;
	}
}

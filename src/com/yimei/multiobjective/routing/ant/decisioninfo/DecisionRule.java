package com.yimei.multiobjective.routing.ant.decisioninfo;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.problem.Problem;

/***
 * The combination of heuristic and pheromone information of each arc for tour construction
 * @author e04499
 *
 */

abstract public class DecisionRule {
	
	protected Problem problem; // the graph of the problem is to be used
	
	protected double alpha;
	protected double beta;
	
	public DecisionRule(Problem problem, double alpha, double beta) {
		this.problem = problem;
		this.alpha = alpha;
		this.beta = beta;
	}
	
	// the value of the arc
	abstract public double value(Arc arc);
}

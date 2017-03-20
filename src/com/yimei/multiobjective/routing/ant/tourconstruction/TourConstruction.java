package com.yimei.multiobjective.routing.ant.tourconstruction;

import java.util.Random;

import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.ant.decisioninfo.DecisionRule;
import com.yimei.multiobjective.routing.core.Solution;

/***
 * The tour construction based on the decision information
 * @author e04499
 *
 */

abstract public class TourConstruction {

	protected Problem problem;
	protected DecisionRule decisionRule;
	protected Random rnd;
	
	public TourConstruction(Problem problem, DecisionRule dr, Random rnd) {
		this.problem = problem;
		this.decisionRule = dr;
		this.rnd = rnd;
	}
	
	abstract public Solution execute();
}

package com.yimei.singleobjective.algorithms;

import java.util.List;
import java.util.Random;

import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.core.ParetoFront;
import com.yimei.multiobjective.routing.core.Solution;

public abstract class Algorithm {

	protected Problem problem;
	protected Random rnd;
	
	public Algorithm(Problem problem, Random rnd) {
		this.problem = problem;
		this.rnd = rnd;
	}
	
	abstract public ParetoFront execute();
}

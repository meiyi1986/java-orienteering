package com.yimei.multiobjective.routing.ma.initialization;

import java.util.Random;

import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.core.Solution;

public abstract class Initialization {
	
	protected Random rnd;
	
	public Initialization(Random rnd) {
		this.rnd = rnd;
	}

	abstract public Solution execute(Problem problem);
}

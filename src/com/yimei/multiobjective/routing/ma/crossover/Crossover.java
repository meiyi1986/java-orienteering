package com.yimei.multiobjective.routing.ma.crossover;

import java.util.List;
import java.util.Random;

import com.yimei.multiobjective.routing.core.Solution;

public abstract class Crossover {

	protected Random rnd;
	
	public Crossover(Random rnd) {
		this.rnd = rnd;
	}
	
	// generate one offspring
	abstract public Solution execute(List<Solution> parents);
	
	// generate two offsprings
	abstract public List<Solution> execute2(List<Solution> parents);
}

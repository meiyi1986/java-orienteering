package com.yimei.multiobjective.routing.ma.mutation;

import java.util.Random;

import com.yimei.multiobjective.routing.core.Solution;


public abstract class Mutation {

	protected Random rnd;
	
	public Mutation(Random rnd) {
		this.rnd = rnd;
	}
	
	abstract public Solution execute(Solution indi);
}

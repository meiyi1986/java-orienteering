package com.yimei.multiobjective.routing.ma.selection;

import java.util.List;
import java.util.Random;

import com.yimei.multiobjective.routing.core.Solution;


public abstract class Selection {

	protected Random rnd;
	
	public Selection(Random rnd) {
		this.rnd = rnd;
	}
	
	abstract public List<Solution> execute(List<Solution> population);
}

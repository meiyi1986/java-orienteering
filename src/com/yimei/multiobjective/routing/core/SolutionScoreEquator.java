package com.yimei.multiobjective.routing.core;

import yimei.util.Equator;

public class SolutionScoreEquator extends Equator<Solution> {

	@Override
	public boolean equal(Solution e1, Solution e2) {
		return e1.equalScores(e2);
	}

}

package com.yimei.singleobjective.routing.core;

import java.util.Comparator;

public class SolutionNDComparator implements Comparator<Solution> {

	@Override
	public int compare(Solution o1, Solution o2) {
		
		return o1.dominanceRelation(o2);
	}

}

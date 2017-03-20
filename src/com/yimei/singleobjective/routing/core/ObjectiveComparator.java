package com.yimei.singleobjective.routing.core;

import java.util.Comparator;

public class ObjectiveComparator implements Comparator<Solution> {
	
	private int index;
	
	public ObjectiveComparator(int index) {
		this.index = index;
	}

	@Override
	public int compare(Solution sol1, Solution sol2) {
		if (sol1.score(index) > sol2.score(index)) {
			return -1;
		}
		else if (sol1.score(index) < sol2.score(index)) {
			return 1;
		}
		
		return 0;
	}

}

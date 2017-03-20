package com.yimei.multiobjective.routing.ma.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.yimei.multiobjective.routing.core.Solution;

public class RandomSelection extends Selection {

	private int numOfParents;

	public RandomSelection(Random rnd, int k) {
		super(rnd);
		numOfParents = k;
	}

	// select k parents
	public List<Solution> execute(List<Solution> population) {

		List<Integer> parentIdx = new ArrayList<Integer>();
		for (int i = 0; i < numOfParents; i++) {
			// randomly choose the kth parent

			int r = rnd.nextInt(population.size());

			parentIdx.add(new Integer(r));

		}

//		System.out.println(parentIdx);

		List<Solution> parents = new ArrayList<Solution>();
		for (int i = 0; i < parentIdx.size(); i++) {
			parents.add(population.get(parentIdx.get(i)));
		}

		return parents;
	}
}

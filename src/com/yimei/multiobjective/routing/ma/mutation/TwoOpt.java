package com.yimei.multiobjective.routing.ma.mutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.core.Solution;

/***
 *
 * @author Administrator
 *
 */

public class TwoOpt extends Mutation {

	Problem problem;

	public TwoOpt(Random rnd, Problem problem) {
		super(rnd);
		this.problem = problem;
	}

	@Override
	public Solution execute(Solution indi) {

		List<Vertex> tour = indi.tour();

		// randomly pick two indexes
		List<Integer> exchangeIdx = new ArrayList<Integer>();
		for (int i = 0; i < 2; i++) {
			// randomly choose the kth parent
			boolean duplicated = true;
			while (duplicated) {
				duplicated = false;
				int r = 1 + rnd.nextInt(tour.size()-2);
				if (exchangeIdx.contains(r)) {
					duplicated = true;
				}
				else {
					exchangeIdx.add(new Integer(r));
				}
			}
		}

		int idx1 = exchangeIdx.get(0);
		int idx2 = exchangeIdx.get(1);
		if (idx1 > idx2) {
			int tmp = idx1;
			idx1 = idx2;
			idx2 = tmp;
		}

		int half = (idx2 - idx1) / 2;

		// 2-opt
		for (int i = 0; i <= half; i++) {
			int swap1 = idx1 + i;
			int swap2 = idx2 - i;
			Vertex tmp = tour.get(swap1);
			tour.set(swap1, tour.get(swap2));
			tour.set(swap2, tmp);
		}

//		System.out.println("tmp tour:");
//		for (int i = 0; i < tour.size(); i++) {
//			System.out.print(tour.get(i).id() + " ");
//		}
//		System.out.println();

		// make the tour feasible
		Solution offspring = new Solution(problem.numScores());
		offspring.add(problem.startVertex(), 0, problem);
		offspring.add(problem.endVertex(), 1, problem);

		for (int i = 1; i < tour.size()-1; i++) {
			if (offspring.feasibleToAdd(tour.get(i), offspring.size()-1, problem)) {
				offspring.add(tour.get(i), offspring.size()-1, problem);
			}
		}

		return offspring;
	}
}

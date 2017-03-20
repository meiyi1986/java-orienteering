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

public class Exchange extends Mutation {

	Problem problem;

	public Exchange(Random rnd, Problem problem) {
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

		// exchange
		Vertex v1 = tour.get(exchangeIdx.get(0));
		Vertex v2 = tour.get(exchangeIdx.get(1));
		tour.set(exchangeIdx.get(0), v2);
		tour.set(exchangeIdx.get(1), v1);

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

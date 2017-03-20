package com.yimei.multiobjective.routing.ma.crossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.core.Solution;

public class SinglePointCrossover extends Crossover {
	
	Problem problem;

	public SinglePointCrossover(Random rnd, Problem problem) {
		super(rnd);
		this.problem = problem;
	}
	
	@Override
	public Solution execute(List<Solution> parents) {
		
		// check whether there are two parents
		if (parents.size() != 2) {
			System.err.println("Must have two parents for crossover!");
		}
		
		Solution parent1 = parents.get(0);
		Solution parent2 = parents.get(1);
		
		// find the indexes of all the common vertices
		List<Integer> commonIdx1 = new ArrayList<Integer>();
		List<Integer> commonIdx2 = new ArrayList<Integer>();
		for (int i = 2; i < parent1.tour().size()-2; i++) {
			Vertex v1 = parent1.tour().get(i);
			for (int j = 2; j < parent2.tour().size()-2; j++) {
				Vertex v2 = parent2.tour().get(j);
				if (v1.equals(v2)) {
					commonIdx1.add(new Integer(i));
					commonIdx2.add(new Integer(j));
				}
			}
		}
		
		// randomly select the sublists
		int crossIdx1, crossIdx2;
		if (commonIdx1.isEmpty()) {
			crossIdx1 = 2 + rnd.nextInt(parent1.size()-4);
			crossIdx2 = 2 + rnd.nextInt(parent1.size()-4);
		}
		else {
			crossIdx1 = commonIdx1.get(rnd.nextInt(commonIdx1.size()));
			crossIdx2 = commonIdx2.get(rnd.nextInt(commonIdx2.size()));
		}
		
		// create the offspring
		Solution offspring = new Solution(problem.numScores());
		offspring.add(problem.startVertex(), 0, problem);
		offspring.add(problem.endVertex(), 1, problem);
		
		for (int i = 1; i < crossIdx1; i++) {
			if (offspring.feasibleToAdd(parent1.vertex(i), offspring.tour().size()-1, problem)) {
				offspring.add(parent1.vertex(i), offspring.tour().size()-1, problem);
			}
		}
		for (int i = crossIdx2; i < parent2.tour().size()-1; i++) {
			if (offspring.contains(parent2.vertex(i))) {
				continue;
			}
			
			if (offspring.feasibleToAdd(parent2.vertex(i), offspring.tour().size()-1, problem)) {
				offspring.add(parent2.vertex(i), offspring.tour().size()-1, problem);
			}
		}
		
		
//		// find the common vertex whose index in parent1 and parent2 are closest to 0.5
//		int bestIdx1 = -1, bestIdx2 = -1;
//		double bestRatio1 = -1, bestRatio2 = -1, bestRatio = -1, bestRatioDist = 1.0;
//		for (int i = 1; i < parent1.tour().size()-1; i++) {
//			Vertex v1 = parent1.tour().get(i);
//			for (int j = 1; j < parent2.tour().size()-1; j++) {
//				Vertex v2 = parent2.tour().get(j);
//				if (v1.equals(v2)) {
//					int tmpIdx1 = i;
//					int tmpIdx2 = j;
//					double tmpRatio1 = tmpIdx1 / parent1.tour().size();
//					double tmpRatio2 = tmpIdx2 / parent2.tour().size();
//					double tmpRatio = (tmpRatio1 + tmpRatio2) / 2;
//					double tmpRatioDist = Math.abs(tmpRatio - 0.5);
//					
//					if (tmpRatioDist < bestRatioDist) {
//						bestIdx1 = tmpIdx1;
//						bestIdx2 = tmpIdx2;
//						bestRatio1 = tmpRatio1;
//						bestRatio2 = tmpRatio2;
//						bestRatio = tmpRatio;
//						bestRatioDist = tmpRatioDist;
//					}
//				}
//			}
//		}
		
		return offspring;
	}

	@Override
	public List<Solution> execute2(List<Solution> parents) {
		
		// check whether there are two parents
		if (parents.size() != 2) {
			System.err.println("Must have two parents for crossover!");
		}
		
		Solution parent1 = parents.get(0);
		Solution parent2 = parents.get(1);
		
//		// find the indexes of all the common vertices
//		List<Integer> commonIdx1 = new ArrayList<Integer>();
//		List<Integer> commonIdx2 = new ArrayList<Integer>();
//		for (int i = 2; i < parent1.tour().size()-2; i++) {
//			Vertex v1 = parent1.tour().get(i);
//			for (int j = 2; j < parent2.tour().size()-2; j++) {
//				Vertex v2 = parent2.tour().get(j);
//				if (v1.equals(v2)) {
//					commonIdx1.add(new Integer(i));
//					commonIdx2.add(new Integer(j));
//				}
//			}
//		}
		
//		parent1.printMe();
//		parent2.printMe();
		
		// randomly select the sublists
		int crossIdx1, crossIdx2;
//		if (commonIdx1.isEmpty()) {
			crossIdx1 = 1 + rnd.nextInt(parent1.size()-3);
			crossIdx2 = 1 + rnd.nextInt(parent2.size()-3);
//		}
//		else {
//			int k = Utilities.randInt(0, commonIdx1.size(), rnd);
//			crossIdx1 = commonIdx1.get(k);
//			crossIdx2 = commonIdx2.get(k);
//		}
		
		
//		parent1.printMe();
//		parent2.printMe();
//		System.out.println("cross id1 = " + crossIdx1 + ", id2 = " + crossIdx2);
		
		// create the offspring1
		Solution offspring1 = new Solution(problem.numScores());
		offspring1.add(problem.startVertex(), 0, problem);
		offspring1.add(problem.endVertex(), 1, problem);
		
		for (int i = 1; i < crossIdx1; i++) {
			if (offspring1.feasibleToAdd(parent1.vertex(i), offspring1.tour().size()-1, problem)) {
				offspring1.add(parent1.vertex(i), offspring1.tour().size()-1, problem);
			}
		}
		for (int i = crossIdx2; i < parent2.tour().size()-1; i++) {
			if (offspring1.contains(parent2.vertex(i))) {
				continue;
			}
			
			if (offspring1.feasibleToAdd(parent2.vertex(i), offspring1.tour().size()-1, problem)) {
				offspring1.add(parent2.vertex(i), offspring1.tour().size()-1, problem);
			}
		}
		
		// create the offspring2
		Solution offspring2 = new Solution(problem.numScores());
		offspring2.add(problem.startVertex(), 0, problem);
		offspring2.add(problem.endVertex(), 1, problem);
		
		for (int i = 1; i < crossIdx2; i++) {
			if (offspring2.feasibleToAdd(parent2.vertex(i), offspring2.tour().size()-1, problem)) {
				offspring2.add(parent2.vertex(i), offspring2.tour().size()-1, problem);
			}
		}
		for (int i = crossIdx1; i < parent1.tour().size()-1; i++) {
			if (offspring2.contains(parent1.vertex(i))) {
				continue;
			}
			
			if (offspring2.feasibleToAdd(parent1.vertex(i), offspring2.tour().size()-1, problem)) {
				offspring2.add(parent1.vertex(i), offspring2.tour().size()-1, problem);
			}
		}
		
		List<Solution> offsprings = new ArrayList<Solution>();
		offsprings.add(offspring1);
		offsprings.add(offspring2);
		
		return offsprings;
	}
	
}

package com.yimei.singleobjective.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.yimei.multiobjective.parameters.Parameters;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.core.ParetoFront;
import com.yimei.multiobjective.routing.core.Solution;
import com.yimei.multiobjective.routing.ma.Sorting;
import com.yimei.multiobjective.routing.ma.crossover.Crossover;
import com.yimei.multiobjective.routing.ma.crossover.SinglePointCrossover;
import com.yimei.multiobjective.routing.ma.initialization.Initialization;
import com.yimei.multiobjective.routing.ma.initialization.RandInit;
import com.yimei.multiobjective.routing.ma.mutation.AddVerticesLocalSearch;
import com.yimei.multiobjective.routing.ma.mutation.Mutation;
import com.yimei.multiobjective.routing.ma.mutation.SingleFlip;
import com.yimei.multiobjective.routing.ma.selection.BinaryTournamentSelection;
import com.yimei.multiobjective.routing.ma.selection.Selection;

public class MemeticAlgorithm extends Algorithm {
	
	// the parameters
	private int popsize;
	private int maxEvaluations;
	
	public MemeticAlgorithm(Problem problem, Random rnd) {
		super(problem, rnd);
	}
	
	public MemeticAlgorithm(Problem problem, Random rnd, int popsize, int maxFE) {
		super(problem, rnd);
		this.popsize = popsize;this.maxEvaluations = maxFE;
	}

	
	public void setPopsize(int popsize) {
		this.popsize = popsize;
	}
	
	public void setMaxEvaluations(int maxFE) {
		this.maxEvaluations = maxFE;
	}
	
	public ParetoFront execute() {
		
		// set the operators
		Initialization init = new RandInit(rnd);
		Selection selection = new BinaryTournamentSelection(rnd, 2);
		Crossover crossover = new SinglePointCrossover(rnd, problem);
		Mutation mutation = new AddVerticesLocalSearch(rnd, problem);
//		Mutation mutation = new SingleFlip(rnd, problem);
		
		// initialize population
		List<Solution> population = new ArrayList<Solution>();
		int evaluations = 0;
		
		for (int i = 0; i < popsize; i++) {
			Solution tmpIndi = init.execute(problem);
			tmpIndi.evaluate();
			evaluations ++;
			
//			tmpIndi.printMe();
			
			population.add(tmpIndi);
		}
		
		Sorting sorting = new Sorting(problem, popsize);
		sorting.execute(population);
		
//		for (Solution indi : population) {
//			indi.printMe();
//		}
		
		// search process
		while (evaluations < maxEvaluations) {
			
			for (int i = 0; i < (popsize / 2); i++) {
				if (evaluations < maxEvaluations) {
					// obtain parents
					
					List<Solution> parents = selection.execute(population);
					
//					System.out.println("before");		
//					System.out.println("parent1");
//					parents.get(0).printMe();
//					System.out.println("parent2");
//					parents.get(1).printMe();
					
					// create two offsprings by crossover
					
					List<Solution> xoffsprings = crossover.execute2(parents);
					
//					System.out.println("crossover generated");
//					xoffsprings.get(0).printMe();
//					xoffsprings.get(1).printMe();
					
					
					Solution moffspring1 = mutation.execute(xoffsprings.get(0));
					Solution moffspring2 = mutation.execute(xoffsprings.get(1));
					
//					System.out.println("mutation generated");
//					moffspring1.printMe();
//					moffspring2.printMe();
					
					moffspring1.evaluate();
					moffspring2.evaluate();
					
					population.add(moffspring1);
					population.add(moffspring2);
					
//					System.out.println("after");
//					System.out.println("parent1");
//					parents.get(0).printMe();
//					System.out.println("parent2");
//					parents.get(1).printMe();
			
					evaluations += 2;
				}
			}
			
			// non-dominated sorting for population update
			Sorting ndsort = new Sorting(problem, popsize);
			ndsort.execute(population);
			
//			System.out.println("size = " + population.size() + "/" + popsize);
			
			
		}
		
		if (Parameters.log) {
			System.out.println("population:");
			for (Solution indi : population) {
				indi.printMe();
			}
		}
		
		// get the final Pareto front
		ParetoFront pf = new ParetoFront(problem.numScores());
		for (Solution s : population) {
			pf.add(s);
		}
		
		return pf;
	}
}

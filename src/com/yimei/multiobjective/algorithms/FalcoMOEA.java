package com.yimei.multiobjective.algorithms;

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
import com.yimei.multiobjective.routing.ma.crossover.UniformCrossover;
import com.yimei.multiobjective.routing.ma.initialization.Initialization;
import com.yimei.multiobjective.routing.ma.initialization.RandInit;
import com.yimei.multiobjective.routing.ma.mutation.AddVerticesLocalSearch;
import com.yimei.multiobjective.routing.ma.mutation.Exchange;
import com.yimei.multiobjective.routing.ma.mutation.Mutation;
import com.yimei.multiobjective.routing.ma.mutation.SingleFlip;
import com.yimei.multiobjective.routing.ma.mutation.TwoOpt;
import com.yimei.multiobjective.routing.ma.selection.BinaryTournamentSelection;
import com.yimei.multiobjective.routing.ma.selection.RandomSelection;
import com.yimei.multiobjective.routing.ma.selection.Selection;

public class FalcoMOEA extends Algorithm {

	// the parameters
	private int popsize;
	private int maxEvaluations;

	public static final double CR = 0.4; // crossover rate
	public static final double EM = 0.8; // exchange probability
	public static final double FV = 1.0; // 2-opt mutation

	public FalcoMOEA(Problem problem, Random rnd) {
		super(problem, rnd);
	}

	public FalcoMOEA(Problem problem, Random rnd, int popsize, int maxFE) {
		super(problem, rnd);
		this.popsize = popsize;
		this.maxEvaluations = maxFE;
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
		Selection parentSelection = new RandomSelection(rnd, 2);
		Crossover crossover = new UniformCrossover(rnd, problem, CR);
		Mutation mutation1 = new Exchange(rnd, problem);
		Mutation mutation2 = new TwoOpt(rnd, problem);
		
		Mutation mutation3 = new AddVerticesLocalSearch(rnd, problem);

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
			
//			System.out.println(evaluations);

			List<Solution> ndSolutions = new ArrayList<Solution>();
			for (Solution s : population) {
				if (s.rank == 0) {
					ndSolutions.add(s);
				}
			}

			for (int i = 0; i < popsize; i++) {
				if (evaluations < maxEvaluations) {
					// obtain parents

					List<Solution> parents = parentSelection.execute(ndSolutions);

//					System.out.println("before");
//					System.out.println("parent1");
//					parents.get(0).printMe();
//					System.out.println("parent2");
//					parents.get(1).printMe();


					// create two offsprings by crossover
					Solution xoffspring = crossover.execute(parents);

//					System.out.println("xover");
//					xoffspring.printMe();

					Solution moffspring;
					double r = rnd.nextDouble();
					
					if (xoffspring.size() > 3 && r < EM) {
						moffspring = mutation1.execute(xoffspring);

//						System.out.println("mutation 1");
//						moffspring.printMe();
					}
					else {
						moffspring = xoffspring;
					}					
					
					if (moffspring.size() > 3)
						moffspring = mutation2.execute(moffspring);

//					System.out.println("mutation 2");
//					moffspring.printMe();
					
					Solution lsoffspring = mutation3.execute(moffspring);

					lsoffspring.evaluate();

					if (lsoffspring.dominanceRelation(population.get(i)) == -1) {
						population.set(i, lsoffspring);
					}


//					System.out.println("after");
//					System.out.println("parent1");
//					parents.get(0).printMe();
//					System.out.println("parent2");
//					parents.get(1).printMe();

					evaluations ++;
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

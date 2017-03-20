package com.yimei.multiobjective.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.yimei.multiobjective.parameters.ACOParameters;
import com.yimei.multiobjective.parameters.Parameters;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.ant.colony.Colony;
import com.yimei.multiobjective.routing.ant.colony.WeightedSumColony;
import com.yimei.multiobjective.routing.ant.heuristicinfo.HeuristicInfo;
import com.yimei.multiobjective.routing.ant.heuristicinfo.ScoreLengthRatio;
import com.yimei.multiobjective.routing.ant.pheromone.PheromoneInfo;
import com.yimei.multiobjective.routing.ant.pheromone.init.FixedPheromoneInit;
import com.yimei.multiobjective.routing.ant.pheromone.init.PheromoneInit;
import com.yimei.multiobjective.routing.core.ParetoFront;

/*** 
 * Ant Colony Optimization
 * @author e04499
 *
 */

public class ACO extends Algorithm {

	private List<HeuristicInfo> his;
	private List<PheromoneInfo> pis;
	
	public ACO(Problem problem, Random rnd) {

		super(problem, rnd);
		
		// calculate the heuristic and pheromone info
		his = new ArrayList<HeuristicInfo>();
		pis = new ArrayList<PheromoneInfo>();
		
		System.out.println("init value = " + ACOParameters.initPheromone);
		
		for (int i = 0; i < problem.numScores(); i++) {
			HeuristicInfo hi = new ScoreLengthRatio(problem, i);
			his.add(hi);
			PheromoneInit pInit = new FixedPheromoneInit(problem, ACOParameters.initPheromone);
			PheromoneInfo pi = pInit.execute();
			pis.add(pi);
			
//			pi.printMe();
		}
		
		
	}
	
	public ParetoFront execute() {
		
		Colony colony = new WeightedSumColony(problem, his, pis, rnd);
		
		for (int ite = 0; ite < ACOParameters.maxIters; ite++) {
			
			colony.update();
			
			if (Parameters.log) {
				System.out.println("ite = " + ite);
				colony.getParetoFront().printMe();
			}
		}
		
		return colony.getParetoFront();
	}
}

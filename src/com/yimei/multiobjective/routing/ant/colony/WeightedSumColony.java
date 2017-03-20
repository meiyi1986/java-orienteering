package com.yimei.multiobjective.routing.ant.colony;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.ant.decisioninfo.DecisionRule;
import com.yimei.multiobjective.routing.ant.decisioninfo.WeightedSumDecisionRule;
import com.yimei.multiobjective.routing.ant.heuristicinfo.HeuristicInfo;
import com.yimei.multiobjective.routing.ant.pheromone.PheromoneInfo;
import com.yimei.multiobjective.routing.ant.pheromone.update.GlobalUpdateBest;
import com.yimei.multiobjective.routing.ant.pheromone.update.LocalEvaporate;
import com.yimei.multiobjective.routing.ant.pheromone.update.PheromoneUpdate;
import com.yimei.multiobjective.routing.ant.tourconstruction.BestOrRouletteWheel;
import com.yimei.multiobjective.routing.ant.tourconstruction.TourConstruction;
import com.yimei.multiobjective.routing.core.ObjectiveComparator;
import com.yimei.multiobjective.routing.core.Solution;
import com.yimei.multiobjective.routing.ma.mutation.AddVerticesLocalSearch;
import com.yimei.multiobjective.routing.ma.mutation.Mutation;

public class WeightedSumColony extends Colony {

	public WeightedSumColony(Problem problem, List<HeuristicInfo> his,
			List<PheromoneInfo> pis, Random rnd) {
		super(problem, his, pis, rnd);
	}

	@Override
	public void update() {
		// clear the colony
		this.ants.clear();
		
		// construct the ants
		for (int i = 0; i < numAnts; i++) {
			List<Double> weights = new ArrayList<Double>();
			double w1 = 1.0 * i / (numAnts-1);
			weights.add(w1);
			weights.add(1-w1);
			
			DecisionRule dr = new WeightedSumDecisionRule(problem, alpha, beta, his, pis, weights);
			TourConstruction tcMethod = new BestOrRouletteWheel(problem, dr, q0, rnd);
//			TourConstruction tcMethod = new RouletteWheel(problem, dr, rnd);
			Solution ant = tcMethod.execute();
			
			ants.add(ant);
		}
		
		// iterative improvement
		for (int i = 0; i < numAnts; i++) {
			Mutation improveMethod = new AddVerticesLocalSearch(rnd, problem);
			Solution impAnt = improveMethod.execute(ants.get(i));
			ants.set(i, impAnt);
			pf.add(impAnt);
			
			// local pheromone update
			PheromoneUpdate localUpdate = new LocalEvaporate(phi, initPheromone, impAnt);
			for (int j = 0; j < pis.size(); j++) {
				localUpdate.execute(pis.get(j));
			}
		}
		
		// global pheromone update
		for (int i = 0; i < problem.numScores(); i++) {
//			ants.sort(new ObjectiveComparator(i));
			Collections.sort(ants, new ObjectiveComparator(i));
			
//			PheromoneUpdate globalUpdate = new GlobalUpdateBest(pho, initPheromone, ants.get(0));
			PheromoneUpdate globalUpdate = new GlobalUpdateBest(pho, ants.get(0).score(i), ants.get(0));
			globalUpdate.execute(pis.get(i));
		}
		
		
		
	}

}

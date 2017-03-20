package com.yimei.multiobjective.routing.ant.decisioninfo;

import java.util.List;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.ant.heuristicinfo.HeuristicInfo;
import com.yimei.multiobjective.routing.ant.pheromone.PheromoneInfo;

public class WeightedSumDecisionRule extends DecisionRule {
	
	private List<HeuristicInfo> his; // the heuristic information for each objective
	private List<PheromoneInfo> pis; // the pheromone information for each objective
	private List<Double> weights; // the size of weights must be equal to the number of scores
	
	public WeightedSumDecisionRule(Problem problem, double alpha, double beta, 
			List<HeuristicInfo> his, List<PheromoneInfo> pis, List<Double> weights) {
		super(problem, alpha, beta);
		this.his = his;
		this.pis = pis;
		this.weights = weights;
	}
	
	
	public double value(Arc arc) {
		double sum = 0;
		for (int index = 0; index < problem.numScores(); index++) {
//			System.out.println("(" + arc.fromVertex().id() + ", " + arc.toVertex().id() + "), value1 = " + pis.get(index).get(arc)
//					+ ", value2 = " + his.get(index).get(arc));
			sum += Math.pow(pis.get(index).get(arc), alpha) * Math.pow(his.get(index).get(arc), beta) * weights.get(index);
		}
		
		return sum;
	}
	
	public List<Double> weights() {
		return weights;
	}
}

package com.yimei.multiobjective.routing.ant.colony;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.parameters.ACOParameters;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.ant.heuristicinfo.HeuristicInfo;
import com.yimei.multiobjective.routing.ant.pheromone.PheromoneInfo;
import com.yimei.multiobjective.routing.core.ParetoFront;
import com.yimei.multiobjective.routing.core.Solution;

abstract public class Colony {
	
	protected Problem problem;
	
	/** parameters **/
	
	protected int numAnts; // number of ants
	protected double alpha; // the weight on the pheromone
	protected double beta; // the weight on the heuristic information
	protected double pho; // the global evaporation rate
	protected double phi; // the local evaporation rate
	protected double initPheromone;
	
	protected double maxPheromone;
	protected double minPheromone;
	
	protected double q0;
	
	protected Random rnd;

	/** variables **/
	protected ParetoFront pf; // non-dominated ants
	protected List<Solution> ants;
	protected List<HeuristicInfo> his; // the heuristic information for each objective
	protected List<PheromoneInfo> pis; // the pheromone information for each objective
	
	public Colony(Problem problem, List<HeuristicInfo> his, List<PheromoneInfo> pis, Random rnd) {
		this.problem = problem;
		this.pf = new ParetoFront(problem.numScores());
		this.ants = new ArrayList<Solution>();
		this.his = his;
		this.pis = pis;
		this.rnd = rnd;
		
		// get parameters
		this.numAnts = ACOParameters.numAnts;
		this.alpha = ACOParameters.alpha;
		this.beta = ACOParameters.nbeta * problem.numVertices();
//		this.beta = ACOParameters.beta;
		this.pho = ACOParameters.pho;
		this.phi = ACOParameters.phi;
		this.initPheromone = ACOParameters.initPheromone;
		this.q0 = ACOParameters.q0;
	}
	
	public ParetoFront getParetoFront() {
		return pf;
	}
	
	public List<Solution> ants() {
		return ants;
	}
	
	public Solution ant(int index) {
		return ants.get(index);
	}
	
	public PheromoneInfo pheromoneInfo(int index) {
		return pis.get(index);
	}
	
	public HeuristicInfo heuristicInfo(int index) {
		return his.get(index);
	}
	
	public void printInfo(int index) {
		for (Arc arc : pis.get(index).keySet()) {
			System.out.print("(" + arc.fromVertex().id() + ", " + arc.toVertex().id() + "), ");
			System.out.println("heuristic = " + his.get(index).get(arc) + ", pheromone = " + pis.get(index).get(arc));
		}
	}
	
	public void printMe() {
		System.out.println("Printing colony:");
		for (int i = 0; i < ants.size(); i++) {
			System.out.println("Ant " + i + ", profit = " + ants.get(i).score(0));
			ants.get(i).printMe();
		}
	}
	
	abstract public void update();
}

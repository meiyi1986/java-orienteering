package com.yimei.multiobjective.routing.ant.pheromone.update;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.routing.ant.pheromone.PheromoneInfo;
import com.yimei.multiobjective.routing.core.Solution;

public class GlobalUpdateBest extends PheromoneUpdate {
	
	private double pho;
	private Solution bestAnt;
	private double delta;
	
	public GlobalUpdateBest(double pho, double delta, Solution ant) {
		this.pho = pho;
		this.bestAnt = ant;
		this.delta = delta;
	}
		
	public void execute(PheromoneInfo pheromoneInfo) {
		
		// evaporate for each arc
		for (Arc arc : pheromoneInfo.keySet()) {
			pheromoneInfo.put(arc, pheromoneInfo.get(arc) * (1 - pho));
		}
		
		// update for the arcs of the best ant
		for (int i = 0; i < bestAnt.tour().size()-1; i++) {
			Vertex v1 = bestAnt.vertex(i);
			Vertex v2 = bestAnt.vertex(i+1);
			
			Arc arc = pheromoneInfo.getArc(v1, v2);
			pheromoneInfo.put(arc, pheromoneInfo.get(arc) + pho * delta);
		}
	}
}

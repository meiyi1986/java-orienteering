package com.yimei.multiobjective.routing.ant.pheromone.update;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.routing.ant.pheromone.PheromoneInfo;
import com.yimei.multiobjective.routing.core.Solution;

/***
 * Global update with the best and second best solutions
 * @author e04499
 *
 */

public class GlobalUpdateBest2 extends PheromoneUpdate {

	private Solution bestAnt;
	private Solution secondBestAnt;
	private double initValue;
	
	public GlobalUpdateBest2(Solution ant1, Solution ant2, double initv) {
		this.bestAnt = ant1;
		this.secondBestAnt = ant2;
	}
	
	public void execute(PheromoneInfo pheromoneInfo) {
		// update for the best ant
		for (int i = 0; i < bestAnt.tour().size()-1; i++) {
			Vertex v1 = bestAnt.vertex(i);
			Vertex v2 = bestAnt.vertex(i+1);
			
			Arc arc = pheromoneInfo.getArc(v1, v2);
			pheromoneInfo.put(arc, pheromoneInfo.get(arc) + initValue);
		}
		
		// update for the second best ant
		for (int i = 0; i < secondBestAnt.tour().size()-1; i++) {
			Vertex v1 = secondBestAnt.vertex(i);
			Vertex v2 = secondBestAnt.vertex(i+1);
			
			Arc arc = pheromoneInfo.getArc(v1, v2);
			pheromoneInfo.put(arc, pheromoneInfo.get(arc) + 0.5 * initValue);
		}
	}
}

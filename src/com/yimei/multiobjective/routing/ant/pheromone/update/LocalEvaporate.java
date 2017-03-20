package com.yimei.multiobjective.routing.ant.pheromone.update;

import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.routing.ant.pheromone.PheromoneInfo;
import com.yimei.multiobjective.routing.core.Solution;

public class LocalEvaporate extends PheromoneUpdate {

	private double phi; // the evaporation rate
	private double initValue;
	private Solution ant;
	
	public LocalEvaporate(double phi, double initv, Solution ant) {
		this.phi = phi;
		this.initValue = initv;
		this.ant = ant;
	}

	@Override
	public void execute(PheromoneInfo pheromoneInfo) {
		
		for (int i = 0; i < ant.tour().size()-1; i++) {
			Vertex v1 = ant.vertex(i);
			Vertex v2 = ant.vertex(i+1);
			
			double value = pheromoneInfo.get(v1, v2);
			double newValue = (1 - phi) * value + phi * initValue;
			
			pheromoneInfo.put(v1, v2, newValue);
		}
	}
	
	
}

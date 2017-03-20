package com.yimei.multiobjective.routing.ant.tourconstruction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.ant.decisioninfo.DecisionRule;
import com.yimei.multiobjective.routing.ant.vertexselection.BestVertex;
import com.yimei.multiobjective.routing.ant.vertexselection.RouletteWheelVertex;
import com.yimei.multiobjective.routing.ant.vertexselection.VertexSelection;
import com.yimei.multiobjective.routing.core.Solution;

/***
 * Select the arc with the best weighted sum of the objectives if q < q_0
 * Select the arc with roulette wheel selection if q > q_0
 * @author e04499
 *
 */

public class BestOrRouletteWheel extends TourConstruction {
	
	private double q0;

	public BestOrRouletteWheel(Problem problem, DecisionRule dr, double q0, Random rnd) {
		super(problem, dr, rnd);
		this.q0 = q0;
	}

	@Override
	public Solution execute() {

		Solution ant = new Solution(problem.numScores());
		ant.add(problem.startVertex(), 0, problem);
		ant.add(problem.endVertex(), 1, problem);
		
		List<Vertex> candiVertices = new LinkedList<Vertex>(problem.candiVertices());
		
		// calculate the decision rule for each unvisited vertex
		Map<Arc, Double> decisionValues = new HashMap<Arc, Double>();
		for (Arc arc : problem.arcs()) {
			decisionValues.put(arc, decisionRule.value(arc));
		}
		
		// add intermediate vertices
		Vertex nextV = null;
		VertexSelection vs;
		do {
			double q = rnd.nextDouble();
			
			if (q < q0) {
				vs = new BestVertex(problem, decisionValues, ant, candiVertices, rnd);
			}
			else {
				vs = new RouletteWheelVertex(problem, decisionValues, ant, candiVertices, rnd);
			}
			
			nextV = vs.execute();
			
			if (nextV != null) {
				ant.add(nextV, ant.size()-1, problem);
			}
		} while (nextV != null);
		
		return ant;
	}
}

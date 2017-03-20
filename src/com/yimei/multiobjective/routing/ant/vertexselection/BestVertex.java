package com.yimei.multiobjective.routing.ant.vertexselection;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.core.Solution;

public class BestVertex extends VertexSelection{

	public BestVertex(Problem problem, Map<Arc, Double> decisionValues,
			Solution ant, List<Vertex> candiVertices, Random rnd) {
		super(problem, decisionValues, ant, candiVertices, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vertex execute() {
		Vertex fromV = ant.vertex(ant.size()-2);
		
		Arc bestArc= null;
		double bestDecisionValue = -Double.MAX_VALUE;
		for (Vertex v : candiVertices) {
			if (ant.feasibleToAdd(v, ant.size()-1, problem)) {
				Arc arc = problem.getArc(fromV, v);
				double tmpDV = decisionValues.get(arc);
				if (tmpDV > bestDecisionValue) {
					bestArc = arc;
					bestDecisionValue = tmpDV;
				}
			}
		}
		
		if (bestArc == null)
			return null;
		
		candiVertices.remove(bestArc.toVertex());
		
		return bestArc.toVertex();
	}
	
	

}

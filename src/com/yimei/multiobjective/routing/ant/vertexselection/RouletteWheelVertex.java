package com.yimei.multiobjective.routing.ant.vertexselection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.core.Solution;

public class RouletteWheelVertex extends VertexSelection {



	public RouletteWheelVertex(Problem problem,
			Map<Arc, Double> decisionValues, Solution ant,
			List<Vertex> candiVertices, Random rnd) {
		super(problem, decisionValues, ant, candiVertices, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vertex execute() {
		
		Vertex fromV = ant.vertex(ant.size()-2);
		
		// the feasible arcs and their probabilities
		Map<Arc, Double> feasibleArcProbs = new HashMap<Arc, Double>();
		double sum = 0;
		for (Vertex v : candiVertices) {
			if (ant.feasibleToAdd(v, ant.size()-1, problem)) {
				Arc arc = problem.getArc(fromV, v);
				feasibleArcProbs.put(arc, decisionValues.get(arc));
				sum += feasibleArcProbs.get(arc);
				
//				if (Parameters.log) {
//					printMe(arc);
//				}
			}
		}
		
		for (Arc arc : feasibleArcProbs.keySet()) {
			feasibleArcProbs.put(arc, feasibleArcProbs.get(arc) / sum);
			
//			if (Parameters.log) {
//				System.out.println("v = " + arc.toVertex().id() + ", dv = " + decisionValues.get(arc) + ", prob = " + feasibleArcProbs.get(arc));
//			}
		}
		
		Arc selArc = null;
		double r = rnd.nextDouble();
//		System.out.println("r = " + r);
		double ub = 0;
		for (Arc arc : feasibleArcProbs.keySet()) {
			ub += feasibleArcProbs.get(arc);
			
//			System.out.println("v = " + arc.toVertex().id() + ", ub = " + ub);
			if (r < ub) {
				selArc = arc;
				break;
			}
		}
		
		if (selArc == null)
			return null;
		
		candiVertices.remove(selArc.toVertex());
		
		return selArc.toVertex();
	}


	
}

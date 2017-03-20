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
import com.yimei.multiobjective.routing.ant.vertexselection.RouletteWheelVertex;
import com.yimei.multiobjective.routing.ant.vertexselection.VertexSelection;
import com.yimei.multiobjective.routing.core.Solution;

public class RouletteWheel extends TourConstruction {

	public RouletteWheel(Problem problem, DecisionRule dr, Random rnd) {
		super(problem, dr, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Solution execute() {
		// TODO Auto-generated method stub
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
		do {
			VertexSelection vs = new RouletteWheelVertex(problem, decisionValues, ant, candiVertices, rnd);
			
			nextV = vs.execute();
			
			if (nextV != null) {
				
//				if (Parameters.log) {
//					System.out.println("next vertex = " + nextV.id());
//				}
				
				ant.add(nextV, ant.size()-1, problem);
			}
		} while (nextV != null);
		
		return ant;
	}

}

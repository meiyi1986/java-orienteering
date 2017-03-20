package com.yimei.multiobjective.routing.ant.vertexselection;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.core.Solution;

abstract public class VertexSelection {

	protected Problem problem;
	protected Map<Arc, Double> decisionValues;
	protected Solution ant; // the partial tour
	protected List<Vertex> candiVertices;
	protected Random rnd;
	
	public VertexSelection(Problem problem, Map<Arc, Double> decisionValues, 
			Solution ant, List<Vertex> candiVertices, Random rnd) {
		this.problem = problem;
		this.decisionValues = decisionValues;
		this.ant = ant;
		this.candiVertices = candiVertices;
		this.rnd = rnd;
	}
	
	abstract public Vertex execute();
	

	public void printMe(Arc arc) {
		System.out.println("Arc (" + arc.fromVertex().id() + ", " + arc.toVertex().id()
				+ "), length = " + arc.length() + ", score = " + arc.toVertex().score(0)
				+ ", dv = " + decisionValues.get(arc));
	}
}

package com.yimei.multiobjective.routing.ma.mutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.core.Solution;

/***
 * A random insertion of a vertex into a feasible location
 * @author Administrator
 *
 */

public class InsertVertex extends Mutation {
	
	Problem problem;

	public InsertVertex(Random rnd, Problem problem) {
		super(rnd);
		this.problem = problem;
	}

	@Override
	public Solution execute(Solution indi) {
		
		Solution offspring = new Solution(indi);
		
		// obtain the candidate insertions
		List<Insertion> candiInsertions = new ArrayList<Insertion>();
		List<Vertex> unvisitedVertices = new ArrayList<Vertex>();
		for (Vertex v : problem.candiVertices()) {
			if (offspring.tour().contains(v)) {
				continue;
			}
			
			unvisitedVertices.add(v);
		}
		
		for (Vertex v : unvisitedVertices) {
			for (int i = 1; i < offspring.size(); i++) {
				if (offspring.feasibleToAdd(v, i, problem)) {
					Insertion tmpInsert = new Insertion(v, i);
					candiInsertions.add(tmpInsert);
				}
			}
		}
		
		// randomly select an insertion
		int k = rnd.nextInt(candiInsertions.size()-1);
		Insertion selInsert = candiInsertions.get(k);
		offspring.add(selInsert.vertex, selInsert.index, problem);
		
		return offspring;
	}

	
	public class Insertion {
		public Vertex vertex; // the vertex to be inserted
		public int index; // the index to insert the vertex
		
		public Insertion(Vertex v, int i) {
			this.vertex = v;
			this.index = i;
		}
	}
}

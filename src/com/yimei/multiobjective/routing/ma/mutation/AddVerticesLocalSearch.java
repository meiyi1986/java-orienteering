package com.yimei.multiobjective.routing.ma.mutation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.mapdb.Fun.Tuple2;

import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.core.Solution;

public class AddVerticesLocalSearch extends Mutation {
	
	Problem problem;

	public AddVerticesLocalSearch(Random rnd, Problem problem) {
		super(rnd);
		this.problem = problem;
	}

	@Override
	public Solution execute(Solution indi) {
		
		// find the unvisited vertices
		List<Vertex> unvisitedVertices = new ArrayList<Vertex>();
		for (Vertex v : problem.candiVertices()) {
			if (indi.tour().contains(v)) {
				continue;
			}
			
			unvisitedVertices.add(v);
		}

		
		Solution offspring = shuffleInsertion(unvisitedVertices, indi);
//		Solution offspring = bestDeltaTimeInsertion(unvisitedVertices, indi);
		
		return offspring;
	}
	
	private Solution shuffleInsertion(List<Vertex> vlist, Solution indi) {
		Solution offspring = new Solution(indi);
		
		// randomly shuffle
		Collections.shuffle(vlist, rnd);
		
		// enumerate the insertion of each vertex
		for (Vertex v : vlist) {
			// find the best insertion index
			Tuple2<Integer, Double> insertTuple = bestInsertionIdx(v, offspring);
			if (insertTuple.a > 0) {
				offspring.add(v, insertTuple.a, problem);
			}
		}
		
		return offspring;
	}
	
	private Solution bestDeltaTimeInsertion(List<Vertex> vlist, Solution indi) {
		Solution offspring = new Solution(indi);
		
		List<Vertex> left = new ArrayList<Vertex>(vlist);
		
		Tuple2<Integer, Integer> insertTuple = bestVertexAndIndex(left, offspring);
		
		while (insertTuple.a != -1) {
//			offspring.printMe();
//			for (int i = 0; i < left.size(); i++) {
//				System.out.print(left.get(i).id() + " ");
//			}
//			System.out.println();
//			System.out.println("a = " + insertTuple.a + ", b = " + insertTuple.b); 
			
			offspring.add(left.get(insertTuple.a), insertTuple.b, problem);
			left.remove(left.get(insertTuple.a));
			
//			offspring.printMe();
//			for (int i = 0; i < left.size(); i++) {
//				System.out.print(left.get(i).id() + " ");
//			}
//			System.out.println();
			
			insertTuple = bestVertexAndIndex(left, offspring);
		}
		
		return offspring;
	}
	
	private Tuple2<Integer, Integer> bestVertexAndIndex(List<Vertex> vlist, Solution indi) {
		int bestVertexIdx = -1;
		int bestInsertIdx = -1;
		double bestDeltaTime = Double.MAX_VALUE;
		
		for (int i = 0; i < vlist.size(); i++) {
			Tuple2<Integer, Double> insertTuple = bestInsertionIdx(vlist.get(i), indi);
			
			if (insertTuple.b < bestDeltaTime) {
				bestVertexIdx = i;
				bestInsertIdx = insertTuple.a;
				bestDeltaTime = insertTuple.b;
			}
		}
		
		Tuple2<Integer, Integer> tuple = new Tuple2<Integer, Integer>(bestVertexIdx, bestInsertIdx);
		
		return tuple;
	}

	private Tuple2<Integer, Double> bestInsertionIdx(Vertex v, Solution indi) {
		int index = -1;
		double deltaTime = Double.MAX_VALUE;
		
		for (int i = 1; i < indi.tour().size(); i++) {
			double t1 = problem.travelTime(indi.vertex(i-1), v, indi.timeOf(i-1));
			
			// examine whether the departure time of v exceeds the time horizon
			if (indi.timeOf(i-1) + t1 > problem.startTime + problem.timeLimit()) {
				continue;
			}
			
			double t2 = problem.travelTime(v, indi.vertex(i), indi.timeOf(i-1) + t1);
			
			double tmpTime = indi.timeOf(i-1) + t1 + t2;
			if (tmpTime > indi.latestTimeOf(i)) {
				continue;
			}
			
			double tmpDeltaTime = tmpTime - indi.timeOf(i);
			
			if (tmpDeltaTime < deltaTime) {
				index = i;
				deltaTime = tmpDeltaTime;
			}
		}
		
		Tuple2<Integer, Double> tuple = new Tuple2<Integer, Double>(index, deltaTime);
		
		return tuple;
	}
}

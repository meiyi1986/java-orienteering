package com.yimei.multiobjective.routing.ma.initialization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.core.Solution;

public class RandInit extends Initialization {
	
	public RandInit(Random rnd) {
		super(rnd);
	}

	public Solution execute(Problem problem) {
		
		Solution indi = new Solution(problem.numScores());
		indi.add(problem.startVertex(), 0, problem);
		indi.add(problem.endVertex(), 1, problem);
		
		// random permutation
		List<Vertex> universal = new ArrayList<Vertex>(problem.candiVertices());
		Collections.shuffle(universal, rnd);

		for (int i = 0; i < universal.size(); i++) {
			if (indi.feasibleToAdd(universal.get(i), 1, problem)) {
				indi.add(universal.get(i), 1, problem);
			}
		}
		
		return indi;
	}
}

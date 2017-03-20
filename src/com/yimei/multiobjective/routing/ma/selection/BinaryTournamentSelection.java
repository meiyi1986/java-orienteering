package com.yimei.multiobjective.routing.ma.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.yimei.multiobjective.routing.core.Solution;

public class BinaryTournamentSelection extends Selection {
	
	private int numOfParents;

	public BinaryTournamentSelection(Random rnd, int k) {
		super(rnd);
		numOfParents = k;
	}

	@Override
	public List<Solution> execute(List<Solution> population) {
		
		List<Solution> parents = new ArrayList<Solution>();
		
		for (int i = 0; i < numOfParents; i++) {
			// choose the kth parent by binary tournament
			boolean duplicated = true;
			while (duplicated) {
				duplicated = false;
				
				// select one parent by binary tournament
				Solution nextParent = oneIndi(population);
				
				for (int j = 0; j < parents.size(); j++) {
					if (parents.get(j).equals(nextParent)) {
						duplicated = true;
						break;
					}
				}
				
				if (!duplicated) {
					parents.add(nextParent);
				}
			}
		}
		
		return parents;
	}
	
	
	public Solution oneIndi(List<Solution> population) {
		
		Selection rdSel = new RandomSelection(rnd, 2);
		List<Solution> candidates = rdSel.execute(population);
		
		// select the better one by first checking dominance, and then the crowding distance
		int dominanceFlag = candidates.get(0).dominanceRelation(candidates.get(1));
		if (dominanceFlag == -1) {
			return candidates.get(0);
		}
		else if (dominanceFlag == 1) {
			return candidates.get(1);
		}
		else {
			if (candidates.get(0).crowdingDistance > candidates.get(1).crowdingDistance) {
				return candidates.get(0);
			}
			else if (candidates.get(0).crowdingDistance < candidates.get(1).crowdingDistance) {
				return candidates.get(1);
			}
			else {
				// randomly pick one
				if (rnd.nextDouble() < 0.5) {
					return candidates.get(0);
				}
				else {
					return candidates.get(1);
				}
			}
		}
	}

	
	

}

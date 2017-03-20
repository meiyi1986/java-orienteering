package com.yimei.singleobjective.experiment;

import java.util.ArrayList;
import java.util.List;

import com.yimei.multiobjective.algorithms.Algorithm;
import com.yimei.multiobjective.routing.core.ParetoFront;
import com.yimei.multiobjective.routing.core.Solution;

public class SolveInstance {
	
	private Instance instance;
	private Algorithm algorithm;
	private int numRuns;
	
	public SolveInstance(Instance instance, Algorithm algorithm, int numRuns) {
		this.instance = instance;
		this.algorithm = algorithm;
		this.numRuns = numRuns;
	}
	
	public InstanceOutput execute() {
		
		List<RunOutput> runOutputs = new ArrayList<RunOutput>();
		
		for (int i = 0; i < numRuns; i++) {
			ParetoFront pf = algorithm.execute();
			
			RunOutput rop = new RunOutput(instance, i, pf.getSolutions());
			runOutputs.add(rop);
		}
		
		
		InstanceOutput iop = new InstanceOutput(instance, runOutputs);
		
		return iop;
	}
}

package com.yimei.singleobjective.experiment;

import com.yimei.multiobjective.algorithms.Algorithm;
import com.yimei.multiobjective.routing.core.ParetoFront;

public class Run {

	private Instance instance;
	private Algorithm algorithm;
	private int runID;
	
	public Run(Instance instance, Algorithm algorithm, int runID) {
		this.instance = instance;
		this.algorithm = algorithm;
		this.runID = runID;
	}
	
	public RunOutput execute() {
		ParetoFront pf = algorithm.execute();
		
		RunOutput rop = new RunOutput(instance, runID, pf.getSolutions());
		return rop;
	}
}

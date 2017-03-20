package com.yimei.singleobjective.experiment;

import java.io.IOException;
import java.util.List;

public class InstanceOutput {
	
	private Instance instance;
	private List<RunOutput> runOutputs;
	
	public InstanceOutput(Instance instance, List<RunOutput> runOutputs) {
		this.instance = instance;
		this.runOutputs = runOutputs;
	}
	
	public String name() {
		return instance.name;
	}
	
//	public void toFile(String resDir) throws IOException {
//		for (RunOutput rop : runOutputs) {
//			rop.printToFile(resDir);
//		}
//	}
}

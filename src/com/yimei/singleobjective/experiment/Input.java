package com.yimei.singleobjective.experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Input {
	
	private List<Instance> instances;
	
	public Input() {
		instances = new ArrayList<Instance>();
	}

	public void addInstance(String name, String fileStr, String speedFileStr, String catFileStr) {
		File file = new File(fileStr);
		File speedFile = new File(speedFileStr);
		File catFile = new File(catFileStr);
		
		Instance inst = new Instance(name, file, speedFile, catFile);
		instances.add(inst);
	}
	
	public List<Instance> getInstances() {
		return instances;
	}
	
	public Instance instance(int index) {
		return instances.get(index);
	}
	
	
	
	// setup input for the entire experiment
	public static Input entireInput() {
		Input input = new Input();
		
		String [] nameSet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"}; // used to create the file name
		
		int [] numInstances = {9, 9, 9, 10, 3, 9, 10}; // the number of instances in each category
		int numDatasets = numInstances.length;
		
		String path = "data/MOTDOP/";
		String speedFileStr = path + "speedMatrix.txt";
		
		for (int i = 1; i < numDatasets+1; i++) {
			String datasetPath = path + "dataset " + i + "/";
			String catFileStr = datasetPath + "arc_cat_" + i + ".txt";
			
			for (int j = 0; j < numInstances[i-1]; j++) {
				String name = "p" + i + ".1." + nameSet[j];
				String fileStr = datasetPath + "OP_instances/" + name + ".txt";
				
				input.addInstance(name, fileStr, speedFileStr, catFileStr);
			}
		}
		
		return input;
	}
}

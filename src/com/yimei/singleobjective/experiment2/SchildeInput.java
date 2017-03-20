package com.yimei.singleobjective.experiment2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.yimei.multiobjective.experiment.Instance;

public class SchildeInput {
	
	private List<Instance> instances;
	
	public SchildeInput() {
		instances = new ArrayList<Instance>();
	}

	public void addInstance(String name, String fileStr, String speedFileStr, String catFileStr) {
		File file = new File(fileStr);
		File speedFile = new File(speedFileStr);
		File catFile = new File(catFileStr);
		
		Instance inst = new Instance(name, file, speedFile, catFile);
		instances.add(inst);
	}
	
	public void addInstance(String name, String fileStr, String speedFileStr, String catFileStr, String solutionFileStr, int size, double tmax) {
		File file = new File(fileStr);
		File speedFile = new File(speedFileStr);
		File catFile = new File(catFileStr);
		
		Instance inst = new Instance(name, file, speedFile, catFile);
		inst.solutionFile = new File(solutionFileStr);
		inst.numPOIs = size;
		inst.tmax = tmax;
		instances.add(inst);
	}
	
	public List<Instance> getInstances() {
		return instances;
	}
	
	public Instance instance(int index) {
		return instances.get(index);
	}
	
	
	
	// setup input for the entire experiment
	public static SchildeInput entireInput() {
		SchildeInput input = new SchildeInput();
		
		double[] tmax21 = new double[]{1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
		double[] tmax32 = new double[]{2.0, 3.0, 4.0, 5.0,
				6.0, 7.0, 8.0};
		double[] tmax33 = new double[]{2.0, 3.0, 4.0, 5.0,
				6.0, 7.0, 8.0, 9.0, 10.0};
		double[] tmax64 = new double[]{2.0, 3.0, 4.0, 5.0,
				6.0, 7.0, 8.0};
		double[] tmax66 = new double[]{2.0, 3.0, 4.0, 5.0,
				6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0};
		double[] tmax559 = new double[]{4.0, 5.0,
				6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0,
				14.0};
		double[] tmax562 = new double[]{2.0, 3.0, 4.0, 5.0,
				6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0,
				14.0};
		
		int n21 = tmax21.length;
		int n32 = tmax32.length;
		int n33 = tmax33.length;
		int n64 = tmax64.length;
		int n66 = tmax66.length;
		int n559 = tmax559.length;
		int n562 = tmax562.length;
		int[] lengths = new int[]{n21, n32, n33, n64, n66, n559, n562};
		
		
		String[] nameSet = {"2_p21", "2_p32", "2_p33", "2_p64", "2_p66", 
				"2_p559", "2_p562"}; // used to create the file name
		
		int[] sizes = new int[]{21, 32, 33, 64, 66, 559, 562};
		
		List<double[]> tmaxList = new ArrayList<double[]>();
		tmaxList.add(tmax21);
		tmaxList.add(tmax32);
		tmaxList.add(tmax33);
		tmaxList.add(tmax64);
		tmaxList.add(tmax66);
		tmaxList.add(tmax559);
		tmaxList.add(tmax562);
		
		String path = "data/moop/2 objectives/";
		String speedFileStr = path + "speedMatrix.txt";
		
		String resPath = "data/moop/results/";
		
		int idx = 0;
		for (int i = 0; i < nameSet.length; i++) {
			String datasetPath = path + nameSet[i] + "/";
			String catFileStr = datasetPath + "arc_cat" + ".txt";
			
			String datasetResPath = resPath + nameSet[i] + "/";
			
			System.out.println(nameSet[i] + " starts from index " + idx);
			idx += lengths[i];
			
			if (i < 5) {
				for (int j = 0; j < tmaxList.get(i).length; j++) {
					String name = nameSet[i] + "_t" + tmaxList.get(i)[j];
					String fileStr = datasetPath + nameSet[i] + "_t030.txt";
					
					String solFileSuffix = "" + ((int)(10*tmaxList.get(i)[j]));
					if (tmaxList.get(i)[j] < 10) {
						solFileSuffix = "0" + solFileSuffix;
					}
					
					String solFileStr = datasetResPath + nameSet[i] + "_t" + solFileSuffix + ".csv";
					
					input.addInstance(name, fileStr, speedFileStr, catFileStr, solFileStr, sizes[i], tmaxList.get(i)[j]);
				}
			}
			else {
				for (int j = 0; j < tmaxList.get(i).length; j++) {
					String name = nameSet[i] + "_t" + tmaxList.get(i)[j];
					String fileStr = datasetPath + nameSet[i] + ".txt";
					
					String solFileSuffix = "" + ((int)(10*tmaxList.get(i)[j]));
					if (tmaxList.get(i)[j] < 10) {
						solFileSuffix = "0" + solFileSuffix;
					}
					
					String solFileStr = datasetResPath + nameSet[i] + "_t" + solFileSuffix + ".csv";
					
					input.addInstance(name, fileStr, speedFileStr, catFileStr, solFileStr, sizes[i], tmaxList.get(i)[j]);
				}
			}
		}
		
		return input;
	}
}

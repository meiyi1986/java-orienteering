package com.yimei.multiobjective.experiment2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yimei.moo.NDList;

import com.yimei.multiobjective.core.Vertex;
import com.yimei.multiobjective.experiment.Instance;
import com.yimei.multiobjective.experiment.RunOutput;
import com.yimei.multiobjective.problem.Dataset;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.core.Solution;
import com.yimei.multiobjective.routing.core.SolutionNDComparator;
import com.yimei.multiobjective.routing.core.SolutionScoreEquator;

public class SchildeSolutionReader {
	
	private final String resDir = "data/SchildeResults/";
	
	private Problem problem;
	private File solutionFile;
	private String instanceName;
	
	private List<Solution> ACOSolutions;
	private List<Solution> VNSSolutions;
	
	private SolutionNDComparator comparator = new SolutionNDComparator();
	private SolutionScoreEquator equator = new SolutionScoreEquator();
	
	public SchildeSolutionReader(Problem problem, File solutionFile) {
		this.problem = problem;
		this.solutionFile = solutionFile;
		instanceName = "";
	}
	
	public SchildeSolutionReader(Instance instance) {
		problem = new Problem(instance.speedFile, instance.categoryFile,
				instance.file, Dataset.SCHILDE);
		problem.setTimeLimit(instance.tmax);
		
		solutionFile = instance.solutionFile;
		instanceName = instance.name;
	}
	
	public void read() {
		
		
		
		
		String line = "";
		String cvsSplitBy = ";";
		String[] segments;
		
		int runACO = 0;
		int runVNS = 0;
		
		try (BufferedReader br = new BufferedReader(new FileReader(solutionFile))) {
			while ((line = br.readLine()) != null) {
				if (line.equals(";Tours ACO")) {
					ACOSolutions = new ArrayList<Solution>();
					
					line = br.readLine();
					while ((line = br.readLine()) != null) {
						if (line.isEmpty())
							break;
						
						segments = line.split(cvsSplitBy);
						String[] sequence = segments[segments.length-1].split("-");
						List<Vertex> vSeq = new ArrayList<Vertex>();
						for (String str : sequence) {
							int idx = Integer.parseInt(str);
							vSeq.add(problem.vertices().get(idx));
						}
						
						Solution indi = new Solution(problem.numScores());
						indi.add(vSeq.get(0), 0, problem);
						indi.add(vSeq.get(vSeq.size()-1), 1, problem);
						int insertPosition = 1;
						for (int i = 1; i < vSeq.size()-1; i++) {
							if (indi.feasibleToAdd(vSeq.get(i), insertPosition, problem)) {
								indi.add(vSeq.get(i), insertPosition, problem);
								insertPosition ++;
							}
						}
						
						ACOSolutions.add(indi);
					}
					
					RunOutput rop = new RunOutput(null, runACO, ACOSolutions);
					
					File resInstanceDir = new File(resDir + "/ACOSolutions/" + instanceName);
					if (!resInstanceDir.exists())
						resInstanceDir.mkdirs();
					File runFile = new File(resInstanceDir + "/" + instanceName + ".res" + rop.runID());

					
					try {
						rop.printToFile(runFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					runACO ++;
				}
				
				if (line.equals(";Tours VNS")) {					
					VNSSolutions = new ArrayList<Solution>();
					
					line = br.readLine();
					while ((line = br.readLine()) != null) {
						if (line.isEmpty())
							break;
						
						segments = line.split(cvsSplitBy);
						String[] sequence = segments[segments.length-1].split("-");
						List<Vertex> vSeq = new ArrayList<Vertex>();
						for (String str : sequence) {
							int idx = Integer.parseInt(str);
							vSeq.add(problem.vertices().get(idx));
						}
						
						Solution indi = new Solution(problem.numScores());
						indi.add(vSeq.get(0), 0, problem);
						indi.add(vSeq.get(vSeq.size()-1), 1, problem);
						int insertPosition = 1;
						for (int i = 1; i < vSeq.size()-1; i++) {
							if (indi.feasibleToAdd(vSeq.get(i), insertPosition, problem)) {
								indi.add(vSeq.get(i), insertPosition, problem);
								insertPosition ++;
							}
						}
						
						VNSSolutions.add(indi);
					}
					
					RunOutput rop = new RunOutput(null, runVNS, VNSSolutions);
					
					File resInstanceDir = new File(resDir + "/VNSSolutions/" + instanceName);
					if (!resInstanceDir.exists())
						resInstanceDir.mkdirs();
					File runFile = new File(resInstanceDir + "/" + instanceName + ".res" + rop.runID());

					
					try {
						rop.printToFile(runFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					runVNS ++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		// write to another file
//		File file = new File(solutionFile + "_td.txt");
//		
//		List<Solution> solutions = new ArrayList<Solution>();
//		for (int i = 0; i < ACOSolutions.size(); i++) {
//			solutions.add(ACOSolutions.get(i));
//		}
//		
//		for (int i = 0; i < VNSSolutions.size(); i++) {
//			solutions.add(VNSSolutions.get(i));
//		}
//		
//		RunOutput rop = new RunOutput(null, 0, solutions);
//		try {
//			rop.printToFile(file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	
	public static void main(String[] args) {
		SchildeInput input = SchildeInput.entireInput();
		for (int n = 0; n < 69; n++) {
			Instance instance = input.getInstances().get(n);
			SchildeSolutionReader reader = new SchildeSolutionReader(instance);
			reader.read();
		}
		
		
//		File speedFile = new File("data/moop/2 objectives/speedMatrix.txt");
//		File catFile = new File("data/moop/2 objectives/2_p21/arc_cat.txt");
//		File problemFile = new File("data/moop/2 objectives/2_p21/2_p21_t030.txt");
//		Problem problem = new Problem(speedFile, catFile, problemFile, Dataset.SCHILDE);
//		problem.setTimeLimit(4);
//		
//		File solutionFile = new File("data/moop/results/2_p21/2_p21_t040.csv");
//		
//		SchildeSolutionReader reader = new SchildeSolutionReader(problem, solutionFile);
//		reader.read();
	}

}

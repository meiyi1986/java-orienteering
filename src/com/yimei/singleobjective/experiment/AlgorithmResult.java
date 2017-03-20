package com.yimei.singleobjective.experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yimei.multiobjective.routing.core.Solution;

import yimei.moo.Objective;
import yimei.moo.ObjectiveValues;
import yimei.moo.OptType;

public class AlgorithmResult {
	
	private List<Solution> solutions;
	private long seed;
	private double time;
	
	public AlgorithmResult (File file) {
		solutions = new ArrayList<Solution>();
		
		// read the result file
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String sCurrentLine;
			String [] lineElements;
			
			// first line is the number of solutions
			sCurrentLine = br.readLine();
			int numSolutions = Integer.parseInt(sCurrentLine);
			
			// enumerate the solutions
			for (int i = 0; i < numSolutions; i++) {
				sCurrentLine = br.readLine();
				lineElements = sCurrentLine.split("\\s+");
				Solution sol = new Solution(lineElements.length);
				
				for (int index = 0; index < lineElements.length; index++) {
					sol.setScore(index, Double.parseDouble(lineElements[index]));
				}
				
				solutions.add(sol);
			}
			
			// skip an empty line
			sCurrentLine = br.readLine();
			// skip the lines of all the solutions
			for (int i = 0; i < numSolutions; i++) {
				sCurrentLine = br.readLine();
			}
			// skip another empty line
			sCurrentLine = br.readLine();
			
			// read the seed
			sCurrentLine = br.readLine();
			long seed = Long.parseLong(sCurrentLine);
			
			// read the time
			sCurrentLine = br.readLine();
			time = Double.parseDouble(sCurrentLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Solution> getSolutions() {
		return solutions;
	}
	
	public List<ObjectiveValues> getObjectiveValues() {
		List<ObjectiveValues> objsList = new ArrayList<ObjectiveValues>();
		
		for (Solution sol : solutions) {
			ObjectiveValues objs = new ObjectiveValues();
			for (int i = 0; i < sol.scores().size(); i++) {
				objs.setObjective(new Objective(("obj" + i), OptType.MAX), sol.score(i));
			}
			
			objsList.add(objs);
		}
		
		return objsList;
	}
	
	public double getTime() {
		return time;
	}
}

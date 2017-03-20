package com.yimei.singleobjective.experiment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.yimei.multiobjective.routing.core.Solution;

public class RunOutput {

	private Instance instance;
	private int runID;
	private List<Solution> solutions;
	
	public RunOutput(Instance instance, int runID, List<Solution> solutions) {
		this.instance = instance;
		this.runID = runID;
		this.solutions = solutions;
	}
	
	public String name() {
		return instance.name;
	}
	
	public int runID() {
		return runID;
	}
	
	public void printToFile(File runFile) throws IOException {		
		if (!runFile.exists()) {
			runFile.createNewFile();
		}
		
		FileWriter fileWriter = new FileWriter(runFile);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		// write the number of solutions
		bufferedWriter.write(Integer.toString(solutions.size()));
		bufferedWriter.newLine();
		
		// write the objective vectors
		for (Solution solution: solutions) {
			bufferedWriter.write(solution.scoreString());
			bufferedWriter.newLine();
		}
		
		// write the solutions
		bufferedWriter.newLine();
		for (Solution solution: solutions) {
			bufferedWriter.write(solution.tourString());
			bufferedWriter.newLine();
		}
		
		bufferedWriter.close();
	}
}

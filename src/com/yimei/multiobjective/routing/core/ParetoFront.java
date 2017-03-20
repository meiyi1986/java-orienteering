package com.yimei.multiobjective.routing.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***
 * The Pareto front of the algorithm, consisting of non-dominated solutions
 * @author Administrator
 *
 */

public class ParetoFront {

	private List<Solution> solutions;
	private List<Double> bestScores;
	
	
	public ParetoFront(int numScores) {
		solutions = new ArrayList<Solution>();
		bestScores = new ArrayList<Double>(Collections.nCopies(numScores, 0.0));
	}
	
	public List<Solution> getSolutions() {
		return solutions;
	}
	
	public Solution getSolution(int index) {
		return solutions.get(index);
	}
	
	public List<Double> getBestScores() {
		return bestScores;
	}
	
	public double getBestScore(int index) {
		return bestScores.get(index);
	}
	
	public void computeBestScores() {
		for (int i = 0; i < bestScores.size(); i++) {
			bestScores.set(i, 0.0);
			for (Solution solution : solutions) {
				if (solution.score(i) > bestScores.get(i)) {
					bestScores.set(i, solution.score(i));
				}
			}
		}
	}
	
	public void add(Solution s) {
		
		// detect if s has exactly the same objective as some solution in the front
		boolean redundant = false;
		for (Solution solution : solutions) {
			int sameObj = 0;
			for (int i = 0; i < s.scores().size(); i++) {
				if (s.score(i) == solution.score(i)) {
					sameObj ++;
				}
			}
			
			if (sameObj == s.scores().size()) {
				redundant = true;
				break;
			}
		}
		
		if (redundant)
			return;
		
		// detect whether s is dominated by any solution in the front
		boolean isDominated = false;
		for (Solution solution : solutions) {
			if (solution.dominanceRelation(s) == -1) {
				isDominated = true;
				break;
			}
		}
		
		if (isDominated) {
			return;
		}
		
		// s is not dominated by any solution in the front
		// remove all the solutions in the front that are dominated by s
		for (int i = solutions.size()-1; i > -1; i--) {
			if (solutions.get(i).dominanceRelation(s) == 1) {
				solutions.remove(i);
			}
		}
		
		// add s to the front
		solutions.add(s);
		
		// update best scores
		computeBestScores();
	}
	
	
	public void printToFile(File file) throws IOException {

		if (!file.exists()) {
			file.createNewFile();
		}
		
		FileWriter fileWriter = new FileWriter(file);
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
	
	
	public void printMe() {
		System.out.println("printing Pareto front:");
		for (Solution solution : solutions) {
			solution.printMe();
		}
		System.out.println(bestScores);
	}
}

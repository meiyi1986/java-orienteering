package com.yimei.singleobjective.experiment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.inference.TestUtils;

import com.yimei.multiobjective.routing.core.ParetoFront;
import com.yimei.multiobjective.routing.core.Solution;

import yimei.util.Statistics;



/***
 * Calculate multi-objective measures
 * @author yi
 *
 */

public class Measures {
	
	public static ParetoFront getParetoFront(List<AlgorithmResult> ars, int n) {
		
		ParetoFront PF = new ParetoFront(n);
		for (AlgorithmResult ar : ars) {
			for (Solution sol : ar.getSolutions()) {
				PF.add(sol);
			}
		}
		
		return PF;
	}
	
	public static double IGD(List<Solution> solutions, ParetoFront PF) {
		double IGDValue = 0.0;
		
		for (Solution xSol : PF.getSolutions()) {
			// find the closest solution to xSol
			double minDist = Double.MAX_VALUE;
			Solution closestSol;
			
			for (Solution ySol : solutions) {
				double dist = ySol.euclideanDistanceFrom(xSol);
				
				if (dist < minDist) {
					minDist = dist;
					closestSol = ySol;
				}
			}
			
			IGDValue += minDist;
		}
		
		IGDValue /= PF.getSolutions().size();
		
		return IGDValue;
	}
	
	
	public static double hypervolume2D(List<Solution> solutions, Solution ref) {
		// sort the solutions in increasing order of the first objective
		for (int i = 0; i < solutions.size()-1; i++) {
			for (int j = i+1; j < solutions.size(); j++) {
				if (solutions.get(i).score(0) > solutions.get(j).score(0)) {
					// swap
					Solution tmp = solutions.get(i);
					solutions.set(i, solutions.get(j));
					solutions.set(j, tmp);
				}
			}
		}
		
		double hv = 0.0;
		double width, height, area;
		
		// the first area
		width = (solutions.get(0).score(0) - ref.score(0));
		height = solutions.get(0).score(1) - ref.score(1);
		area = width * height;
		hv += area;
		
		for (int i = 1; i < solutions.size(); i++) {
			width = solutions.get(i).score(0) - solutions.get(i-1).score(0);
			height = solutions.get(i).score(1) - ref.score(1);
			area = width * height;
			hv += area;
		}
		
		return hv;
	}
	

	
	public static double meanTime(List<AlgorithmResult> ars) {
		double mean = 0.0;
		for (AlgorithmResult ar : ars) {
			mean += ar.getTime();
		}
		
		mean /= ars.size();
		
		return mean;
	}
	
	
	public static void main(String[] args) throws IOException {
		
		// get the input
		Input input = Input.entireInput();
		
		// result file path
		String ACSResDir = "data/Results/ACS/p0=0.25_rho=0.005/";
		String MAResDir = "data/Results/MA/";
		
		
		
		List<Double> ACSIGDMean = new ArrayList<Double>();
		List<Double> ACSIGDStd = new ArrayList<Double>();
		List<Double> ACSIGDBest = new ArrayList<Double>();
		List<Double> ACSTimeMean = new ArrayList<Double>();
		
		List<Double> ACSHVMean = new ArrayList<Double>();
		List<Double> ACSHVStd = new ArrayList<Double>();
		List<Double> ACSHVBest = new ArrayList<Double>();
		
		List<Double> MAIGDMean = new ArrayList<Double>();
		List<Double> MAIGDStd = new ArrayList<Double>();
		List<Double> MAIGDBest = new ArrayList<Double>();
		List<Double> MATimeMean = new ArrayList<Double>();
		
		List<Double> MAHVMean = new ArrayList<Double>();
		List<Double> MAHVStd = new ArrayList<Double>();
		List<Double> MAHVBest = new ArrayList<Double>();
		
		
		List<Boolean> IGDh = new ArrayList<Boolean>();
		List<Boolean> HVh = new ArrayList<Boolean>();
		
		for (Instance instance : input.getInstances()) {
			List<AlgorithmResult> ACSARList = new ArrayList<AlgorithmResult>();
			List<AlgorithmResult> MAARList = new ArrayList<AlgorithmResult>();
			List<AlgorithmResult> ARList = new ArrayList<AlgorithmResult>();
			
			
			
			/** get the Pareto front of ACS **/
			for (int i = 0; i < 30; i++) {
				File runFile = new File(ACSResDir + instance.name + ".res" + i);

				System.out.println(runFile.toString());
				
				AlgorithmResult ar = new AlgorithmResult(runFile);
				ACSARList.add(ar);
				ARList.add(ar);
			}
			
			ParetoFront ACSPF = getParetoFront(ACSARList, 2);
			
			// output Pareto front file
			File ACSPFFile = new File(ACSResDir + instance.name + ".PF");
			ACSPF.printToFile(ACSPFFile);

			/** get the Pareto front of MA **/
			for (int i = 0; i < 30; i++) {
				File runFile = new File(MAResDir + instance.name + ".res" + i);

				System.out.println(runFile.toString());
				
				AlgorithmResult ar = new AlgorithmResult(runFile);
				MAARList.add(ar);
				ARList.add(ar);
			}
			
			ParetoFront MAPF = getParetoFront(MAARList, 2);
			
			// output Pareto front file
			File MAPFFile = new File(MAResDir + instance.name + ".PF");
			MAPF.printToFile(MAPFFile);
			
			ParetoFront PF = getParetoFront(ARList, 2);
			
			/*** Calculate the IGD measures ***/
			double [] ACSIGDs = new double [30];
			double [] MAIGDs = new double [30];
			
			for (int i = 0; i < 30; i++) {
				double ACSIGD = IGD(ACSARList.get(i).getSolutions(), PF);
				
				double MAIGD = IGD(MAARList.get(i).getSolutions(), PF);
				
				ACSIGDs[i] = ACSIGD;
				
				MAIGDs[i] = MAIGD;
			}
			
			SummaryStatistics ACSIGDStats = new SummaryStatistics();
			
			SummaryStatistics MAIGDStats = new SummaryStatistics();
			
			for (int i = 0; i < 30; i++) {
				ACSIGDStats.addValue(ACSIGDs[i]);
				
				MAIGDStats.addValue(MAIGDs[i]);
			}
			
			
			double currACSIGDMean = ACSIGDStats.getMean();
			double currACSIGDStd = ACSIGDStats.getStandardDeviation();
			double currACSIGDBest = IGD(ACSPF.getSolutions(), PF);
			
			double currMAIGDMean = MAIGDStats.getMean();
			double currMAIGDStd = MAIGDStats.getStandardDeviation();
			double currMAIGDBest = IGD(MAPF.getSolutions(), PF);

			boolean currIGDh = TestUtils.tTest(ACSIGDs, MAIGDs, 0.05);
			
			ACSIGDMean.add(currACSIGDMean);
			ACSIGDStd.add(currACSIGDStd);
			ACSIGDBest.add(currACSIGDBest);
			
			MAIGDMean.add(currMAIGDMean);
			MAIGDStd.add(currMAIGDStd);
			MAIGDBest.add(currMAIGDBest);
			
			IGDh.add(currIGDh);
			
			
			/*** Calculate the HV measures ***/
			double [] ACSHVs = new double [30];
			double [] MAHVs = new double [30];
			
			Solution ref = new Solution(2);
			ref.setScore(0, 0.0);
			ref.setScore(1, 0.0);
			for (int i = 0; i < 30; i++) {
				double ACSHV = hypervolume2D(ACSARList.get(i).getSolutions(), ref);
				
				double MAHV = hypervolume2D(MAARList.get(i).getSolutions(), ref);
				
				ACSHVs[i] = ACSHV;
				
				MAHVs[i] = MAHV;
			}
			
			SummaryStatistics ACSHVStats = new SummaryStatistics();
			
			SummaryStatistics MAHVStats = new SummaryStatistics();
			
			for (int i = 0; i < 30; i++) {
				ACSHVStats.addValue(ACSHVs[i]);
				
				MAHVStats.addValue(MAHVs[i]);
			}
			
			
			double currACSHVMean = ACSHVStats.getMean();
			double currACSHVStd = ACSHVStats.getStandardDeviation();
			double currACSHVBest = hypervolume2D(ACSPF.getSolutions(), ref);
			
			double currMAHVMean = MAHVStats.getMean();
			double currMAHVStd = MAHVStats.getStandardDeviation();
			double currMAHVBest = hypervolume2D(MAPF.getSolutions(), ref);

			boolean currHVh = TestUtils.tTest(ACSHVs, MAHVs, 0.05);
			
			ACSHVMean.add(currACSHVMean);
			ACSHVStd.add(currACSHVStd);
			ACSHVBest.add(currACSHVBest);
			
			MAHVMean.add(currMAHVMean);
			MAHVStd.add(currMAHVStd);
			MAHVBest.add(currMAHVBest);
			
			HVh.add(currHVh);
			
			
			/*** compute the mean time ***/
			double thisACSTime = meanTime(ACSARList);
			double thisMATime = meanTime(MAARList);
			
			ACSTimeMean.add(thisACSTime);
			MATimeMean.add(thisMATime);
		}
		
		/***** Latex Table ***/
//		for (int i = 0; i < input.getInstances().size(); i++) {
//			System.out.println("{" + input.instance(i).name + "} & " +
//					"{" + ACSIGDMean.get(i) + "(" + ACSIGDStd.get(i) + ")} & " +
//					"{" + MAIGDMean.get(i) + "(" + MAIGDStd.get(i) + ")} & " +
//					"{" + ACSTimeMean.get(i) + "} & {" + MATimeMean.get(i) + "} " +
//					"\\\\");
//			
//		}
		
		// IGD
		for (int i = 0; i < input.getInstances().size(); i++) {
			if (IGDh.get(i) == true) {
				System.out.format("{%s} & {%.2f(%.2f)} & {\\bf %.2f(%.2f)} & & ",
						input.instance(i).name, ACSIGDMean.get(i), ACSIGDStd.get(i),
						MAIGDMean.get(i), MAIGDStd.get(i));
			}
			else {
				System.out.format("{%s} & {%.2f(%.2f)} & {%.2f(%.2f)} & & ",
						input.instance(i).name, ACSIGDMean.get(i), ACSIGDStd.get(i),
						MAIGDMean.get(i), MAIGDStd.get(i));
			}
			
			if (ACSIGDBest.get(i) > MAIGDBest.get(i)) {
				System.out.format("{%.2f} & {%.2f$^\\dagger$} ", 
						ACSIGDBest.get(i), MAIGDBest.get(i));
			}
			else if (ACSIGDBest.get(i) < MAIGDBest.get(i)) {
				System.out.format("{%.2f$^\\dagger$} & {%.2f} ", 
						ACSIGDBest.get(i), MAIGDBest.get(i));
			}
			else {
				System.out.format("{%.2f} & {%.2f} ", 
						ACSIGDBest.get(i), MAIGDBest.get(i));
			}
			
			if ((i % 2) == 1 || i == input.getInstances().size()-1) {
				System.out.format("\\\\\n");
			}
			else {
				System.out.format(" & ");
			}
		}
		System.out.format("Overall & {%.2f(%.2f)} & {%.2f(%.2f)} & & {%.2f} & {%.2f} \\\\\n",
				Statistics.doubleMean(ACSIGDMean), Statistics.doubleMean(ACSIGDStd),
				Statistics.doubleMean(MAIGDMean), Statistics.doubleMean(MAIGDStd),
				Statistics.doubleMean(ACSIGDBest), Statistics.doubleMean(MAIGDBest));
		
		System.out.println("");
		
		// hypervolume
		for (int i = 0; i < input.getInstances().size(); i++) {
			if (IGDh.get(i) == true) {
				System.out.format("{%s} & {%.1f(%.1f)} & {\\bf %.1f(%.1f)} & & ",
						input.instance(i).name, ACSHVMean.get(i), ACSHVStd.get(i),
						MAHVMean.get(i), MAHVStd.get(i));
			}
			else {
				System.out.format("{%s} & {%.1f(%.1f)} & {%.1f(%.1f)} & & ",
						input.instance(i).name, ACSHVMean.get(i), ACSHVStd.get(i),
						MAHVMean.get(i), MAHVStd.get(i));
			}
			
			if (ACSHVBest.get(i) < MAHVBest.get(i)) {
				System.out.format("{%.1f} & {%.1f$^\\dagger$} \\\\\n", 
						ACSHVBest.get(i), MAHVBest.get(i));
			}
			else if (ACSHVBest.get(i) > MAHVBest.get(i)) {
				System.out.format("{%.1f$^\\dagger$} & {%.1f} \\\\\n", 
						ACSHVBest.get(i), MAHVBest.get(i));
			}
			else {
				System.out.format("{%.1f} & {%.1f} \\\\\n", 
						ACSHVBest.get(i), MAHVBest.get(i));
			}
		}
		System.out.format("Overall & {%.1f(%.1f)} & {%.1f(%.1f)} & & {%.1f} & {%.1f} \\\\\n",
				Statistics.doubleMean(ACSHVMean), Statistics.doubleMean(ACSHVStd),
				Statistics.doubleMean(MAHVMean), Statistics.doubleMean(MAHVStd),
				Statistics.doubleMean(ACSHVBest), Statistics.doubleMean(MAHVBest));
		
		System.out.println("");
		
		// hypervolume average
		for (int i = 0; i < input.getInstances().size(); i++) {
			if (IGDh.get(i) == true) {
				System.out.format("{%s} & {%.1f(%.1f)} & {\\bf %.1f(%.1f)} ",
						input.instance(i).name, ACSHVMean.get(i), ACSHVStd.get(i),
						MAHVMean.get(i), MAHVStd.get(i));
			}
			else {
				System.out.format("{%s} & {%.1f(%.1f)} & {%.1f(%.1f)} ",
						input.instance(i).name, ACSHVMean.get(i), ACSHVStd.get(i),
						MAHVMean.get(i), MAHVStd.get(i));
			}
			
			if ((i % 2 == 1 || i == input.getInstances().size()-1)) {
				System.out.format(" \\\\\n");
			}
			else {
				System.out.format(" & ");
			}
		}
		System.out.format("Overall & {%.1f(%.1f)} & {%.1f(%.1f)} \\\\\n",
				Statistics.doubleMean(ACSHVMean), Statistics.doubleMean(ACSHVStd),
				Statistics.doubleMean(MAHVMean), Statistics.doubleMean(MAHVStd));
		
		System.out.println("");
		
		// hypervolume best
		for (int i = 0; i < input.getInstances().size(); i++) {
			
			if (ACSHVBest.get(i) < MAHVBest.get(i)) {
				System.out.format("{%s} & {%.1f} & {%.1f$^\\dagger$} ", 
						input.instance(i).name, ACSHVBest.get(i), MAHVBest.get(i));
			}
			else if (ACSHVBest.get(i) > MAHVBest.get(i)) {
				System.out.format("{%s} & {%.1f$^\\dagger$} & {%.1f} ", 
						input.instance(i).name, ACSHVBest.get(i), MAHVBest.get(i));
			}
			else {
				System.out.format("{%s} & {%.1f} & {%.1f} ", 
						input.instance(i).name, ACSHVBest.get(i), MAHVBest.get(i));
			}
			
			if ((i % 3 == 2 || i == input.getInstances().size()-1)) {
				System.out.format(" \\\\\n");
			}
			else {
				System.out.format(" & ");
			}
		}
		System.out.format("Overall & {%.1f} & {%.1f} \\\\\n",
				Statistics.doubleMean(ACSHVBest), Statistics.doubleMean(MAHVBest));
		
		System.out.println("");
		
		int [] numVertices = {
				32, 32, 32, 32, 32, 32, 32, 32, 32,
				21, 21, 21, 21, 21, 21, 21, 21, 21,
				33, 33, 33, 33, 33, 33, 33, 33, 33,
				100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
				66, 66, 66, 
				64, 64, 64, 64, 64, 64, 64, 64, 64, 
				102, 102, 102, 102, 102, 102, 102, 102, 102, 102,
		};
		
		int[] numInstances = {
				9, 9, 9, 10, 3, 9, 10
		};
		
		double[] tmax = {
				5, 6, 7, 8, 9, 10, 11, 12, 13,
				5, 6, 7, 8, 9, 10, 11, 12, 13,
				5.5, 6.5, 7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5,
				5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
				5.5, 6, 7,
				6.5, 7, 8, 9, 10, 11, 12, 13, 14,
				4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
		};
		
		// mean time
		
		
		for (int i = 0; i < input.getInstances().size(); i++) {
			System.out.format("{%s} & {%d} & {%.1f} &", 
					input.instance(i).name, numVertices[i], tmax[i]);
			
			if (ACSTimeMean.get(i) < MATimeMean.get(i)) {
				System.out.format("{\\bf %.1f} & {%.1f} \\\\\n", 
						ACSTimeMean.get(i) / 1000, MATimeMean.get(i) / 1000);
			}
			else if (ACSTimeMean.get(i) > MATimeMean.get(i)) {
				System.out.format("{%.1f} & {\\bf %.1f} \\\\\n", 
						ACSTimeMean.get(i) / 1000, MATimeMean.get(i) / 1000);
			}
			else {
				System.out.format("{%.1f} & {%.1f} \\\\\n", 
						ACSTimeMean.get(i) / 1000, MATimeMean.get(i) / 1000);
			}
		}
		System.out.format("Overall & {%.1f} & {%.1f} & {%.1f} & {%.1f} \\\\\n", 
				Statistics.intMean(numVertices), Statistics.doubleMean(tmax),
				Statistics.doubleMean(ACSTimeMean) / 1000, Statistics.doubleMean(MATimeMean) / 1000);
		
//		// mean time
//		for (int i = 0; i < 27; i++) {
//			int index;
//			index = i;
//			if (ACSTimeMean.get(index) < MATimeMean.get(index)) {
//				System.out.format("{%s} & {\\bf %.1f} & {%.1f} & ", 
//						input.instance(index).name, ACSTimeMean.get(index) / 1000, MATimeMean.get(index) / 1000);
//			}
//			else if (ACSTimeMean.get(index) > MATimeMean.get(index)) {
//				System.out.format("{%s} & {%.1f} & {\\bf %.1f} & ", 
//						input.instance(index).name, ACSTimeMean.get(index) / 1000, MATimeMean.get(index) / 1000);
//			}
//			else {
//				System.out.format("{%s} & {%.1f} & {%.1f} & ", 
//						input.instance(index).name, ACSTimeMean.get(index) / 1000, MATimeMean.get(index) / 1000);
//			}
//			
//			index = i + 27;
//			if (ACSTimeMean.get(index) < MATimeMean.get(index)) {
//				System.out.format("{%s} & {\\bf %.1f} & {%.1f} \\\\\n", 
//						input.instance(index).name, ACSTimeMean.get(index) / 1000, MATimeMean.get(index) / 1000);
//			}
//			else if (ACSTimeMean.get(index) > MATimeMean.get(index)) {
//				System.out.format("{%s} & {%.1f} & {\\bf %.1f} \\\\\n", 
//						input.instance(index).name, ACSTimeMean.get(index) / 1000, MATimeMean.get(index) / 1000);
//			}
//			else {
//				System.out.format("{%s} & {%.1f} & {%.1f} \\\\\n", 
//						input.instance(index).name, ACSTimeMean.get(index) / 1000, MATimeMean.get(index) / 1000);
//			}
//		}
//		
//		for (int i = 54; i < 59; i++) {
//			int index = i;
//			if (ACSTimeMean.get(index) < MATimeMean.get(index)) {
//				System.out.format("& & & {%s} & {\\bf %.1f} & {%.1f} \\\\\n", 
//						input.instance(index).name, ACSTimeMean.get(index) / 1000, MATimeMean.get(index) / 1000);
//			}
//			else if (ACSTimeMean.get(index) > MATimeMean.get(index)) {
//				System.out.format("& & & {%s} & {%.1f} & {\\bf %.1f} \\\\\n", 
//						input.instance(index).name, ACSTimeMean.get(index) / 1000, MATimeMean.get(index) / 1000);
//			}
//			else {
//				System.out.format("& & & {%s} & {%.1f} & {%.1f} \\\\\n", 
//						input.instance(index).name, ACSTimeMean.get(index) / 1000, MATimeMean.get(index) / 1000);
//			}
//		}
//		
//		System.out.format("Overall & {%.1f} & {%.1f} \\\\\n", 
//				Statistics.doubleMean(ACSTimeMean) / 1000, Statistics.doubleMean(MATimeMean) / 1000);
	}
}

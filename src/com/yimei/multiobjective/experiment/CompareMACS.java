package com.yimei.multiobjective.experiment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.inference.TestUtils;

import com.yimei.multiobjective.routing.core.ParetoFront;
import com.yimei.multiobjective.routing.core.Solution;

import yimei.util.Statistics;

public class CompareMACS {

	public static void main(String[] args) throws IOException {
		
		// get the input
		Input input = Input.entireInput();
		
		// result file path
		String[] resDirs = {
				"data/Results/ACS/p0=0.25_rho=0.005/",
				"data/Results/ACS/p0=0.5_rho=0.005/",
				"data/Results/ACS/p0=0.75_rho=0.005/",
				"data/Results/ACS/p0=0.25_rho=0.01/",
				"data/Results/ACS/p0=0.5_rho=0.01/",
				"data/Results/ACS/p0=0.75_rho=0.01/",
				"data/Results/ACS/p0=0.25_rho=0.02/",
				"data/Results/ACS/p0=0.5_rho=0.02/",
				"data/Results/ACS/p0=0.75_rho=0.02/",
		};
		
		List<List<Double>> IGDMeans = new ArrayList<List<Double>>();
		List<List<Double>> IGDStds = new ArrayList<List<Double>>();
		List<List<Double>> IGDBests = new ArrayList<List<Double>>();
		for (int i = 0; i < resDirs.length; i++) {
			IGDMeans.add(new ArrayList<Double>());
			IGDStds.add(new ArrayList<Double>());
			IGDBests.add(new ArrayList<Double>());
		}
		
		List<List<Double>> HVMeans = new ArrayList<List<Double>>();
		List<List<Double>> HVStds = new ArrayList<List<Double>>();
		List<List<Double>> HVBests = new ArrayList<List<Double>>();
		for (int i = 0; i < resDirs.length; i++) {
			HVMeans.add(new ArrayList<Double>());
			HVStds.add(new ArrayList<Double>());
			HVBests.add(new ArrayList<Double>());
		}
		
		List<List<Double>> timeMeans = new ArrayList<List<Double>>();
		for (int i = 0; i < resDirs.length; i++) {
			timeMeans.add(new ArrayList<Double>());
		}
		
//		List<Boolean> IGDh12 = new ArrayList<Boolean>();
//		List<Boolean> IGDh13 = new ArrayList<Boolean>();
//		List<Boolean> IGDh23 = new ArrayList<Boolean>();
//		List<Boolean> HVh12 = new ArrayList<Boolean>();
//		List<Boolean> HVh13 = new ArrayList<Boolean>();
//		List<Boolean> HVh23 = new ArrayList<Boolean>();
		
		for (Instance instance : input.getInstances()) {
			List<List<AlgorithmResult>> ARLists = new ArrayList<List<AlgorithmResult>>();
			for (int i = 0; i < resDirs.length; i++) {
				ARLists.add(new ArrayList<AlgorithmResult>());
			}
			List<AlgorithmResult> entireARList = new ArrayList<AlgorithmResult>();
			List<ParetoFront> PFs = new ArrayList<ParetoFront>();
			
			for (int i = 0; i < resDirs.length; i++) {
				String currResDir = resDirs[i];
				List<AlgorithmResult> currARList = ARLists.get(i);
				
				for (int j = 0; j < 30; j ++) {
				
					File runFile = new File(currResDir + instance.name + ".res" + j);

					System.out.println(runFile.toString());
					
					AlgorithmResult ar = new AlgorithmResult(runFile);
					currARList.add(ar);
					entireARList.add(ar);
				}
				
				ParetoFront currPF = Measures.getParetoFront(currARList, 2);
				PFs.add(currPF);
				
				// output Pareto front file
				File currPFFile = new File(currResDir + instance.name + ".PF");
				currPF.printToFile(currPFFile);
			}
			
			
			// the entire Pareto front
			ParetoFront entirePF = Measures.getParetoFront(entireARList, 2);
			
			/*** Calculate the IGD measures ***/
			List<double[]> IGDs = new ArrayList<double[]>();
			for (int i = 0; i < resDirs.length; i++) {
				IGDs.add(new double[30]);
			}
			
			for (int i = 0; i < resDirs.length; i++) {
				List<AlgorithmResult> currARList = ARLists.get(i);
				double[] currIGDs = IGDs.get(i);
				
				for (int j = 0; j < 30; j++) {
					double currIGD = Measures.IGD(currARList.get(j).getSolutions(), entirePF);
					currIGDs[j] = currIGD;
				}
				
				SummaryStatistics IGDStats = new SummaryStatistics();
				for (int j = 0; j < 30; j++) {
					IGDStats.addValue(currIGDs[j]);
				}
				
				double currIGDMean = IGDStats.getMean();
				double currIGDStd = IGDStats.getStandardDeviation();
				double currIGDBest = Measures.IGD(PFs.get(i).getSolutions(), entirePF);
				
				IGDMeans.get(i).add(currIGDMean);
				IGDStds.get(i).add(currIGDStd);
				IGDBests.get(i).add(currIGDBest);
			}
			
			

//			boolean currIGDh12 = TestUtils.tTest(IGDs1, IGDs2, 0.05);
//			boolean currIGDh13 = TestUtils.tTest(IGDs1, IGDs3, 0.05);
//			boolean currIGDh23 = TestUtils.tTest(IGDs2, IGDs3, 0.05);

			
//			IGDh12.add(currIGDh12);
//			IGDh13.add(currIGDh13);
//			IGDh23.add(currIGDh23);
			
			
			/*** Calculate the HV measures ***/
			Solution ref = new Solution(2);
			ref.setScore(0, 0.0);
			ref.setScore(1, 0.0);
			
			List<double[]> HVs = new ArrayList<double[]>();
			for (int i = 0; i < resDirs.length; i++) {
				HVs.add(new double[30]);
			}
			
			for (int i = 0; i < resDirs.length; i++) {
				List<AlgorithmResult> currARList = ARLists.get(i);
				double[] currHVs = HVs.get(i);
				
				for (int j = 0; j < 30; j++) {
					double currHV = Measures.hypervolume2D(currARList.get(j).getSolutions(), ref);
					currHVs[j] = currHV;
				}
				
				SummaryStatistics HVStats = new SummaryStatistics();
				for (int j = 0; j < 30; j++) {
					HVStats.addValue(currHVs[j]);
				}
				
				double currHVMean = HVStats.getMean();
				double currHVStd = HVStats.getStandardDeviation();
				double currHVBest = Measures.hypervolume2D(PFs.get(i).getSolutions(), ref);
				
				HVMeans.get(i).add(currHVMean);
				HVStds.get(i).add(currHVStd);
				HVBests.get(i).add(currHVBest);
			}
			
//			double [] HVs1 = new double [30];
//			double [] HVs2 = new double [30];
//			double [] HVs3 = new double [30];
//			
//			Solution ref = new Solution(2);
//			ref.setScore(0, 0.0);
//			ref.setScore(1, 0.0);
//			for (int i = 0; i < 30; i++) {
//				System.out.println("i = " + i);
//				double HV1 = Measures.hypervolume2D(ARList1.get(i).getSolutions(), ref);
//				double HV2 = Measures.hypervolume2D(ARList2.get(i).getSolutions(), ref);
//				double HV3 = Measures.hypervolume2D(ARList3.get(i).getSolutions(), ref);
//				
//				HVs1[i] = HV1;				
//				HVs2[i] = HV2;
//				HVs3[i] = HV3;
//			}
//			
//			SummaryStatistics HV1Stats = new SummaryStatistics();			
//			SummaryStatistics HV2Stats = new SummaryStatistics();
//			SummaryStatistics HV3Stats = new SummaryStatistics();
//			
//			for (int i = 0; i < 30; i++) {
//				HV1Stats.addValue(HVs1[i]);
//				HV2Stats.addValue(HVs2[i]);
//				HV3Stats.addValue(HVs3[i]);
//			}
//			
//			
//			double currHV1Mean = HV1Stats.getMean();
//			double currHV1Std = HV1Stats.getStandardDeviation();
//			double currHV1Best = Measures.hypervolume2D(PF1.getSolutions(), ref);
//			
//			double currHV2Mean = HV2Stats.getMean();
//			double currHV2Std = HV2Stats.getStandardDeviation();
//			double currHV2Best = Measures.hypervolume2D(PF2.getSolutions(), ref);
//			
//			double currHV3Mean = HV3Stats.getMean();
//			double currHV3Std = HV3Stats.getStandardDeviation();
//			double currHV3Best = Measures.hypervolume2D(PF3.getSolutions(), ref);
//
//			boolean currHVh12 = TestUtils.tTest(HVs1, HVs2, 0.05);
//			boolean currHVh13 = TestUtils.tTest(HVs1, HVs3, 0.05);
//			boolean currHVh23 = TestUtils.tTest(HVs2, HVs3, 0.05);
//			
//			HVMean31.add(currHV1Mean);
//			HVStd31.add(currHV1Std);
//			HVBest1.add(currHV1Best);
//			
//			HVMean2.add(currHV2Mean);
//			HVStd2.add(currHV2Std);
//			HVBest2.add(currHV2Best);
//			
//			HVMean3.add(currHV3Mean);
//			HVStd3.add(currHV3Std);
//			HVBest3.add(currHV3Best);
//			
//			HVh12.add(currHVh12);
//			HVh13.add(currHVh13);
//			HVh23.add(currHVh23);
			
			
			/*** compute the mean time ***/
			for (int i = 0; i < resDirs.length; i++) {
				double currTime = Measures.meanTime(ARLists.get(i));
				timeMeans.get(i).add(currTime);
			}
		}
		
		/***** Latex Table ***/
		String[] rowNames = {
				"$\\rho = 0.005$",
				"$\\rho = 0.01$",
				"$\\rho = 0.02$",
		};
		
		// IGD
		for (int i = 0; i < resDirs.length/3; i++) {
			System.out.format("%s ", rowNames[i]);
			for (int j = 3*i; j < 3*i+3; j++) {
				System.out.format("& %.2f(%.2f) ", Statistics.doubleMean(IGDMeans.get(j)), 
						Statistics.doubleMean(IGDStds.get(j)));
			}
			System.out.format("\\\\\n");
		}
		
		System.out.println("");
		
		// hypervolume
		for (int i = 0; i < resDirs.length/3; i++) {
			System.out.format("%s ", rowNames[i]);
			for (int j = 3*i; j < 3*i+3; j++) {
				System.out.format("& %.1f(%.1f) ", Statistics.doubleMean(HVMeans.get(j)), 
						Statistics.doubleMean(HVStds.get(j)));
			}
			System.out.format("\\\\\n");
		}
		
		System.out.println("");
		
		// mean time
		for (int i = 0; i < resDirs.length/3; i++) {
			System.out.format("%s ", rowNames[i]);
			for (int j = 3*i; j < 3*i+3; j++) {
				System.out.format("& %.1f ", Statistics.doubleMean(timeMeans.get(j)) / 1000);
			}
			System.out.format("\\\\\n");
		}
		
//		// IGD
//		for (int i = 0; i < input.getInstances().size(); i++) {
//			int bestIdx = 0;
//			double bestIGD = Double.POSITIVE_INFINITY;
//			
//			if (IGDMean31.get(i) < bestIGD) {
//				bestIGD = IGDMean31.get(i);
//				bestIdx = 1;
//			}
//			
//			if (IGDMean2.get(i) < bestIGD) {
//				bestIGD = IGDMean2.get(i);
//				bestIdx = 2;
//			}
//			
//			if (IGDMean3.get(i) < bestIGD) {
//				bestIGD = IGDMean3.get(i);
//				bestIdx = 3;
//			}
//			
//			if (bestIdx == 1) {
//				if (IGDh12.get(i) == true && IGDh13.get(i) == true) {
//					System.out.format("{%s} & {\\bf %.2f(%.2f)} & {%.2f(%.2f)} & {%.2f(%.2f)} \\\\\n", 
//							input.instance(i).name, IGDMean31.get(i), IGDStd31.get(i),
//							IGDMean2.get(i), IGDStd2.get(i), IGDMean3.get(i), IGDStd3.get(i));
//				}
//				else {
//					System.out.format("{%s} & {%.2f(%.2f)} & {%.2f(%.2f)} & {%.2f(%.2f)} \\\\\n", 
//							input.instance(i).name, IGDMean31.get(i), IGDStd31.get(i),
//							IGDMean2.get(i), IGDStd2.get(i), IGDMean3.get(i), IGDStd3.get(i));
//				}
//			}
//			else if (bestIdx == 2) {
//				if (IGDh12.get(i) == true && IGDh23.get(i) == true) {
//					System.out.format("{%s} & {%.2f(%.2f)} & {\\bf %.2f(%.2f)} & {%.2f(%.2f)} \\\\\n", 
//							input.instance(i).name, IGDMean31.get(i), IGDStd31.get(i),
//							IGDMean2.get(i), IGDStd2.get(i), IGDMean3.get(i), IGDStd3.get(i));
//				}
//				else {
//					System.out.format("{%s} & {%.2f(%.2f)} & {%.2f(%.2f)} & {%.2f(%.2f)} \\\\\n", 
//							input.instance(i).name, IGDMean31.get(i), IGDStd31.get(i),
//							IGDMean2.get(i), IGDStd2.get(i), IGDMean3.get(i), IGDStd3.get(i));
//				}
//			}
//			else {
//				if (IGDh13.get(i) == true && IGDh23.get(i) == true) {
//					System.out.format("{%s} & {%.2f(%.2f)} & {%.2f(%.2f)} & {\\bf %.2f(%.2f)} \\\\\n", 
//							input.instance(i).name, IGDMean31.get(i), IGDStd31.get(i),
//							IGDMean2.get(i), IGDStd2.get(i), IGDMean3.get(i), IGDStd3.get(i));
//				}
//				else {
//					System.out.format("{%s} & {%.2f(%.2f)} & {%.2f(%.2f)} & {%.2f(%.2f)} \\\\\n", 
//							input.instance(i).name, IGDMean31.get(i), IGDStd31.get(i),
//							IGDMean2.get(i), IGDStd2.get(i), IGDMean3.get(i), IGDStd3.get(i));
//				}
//			}
//		}
//		System.out.format("Overall & {%.2f(%.2f)} & {%.2f(%.2f)} & {%.2f(%.2f)} \\\\\n",
//				Utilities.doubleMean(IGDMean31), Utilities.doubleMean(IGDStd31),
//				Utilities.doubleMean(IGDMean2), Utilities.doubleMean(IGDStd2),
//				Utilities.doubleMean(IGDMean3), Utilities.doubleMean(IGDStd3));
//		
//		System.out.println("");
//		
//		// hypervolume
//		for (int i = 0; i < input.getInstances().size(); i++) {
//			int bestIdx = 0;
//			double bestHV = Double.NEGATIVE_INFINITY;
//			
//			if (HVMean31.get(i) > bestHV) {
//				bestHV = HVMean31.get(i);
//				bestIdx = 1;
//			}
//			
//			if (HVMean2.get(i) > bestHV) {
//				bestHV = HVMean2.get(i);
//				bestIdx = 2;
//			}
//			
//			if (HVMean3.get(i) > bestHV) {
//				bestHV = HVMean3.get(i);
//				bestIdx = 3;
//			}
//			
//			if (bestIdx == 1) {
//				if (HVh12.get(i) == true && HVh13.get(i) == true) {
//					System.out.format("{%s} & {\\bf %.1f(%.1f)} & {%.1f(%.1f)} & {%.1f(%.1f)} \\\\\n", 
//							input.instance(i).name, HVMean31.get(i), HVStd31.get(i),
//							HVMean2.get(i), HVStd2.get(i), HVMean3.get(i), HVStd3.get(i));
//				}
//				else {
//					System.out.format("{%s} & {%.1f(%.1f)} & {%.1f(%.1f)} & {%.1f(%.1f)} \\\\\n", 
//							input.instance(i).name, HVMean31.get(i), HVStd31.get(i),
//							HVMean2.get(i), HVStd2.get(i), HVMean3.get(i), HVStd3.get(i));
//				}
//			}
//			else if (bestIdx == 2) {
//				if (HVh12.get(i) == true && HVh23.get(i) == true) {
//					System.out.format("{%s} & {%.1f(%.1f)} & {\\bf %.1f(%.1f)} & {%.1f(%.1f)} \\\\\n", 
//							input.instance(i).name, HVMean31.get(i), HVStd31.get(i),
//							HVMean2.get(i), HVStd2.get(i), HVMean3.get(i), HVStd3.get(i));
//				}
//				else {
//					System.out.format("{%s} & {%.1f(%.1f)} & {%.1f(%.1f)} & {%.1f(%.1f)} \\\\\n", 
//							input.instance(i).name, HVMean31.get(i), HVStd31.get(i),
//							HVMean2.get(i), HVStd2.get(i), HVMean3.get(i), HVStd3.get(i));
//				}
//			}
//			else {
//				if (HVh13.get(i) == true && HVh23.get(i) == true) {
//					System.out.format("{%s} & {%.1f(%.1f)} & {%.1f(%.1f)} & {\\bf %.1f(%.1f)} \\\\\n", 
//							input.instance(i).name, HVMean31.get(i), HVStd31.get(i),
//							HVMean2.get(i), HVStd2.get(i), HVMean3.get(i), HVStd3.get(i));
//				}
//				else {
//					System.out.format("{%s} & {%.1f(%.1f)} & {%.1f(%.1f)} & {%.1f(%.1f)} \\\\\n", 
//							input.instance(i).name, HVMean31.get(i), HVStd31.get(i),
//							HVMean2.get(i), HVStd2.get(i), HVMean3.get(i), HVStd3.get(i));
//				}
//			}
//		}
//		
//		System.out.format("Overall & {%.1f(%.1f)} & {%.1f(%.1f)} & {%.1f(%.1f)} \\\\\n",
//				Utilities.doubleMean(HVMean31), Utilities.doubleMean(HVStd31),
//				Utilities.doubleMean(HVMean2), Utilities.doubleMean(HVStd2),
//				Utilities.doubleMean(HVMean3), Utilities.doubleMean(HVStd3));
//		
//		
//		System.out.println("");
//		
//		// mean time
//		for (int i = 0; i < input.getInstances().size(); i++) {
//			System.out.format("{%s} & {%.1f} & {%.1f)} & {%.1f)} \\\\\n", 
//					input.instance(i).name, timeMean1.get(i), timeMean2.get(i), timeMean3.get(i));
//		}
	}
}

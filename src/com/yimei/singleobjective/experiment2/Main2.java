package com.yimei.singleobjective.experiment2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import com.yimei.multiobjective.algorithms.ACO;
import com.yimei.multiobjective.algorithms.Algorithm;
import com.yimei.multiobjective.algorithms.FalcoMOEA;
import com.yimei.multiobjective.algorithms.MemeticAlgorithm;
import com.yimei.multiobjective.experiment.Instance;
import com.yimei.multiobjective.experiment.Run;
import com.yimei.multiobjective.experiment.RunOutput;
import com.yimei.multiobjective.parameters.Parameters;
import com.yimei.multiobjective.problem.Dataset;
import com.yimei.multiobjective.problem.Problem;

import yimei.util.Utilities;

public class Main2 {

	public static void main(String[] args) throws IOException {
		
//		System.out.println("seed = " + Parameters.seed);
//		Random rnd = new Random(Parameters.seed);
		
		// get the input
		SchildeInput input = SchildeInput.entireInput();
		
		// output
		String resDir = "data/SchildeResults/";
		File dirFile = new File(resDir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		
		// solve the instances
		for (Instance instance : input.getInstances()) {
//		for (int n = 60; n < 61; n++) {
//			Instance instance = input.getInstances().get(n);
			long seed = Integer.valueOf(args[0]);
//			long seed = 159720000;
	        Random rnd = new Random(seed);
	        
//	        System.out.println(seed);
	        
//	        instance.printMe();

	        // get the tmax from the name of the instance
	        int index = instance.name.indexOf("_t");
	        String tmaxStr = instance.name.substring(index+2);
	        double tmax = Double.parseDouble(tmaxStr);
	        
//	        System.out.println(tmax);

			Problem problem = new Problem(instance.speedFile, instance.categoryFile,
					instance.file, Dataset.SCHILDE);
			problem.setTimeLimit(tmax);
			
//			problem.printMe();
			
			// decide the algorithm
			Algorithm algorithm = new FalcoMOEA(problem, rnd, 30, 3750);
//			Algorithm algorithm = new ACO(problem, rnd);
//			Algorithm algorithm = new MemeticAlgorithm(problem, rnd, 30, 10000);
			
			// run
	        long startCpuTimeNano = Utilities.getCpuTime();
	        
			Run run = new Run(instance, algorithm, Integer.valueOf(args[0]));
			RunOutput rop = run.execute();
			
			double estimatedCpuTime = (Utilities.getCpuTime() - startCpuTimeNano) / (1000 * 1000);
			
			// append seed and time
			File resInstanceDir = new File(resDir + instance.name);
			if (!resInstanceDir.exists())
				resInstanceDir.mkdirs();
			File runFile = new File(resInstanceDir + "/" + instance.name + ".res" + rop.runID());

			System.out.println(runFile.toString());
			
			// write the file
			rop.printToFile(runFile);
			
			FileWriter fileWriter = new FileWriter(runFile, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.newLine();
			bufferedWriter.write(Long.toString(seed));
			bufferedWriter.newLine();
			bufferedWriter.write(Double.toString(estimatedCpuTime));
			
			bufferedWriter.close();
			
//			System.exit(0);
		}
	}
}

package com.yimei.singleobjective.experiment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import com.yimei.multiobjective.algorithms.ACO;
import com.yimei.multiobjective.algorithms.Algorithm;
import com.yimei.multiobjective.algorithms.FalcoMOEA;
import com.yimei.multiobjective.algorithms.MemeticAlgorithm;
import com.yimei.multiobjective.parameters.Parameters;
import com.yimei.multiobjective.problem.Dataset;
import com.yimei.multiobjective.problem.Problem;

import yimei.util.Utilities;

public class Main {

	public static void main(String[] args) throws IOException {

//		System.out.println("seed = " + Parameters.seed);
//		Random rnd = new Random(Parameters.seed);

		// get the input
		Input input = Input.entireInput();

		// output
		String resDir = "data/Results/";
		File dirFile = new File(resDir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		// solve the instances
		for (Instance instance : input.getInstances()) {
//			long seed = Utilities.getCpuTime();
			long seed = Integer.valueOf(args[0]);
	        Random rnd = new Random(seed);

//	        instance.printMe();

			Problem problem = new Problem(instance.speedFile, instance.categoryFile, instance.file, Dataset.VERBEECK);

			// decide the algorithm
			Algorithm algorithm = new FalcoMOEA(problem, rnd, 30, 3750);
//			Algorithm algorithm = new ACO(problem, rnd);
//			Algorithm algorithm = new MemeticAlgorithm(problem, rnd, 30, 3750);

			// run
	        long startCpuTimeNano = Utilities.getCpuTime();

			Run run = new Run(instance, algorithm, Integer.valueOf(args[0]));
			RunOutput rop = run.execute();

			double estimatedCpuTime = (Utilities.getCpuTime() - startCpuTimeNano) / (1000 * 1000);


			// append seed and time
			File runFile = new File(resDir + instance.name + ".res" + rop.runID());

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
		}
	}
}

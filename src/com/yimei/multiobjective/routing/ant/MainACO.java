package com.yimei.multiobjective.routing.ant;

import java.io.File;
import java.util.Random;

import com.yimei.multiobjective.algorithms.ACO;
import com.yimei.multiobjective.parameters.Parameters;
import com.yimei.multiobjective.problem.Dataset;
import com.yimei.multiobjective.problem.Problem;
import com.yimei.multiobjective.routing.core.ParetoFront;

public class MainACO {
	
	public static void main(String [] args) {
		// read problem
		String ff = "p2.1.h.txt";
		String td = "data/MOTDOP/dataset 2/OP_instances/";
		String tf = ff;
		
		File speedFile = new File("data/TDOP/speedmatrix.txt");
		File categoryFile = new File("data/TDOP/dataset 2/arc_cat_2.txt");
		File toFile = new File(td + tf);
		
		Problem problem = new Problem(speedFile, categoryFile, toFile, Dataset.VERBEECK);
		
		// random seed
//		long seed = Utilities.getCpuTime();
		
		System.out.println("seed = " + Parameters.seed);
		Random rnd = new Random(Parameters.seed);
		
		// solve the problem
		ACO aco = new ACO(problem, rnd);
		
		ParetoFront pf = aco.execute();

	}
}

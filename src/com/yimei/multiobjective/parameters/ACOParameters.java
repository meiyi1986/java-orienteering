package com.yimei.multiobjective.parameters;

/***
 * The parameter list of the ACO
 * @author Administrator
 *
 */

public class ACOParameters {

	public static double initPheromone = 1;
	public static int maxIters = 50;
	
	public static int numAnts = 75; // number of ants
	public static double alpha = 4; // the weight on the pheromone
	public static double beta = 1; // the weight on the heuristic information
	public static double pho = 0.005; // the global evaporation rate
	public static double phi = 0.01; // the local evaporation rate
	
	public static double nbeta = 0.07;
	
	public static double q0 = 0.25;
}

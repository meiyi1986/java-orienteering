package com.yimei.singleobjective.benchmarkcreator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/***
 * This is the benchmark creator which transforms a Time-Dependent Orienteering Problem into
 * a corresponding Multi-Objective Time-Dependent Orienteering Problem by adding one more benefit.
 * The additional benefit vector is generated by randomly permutating the original benefit vector.
 * @author Yi Mei
 *
 */

public class AddObjective extends BenchmarkCreator {

	public AddObjective(File fromDir, File fromFile, File toDir, File toFile, Random rnd) {
		super(fromDir, fromFile, toDir, toFile, rnd);
	}

	@Override
	public void execute() {
		/** define the original and additional benefit lists **/
		List<Double> benefits1 = new ArrayList<Double>();
		List<Double> benefits2 = new ArrayList<Double>();
		
		/** read the file **/
		try (BufferedReader br = new BufferedReader(new FileReader(fromFile)))
		{
 
			String sCurrentLine;
			
			/* skip the headers */
			int numHeaders = 3;
			for (int i = 0; i < numHeaders; i++) {
				br.readLine();
			}
 
			/* read benefits1 */
			while ((sCurrentLine = br.readLine()) != null) {
				
				if (sCurrentLine.isEmpty())
					break;
				
//				System.out.println(sCurrentLine);
				String [] lineElements = sCurrentLine.split("\\s+");
				benefits1.add(new Double(lineElements[2]));
			}
			
			/* randomly permutate benefits1 except the first and last elements to obtain benefits2 */
			List<Double> subBenefits2 = new ArrayList<Double>(benefits1);
			subBenefits2 = subBenefits2.subList(1, subBenefits2.size()-1);
			
			Collections.shuffle(subBenefits2, rnd);
			
			
			benefits2.add(new Double(benefits1.get(0)));
			benefits2.addAll(subBenefits2);
			benefits2.add(new Double(benefits1.get(benefits1.size()-1)));
			
//			System.out.println(benefits1);
//			System.out.println(benefits2);
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/** create the new directory **/
		if (!toDir.exists()) {
			toDir.mkdirs();
		}
		
		/** write the new file **/
		try (BufferedWriter output = new BufferedWriter(new FileWriter(toFile)))
		{
			try (BufferedReader br = new BufferedReader(new FileReader(fromFile)))
			{
				String sCurrentLine;
				
				/* skip the headers */
				int numHeaders = 3;
				for (int i = 0; i < numHeaders; i++) {
					sCurrentLine = br.readLine();
					output.write(sCurrentLine);
					output.newLine();
				}
				
				int idx = 0;
				/* add benefits2 */
				while ((sCurrentLine = br.readLine()) != null) {
					
					if (sCurrentLine.isEmpty())
						break;
					
//					System.out.println(sCurrentLine);
					String addLine = sCurrentLine + "  " + benefits2.get(idx);
//					System.out.println(addLine);
					output.write(addLine);
					output.newLine();
					idx ++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	public static void main(String [] args) {
		
		String fd = "data/TDOP/dataset 1/OP_instances/";
		String ff = "p1.1.a.txt";
		String td = "data/MOTDOP/dataset 1/OP_instances/";
		String tf = ff;
		
		File fromDir = new File(fd);
		File fromFile = new File(fd + ff);
		File toDir = new File(td);
		File toFile = new File(td + tf);
		
		Random rnd = new Random(0);
		
		BenchmarkCreator bc = new AddObjective(fromDir, fromFile, toDir, toFile, rnd);
		bc.execute();
	}
}

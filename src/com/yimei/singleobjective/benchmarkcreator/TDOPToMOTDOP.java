package com.yimei.singleobjective.benchmarkcreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

public class TDOPToMOTDOP {
	
	public static void main (String [] args) throws IOException {
		
		String sourceDir = "data/TDOP/"; // source dir - TDOP
		String targetDir = "data/MOTDOP/"; // target dir - MOTDOP
		String catDir = "dataset "; // to be appended by the index of category
		
		File dirFile; // used to create dirs

		String [] nameSet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"}; // used to create the file name
		
		int [] numInstances = {9, 9, 9, 10, 3, 9, 10}; // the number of instances in each category
		int numCategories = numInstances.length;
		
		/** create the new directory **/
		dirFile = new File(targetDir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		
		// copy the speed matrix
		Path speedMatrix1 = Paths.get(sourceDir + "speedMatrix.txt");
		Path speedMatrix2 = Paths.get(targetDir + "speedMatrix.txt");
		Files.copy(speedMatrix1, speedMatrix2, StandardCopyOption.REPLACE_EXISTING);
		
		// copy all the category matrices
		for (int i = 1; i < numCategories + 1; i++) {
			String thisCatDir1 = sourceDir + catDir + i + "/";
			String thisCatDir2 = targetDir + catDir + i + "/";
			dirFile = new File(thisCatDir2);
			
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			
			Path catMatrix1 = Paths.get(thisCatDir1 + "arc_cat_" + i + ".txt");
			Path catMatrix2 = Paths.get(thisCatDir2 + "arc_cat_" + i + ".txt");
			
			Files.copy(catMatrix1, catMatrix2, StandardCopyOption.REPLACE_EXISTING);
		}
		
		// create all the instance files
		for (int i = 1; i < numCategories + 1; i++) {
			String instanceDirStr1 = sourceDir + catDir + i + "/OP_instances/";
			String instanceDirStr2 = targetDir + catDir + i + "/OP_instances/";
			
			for (int j = 1; j < numInstances[i-1] + 1; j++) {
				String instStr1 = instanceDirStr1 + "p" + i + ".1." + nameSet[j-1] + ".txt";
				String instStr2 = instanceDirStr2 + "p" + i + ".1." + nameSet[j-1] + ".txt";
				
				System.out.println(instStr1 + ", " + instStr2);
				
				File instanceDir1 = new File(instanceDirStr1);
				File instanceDir2 = new File(instanceDirStr2);
				File instance1 = new File(instStr1);
				File instance2 = new File(instStr2);
				
				Random rnd = new Random(0);
				BenchmarkCreator bc = new AddObjective(instanceDir1, instance1, instanceDir2, instance2, rnd);
				bc.execute();
			}
		}
	}

}

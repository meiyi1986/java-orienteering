package com.yimei.singleobjective.experiment;

import java.io.File;

public class Instance {
	
	public String name;
	public File file;
	public File speedFile;
	public File categoryFile;
	
	public File solutionFile;
	public int numPOIs;
	public double tmax;

	public Instance(String name, File file, File speedFile, File catFile) {
		this.name = name;
		this.file = file;
		this.speedFile = speedFile;
		this.categoryFile = catFile;
	}
	
	public void printMe() {
		System.out.println(name);
		System.out.println(file);
		System.out.println(speedFile);
		System.out.println(categoryFile);
	}
}

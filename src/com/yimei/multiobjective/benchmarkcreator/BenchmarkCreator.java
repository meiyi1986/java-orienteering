package com.yimei.multiobjective.benchmarkcreator;

import java.io.File;
import java.util.Random;

public abstract class BenchmarkCreator {

	protected Random rnd;
	protected File fromDir;
	protected File fromFile;
	protected File toDir;
	protected File toFile;
	
	public BenchmarkCreator(File fromDir, File fromFile, File toDir, File toFile, Random rnd) {
		this.fromDir = fromDir;
		this.fromFile = fromFile;
		this.toDir = toDir;
		this.toFile = toFile;
		this.rnd = rnd;
	}
	
	abstract public void execute();
}

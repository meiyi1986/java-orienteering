# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* This is a package for algorithms for time-dependent orienteering problems. Written by Yi Mei.
* Version 1.0.0

### How do I get set up? ###

1. Download the source files in the `src/` folder and the dependencies in the `lib/` folder.
2. Create a Java Project using `src/` and `lib/`.
3. The benchmark instances are located in the `data/` folder.

### Project structure ###

The main projects are located in `/src/com/yimei/singleobjective/` and `/src/com/yimei/multiobjective/`, corresponding to single-objective and multi-objective time-dependent orienteering problems respectively. They contain the following packages:

* `algorithms/` contains the algorithms.
* `benchmarkgenerator/` contains the benchmark generator that generates time-dependent instances from existing time-independent ones.
* `core/` contains the core classes for representing the elements in the orienteering problem.
* `experiment/` and `experiment2/` are the main entries of the program, containing the main class `Run` for running experiments on different datasets.
* `parameters/` contains the parameter classes for the algorithms.
* `problem/` contains the classes for representing the problem.
* `routing/` contains the core classes for the routing algorithms.

### Who do I talk to? ###

* Email: yi.mei@ecs.vuw.ac.nz

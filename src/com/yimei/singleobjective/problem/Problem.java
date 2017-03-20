package com.yimei.singleobjective.problem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mapdb.Fun.Tuple2;

import com.yimei.multiobjective.core.Arc;
import com.yimei.multiobjective.core.Period;
import com.yimei.multiobjective.core.PeriodSpeed;
import com.yimei.multiobjective.core.SpeedTable;
import com.yimei.multiobjective.core.Vertex;

/***
 * The model of the multi-objective time-dependent orienteering problem
 * @author yi
 *
 */

public class Problem {
	
	private Dataset data;
	private boolean distConstraint = false;
	
	public final double startTime = 7.0; // 7:00am
	public final double finishTime = 24.0; // 9:00pm
	
	private int numVertices; // number of vertices
	private int numPaths; // number of paths
	private double tmax; // the travel time limit (in hours)
	
	private int numScores; // number of scores
	
	private boolean distMtx = false; // does the file include the distance matrix?
	
	/* graph information */
	private List<Vertex> vertices;
	private List<Arc> arcs;
	private Map<Tuple2<Vertex, Vertex>, Arc> graph;
	
	private Vertex startVertex;
	private Vertex endVertex;
	private List<Vertex> candiVertices;
	
	/* time-dependent information */
	List<Period> periods;
	List<SpeedTable> speedTables; // the speed tables of all the categories
	
	SpeedTable allOneSpeedTable; // the speed table with all speeds as one. This is used when distance equals the time.
	
	public Problem() {
		
	}
	
	public Problem(File speedFile, File categoryFile, File file, Dataset data) {
		this.data = data;
		
		vertices = new ArrayList<Vertex>();
		arcs = new ArrayList<Arc>();
		graph = new HashMap<Tuple2<Vertex, Vertex>, Arc>();
		
		if (data == Dataset.VERBEECK) {
			readSpeedFile(speedFile);
			readFile(file);
			readCategoryFile(categoryFile);
			
			numScores = vertices.get(0).numScores();
			startVertex = vertices.get(0);
			endVertex = vertices.get(vertices.size()-1);
			candiVertices = vertices.subList(1, vertices.size()-1);
		}
		else if (data == Dataset.SCHILDE) {
			readSpeedFile(speedFile);
			readSchildeFile(file);
			readCategoryFile(categoryFile);
		}
		
//		startVertex.printMe();
//		endVertex.printMe();
//		for(Vertex v : candiVertices) {
//			v.printMe();
//		}
		
		
//		for (Arc arc : arcs) {
//			arc.printMe();
//		}
		
//		for (Map.Entry<Tuple2<Vertex, Vertex>, Arc> entry : graph.entrySet()) {
//			System.out.print("V1 = " + entry.getKey().a.id()
//					+ ", V2 = " + entry.getKey().b.id()
//					+ ": ");
//			entry.getValue().printMe();
//		}
		
	}
	
	public void readSpeedFile(File file) {
		/** predefine the periods **/
		periods = Period.universalDefine();
		
		speedTables = new ArrayList<SpeedTable>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String sCurrentLine;
			String [] lineElements;
			
			while ((sCurrentLine = br.readLine()) != null) {
//				System.out.println(sCurrentLine);
				lineElements = sCurrentLine.split("\\s+");
				SpeedTable speedTable = new SpeedTable();
				for (int i = 0; i < lineElements.length; i++) {
					double fromTime = periods.get(i).fromTime();
					double toTime = periods.get(i).toTime();
					double speed = Double.parseDouble(lineElements[i]);
					PeriodSpeed periodSpeed = new PeriodSpeed(fromTime, toTime, speed);
					speedTable.add(periodSpeed);
				}
				
				speedTables.add(speedTable);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// create the all one speed table
		allOneSpeedTable = new SpeedTable();
		for (int i = 0; i < periods.size(); i++) {
			double fromTime = periods.get(i).fromTime();
			double toTime = periods.get(i).toTime();
			allOneSpeedTable.add(new PeriodSpeed(fromTime, toTime, 1));
		}
		
//		for (int i = 0; i < speedTables.size(); i++) {
//			System.out.println("For category " + i);
//			speedTables.get(i).printMe();
//		}
	}
	
	
	public void readCategoryFile(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String sCurrentLine;
			String [] lineElements;
			
			int row = 0;
			while ((sCurrentLine = br.readLine()) != null) {
//				System.out.println(sCurrentLine);
				lineElements = sCurrentLine.split("\\s+");
				for (int column = 0; column < lineElements.length; column++) {					
					int category = Integer.parseInt(lineElements[column]);
					Vertex v1 = vertices.get(row);
					Vertex v2 = vertices.get(column);
					
					Arc arc = getArc(v1, v2);
					if (!distMtx) {
						double distance = v1.distanceTo(v2);
						if (data == Dataset.VERBEECK) {
							distance = distance / 5.0;
						}
						else {
							distance = distance / 10.0;
						}
						
						arc.setLength(distance);
					}
					arc.setCategory(category);
					if (distConstraint) {
						arc.setSpeedTable(allOneSpeedTable);
					}
					else {
						arc.setSpeedTable(speedTables.get(category));
					}					
				}
				
				row ++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void readFile(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{

			String sCurrentLine;
			String [] lineElements;
			
			// read the headlines
			sCurrentLine = br.readLine();
			lineElements = sCurrentLine.split("\\s+");
			numVertices = Integer.parseInt(lineElements[1]);
			sCurrentLine = br.readLine();
			lineElements = sCurrentLine.split("\\s+");
			numPaths = Integer.parseInt(lineElements[1]);
			sCurrentLine = br.readLine();
			lineElements = sCurrentLine.split("\\s+");
			tmax = Double.parseDouble(lineElements[1]);
			
//			System.out.println(numVertices + ", " + numPaths + ", " + tmax);

			/* read benefits1 */
			int index = 0;
			while ((sCurrentLine = br.readLine()) != null) {
//					System.out.println(sCurrentLine);
				lineElements = sCurrentLine.split("\\s+");
				double x = Double.parseDouble(lineElements[0]);
				double y = Double.parseDouble(lineElements[1]);
				List<Double> scores = new ArrayList<Double>();
				for (int i = 2; i < lineElements.length; i++) {
					double score = Double.parseDouble(lineElements[i]);
					scores.add(new Double(score));
				}
				Vertex vertex = new Vertex(index, x, y, scores);
//				vertex.printMe();
				vertices.add(vertex);
				index ++;
			}
			
			// create the arcs and the graph
			for (int i = 0; i < vertices.size(); i++) {
				for (int j = 0; j < vertices.size(); j++) {
					Arc arc = new Arc(vertices.get(i), vertices.get(j));
					arcs.add(arc);
					graph.put(new Tuple2<Vertex, Vertex>(vertices.get(i), vertices.get(j)), arc);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read the Schilde's file.
	 * 
	 * @param file the file.
	 */
	public void readSchildeFile(File file) {
		
		vertices = new ArrayList<Vertex>();
		
		int beginIdx = -1, endIdx = -1;
		int numConstraints = -1;

		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{

			String sCurrentLine;
			String[] commaSep;
			String[] spaceSep;
			
			// the number of vertices, scores, constraints, and start and ending points
			while ((sCurrentLine = br.readLine()) != null) {
				commaSep = sCurrentLine.split(",");
				
				if (commaSep[0].equals("N")) {
					spaceSep = commaSep[1].split("\\s+");
					for (int i = 0; i < spaceSep.length; i++) {
						if (spaceSep[i].length() > 0) {
							numVertices = Integer.parseInt(spaceSep[i]);
							break;
						}
					}
					
					sCurrentLine = br.readLine();
					commaSep = sCurrentLine.split(",");
					spaceSep = commaSep[1].split("\\s+");
					for (int i = 0; i < spaceSep.length; i++) {
						if (spaceSep[i].length() > 0) {
							numScores = Integer.parseInt(spaceSep[i]);
							break;
						}
					}
					
					sCurrentLine = br.readLine();
					commaSep = sCurrentLine.split(",");
					spaceSep = commaSep[1].split("\\s+");
					for (int i = 0; i < spaceSep.length; i++) {
						if (spaceSep[i].length() > 0) {
							numConstraints = Integer.parseInt(spaceSep[i]);
							break;
						}
					}
					
					sCurrentLine = br.readLine();
					commaSep = sCurrentLine.split(",");
					spaceSep = commaSep[1].split("\\s+");
					for (int i = 0; i < spaceSep.length; i++) {
						if (spaceSep[i].length() > 0) {
							beginIdx = Integer.parseInt(spaceSep[i]);
							break;
						}
					}
					
					sCurrentLine = br.readLine();
					commaSep = sCurrentLine.split(",");
					spaceSep = commaSep[1].split("\\s+");
					for (int i = 0; i < spaceSep.length; i++) {
						if (spaceSep[i].length() > 0) {
							endIdx = Integer.parseInt(spaceSep[i]);
							break;
						}
					}
					
					sCurrentLine = br.readLine();
					commaSep = sCurrentLine.split(",");
					if (commaSep[0].equals("M")) {
						spaceSep = commaSep[1].split("\\s+");
						for (int i = 0; i < spaceSep.length; i++) {
							if (spaceSep[i].length() > 0) {
								int value = Integer.parseInt(spaceSep[i]);
								
								if (value == 1) {
									distMtx = true;
								}
								
								break;
							}
						}
					}
					
					break;
				}				
			}
			
//			System.out.println("distMtx = " + distMtx);
			
//			System.out.println(numVertices);

			/* read each vertex */
			
			while ((sCurrentLine = br.readLine()) != null) {
//					System.out.println(sCurrentLine);
				
				double x, y, p1, p2;
				if (sCurrentLine.equals("// Index,x-coord,y-coord,score1,score2,score3,...")) {
					for (int index = 0; index < numVertices; index++) {
						sCurrentLine = br.readLine();
						commaSep = sCurrentLine.split(",");
						spaceSep = commaSep[1].split("\\s+");
						x = Double.parseDouble(spaceSep[1]);
						spaceSep = commaSep[2].split("\\s+");
						y = Double.parseDouble(spaceSep[1]);
						spaceSep = commaSep[3].split("\\s+");
						p1 = Double.parseDouble(spaceSep[1]);
						spaceSep = commaSep[4].split("\\s+");
						p2 = Double.parseDouble(spaceSep[1]);
						
						List<Double> scores = new ArrayList<Double>();
						
						scores.add(new Double(p1));
						scores.add(new Double(p2));
						
						Vertex vertex = new Vertex(index, x, y, scores);
//						vertex.printMe();
						vertices.add(vertex);
						
//						System.out.println(index + ", " + x + ", " + y + ", " + p1 + ", " + p2);
					}
					
					break;
				}
			}
			
			startVertex = vertices.get(beginIdx);
			endVertex = vertices.get(endIdx);
			candiVertices = new ArrayList<Vertex>(vertices);
			candiVertices.remove(startVertex);
			candiVertices.remove(endVertex);
			
//			System.out.println("start vertex");
//			startVertex.printMe();
//			System.out.println("end vertex");
//			endVertex.printMe();
//			System.out.println("other vertices");
//			for (Vertex v : candiVertices) {
//				v.printMe();
//			}
			
			// create the arcs and the graph
			for (int i = 0; i < vertices.size(); i++) {
				for (int j = 0; j < vertices.size(); j++) {
					Arc arc = new Arc(vertices.get(i), vertices.get(j));
					
					if (j == i)
						arc.setLength(0);

					arcs.add(arc);
					graph.put(new Tuple2<Vertex, Vertex>(vertices.get(i), vertices.get(j)), arc);
				}
			}
			
			// read the distance matrix if there is any
			if (distMtx) {
				while ((sCurrentLine = br.readLine()) != null) {
//					System.out.println(sCurrentLine);
					
					if (sCurrentLine.equals("// D, Distance 0-0, Distance 0-1, Distance 0-2, ..., Distance 0-N")) {
						for (int i = 0; i < vertices.size(); i++) {
							sCurrentLine = br.readLine();
							commaSep = sCurrentLine.split(",");
							Vertex v1 = vertices.get(i);
							for (int j = 0; j < vertices.size(); j++) {
								Vertex v2 = vertices.get(j);
								Arc arc = getArc(v1, v2);
								double length = Double.parseDouble(commaSep[j+1]);
								length = length / 10;
								arc.setLength(length);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setTimeLimit(double limit) {
		tmax = limit;
	}
	
	public List<Vertex> vertices() {
		return vertices;
	}
	
	public List<Arc> arcs() {
		return arcs;
	}
	
	public Vertex startVertex() {
		return startVertex;
	}
	
	public Vertex endVertex() {
		return endVertex;
	}
	
	public List<Vertex> candiVertices() {
		return candiVertices;
	}
	
	
	public int numVertices() {
		return numVertices;
	}
	
	public double timeLimit() {
		return tmax;
	}
	
	public int numScores() {
		return numScores;
	}
	
	public Arc getArc(Vertex fromV, Vertex toV) {
		Tuple2<Vertex, Vertex> key = new Tuple2<Vertex, Vertex>(fromV, toV);
		return graph.get(key);
	}
	
	// calculate the travel time of the given arc at the given time
	public double timeToTravelArc(Arc arc, double time) {
		
		double remainingLength = arc.length();
		double spentTime = 0;
		
//		System.out.println("time = " + time);
		
		int periodIdx = arc.speedTable().periodOf(time);
		double periodSpeed = arc.speedTable().speedOfPeriod(periodIdx);
		double periodTravelTime = arc.speedTable().get(periodIdx).toTime() - time;
		double periodTravelLength = periodTravelTime * periodSpeed;
		
//		if (arc.fromVertex().id() == 1714) {
//			arc.printMe();
//			System.out.println("period = " + periodIdx + ", from time " + time + " to " + arc.speedTable().get(periodIdx).toTime());
//			System.out.println("travelled = " + periodTravelLength + ", remaining = " + remainingLength);
//		}
		
//		arc.printMe();
//		System.out.println("period id1 = " + periodIdx + ", from time " + time + " to " + arc.speedTable().get(periodIdx).toTime());
//		System.out.println("orig length = " + periodTravelLength + ", total = " + remainingLength);
		
		while (periodTravelLength < remainingLength) {
			spentTime += periodTravelTime;
			remainingLength -= periodTravelLength;
			
			periodIdx ++;
			
			if (periodIdx > periods.size() - 1) {
				periodSpeed = 0.0000001; // set to a very slow speed
				break;
			}
			
			periodSpeed = arc.speedTable().speedOfPeriod(periodIdx);
			periodTravelTime = arc.speedTable().get(periodIdx).toTime() - arc.speedTable().get(periodIdx).fromTime();
			periodTravelLength = periodTravelTime * periodSpeed;
			
//			if (arc.fromVertex().id() == 1714) {
//				arc.printMe();
//				System.out.println("period = " + periodIdx + " to " + arc.speedTable().get(periodIdx).toTime());
//				System.out.println("travelled = " + periodTravelLength + ", remaining = " + remainingLength);
//			}
		}
		
		spentTime += remainingLength / periodSpeed;
		
//		if (arc.fromVertex().id() == 1714) {
//			arc.printMe();
//			System.out.println(", time = " + time + "finish time = " + (time+spentTime));
//		}
		
//		System.out.println("period id2 = " + periodIdx + ", spent = " + spentTime);
		
		return spentTime;
	}
	
	// calculate the travel time from fromV to toV at the given time
	public double travelTime(Vertex fromV, Vertex toV, double time) {
		Arc arc = getArc(fromV, toV);
		return timeToTravelArc(arc, time);
	}
	
	// calculate the travel time of the given arc given the arrival time instead of departure time
	public double timeToTravelArcReverse(Arc arc, double time) {
		
		double remainingLength = arc.length();
		double spentTime = 0;
		
		int periodIdx = arc.speedTable().periodOf(time);
		double periodSpeed = arc.speedTable().speedOfPeriod(periodIdx);
		double periodTravelTime = time - arc.speedTable().get(periodIdx).fromTime();
		double periodTravelLength = periodTravelTime * periodSpeed;
		
//		if (arc.fromVertex().id() == 1714) {
//			arc.printMe();
//			System.out.println("period = " + periodIdx + ", from time " + arc.speedTable().get(periodIdx).fromTime() + " to " + time);
//			System.out.println("travelled = " + periodTravelLength + ", remaining = " + remainingLength);
//		}

		while (periodTravelLength < remainingLength) {
			spentTime += periodTravelTime;
			remainingLength -= periodTravelLength;
			
			periodIdx --;
			
			if (periodIdx < 0) {
				periodSpeed = 0.0000001; // set to a very slow speed
				break;
			}
			
			periodSpeed = arc.speedTable().speedOfPeriod(periodIdx);
			periodTravelTime = arc.speedTable().get(periodIdx).toTime() - arc.speedTable().get(periodIdx).fromTime();
			periodTravelLength = periodTravelTime * periodSpeed;
			
//			if (arc.fromVertex().id() == 1714) {
//				arc.printMe();
//				System.out.println("period = " + periodIdx + ", from time " + arc.speedTable().get(periodIdx).fromTime());
//				System.out.println("travelled = " + periodTravelLength + ", remaining = " + remainingLength + ", spent time = " + spentTime);
//			}
		}
		
		spentTime += remainingLength / periodSpeed;
		
//		if (arc.fromVertex().id() == 1714) {
//			System.out.println("remaining = " + remainingLength + ", speed = " + periodSpeed +
//					", spent time = " + spentTime);
//		}
		
//		System.out.println("period id2 = " + periodIdx + ", spent = " + spentTime);
		
		return spentTime;
	}
	
	// calculate the travel time from fromV to toV given the arrival time
	public double travelTimeReverse(Vertex fromV, Vertex toV, double time) {
		Arc arc = getArc(fromV, toV);
		return timeToTravelArcReverse(arc, time);
	}
	
	
	public void catInfo() {
		for (int i = 0; i < vertices.size(); i++) {
			for (int j = 0; j < vertices.size(); j++) {
				if (j == i) {
					System.out.print("4 ");
				}
				else {
					Arc arc = getArc(vertices.get(i), vertices.get(j));
					System.out.print(arc.category() + " ");
				}
			}
			System.out.println();
		}
	}
	
	public void printMe() {
		for (Vertex v : vertices) {
			v.printMe();
		}
		
		for (Arc arc : arcs) {
			arc.printMe();
		}
		
		System.out.print("start from ");
		startVertex.printMe();
		System.out.print("end at ");
		endVertex.printMe();
		System.out.println("tmax = " + tmax);
	}
	
	
//	public static void main(String [] args) {
//		String ff = "p1.1.a.txt";
//		String td = "data/MOTDOP/dataset 1/OP_instances/";
//		String tf = ff;
//		
//		File speedFile = new File("data/TDOP/speedmatrix.txt");
//		File categoryFile = new File("data/TDOP/dataset 1/arc_cat_1.txt");
//		File toFile = new File(td + tf);
//		
//		Problem problem = new Problem(speedFile, categoryFile, toFile);
//	}
}

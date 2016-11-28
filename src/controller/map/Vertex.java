package controller.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import controller.player.Building;
import controller.player.Settlement;

public class Vertex implements TableElement{
	private final String ID; 
	
	public Vertex(String id){
		ID = id;
	}
	
	public String getID() {
		return ID;
	}
	
	private Building town;
	
	public Map<String, Hex> hexes = new TreeMap<String, Hex>();
	
	ArrayList<Edge> edges = new ArrayList<Edge>();
	
	private ArrayList<Hex> neighbourHexes = new ArrayList<Hex>();

	private ArrayList<Vertex> neighbourVertices = new ArrayList<Vertex>(); 
	
	/**
	 * @return the Settlement contained by this Vertex
	 */
	public Building getBuilding() {
		return town;
	}
	
	/**
	 * @return the Settlement contained by this Vertex
	 */
	public void setBuilding(Building b) {
		town = b;
	}
	
	/**
	 * @return List of neighboring Vertexes
	 */
	public ArrayList<Vertex> getNeighbours() {
		return neighbourVertices;
	}
	
	public ArrayList<Hex> getNeighbourHexes() {
		return neighbourHexes;
	}
	
	public ArrayList<Edge> getNeighbourEdges() {
		return edges;
	}
	
	void generateNeighbourHexes(){
				for(Map.Entry<String, Hex> e: hexes.entrySet()){
					neighbourHexes.add(e.getValue());
				}
	}
	
	void generateNeighbourVertices(){
		Set<Hex> base = new HashSet<Hex>(neighbourHexes);
		for(Hex h : neighbourHexes){
			for(Vertex v: h.getNeighbouringVertices()){
				TreeSet<Hex> other = new TreeSet<Hex>(v.getNeighbourHexes());
				other.retainAll(base);
				if(other.size() == 2)
					if(!neighbourVertices.contains(v))
						neighbourVertices.add(v);
			}
		}
		
	}
	
	/**
	 * @param other
	 * @return true if two Vertexes are the same else false
	 */
	public boolean equals(Vertex other) {
		if (ID == other.getID()){
			return true;
		}
		return false;
	}

	@Override
	public boolean isBuildPossible(Building what) {
		for(Vertex v : getNeighbours())
			if(v.getBuilding() != null)
				return false;
		for(Edge e : getNeighbourEdges())
			if(e.getBuilding() != null)//just to avoid nullpointers
				if(e.getBuilding().getOwner().equals(what.getOwner()))
				return true;
		return false;
	}

	@Override
	public boolean isFirstBuildPossible(Building r) {
		for(Vertex v : getNeighbours()){
			if(v.getBuilding() != null)
				return false;
		}
		return true;
	}
}


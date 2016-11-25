package controller.map;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import controller.player.Settlement;

public class Vertex {
	private final String ID; 
	
	public Vertex(String id){
		ID = id;
	}
	
	public String getID() {
		return ID;
	}
	
	private Settlement settlement = new Settlement();
	
	public Map<String, Hex> hexes = new TreeMap<String, Hex>();
	
	ArrayList<Edge> edges;

	private ArrayList<Vertex> neighbours = new ArrayList<Vertex>(); 
	
	/**
	 * @return the Settlement contained by this Vertex
	 */
	public Settlement getSettlement() {
		return settlement;
	}
	/**
	 * @return List of neighboring Vertexes
	 */
	public ArrayList<Vertex> getNeighbours() {
		return neighbours;
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
}


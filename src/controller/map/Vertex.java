package controller.map;

import java.util.ArrayList;

import controller.player.Settlement;

public class Vertex {
	private /*final*/ int ID; //TODO
	
	public int getID() {
		return ID;
	}
	
	private Settlement settlement = new Settlement();
	
	ArrayList<Hex> hexes;
	
	ArrayList<Edge> edges;

	private ArrayList<Vertex> neighbors = new ArrayList<Vertex>(); 
	
	/**
	 * @return the Settlement contained by this Vertex
	 */
	Settlement getSettlement() {
		return settlement;
	}
	/**
	 * @return List of neighboring Vertexes
	 */
	ArrayList<Vertex> getNeighbours() {
		return neighbors;
	}
	
	/**
	 * @param other
	 * @return true if two Vertexes are the same else false
	 */
	boolean equals(Vertex other) {
		if (ID == other.getID()){
			return true;
		}
		return false;
	}
}


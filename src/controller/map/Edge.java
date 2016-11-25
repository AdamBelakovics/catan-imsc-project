package controller.map;

import java.util.ArrayList;

import controller.player.Building;
import controller.player.Road;

public class Edge implements TableElement {
	Vertex first; 
	Vertex second;
	Road road = new Road();
	
	/**
	 * @return returns the Road contained by this Edge
	 */
	public Road getRoad(){
		return road;
	}
	
	/**
	 * @return  returns the Vertexes which the Edge connects
	 */
	public ArrayList<Vertex> getEnds(){
		ArrayList<Vertex> ends = new ArrayList<Vertex>();
		ends.add(first);
		ends.add(second);
		return ends;	
	}
	
	/**
	 * @param other
	 * @return true if two Edges are the same else false
	 */
	public boolean equals(Edge other) {
		if (other.first == first && other.second == second)
			return true;
		return false;
	}
	
	
	@Override
	public boolean isBuildPossible(Building what) {
		// TODO Auto-generated method stub
		return false;
	}
}

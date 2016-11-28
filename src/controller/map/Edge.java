package controller.map;

import java.util.ArrayList;

import controller.player.Building;
import controller.player.Road;

public class Edge implements TableElement {
	Vertex first; 
	Vertex second;
	Road road;
	
	
	public Edge(Vertex f, Vertex s){
		first = f;
		second = s;
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
	@Override
	public boolean equals(Object other) {
		if ((((Edge)other).first == first && ((Edge)other).second == second) || 
				(((Edge)other).first == second && ((Edge)other).second == first)){
			return true;
		}
		return false;
	}
	
	
	@Override
	public boolean isBuildPossible(Building what) {
		for(Vertex v : this.getEnds())
			for(Edge e : v.getNeighbourEdges())
				if(e.getBuilding() != null) //just to avoid nullpointers
					if(e.getBuilding().getOwner().equals(what.getOwner()))
						return true;
		return false;
	}

	@Override
	public void setBuilding(Building b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Building getBuilding() {
		return road;
	}
}

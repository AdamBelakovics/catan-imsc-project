//By Adam Belakovics 2016. 10. 25

package controller.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.player.Building;
import controller.player.Resource;


public class Hex implements Comparable {
	public HashMap<String, Vertex> vertices = new HashMap<String, Vertex>();
	public ArrayList<Hex> neighbourHexes = new ArrayList<Hex>();
	public ArrayList<Vertex> neighbourVertices = new ArrayList<Vertex>();
	String id;
	Resource res = Resource.Lumber;
	int prosperity = 0;
	public boolean hasThief = false;
	
	public Hex (String identifier){
		id = identifier;
	}
	
	//INTERFACE public methods -------------------------------------------->
	
	public String getID(){
		return id;
	}
	
	public Resource getResource(){
		return res;
	}
	
	public int getProsperity(){
		return prosperity;
	}
	
	public boolean hasThief(){
		return hasThief;
	}
	
	public ArrayList<Hex> getNeighbouringHexes(){
		return neighbourHexes;
	}
	
	public ArrayList<Vertex> getNeighbouringVertices(){
		return neighbourVertices;
	}

	@Override
	public int compareTo(Object o) {
		if(((Hex) o).getID() == this.getID())
			return 0;
		return 1;
	}
	
	//USED for Table generation PRIVATE (mostly) --------------------------------->
	
	/**
	 * Generating all Neighbours. The Table will call this function.
	 * @param hexNeighbours
	 */
	void generateNeighbours(ArrayList<Hex> hexNeighbours){
		neighbourHexes = hexNeighbours;
		for(Map.Entry<String, Vertex> v : vertices.entrySet())
			neighbourVertices.add(v.getValue());
	}
	
	public void setResource(Resource r){
		res = r;
	}

}

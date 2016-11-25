//By Adam Belakovics 2016. 10. 25

package controller.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.player.Building;
import controller.player.Resource;


public class Hex implements TableElement {
	public HashMap<String, Vertex> vertices = new HashMap<String, Vertex>();
	public ArrayList<Hex> neighbourHexes = new ArrayList<Hex>();
	public ArrayList<Vertex> neighbourVertices = new ArrayList<Vertex>();
	String id;
	Resource res = Resource.Lumber;
	int prosperity = 0;
	
	public Hex (String identifier){
		id = identifier;
	}
	
	/**
	 * Generating all Neighbours. The Table will call this function.
	 * @param hexNeighbours
	 */
	void generateNeighbours(ArrayList<Hex> hexNeighbours){
		neighbourHexes = hexNeighbours;
		for(Map.Entry<String, Vertex> v : vertices.entrySet())
			neighbourVertices.add(v.getValue());
	}
	
	public String getID(){
		return id;
	}
	
	public Resource getResource(){
		return res;
	}
	
	public int getProsperity(){
		return prosperity;
	}
	
	public ArrayList<Hex> getNeighbouringHexes(){
		return neighbourHexes;
	}
	
	public ArrayList<Vertex> getNeighbouringVertices(){
		return neighbourVertices;
	}

	@Override
	public boolean isBuildPossible(Building what) {
		// TODO Auto-generated method stub
		return false;
	}

}

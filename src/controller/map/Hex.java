//By Adam Belakovics 2016. 10. 25

package controller.map;

import java.awt.Polygon;
import java.util.HashMap;


import controller.player.Building;


public class Hex implements TableElement {
	HashMap<Orientation, Vertex> vertices = new HashMap<Orientation, Vertex>(6);
	String id;
	Resource res = new Resource();
	int prosperity = 0;
	
	public Hex (String identifier){
		id = identifier;
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
	
	private void generateVertices() {
		for(Orientation o : Orientation.values()){
			if(o == Orientation.EAST || o == Orientation.WEST){
				//do nothing, we dont need vertices there
			}
			else{
				
			}
		}
	}
	

	@Override
	public boolean isBuildPossible(Building what) {
		// TODO Auto-generated method stub
		return false;
	}

}

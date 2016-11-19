package ai;

import controller.map.Table;
import controller.map.Vertex;

import java.util.ArrayList;


/**
 * Building a city
 * 
 * The class determines the best place to build a city.
 * 
 * @author Gergely Olah
 */
public class BuildCity {
	private Table map;
	private AI owner;
	private Vertex node;
	private double buildValue;
	
	/**
	 * Constructor. Initializes attributes.
	 * @param map - the table
	 * @param owner - the ai player who uses this class
	 * @author Gergely Olah
	 */
	public BuildCity(Table map, AI owner){
		this.map = map;
		this.owner = owner;
		node = null;
		buildValue = 0;
	}
	
	/**
	 * Determines the value of the best city the AI can build. The refresh
	 * function should be called before if the table have been changed since
	 * last call, or if no refresh function was called before.
	 * @return - the value
	 * @author Gergely Olah
	 */
	public double getBuildValue(){
		return buildValue;
	}
	
	/**
	 * Determines the best place to build a city. The refresh
	 * function should be called before if the table have been changed since
	 * last call, or if no refresh function was called before.
	 * @return - the node to build the city on, null if can't build any
	 * @author Gergely Olah
	 */
	public Vertex getNode(){
		return node;
	}
	
	/**
	 * Refreshes the node and the buildValue. Must call this before
	 * before calling the getter methods, as they may not return
	 * up to date values. 
	 * @author Gergely Olah
	 */
	public void refresh(){		
		buildValue = 0;
		node = null;
		ArrayList<Vertex> nodes = map.getVillages(owner.getPlayerID());
		for(Vertex n: nodes){
			double currentVal = owner.nodePersonalValue(n);
			if(currentVal > buildValue){
				buildValue = currentVal;
				node = n;
			}
		}
	}
}

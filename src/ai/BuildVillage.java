package ai;

import controller.map.Table;
import controller.map.Vertex;
import controller.player.Player;

import java.util.ArrayList;

/**
 * Building a village
 * 
 * The class determines the best place to build a village.
 * 
 * @author Gergely Olah
 */
public class BuildVillage {
	private Table map;
	// TODO needs ai
	private AiController owner;
	private Vertex node;
	private double buildValue;
	private Player aiPlayer;
	private ArrayList<Player> otherPlayers;
	
	/**
	 * Constructor. Initializes attributes.
	 * @param map - the table
	 * @param owner - the ai player who uses this class
	 * @author Gergely Olah
	 */
	// TODO contructor should get ai
	public BuildVillage(Table map, AiController owner, Player aiPlayer, ArrayList<Player> otherPlayers){
		this.map = map;
		// need ai
		this.owner = owner;
		node = null;
		buildValue = 0;
		this.aiPlayer = aiPlayer;
		this.otherPlayers = otherPlayers;
	}
	
	/**
	 * Determines the value of the best village the AI can build. The refresh
	 * function should be called before if the table have been changed since
	 * last call, or if no refresh function was called before.
	 * @return - the value
	 * @author Gergely Olah
	 */
	public double getBuildValue(){
		return buildValue;
	}
	
	/**
	 * Determines the best place to build a village. The refresh
	 * function should be called before if the table have been changed since
	 * last call, or if no refresh function was called before.
	 * @return - the node to build the village on, null if can't build any
	 * @author Gergely Olah
	 */
	public Vertex getNode(){
		return node;
	}
	
	/**
	 * Lists all nodes the AI can build a village on.
	 * @return - the list of the nodes
	 */
	private ArrayList<Vertex> listValidNodes(){
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		// TODO this might be different, waiting for Table's implemention
		/*
		for(Vertex n : map.getNodes()){
			if(n.getRoads(aiPlayer.getId()).size() > 0 && n.isValid()){
				result.add(n);
			}
		}
		*/
		return result;
	}
	
	/**
	 * Refreshes the node and the buildValue. Must call this before
	 * before calling the getter methods, as they may not return
	 * up to date values. 
	 * @author Gergely
	 */
	public void refresh(){
		buildValue = 0;
		node = null;
		ArrayList<Vertex> nodes = listValidNodes();		
		for(Vertex n : nodes){
			// TODO need ai
			//double currentValue = owner.nodePersonalValue(n);
			double currentValue = 0;
			if(currentValue > buildValue){
				buildValue = currentValue;
				node = n;
			}
		}
	}
}

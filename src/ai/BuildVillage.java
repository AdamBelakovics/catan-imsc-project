package ai;

import controller.map.Edge;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.Player;
import controller.player.Road;
import controller.player.Settlement;

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
	public BuildVillage(Table map, AiController owner, Player aiPlayer, ArrayList<Player> otherPlayers){
		this.map = map;
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
	 * @author Gergely Olah
	 */
	private ArrayList<Vertex> listValidNodes(){
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		// TODO this might be different, waiting for Table's implemention
		
		for(Vertex n : map.getNodes()){
			if(getPlayerRoadsFromNode(aiPlayer, n).size() > 0 && n.isBuildPossible(new Settlement(aiPlayer))){
				result.add(n);
			}
		}
		
		return result;
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
		ArrayList<Vertex> nodes = listValidNodes();		
		for(Vertex n : nodes){
			// TODO need ai
			double currentValue = owner.nodePersonalValue(n);
			if(currentValue > buildValue){
				buildValue = currentValue;
				node = n;
			}
		}
	}
	
	/**
	 * Lists roads starting from the given vertex
	 * owned by given player as list of Edge-s
	 * @param p - the player
	 * @return - the edges from the node the player has roads on,
	 * empty if no roads
	 */
	// TODO this is pretty much the same function as in BuildRoad, 
	// but it was the easiest to access it here too
	private ArrayList<Edge> getPlayerRoadsFromNode(Player p, Vertex v){
		ArrayList<Edge> result = new ArrayList<Edge>();
		Road tmpRoad = null;
		for(Edge e : map.getEdges()){
			tmpRoad = e.getRoad();
			if(tmpRoad != null && tmpRoad.getOwner().equals(p)){
				if(e.getEnds().contains(e))
					result.add(e);
			}
		}
		return result;
	}
}

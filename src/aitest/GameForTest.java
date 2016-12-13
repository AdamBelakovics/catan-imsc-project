package aitest;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import ai.AiController;
import controller.map.Edge;
import controller.map.Table;
import controller.map.TableElement;
import controller.map.Vertex;
import controller.player.Building;
import controller.player.City;
import controller.player.Player;
import controller.player.Road;
import controller.player.Settlement;
import ux.Renderer;
import ux.ui.InterfaceColorProfile;
import ux.ui.UIController;
/**
 * The class stores data used by the ai to access them easily
 * and without having to recalculate them. Data is stored as
 * static members for easy access.
 * @author Gergely
 *
 */
public class GameForTest {
	// nodes that have village on them or road starting from them
	private static HashMap<Player, HashSet<Vertex>> reachedNodes;
	// nodes that have no building on them and neither on their neighbors
	private static HashSet<Vertex> freeNodes;
	// location of the villages
	private static HashMap<Player, HashSet<Vertex>> villages;
	// current maximum length of roads
	private static HashMap<Player, Integer> maxRoadLengths;
	// current maximum length of roads from node
	private static HashMap<Player, HashMap<Vertex, Integer>> maxRoadLengthsFromNode;
	// the current table the players play on
	private static Table board;
	// current ai's playing
	private static ArrayList<AiController> aclist;
	// the player who has the two point bonus for longest road, null if no one has it
	// this has no real purpose, used for testing
	public static Player longestRoadKing = null;
	
	/**
	 * Initializes the state of the game. This should be called
	 * when a new game is started so this class can update data
	 * correctly.
	 * @param players - list of players playing
	 * @param acs - list of AiControllers playing
	 * @param t - the Table they play on
	 */
	public static void initialize(ArrayList<Player> players, ArrayList<AiController> acs, Table t){
		aclist = acs;
		board = t;
		reachedNodes = new HashMap<Player, HashSet<Vertex>>();
		villages = new HashMap<Player, HashSet<Vertex>>();
		maxRoadLengths = new HashMap<Player, Integer>();
		maxRoadLengthsFromNode = new HashMap<Player, HashMap<Vertex, Integer>>();
		for(Player p : players){
			reachedNodes.put(p, new HashSet<Vertex>());
			villages.put(p, new HashSet<Vertex>());
			maxRoadLengths.put(p, 0);
			maxRoadLengthsFromNode.put(p, new HashMap<Vertex, Integer>());
		}
		freeNodes = new HashSet<Vertex>();
		freeNodes.addAll(t.getNodes());
	}
	
	/**
	 * When a player build a settlement at the first turn.
	 * Updates free nodes, adds node to reached nodes and
	 * adds node to villages. Updates longest road from this
	 * node to 0 as it is now reached and it would be not updated
	 * from Player class otherwise.
	 * @param p - the player who built the settlement
	 * @param where - location of the village
	 */
	public static void settlementBuiltFirst(Player p, Vertex where){
		freeNodes.remove(where);
		for(Vertex v : where.getNeighbours()){
			freeNodes.remove(v);
		}
		villages.get(p).add(where);
		reachedNodes.get(p).add(where);
		maxRoadLengths.put(p, 0);
		maxRoadLengthsFromNode.get(p).put(where, 0);
	}
	
	/**
	 * When a player builds a settlement during the game.
	 * Updates free nodes and adds the location to the villages.
	 * @param p - the player who built the village
	 * @param where - location of the village
	 */
	public static void settlementBuilt(Player p, Vertex where){
		freeNodes.remove(where);
		for(Vertex v : where.getNeighbours()){
			freeNodes.remove(v);
		}
		villages.get(p).add(where);
	}
	
	/**
	 * Removes the vertex from the villages as there is no
	 * longer a village but a city.
	 * @param p - the player who built the city
	 * @param where - location of the city
	 */
	public static void cityBuilt(Player p, Vertex where){
		villages.get(p).remove(where);
	}
	
	/**
	 * Updates the reached nodes of the given player.
	 * Should be called when a player builds a road at any
	 * point of the game.
	 * @param where - location of the road
	 * @param p - the player who built the road
	 */
	public static void roadBuilt(Edge where, Player p){
		reachedNodes.get(p).add(where.getEnds().get(0));
		reachedNodes.get(p).add(where.getEnds().get(1));
	}
	
	/**
	 * Setter for length of longest road of a player. This should
	 * be called when the class Player updates the new maximum road
	 * of a player.
	 * @param p - the player
	 * @param l - the new length
	 */
	public static void updateMaxRoadLength(Player p, int l){
		maxRoadLengths.put(p, l);
	}
	
	/**
	 * Setter for length of longest road from a node. This should
	 * be called when the class Player updates the new maximum road
	 * of a specific node.
	 * @param p - the player
	 * @param v - the node
	 * @param l - the new length
	 */
	public static void updateMaxRoadLengthForNode(Player p, Vertex v, int l){
		maxRoadLengthsFromNode.get(p).put(v, l);
	}
	
	/**
	 * Lists all vertexes the given player can build a village on.
	 * The result is calculated as the intersection of the player's
	 * reached nodes and nodes that are free (no building on it or
	 * at its neighbours)
	 * @param p - the player
	 * @return - list of the valid nodes
	 */
	public static ArrayList<Vertex> validNodesForSettlement(Player p){
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		result.addAll(reachedNodes.get(p));
		result.retainAll(freeNodes);
		return result;
	}
	
	/**
	 * The location of the given player's villages
	 * @param p - the player
	 * @return - list of vertexes where the player has villages
	 */
	public static ArrayList<Vertex> getVillages(Player p){
		ArrayList<Vertex> result = new ArrayList<>();
		result.addAll(villages.get(p));
		return result;
	}
	
	/**
	 * The length of the longest road of all players in
	 * the current state of the game.
	 * @return - length of the longest road, 0 no one has roads
	 */
	public static int maxRoadLength(){
		int result = 0;
		for(Integer current : maxRoadLengths.values()){
			if(current > result)
				result = current;
		}
		return result;
	}
	
	/**
	 * The length of the longest road of Player p in
	 * the current state of the game.
	 * @param p - the player
	 * @return - length of the longest road, 0 if player has no roads
	 */
	public static int maxRoadLength(Player p){
		return maxRoadLengths.get(p);
	}
	
	/**
	 * The given player's longest road from the given node.
	 * @param p - the player
	 * @param v - source node
	 * @return - length of the longest road, -1 if the player 
	 * hasn't reached this node yet
	 */
	public static int maxRoadLengthFromNode(Player p, Vertex v){
		//System.out.println(maxRoadLengthsFromNode.toString());
		if(!maxRoadLengthsFromNode.get(p).containsKey(v))
			return -1;
		return maxRoadLengthsFromNode.get(p).get(v);
	}
}

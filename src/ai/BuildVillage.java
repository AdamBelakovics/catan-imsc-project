package ai;

import controller.map.Buildable;
import controller.map.Edge;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.Building;
import controller.player.City;
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
	private AiController owner;
	private Vertex node;
	private double buildValue;
	private Player aiPlayer;
	private ArrayList<Player> otherPlayers;
	
	// for testing, collects statistics of build value
	public static double minValue = Double.MAX_VALUE;
	public static double maxValue = - Double.MAX_VALUE;
	public static double sumValue = 0;
	public static int cnt = 0;
	
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
	 * Determines the value of the best village the AI can build.
	 * @return - the value
	 * @author Gergely Olah
	 */
	public double getBuildValue(){
		//refresh();
		return buildValue;
	}
	
	/**
	 * Determines the best place to build a village.
	 * @return - the node to build the village on, null if can't build any
	 * @author Gergely Olah
	 */
	public Vertex getNode(){
		//refresh();
		//System.out.println("Village value: " + buildValue);
		return node;
	}
	
	/**
	 * Lists all nodes the AI can build a village on.
	 * @return - the list of the nodes
	 * @author Gergely Olah
	 */
	private ArrayList<Vertex> listValidNodes(){
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		
		for(Vertex n : map.getNodes()){
			if(aiPlayer.getRoadsFromNode(n).size() > 0 && aiPlayer.isBuildPossible(Buildable.Settlement, n)){
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
		if(isVillageAvailable()){
			ArrayList<Vertex> nodes = listValidNodes();		
			for(Vertex n : nodes){
				double currentValue = 8 * owner.nodePersonalValue(n);
				if(currentValue > buildValue){
					buildValue = currentValue;
					node = n;
				}
			}
		}
		// for testing, collects statistics of build value
		if(buildValue > 0){
			sumValue += buildValue;
			cnt++;
			if(buildValue > maxValue)
				maxValue = buildValue;
			if(buildValue < minValue)
				minValue = buildValue;
		}
	}
	
	// For the ai's test to see the value of a node
	public double nodeValueForTest(Vertex v){
		ArrayList<Vertex> nodes = listValidNodes();	
		if(nodes.contains(v))
			return 8 * owner.nodePersonalValue(v);
		return 0;
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
	/*private ArrayList<Edge> getPlayerRoadsFromNode(Player p, Vertex v){
		ArrayList<Edge> edgeList = new ArrayList<Edge>(); 
		for(Edge e : v.getNeighbourEdges()){
			if((e.getBuilding() != null) && (e.getBuilding().getOwner() == p))
			edgeList.add(e);
		}
			
		return edgeList;
	}*/
	
	/**
	 * Counts ai's villages on the map, and returns true if
	 * less than 5, returns false otherwise
	 * @return - whether we have village available to build
	 * @author Gergely Olah
	 */
	private boolean isVillageAvailable(){
		int cnt = 0;
		Building tmpBuild;
		for(Vertex v : map.getNodes()){
			tmpBuild = v.getBuilding();
			if(tmpBuild != null && tmpBuild.getClass().equals(Settlement.class) && tmpBuild.getOwner().equals(aiPlayer)){
				cnt++;
			}
		}
		return cnt < 5;
	}
	
	
	/**
	 * This must be used for the first turn!!
	 * Determines the value of the best village the AI can build.
	 * This function might not be needed.
	 * @return - the value
	 * @author Gergely Olah
	 */
	public double getBuildValueFirstTurn(){
		refreshFirstTurn();
		return buildValue;
	}
	
	/**
	 * This must be used for the first turn!!
	 * Determines the best place to build a village.
	 * @return - the node to build the village on, null if can't build any
	 * @author Gergely Olah
	 */
	public Vertex getNodeFirstTurn(){
		refreshFirstTurn();
		return node;
	}
	
	/**
	 * This function must be used at the first and second turn!
	 * Refreshes the node and the buildValue. Must call this before
	 * before calling the getter methods, as they may not return
	 * up to date values. 
	 * @author Gergely Olah
	 */
	private void refreshFirstTurn(){
		buildValue = 0;
		node = null;		
		for(Vertex n : map.getNodes()){
			if(owner.isNodeValid(n)){
				double currentValue = owner.nodePersonalValue(n);
				if(currentValue > buildValue){
					buildValue = currentValue;
					node = n;
				}
			}
		}
	}
}

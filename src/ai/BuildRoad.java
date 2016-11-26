package ai;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.Set;

import controller.map.Vertex;
import controller.map.Table;
import controller.map.Edge;
import controller.player.Player;
import controller.player.Road;

/**
 * Building a road
 * 
 * The class determines the best place to build a road.
 * 
 * @author Gergely Olah
 */
public class BuildRoad {
	private Table map;
	private AiController owner;
	private double buildValue;
	private Vertex nodeFrom;
	private Vertex nodeTo;
	private Player aiPlayer;
	private ArrayList<Player> otherPlayers;
	
	/**
	 * Constructor. Initializes attributes.
	 * @param map - the table
	 * @param owner - the ai player who uses this class
	 * @author Gergely Olah
	 */
	public BuildRoad(Table map, AiController owner, Player aiPlayer, ArrayList<Player> otherPlayers){
		this.map = map;
		this.owner = owner;
		nodeFrom = null;
		nodeTo = null;
		buildValue = 0;
		this.aiPlayer = aiPlayer;
		this.otherPlayers = otherPlayers;
	}
	
	/**
	 * Determines the value of the best road the AI can build. The refresh
	 * function should be called before if the table have been changed since
	 * last call, or if no refresh function was called before.
	 * @return - the value
	 * @author Gergely Olah
	 */
	public double getBuildValue(){
		return buildValue;
	}
	
	/**
	 * Determines a node to build the best road from. The refresh function
	 * should be called before if the table have been changed since last
	 * call, or if no refresh function was called before.
	 * @return - the node to build the road from, null if can't build any
	 * @author Gergely Olah
	 */
	public Vertex getNodeFrom(){
		return nodeFrom;
	}
	
	/**
	 * Returns the best node to build a road to. The refresh function
	 * should be called before if the table have been changed since last
	 * call, or if no refresh function was called before.
	 * @return - the node to build the road to, null if can't build any
	 * @author Gergely Olah
	 */
	public Vertex getNodeTo(){
		return nodeTo;
	}
	
	/**
	 * Refreshes the nodes and the buildValue. Must call this before
	 * before calling the getter methods, as they may not return
	 * up to date values. 
	 * @author Gergely Olah
	 */
	public void refresh(){
		this.nodeFrom = null;
		this.nodeTo = null;
		double maxVal = 0;
		double val;
		int dif = calculateMaxRoadDifference();
		double difVal = 1;
		for(Vertex nodeTo : map.getNodes()){
			Vertex nodeFrom = fromWhereCanBuildRoad(nodeTo);
			if(nodeFrom != null){
				val = nodePersonalValueForRoad(nodeTo);
				if(dif < 2){
					if(dif >= -1)
						difVal = 1.5;
					else
						difVal = 1.2;
					if(isMaxRoadStart(nodeFrom))
						val = difVal * val;
				}
				if(val > maxVal){
					maxVal = val;
					this.nodeTo = nodeTo;
					this.nodeFrom = nodeFrom;
				}
			}
		}
		buildValue = maxVal;
	}
	
	/**
	 * Finds a Node where you can build a road to the given Node.
	 * It doesn't counts nodes that are already reached, as there
	 * is no point in building a road to that node.
	 * @param nodeTo - end of the road
	 * @return - start of the road, null if can't build any.
	 * @author Gergely Olah
	 */
	private Vertex fromWhereCanBuildRoad(Vertex nodeTo){
		// TODO getPlayerRoads, getRoadsFromNode, needs some work
		
		// there is no point in building multiple roads to same node
		for(Edge r : getPlayerRoads(this.aiPlayer)){
			if(r.getEnds().contains(nodeTo))
				return null;
		}
		for(Vertex nodeFrom : nodeTo.getNeighbours()){
			// if there is at least one road to this node
			if(getPlayerRoadsFromNode(aiPlayer, nodeFrom).size() > 0){
				// we have to check whether there is someone else's road there
				boolean isRoadBuilt = false;
				
				// checks all players except ai, as we know we have no road here
				for(Edge e : map.getEdges()){
					if(e.getRoad() != null)
						isRoadBuilt = true;
				}
				// if no road is blocking, we can build
				if(!isRoadBuilt){
					return nodeFrom;
				}
			}
		}
		return null;
	}
	
	/**
	 * Calculates the difference between AI's max road and the maximum of
	 * other player's max roads. Negative if AI's road is longer.
	 * @return - the difference
	 * @author Gergely Olah
	 */
	public int calculateMaxRoadDifference(){
		int maxRoadVal = 0;
		for(Player player : otherPlayers){
			int playersMax = calculatePlayerMaxRoad(player);
			if(playersMax > maxRoadVal)
				maxRoadVal = playersMax;
		}
		int AIMaxVal = calculatePlayerMaxRoad(aiPlayer);
		return maxRoadVal - AIMaxVal;
	}
	
	/**
	 * Calculates the given players maximum length road.
	 * @param player - the player
	 * @return - length of the maximum road
	 * @author Gergely Olah
	 */
	private int calculatePlayerMaxRoad(Player player){
		int max = 0;
		HashSet<Vertex> visitedNodes = new HashSet<Vertex>();
		for(Vertex n : map.getNodes()){
			visitedNodes.clear();
			int currentMax = calculatePlayerMaxRoadFromNode(n, player, visitedNodes);
			if(currentMax > max){
				max = currentMax;
			}
		}
		return max;
	}
	
	/**
	 * Recursive function, calculates longest road of given player from
	 * given node.
	 * @param fromNode - start of the road
	 * @param player - who's longest road we are lookin for
	 * @param visitedNodes - the Nodes we visited
	 * @return - the length of the longest road
	 * @author Gergely Olah
	 */
	private int calculatePlayerMaxRoadFromNode(Vertex fromNode, Player player, HashSet<Vertex> visitedNodes){
		// TODO needs getRoadsFromNode(player) (as Edges), and Rode.getNodes() if getRoads..() returns with Road-s
		
		int dist, max = 0;
	    visitedNodes.add(fromNode);
	    for(Edge road : getPlayerRoadsFromNode(player, fromNode)){
	    	Vertex n1, n2;
	        ArrayList<Vertex> roadNodes = road.getEnds();
	        n1 = roadNodes.get(0);
	        n2 = roadNodes.get(1);
	        // if we havent visited the node yet
	        if(fromNode.equals(n1)){
	        	if(!visitedNodes.contains(n2)){
	        		dist = 1 + calculatePlayerMaxRoadFromNode(n2, player, visitedNodes);
		            if(dist>max)
		                max=dist;
	        	}
	        } else if(fromNode.equals(n2)){
	        	if(!visitedNodes.contains(n1)){
	        		dist = 1 + calculatePlayerMaxRoadFromNode(n1, player, visitedNodes);
		            if(dist>max)
		                max=dist;
	        	}
	        }
	    }

	    visitedNodes.remove(fromNode);
	    return max;
	}
	
	/**
	 * Decides whether there is a longest road starting from
	 * the given Node.
	 * @param fromNode -the starting Node
	 * @return - true, if a longest road is starting here, false otherwise
	 * @author Gergely Olah
	 */
	private boolean isMaxRoadStart(Vertex fromNode){
		HashSet<Vertex> visitedNodes = new HashSet<Vertex>();
		int nodesMaxRoad = calculatePlayerMaxRoadFromNode(fromNode,aiPlayer, visitedNodes);
		int maxRoad = calculatePlayerMaxRoad(aiPlayer);
		return nodesMaxRoad == maxRoad;
	}
	/**
	 * Calculates the value of a node considering the personalValue
	 * of the node itself, and its neighbors.
	 * @param n - the node
	 * @return - the personal value from 0 to 10
	 */
	private double nodePersonalValueForRoad(Vertex n){
		HashSet<Vertex> visitedNodes = new HashSet<Vertex>();
		return nodePersonalValueForRoadRecursive(n, visitedNodes, 3);
	}
	
	/**
	 * Helper function for nodePersonalValueForRode(Vertex n).
	 * Recursive function, calculates given node's own personal
	 * value plus it's first 'cnt' neighbors personal value.
	 * The function is 'optimized' for a starter value of cnt = 3.
	 * with a magic formula.
	 * @param n - the node
	 * @param visitedNodes - nodes that we already counted in
	 * @param cnt - distance of neighbors that should be counted in
	 * @return - the personal value of the given node
	 */
	private double nodePersonalValueForRoadRecursive(Vertex n, Set<Vertex> visitedNodes, int cnt){
		// if we should not go further
		if(cnt < 0 || visitedNodes.contains(n) || n.getSettlement() != null)
			return 0;
		double result = 0;
		visitedNodes.add(n);
		ArrayList<Double> persValues = new ArrayList<Double>();
		for(Vertex next : n.getNeighbours()){
			persValues.add(nodePersonalValueForRoadRecursive(next, visitedNodes, cnt - 1));
		}
		double ownPersVal = owner.nodePersonalValue(n);
		if(ownPersVal == 0)
			return 0;
		// some magic formula, looks useable
		result += Math.min(1, ownPersVal - 2) * (ownPersVal + 2) * (0.1) * (cnt + 1) * (cnt + 1) * (cnt + 1) * 0.0156215;
		for(Double val : persValues){
			result += val;
		}
		return Math.max(10, result);
	}
	/**
	 * Lists roads owned by given player as list of Edge-s
	 * @param p - the player
	 * @return - the edges the player has roads on
	 */
	private ArrayList<Edge> getPlayerRoads(Player p){
		ArrayList<Edge> result = new ArrayList<Edge>();
		Road tmpRoad = null;
		for(Edge e : map.getEdges()){
			tmpRoad = e.getRoad();
			if(tmpRoad != null && tmpRoad.getOwner().equals(p)){
				result.add(e);
			}
		}
		return result;
	}
	
	/**
	 * Lists roads starting from the given vertex
	 * owned by given player as list of Edge-s
	 * @param p - the player
	 * @return - the edges from the node the player has roads on,
	 * empty if no roads
	 */
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

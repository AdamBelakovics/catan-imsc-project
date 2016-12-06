package ai;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.Set;

import controller.map.Vertex;
import controller.map.Table;
import controller.map.Edge;
import controller.player.Building;
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
	//private Vertex nodeFrom;
	//private Vertex nodeTo;
	private Edge edge;
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
		edge = null;
		buildValue = 0;
		this.aiPlayer = aiPlayer;
		this.otherPlayers = otherPlayers;
	}
	
	/**
	 * Determines the value of the best road the AI can build.
	 * @return - the value
	 * @author Gergely Olah
	 */
	public double getBuildValue(){
		refresh();
		return buildValue;
	}
	
	/**
	 * Determines the edge to build the best road on.
	 * @return - the edge to build the road on, null if can't build any
	 * @author Gergely Olah
	 */
	public Edge getEdge(){
		refresh();
		//System.out.println("Road value: " + buildValue);
		return edge;
	}
		
	/**
	 * Refreshes the nodes and the buildValue. Must call this before
	 * before calling the getter methods, as they may not return
	 * up to date values. 
	 * @author Gergely Olah
	 */
	private void refresh(){
		edge = null;
		buildValue = 0;
		if(isRoadAvailable()){
			double maxVal = -1;
			double val;
			int dif = calculateMaxRoadDifference();
			double difVal = 1;
			for(Edge e : listValidEdges()){
				Vertex node1 = e.getEnds().get(0);
				Vertex node2 = e.getEnds().get(1);
				if(aiPlayer.getRoadsFromNode(node1).isEmpty()){
					// if we haven't reached node1
					val = nodePersonalValueForRoad(node1);
					//System.out.println("SpecialRoadValue: " + val);
					if(dif < 2){
						if(dif >= -1)
							difVal = 1.5;
						else
							difVal = 1.2;
						if(isMaxRoadStart(node1))
							val = difVal * val;
					}
					if(val > maxVal){
						maxVal = val;
						edge = e;
					}
				} else if(aiPlayer.getRoadsFromNode(node2).isEmpty()){
					// if we haven't reached node2
					val = nodePersonalValueForRoad(node2);
					//System.out.println("SpecialRoadValue: " + val);
					if(dif < 2){
						if(dif >= -1)
							difVal = 1.5;
						else
							difVal = 1.2;
						if(isMaxRoadStart(node2))
							val = difVal * val;
					}
					if(val > maxVal){
						maxVal = val;
						edge = e;
					}
				} else {
					// if we reached both ends, the only good possibility
					// is when our max road will be bigger than before
					// TODO to check if we will have a longer max road
				}
			}
			buildValue = maxVal;
		}
	}
	/**
	 * Returns with the best edge to build in the first turn
	 * next to the given Vertex.
	 * @param where - the node, where the road should start
	 * @return - the best of the roads
	 */
	public Edge getEdgeFirstTurn(Vertex where){
		Vertex bestNode = null;
		double maxVal = 0;
		for(Vertex v : where.getNeighbours()){
			double val = nodePersonalValueForRoad(v);
			if(val > maxVal){
				bestNode = v;
				maxVal = val;
			}
		}
		for(Edge e : where.getNeighbourEdges())
			if(e.getEnds().contains(bestNode))
				return e;
		return null;
	}
	/**
	 * List of valid edges the ai can build
	 * @return - the list of valid edges
	 * @author Gergely Olah
	 */
	private ArrayList<Edge> listValidEdges(){
		ArrayList<Edge> result = new ArrayList<Edge>();
		for(Edge e: map.getEdges()){
			if(e.isBuildPossible(new Road(aiPlayer)) && e.getBuilding() == null){
				result.add(e);
			}
		}
		return result;
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
			int playersMax = player.calculateMaxRoad();
			if(playersMax > maxRoadVal)
				maxRoadVal = playersMax;
		}
		int AIMaxVal = aiPlayer.calculateMaxRoad();
		return maxRoadVal - AIMaxVal;
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
		int nodesMaxRoad = aiPlayer.calculateMaxRoadFromNode(fromNode, visitedNodes);
		int maxRoad = aiPlayer.calculateMaxRoad();
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
		if(cnt < 0 || visitedNodes.contains(n) || n.getBuilding() != null)
			return 0;
		double result = 0;
		visitedNodes.add(n);
		ArrayList<Double> persValues = new ArrayList<Double>();
		for(Vertex next : n.getNeighbours()){
			persValues.add(nodePersonalValueForRoadRecursive(next, visitedNodes, cnt - 1));
		}
		double ownPersVal = nodePersonalValueForRoadLocal(n);
		// some magic formula, looks useable
		result +=  ownPersVal * ownPersVal * (cnt + 1) * (cnt + 1) * (cnt + 1) * 0.0156215;
		//System.out.println("MagicRoadVal: " + result + " cnt: " + cnt);
		for(Double val : persValues){
			result += val;
		}
		return result;
	}
	
	/**
	 * Modified version of AiController's nodePersonal value
	 * @param v - the vertex
	 * @return - the personal value, 0 if builded or nexts are builded
	 * @author Gergely Olah
	 */
	
	private double nodePersonalValueForRoadLocal(Vertex v){
		if(owner.isNodeValid(v))
			return 2 * owner.nodePersonalValue(v);
		return 0;
	}

	
	/**
	 * Counts ai's roads on the map, and returns true if
	 * less than 15, returns false otherwise
	 * @return - whether we have road available to build
	 * @author Gergely Olah
	 */
	private boolean isRoadAvailable(){
		int cnt = 0;
		Building tmpRoad;
		for(Edge e : map.getEdges()){
			tmpRoad = (Road)e.getBuilding();
			if(tmpRoad != null && tmpRoad.getOwner().equals(aiPlayer)){
				cnt++;
			}
		}
		return cnt < 15;
	}
}

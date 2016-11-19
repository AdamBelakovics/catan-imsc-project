package ai;

import java.util.ArrayList;
import java.util.HashSet;

import controller.map.Vertex;
import controller.map.Table;
import controller.map.Edge;

/**
 * Building a road
 * 
 * The class determines the best place to build a road.
 * 
 * @author Gergely Olah
 */
public class BuildRoad {
	private Table map;
	private AI owner;
	private double buildValue;
	private Vertex nodeFrom;
	private Vertex nodeTo;
	
	/**
	 * Constructor. Initializes attributes.
	 * @param map - the table
	 * @param owner - the ai player who uses this class
	 * @author Gergely Olah
	 */
	public BuildRoad(Table map, AI owner){
		this.map = map;
		this.owner = owner;
		nodeFrom = null;
		nodeTo = null;
		buildValue = 0;
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
		int difVal = 1;
		// n is the node to build the road to
		for(Vertex nodeTo : map.getNodes()){
			Vertex nodeFrom = fromWhereCanBuildRoad(nodeTo);
			if(nodeFrom != null){
				val = owner.nodePersonalValue(nodeTo);
				if(dif < 2){
					if(dif >= -1)
						difVal = 3;
					else
						difVal = 2;
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
	 * @param nodeTo - end of the road
	 * @return - start of the road, null if can't build any.
	 * @author Gergely Olah
	 */
	private Vertex fromWhereCanBuildRoad(Vertex nodeTo){
		// there is no point in building multiple roads to same node
		for(Edge r : map.getPlayerRoads(owner.getPlayerID())){
			if(r.getNodes().contains(nodeTo))
				return null;
		}
		for(Vertex nodeFrom : nodeTo.getNeighbours()){
			// if there is at least one road to this node
			if(nodeFrom.getRoads(owner.getPlayerID()).size() > 0){
				// we have to check whether there is someone else's road there
				boolean isRoadBuilt = false;
				Edge keyRoad = new Edge(nodeFrom, nodeTo);
				// check all players
				for(int player : owner.getPlayerIDs()){
					if(map.getPlayerRoads(player).contains(keyRoad)){
						isRoadBuilt = true;
					}
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
		for(int player : owner.getPlayerIDs()){
			int playersMax = calculatePlayerMaxRoad(player);
			if(playersMax > maxRoadVal)
				maxRoadVal = playersMax;
		}
		int AIMaxVal = calculatePlayerMaxRoad(owner.getPlayerID());
		return maxRoadVal - AIMaxVal;
	}
	
	/**
	 * Calculates the given players maximum length road.
	 * @param player - the player
	 * @return - length of the maximum road
	 * @author Gergely Olah
	 */
	private int calculatePlayerMaxRoad(int player){
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
	private int calculatePlayerMaxRoadFromNode(Vertex fromNode, int player, HashSet<Vertex> visitedNodes){
		int dist, max=0;
	    visitedNodes.add(fromNode);
	    for(Edge road : fromNode.getRoads(player)){
	    	Vertex n1, n2;
	        ArrayList<Vertex> roadNodes = road.getNodes();
	        //if(roadNodes.size() != 2) throw new Exception();
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
		int nodesMaxRoad = calculatePlayerMaxRoadFromNode(fromNode, owner.getPlayerID(), visitedNodes);
		int maxRoad = calculatePlayerMaxRoad(owner.getPlayerID());
		return nodesMaxRoad == maxRoad;
	}
}

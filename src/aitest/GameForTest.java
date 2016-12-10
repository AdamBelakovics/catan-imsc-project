package aitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import controller.map.Edge;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.Player;
/**
 * The class stores data used by the ai to access them easily
 * and without having to recalculate them. Data is stored as
 * static members for easy access.
 * @author Gergely
 *
 */
public class GameForTest {
	
	private static HashMap<Player, HashSet<Vertex>> reachedNodes;
	private static HashSet<Vertex> freeNodes;
	private static HashMap<Player, HashSet<Vertex>> villages;
	private static HashMap<Player, Integer> maxRoadLengths;
	private static HashMap<Player, HashMap<Vertex, Integer>> maxRoadLengthsFromNode;
	
	public static void initialize(ArrayList<Player> players, Table t){
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
	
	public static void settlementBuilt(Player p, Vertex where){
		freeNodes.remove(where);
		for(Vertex v : where.getNeighbours()){
			freeNodes.remove(v);
		}
		villages.get(p).add(where);
	}
	
	public static void cityBuilt(Player p, Vertex where){
		villages.get(p).remove(where);
	}
	
	public static void roadBuilt(Edge where, Player p){
		reachedNodes.get(p).add(where.getEnds().get(0));
		reachedNodes.get(p).add(where.getEnds().get(1));
	}
	
	public static void updateMaxRoadLength(Player p, int l){
		maxRoadLengths.put(p, l);
	}
	
	public static void updateMaxRoadLengthForNode(Player p, Vertex v, int l){
		maxRoadLengthsFromNode.get(p).put(v, l);
	}
	
	public static ArrayList<Vertex> validNodesForSettlement(Player p){
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		result.addAll(reachedNodes.get(p));
		result.retainAll(freeNodes);
		return result;
	}
	
	public static ArrayList<Vertex> getVillages(Player p){
		ArrayList<Vertex> result = new ArrayList<>();
		result.addAll(villages.get(p));
		return result;
	}
	
	public static int maxRoadLength(){
		int result = 0;
		for(Integer current : maxRoadLengths.values()){
			if(current > result)
				result = current;
		}
		return result;
	}
	
	public static int maxRoadLength(Player p){
		return maxRoadLengths.get(p);
	}
	
	public static int maxRoadLengthFromNode(Player p, Vertex v){
		//System.out.println(maxRoadLengthsFromNode.toString());
		if(!maxRoadLengthsFromNode.get(p).containsKey(v))
			return -1;
		return maxRoadLengthsFromNode.get(p).get(v);
	}
}

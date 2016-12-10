package aitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
	
	private static ArrayList<Player> players;
	private static HashMap<Player, HashSet<Vertex>> reachedNodes;
	private static HashSet<Vertex> freeNodes;
	private static HashMap<Player, Integer> maxRoadLengths;
	private static HashMap<Player, HashMap<Vertex, Integer>> maxRoadLengthsFromNode;
	
	public static void initialize(ArrayList<Player> pls, Table t){
		players = pls;
		reachedNodes = new HashMap<>();
		maxRoadLengths = new HashMap<>();
		maxRoadLengthsFromNode = new HashMap<>();
		for(Player p : players){
			reachedNodes.put(p, new HashSet<Vertex>());
			maxRoadLengths.put(p, 0);
			maxRoadLengthsFromNode.put(p, new HashMap<Vertex, Integer>());
		}
		freeNodes = new HashSet<Vertex>();
		freeNodes.addAll(t.getNodes());
	}
	public static void settlementBuilt(Vertex where){
		freeNodes.remove(where);
		for(Vertex v : where.getNeighbours()){
			freeNodes.remove(v);
		}
	}
	public static void roadBuild(Edge where, Player p){
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
	public static int maxRoadLength(Player p){
		return maxRoadLengths.get(p);
	}
	public static int maxRoadLengthFromNode(Player p, Vertex v){
		return maxRoadLengthsFromNode.get(p).get(v);
	}
}

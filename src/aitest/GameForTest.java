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
	
	private static HashMap<Player, HashSet<Vertex>> reachedNodes;
	private static HashSet<Vertex> freeNodes;
	private static HashMap<Player, HashSet<Vertex>> villages;
	private static HashMap<Player, Integer> maxRoadLengths;
	private static HashMap<Player, HashMap<Vertex, Integer>> maxRoadLengthsFromNode;
	private static Table board;
	
	private static ArrayList<AiController> aclist;
	
	public static boolean nextPlayer = true;
	public static Player longestRoadKing = null;
	
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
	
	public static void drawMap(Building lastBuild, TableElement where){
		// TODO jo lesz ez, refresh-t kell optimalizálni
		Renderer rend = new Renderer(new UIController(new Player("", -1, board)), board, 1280, 700);
		//System.out.println(lastBuild.getClass() + " " + where);
		while(true){
			try {
				Thread.sleep(500);
				where.setBuilding(null);
				Thread.sleep(500);
				where.setBuilding(lastBuild);
			} catch (InterruptedException e) {

			}
		}
	}
	
	public static void drawMap(){
		// TODO
		Renderer rend = new Renderer(new UIController(new Player("", -1, board)), board, 1280, 700);
		AiController ai = null;
		Vertex nodeCity = null;
		Vertex nodeVillage = null;
		Edge edgeRoad = null;
		City city = null;
		Settlement villageBefore = null;
		Settlement village = null;
		Road road = null;
		int index = -1;
		while(true){
			if(nextPlayer){
				nextPlayer = false;
				index = (index + 1) % aclist.size();
				ai = aclist.get(index);
				ai.buildCity.refresh();
				ai.buildVillage.refresh();
				ai.buildRoad.refresh();
				nodeCity = ai.buildCity.getNode();
				nodeVillage = ai.buildVillage.getNode();
				edgeRoad = ai.buildRoad.getEdge();
				city = new City(ai.me);
				villageBefore = null;
				if(nodeCity != null)
					villageBefore = (Settlement)nodeCity.getBuilding();
				village = new Settlement(ai.me);
				road = new Road(ai.me);
			}
			try {
				Thread.sleep(500);
				if(nodeCity != null)
					nodeCity.setBuilding(city);
				if(nodeVillage != null)
					nodeVillage.setBuilding(village);
				if(edgeRoad != null)
				edgeRoad.setBuilding(road);
				Thread.sleep(500);
				if(nodeCity != null)
					nodeCity.setBuilding(villageBefore);
				if(nodeVillage != null)
					nodeVillage.setBuilding(null);
				if(edgeRoad != null)
					edgeRoad.setBuilding(null);
			} catch (InterruptedException e) {

			}
		}
	}
}

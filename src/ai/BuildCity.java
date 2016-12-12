package ai;

import controller.map.Table;
import controller.map.Vertex;
import controller.player.Building;
import controller.player.City;
import controller.player.Player;
import controller.player.Settlement;

import java.util.ArrayList;

import aitest.GameForTest;


/**
 * Building a city
 * 
 * The class determines the best place to build a city.
 * 
 * @author Gergely Olah
 */
public class BuildCity {
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
	public BuildCity(Table map, AiController owner, Player aiPlayer, ArrayList<Player> otherPlayers){
		this.map = map;
		this.owner = owner;
		node = null;
		buildValue = 0;
		this.aiPlayer = aiPlayer;
		this.otherPlayers = otherPlayers;
	}
	
	/**
	 * Determines the value of the best city the AI can build. The refresh
	 * function should be called before if the table have been changed since
	 * last call, or if no refresh function was called before.
	 * @return - the value
	 * @author Gergely Olah
	 */
	public double getBuildValue(){
		//refresh();
		return buildValue;
	}
	
	/**
	 * Determines the best place to build a city. The refresh
	 * function should be called before if the table have been changed since
	 * last call, or if no refresh function was called before.
	 * @return - the node to build the city on, null if can't build any
	 * @author Gergely Olah
	 */
	public Vertex getNode(){
		//refresh();
		//System.out.println("City value: " + buildValue);
		return node;
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
		if(isCityAvailable()){
			ArrayList<Vertex> nodes = GameForTest.getVillages(aiPlayer);
			//ArrayList<Vertex> nodes = getVillages();
			for(Vertex n: nodes){
				double currentVal = 9 * owner.nodePersonalValue(n);
				if(currentVal > buildValue){
					buildValue = currentVal;
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
	/**
	 * The vertexes the ai has villages (settlements) on. 
	 * @return  - list of the vertexes
	 */
	private ArrayList<Vertex> getVillages(){
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		Building what;
		Settlement tmpSet;
		for(Vertex v : map.getNodes()){
			what = v.getBuilding();
			if(what != null && what.getClass().equals(Settlement.class)){
				tmpSet = (Settlement)what;
				if(tmpSet.getOwner().equals(aiPlayer)){
					result.add(v);
				}
			}
		}
		return result;
	}
	
	/**
	 * Counts ai's cities on the map, and returns true if
	 * less than 4, returns false otherwise
	 * @return - whether we have city available to build
	 * @author Gergely Olah
	 */
	private boolean isCityAvailable(){
		int cnt = 0;
		Building tmpBuild;
		for(Vertex v : map.getNodes()){
			tmpBuild = v.getBuilding();
			if(tmpBuild != null && tmpBuild.getClass().equals(City.class) && tmpBuild.getOwner().equals(aiPlayer)){
				cnt++;
			}
		}
		return cnt < 4;
	}
}

package aitest;

import java.util.ArrayList;

import ai.AiController;
import controller.map.Edge;
import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.City;
import controller.player.Player;
import controller.player.Resource;
import controller.player.Road;
import controller.player.Settlement;
import ux.Renderer;
import ux.ui.UIController;

public class GameVisualizer {
	// the ai whos calculated values should be visualized
	private static AiController currentAi;
	private static ArrayList<AiController> aclist;
	private static boolean isFirstTurn = true;
	// renderer's mouse click listener can set this to change the current ai to display
	public static boolean nextPlayer = true;
	// renderer's mouse click listener can set this to make a move for the current ai
	public static boolean nextMove = false;
	public static boolean showBuildPlan = true;
	/**
	 * Initializes members of this class, and creates a renderer which draws the map.
	 * @param aclist - the ai's playing in the game
	 * @param table - the table they play on
	 */
	public static void initialize(ArrayList<AiController> aclist, Table table){
		GameVisualizer.aclist = aclist;
		currentAi = aclist.get(0);
		Renderer renderer = new Renderer(new UIController(new Player("", -1, table)), table, 1280, 700);
		isFirstTurn = true;
	}
	/**
	 * Sets isFirstTurn to false.
	 */
	public static void firstTurnEnded(){
		isFirstTurn = false;
	}
	/**
	 * Draws the map in it's current state, and visualizes the current ai's
	 * calculated best options as if it would be its turn. The current ai
	 * can be switched with pressing the space bar.
	 * @param ai - the ai which is currently active
	 */
	public static void drawNextChoice(AiController ai){
		Vertex nodeCity = null;
		Vertex nodeVillage = null;
		Edge edgeRoad = null;
		City city = null;
		Settlement villageBefore = null;
		Settlement village = null;
		Road road = null;
		int index = (aclist.indexOf(ai) - 1) % aclist.size();
		nextMove = false;
		nextPlayer = true;
		while(!nextMove){
			// this is set to true when the space bar is pressed
			if(nextPlayer){
				// we change the current ai to the next in the list, and set the buildings to display
				nextPlayer = false;
				index = (index + 1) % aclist.size();
				currentAi = aclist.get(index);
				currentAi.buildCity.refresh();
				currentAi.buildVillage.refresh();
				currentAi.buildRoad.refresh();
				if(isFirstTurn){
					nodeCity = null;
					if(currentAi.firstBuildVertex != null){
						nodeVillage = null;
						edgeRoad = currentAi.buildRoad.getEdgeFirstTurn(currentAi.firstBuildVertex);
					}
					else{
						nodeVillage = currentAi.buildVillage.getNodeFirstTurn();
						edgeRoad = null;
					}
				} else {
					nodeCity = currentAi.buildCity.getNode();
					nodeVillage = currentAi.buildVillage.getNode();
					edgeRoad = currentAi.buildRoad.getEdge();
				}
				city = new City(currentAi.me);
				villageBefore = null;
				if(nodeCity != null)
					villageBefore = (Settlement)nodeCity.getBuilding();
				village = new Settlement(currentAi.me);
				road = new Road(currentAi.me);
			}
			try {
				// blinking the buildings the ai would buy
				Thread.sleep(500);
				if(showBuildPlan && nodeCity != null)
					nodeCity.setBuilding(city);
				if(showBuildPlan && nodeVillage != null)
					nodeVillage.setBuilding(village);
				if(showBuildPlan && edgeRoad != null)
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
	// TODO
	public static void vertexClicked(Vertex where){
		System.out.println("-------------------------------");
		System.out.println("Node personal value: " + currentAi.nodePersonalValue(where));
		System.out.println("BuildCity personal value: " + currentAi.buildCityNodeValue(where));
		System.out.println("BuildRoad personal value: " + currentAi.buildRoadNodeValue(where));
		if(isFirstTurn){
			System.out.println("BuildVillage personal first value: " + currentAi.buildVillageNodeValueFirstTurn(where));
		} else {
			System.out.println("BuildVillage personal value: " + currentAi.buildVillageNodeValue(where));
		}
		System.out.println("-------------------------------");
	}
	public static void hexClicked(Hex where){
		System.out.println("-------------------------------");
		Resource res = where.getResource();
		if(res != null){
			// TODO
			System.out.println("Resource value: " + currentAi.resourceValue(res));
		}
		System.out.println("Territory value: " + currentAi.territoryPersonalValue(where));
		System.out.println("-------------------------------");
	}
	public static void edgeClicked(Edge where){
		System.out.println("-------------------------------");
		System.out.println("BuildRoad edge value: " + currentAi.buildRoadEdgeValue(where));
		System.out.println("-------------------------------");
	}
	public static void devButtonClicked(){
		System.out.println("-------------------------------");
		System.out.println("BuildDevelopment value: " + currentAi.buildDevelopmentValue());
		System.out.println("-------------------------------");
	}
}

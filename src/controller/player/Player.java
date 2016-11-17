package controller.player;

import java.util.ArrayList;
import java.util.Map;

import controller.map.Hex;
import controller.map.TableElement;

public class Player {
	String name;
	PlayerController controller; 
	int id;
	int points;
	int activeKnights;
	
	//Ezekre kene gettereket/settereket irni - Mate vallalta
	Map<Resource, Integer> changeLUT;
	Map<Resource, Integer> resourcePool;
	ArrayList<DevCard> devCards;
	
	
	
	
	/**
	 * Rolls the dice(e. g. generates two random integers (1-6), and adds them), and allocates new resources to each Player.
	 * If 7 is rolled calls the handleThief method
	 */
	public void rollTheDice() {
		handleThief(); //ennek itt kell lennie
	}
	
	/**
	 * The player can choose to change the position of the Thief. Executes all the changes accordingly.
	 * 
	 */
	private void handleThief(){
		
	}
	
	/**
	 * Builds a Building to a specific TableElement
	 * @param what Reference for a Building
	 * @param where Reference to the TableElement
	 */
	public void build(Building what, TableElement where){
		
	}
	
	/**
	 * Implements trading TODO
	 * Uses the PlayerController.query() function to make offers.
	 * Modifies resources accordingly.
	 */
	public void trade(){
		
	}
	
	/**
	 * Uses one of the players Dev cards
	 * @param dc
	 */
	public void playDev(DevCard dc){
		
	}
}

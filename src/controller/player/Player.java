package controller.player;

import java.util.Map;

import controller.map.Hex;
import controller.map.TableElement;

public class Player {
	String name;
	PlayerController controller; 
	int id;
	int points;
	Map<Resource, Integer> changeLUT;
	//Akos is here! Lets work!
	
	/**
	 * Rolls the dice(e. g. generates two random integers (1-6), and adds them), and allocates new resources to each Player.
	 * If 7 is rolled calls the handleThief method
	 */
	public void rollTheDice() {
		handleThief(); //ennek itt kell lennie
	}
	
	
	private void handleThief(){
		
	}
	
	
	public void build(Building what, TableElement where){
		
	}
	
	public void trade(){
		
	}
	
	public void playDev(DevCard dc){
		
	}
}

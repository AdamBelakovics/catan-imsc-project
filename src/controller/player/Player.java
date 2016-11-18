package controller.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.map.Hex;
import controller.map.TableElement;

public class Player {
	String name;
	PlayerController controller; 
	int id;
	int points;
	int activeKnights;
	
<<<<<<< HEAD
	//Ezekre kene gettereket/settereket irni - Mate vallalta
	Map<Resource, Integer> changeLUT;
	Map<Resource, Integer> resourcePool;
=======
	HashMap<Resource, Integer> changeLUT = new HashMap<Resource, Integer>();
	HashMap<Resource, Integer> resourcePool = new HashMap<Resource, Integer>();
>>>>>>> refs/remotes/origin/MateSzoke
	ArrayList<DevCard> devCards;
	
	/**
	 * Player constructor
	 * @param name
	 * @param id
	 * @param controller
	 * Initialize changeLUT to 4
	 * Initialize resourcePool to 0
	 */
	public Player(String name, int id, PlayerController controller){
		this.name = name;
		this.id = id;
		points = 0;
		activeKnights = 0;
		
		for(Resource r : Resource.values()){		
			changeLUT.put(r, 4);
		}
		
		for(Resource r : Resource.values()){		
			resourcePool.put(r, 0);
		}
		devCards = null;
				
	}
	
	
	/**
	 * Increase player's points with a value
	 * @param value
	 * @throws GameEndsException if points are bigger then 10
	 * TODO Handle exception in main!
	 *  **/
	
	public void incPoints(int value) throws GameEndsException{			
		if((points + value) >= 10){	
			points = 10;
			throw new GameEndsException(id);						 		
		}																	
		points += value;													
	}
	/**
	 * 
	 * @param value
	 * @throws OutOfRangeException if (points - value) goes negative
	 */
	public void decPoints(int value) throws OutOfRangeException{
		if((points - value) < 0) throw new OutOfRangeException("Points goes negative");
		points -= value;
	}
	
	/**
	 * getChangeLUT
	 * @param Resource
	 * @return value of change lut
	 */
	public int getChangeLUT(Resource r){
		return changeLUT.get(r);
	}
	/**
	 * set ChangeLUT
	 * @param Resource r
	 * @param int  value
	 * @throws OutOfRangeException if value not 2, 3 or 4
	 */
	public void setChangeLUT(Resource r, int value)throws OutOfRangeException{
		if(value!=2 || value!=3 || value!=4) throw new OutOfRangeException("Invalid value. (Try 2, 3, 4)");
		changeLUT.replace(r, value);
	}
	
	
	public int getResourcePool(Resource r){
		return resourcePool.get(r);
	}
	
	/**
	 * Increase resourcePool
	 * @param Resource r
	 * @param int value 
	 * @throws OutOfRangeException if value is negative
	 */
	
	public void incResourcePool(Resource r, int value) throws OutOfRangeException{
		if(value < 0) throw new OutOfRangeException("Value can't be negative.");
			resourcePool.replace(r, (resourcePool.get(r)+value));		
	}
	
	/**
	 * Decrease resourcePool
	 * @param Resource r
	 * @param int value 
	 * @throws OutOfRangeException if value is negative OR with this value, player's stuff goes below 0
	 */
	public void decResourcePool(Resource r, int value) throws OutOfRangeException{
		if(value < 0) throw new OutOfRangeException("Value can't be negative.");
		if((resourcePool.get(r)-value) < 0) throw new OutOfRangeException("It can not be much reduced");
		resourcePool.replace(r, (resourcePool.get(r)-value));		
	}
	
	public PlayerController getPlayerController(){
		return controller;
	}
	
	public String getName(){
		return name;
	}
	
	
	public int getId(){
		return id;
	}
	
	public int getPoints(){
		return points;
	}
	
	public int getActiveKnights(){
		return activeKnights;
	}
	
<<<<<<< HEAD
=======
	public void incActiveKnights(int value){
		activeKnights += value;
	}
	
	public void decActiveKnights(int value) throws OutOfRangeException{
		if((activeKnights - value) < 0) throw new OutOfRangeException("It can not be much reduced.");
		activeKnights -= value;
	}
	
	
	
	
	
>>>>>>> refs/remotes/origin/MateSzoke
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

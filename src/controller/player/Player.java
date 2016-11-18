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
<<<<<<< HEAD
	Map<Resource, Integer> resourcePool;
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
	
	public String getPlayerName(){
		return name;
	}
	
	
	/**
	 * Increase player's points with a value
	 * @param value
	 * @throws GameEndsException if it's bigger then 10
	 * TODO Handle exception in main!
	 *  **/
	
	public void inrPoints(int value) throws GameEndsException{			
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
	 * set ChangeLUT
	 * @param Resource r
	 * @param int  value
	 * @throws OutOfRangeException if value not 2, 3 or 4
	 */
	public void setChangeLUT(Resource r, int value)throws OutOfRangeException{
		if(value!=2 || value!=3 || value!=4) throw new OutOfRangeException("Invalid value. (Try 2, 3, 4)");
		changeLUT.replace(r, value);
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
	
	public void decResourcePool(Resource r, int value) throws OutOfRangeException{
		if(value < 0) throw new OutOfRangeException("Value can't be negative.");
		if((resourcePool.get(r)-value) < 0) throw new OutOfRangeException("It can not be much reduced");
		
	}
	
	public PlayerController getPlayerController(){
		return controller;
	}
	
	public int getPlayerId(){
		return id;
	}
	
	public int getPlayerPoints(){
		return points;
	}
	
	public int getActiveKnights(){
		return activeKnights;
	}
	
	
	
=======
	//hey my branch
>>>>>>> refs/remotes/origin/master
	
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

package controller.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.map.Hex;
import controller.map.TableElement;

public class Player {
	String name;
	public PlayerController controller; 
	int id;
	int points;
	int activeKnights;
	
	HashMap<Resource, Integer> changeLUT = new HashMap<Resource, Integer>();
	HashMap<Resource, Integer> resourcePool = new HashMap<Resource, Integer>();
	ArrayList<DevCard> devCards = new ArrayList<DevCard>();
	ArrayList<DevCard> playedDevCards = new ArrayList<DevCard>();
	
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
	
	//PLAYER GETTER/SETTER METHODS PRIVATE---------------------------------------------------------------------->
	//implemented as protected final for JUNIT testing
	
	/**
	 * Increase player's points with a value
	 * @param value
	 * @throws GameEndsException if points are bigger then 10
	 * TODO Handle exception in main!
	 *  **/
	protected final void incPoints(int value) throws GameEndsException{			
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
	protected final void decPoints(int value) throws OutOfRangeException{
		if((points - value) < 0) throw new OutOfRangeException("Points goes negative");
		points -= value;
	}
	
	/**
	 * Number of active knights can be increased by a given amount with this method.
	 * @param value 
	 */
	protected final void incActiveKnights(int value){
		activeKnights += value;
	}
	
	/**
	 * Number of active knights can be decreased by a given amount with this method.
	 * @param value
	 */
	protected final void decActiveKnights(int value) throws OutOfRangeException{
		if((activeKnights - value) < 0) throw new OutOfRangeException("It can't be negative.");
		activeKnights -= value;
	}
	
	/**
	 * Increase resourcePool
	 * @param Resource r
	 * @param int value 
	 * @throws OutOfRangeException if value is negative
	 */
	protected final void incResourceAmount(Resource r, int value) throws OutOfRangeException{
		if(value < 0) throw new OutOfRangeException("Value can't be negative.");
			resourcePool.replace(r, (resourcePool.get(r)+value));		
	}
	
	/**
	 * Decrease resourcePool
	 * @param Resource r
	 * @param int value 
	 * @throws OutOfRangeException if value is negative OR with this value, player's stuff goes below 0
	 */
	protected final void decResourceAmount(Resource r, int value) throws OutOfRangeException{
		if(value < 0) throw new OutOfRangeException("Value can't be negative.");
		if((resourcePool.get(r)-value) < 0) throw new OutOfRangeException("It can not be much reduced");
		resourcePool.replace(r, (resourcePool.get(r)-value));		
	}
	
	/**
	 * set ChangeLUT
	 * @param Resource r
	 * @param int  value
	 * @throws OutOfRangeException if value not 2, 3 or 4
	 */
	protected final void setChangeLUT(Resource r, int value)throws OutOfRangeException{
		if(value!=2 || value!=3 || value!=4) throw new OutOfRangeException("Invalid value. (Try 2, 3, 4)");
		changeLUT.replace(r, value);
	}
	
	//PLAYER GETTER/SETTER METHODS PUBLIC---------------------------------------------------------------------->
	
	/**
	 * getChangeLUT
	 * @param Resource
	 * @return value of change lut
	 */
	public int getChangeLUT(Resource r){
		return changeLUT.get(r);
	}
	
	/**
	 * Getter for Resources the Player currently has.
	 * @param Resource r is the Resource you want to get the amount of
	 * @return int the amount of a specified Resource
	 */
	public int getResourceAmount(Resource r){
		return resourcePool.get(r);
	}
	
	/**
	 * Getter for played DevCards
	 * @param playerID
	 * @return
	 */
	public ArrayList<DevCard> getPlayedDevelopmentCards(){
		return playedDevCards;	
	}
	
	/**
	 * Getter for PlayerController.
	 * @return PlayerController associated with this player.
	 */
	public PlayerController getPlayerController(){
		return controller;
	}
	
	/**
	 * Getter for Player's name.
	 * @return Player's name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Getter for Player's ID
	 * @return Player's ID
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * Getter for Player's points
	 * @return Player's points
	 */
	public int getPoints(){
		return points;
	}
	
	/**
	 * Getter for the number of active knights.
	 * @return number of active knights
	 */
	public int getActiveKnights(){
		return activeKnights;
	}
	
	
	//PLAYER ACTIONS---------------------------------------------------------------------->
	
	/**
	 * PLAYER ACTION
	 * Rolls the dice(e. g. generates two random integers (1-6), and adds them), and allocates new resources to each Player.
	 * If 7 is rolled calls the handleThief method
	 */
	public void rollTheDice() {
		handleThief(); //ennek itt kell majd lennie TODO
	}
	
	/**
	 * PLAYER ACTION
	 * The player can choose to change the position of the Thief. Executes all the changes accordingly.
	 * 
	 */
	private void handleThief(){
		
	}
	
	/**
	 * PLAYER ACTION
	 * Builds a Building to a specific TableElement
	 * @param what Reference for a Building
	 * @param where Reference to the TableElement
	 */
	public void build(Building what, TableElement where){
		
	}
	
	/**
	 * PLAYER ACTION
	 * Implements trading TODO
	 * Uses the PlayerController.query() function to make offers.
	 * Modifies resources accordingly.
	 */
	public void trade(){
		
	}
	
	/**
	 * PLAYER ACTION
	 * Player can buy a random Development Card from the DevCardShop.
	 * It's added to the devCards List.
	 */
	public void buyDevCard(){
		
	}
	
	/**
	 * PLAYER ACTION
	 * Uses one of the players Dev cards
	 * @param dc
	 */
	public void playDev(DevCard dc){
		
	}
}



package controller.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.map.Hex;
import controller.map.Table;
import controller.map.TableElement;
import controller.player.devcards.DevCard;
import controller.player.devcards.DevCardShop;
import controller.player.devcards.KnightCard;
import controller.player.devcards.MonopolyCard;
import controller.player.devcards.RoadBuildingCard;
import controller.player.devcards.VictoryPointCard;
import controller.player.devcards.YearOfPlentyCard;

public class Player {
	String name;
	public PlayerController controller; 
	int id;
	int points;
	int activeKnights;
	Table table;
	
	HashMap<Resource, Integer> changeLUT = new HashMap<Resource, Integer>();
	HashMap<Resource, Integer> resourcePool = new HashMap<Resource, Integer>();
	ArrayList<DevCard> devCards = new ArrayList<DevCard>();
	ArrayList<DevCard> playedDevCards = new ArrayList<DevCard>();
	ArrayList<Building> availableBuildings = new ArrayList<Building>();
	ArrayList<Building> erectedBuildings = new ArrayList<Building>();
	ArrayList<Player> otherPlayers = new ArrayList<Player>();
	
	
	
	/**
	 * Player constructor
	 * @param name
	 * @param id
	 * @param controller
	 * Initialize changeLUT to 4
	 * Initialize resourcePool to 0
	 * Initialize buildings: 15 Roads
	 * 						 5 Settlements
	 * 						 4 Cities
	 */
	public Player(String name, int id, Table t){
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
		
		for(int i = 0; i < 15; i++){
			availableBuildings.add(new Road(this));
		}
		
		for(int i = 0; i < 5; i++){
			availableBuildings.add(new Settlement(this));
		}
		
		for(int i = 0; i < 4; i++){
			availableBuildings.add(new City(this));
		}
		
		devCards = null;
		playedDevCards = null;
		table = t;
				
	}
	
	//PLAYER GETTER/SETTER METHODS PRIVATE---------------------------------------------------------------------->
	//implemented as protected final for JUNIT testing
	
	
	
	/**
	 * Setter for PlayerController
	 * @param PlayerController for the Player
	 */
	public void setPlayerController(PlayerController c){
		controller = c;
	}
	
	/**
	 * Increase player's points with a value
	 * @param value
	 * @throws GameEndsException if points are bigger then 10
	 * TODO Handle exception in main!
	 *  **/
	public final void incPoints(int value) throws GameEndsException{			
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
	public final void incActiveKnights(int value){
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
	public final void incResourceAmount(Resource r, int value) throws OutOfRangeException{
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
	
	
	public boolean equals(Player p){
		return this.getId() == p.getId();
	}
	
	
	//PLAYER ACTIONS---------------------------------------------------------------------->
	
	/**
	 * PLAYER ACTION
	 * Rolls the dice(e. g. generates two random integers (1-6), and adds them), and allocates new resources to each Player.
	 * If 7 is rolled calls the handleThief method and calls
	 */
	public void rollTheDice() {
		int result = (int)(Math.random()*6+1) + (int)(Math.random()*6+1);
		if(result == 7){
		//HERE												TODO If Players have more than 7 Resources card, they lose half of them
			handleThief();
		}
		table.allocateResources(result);
	}
	
	/**
	 * PLAYER ACTION
	 * The player can choose to change the position of the Thief. Executes all the changes accordingly.
	 * 
	 */
	public void handleThief(){
		
	}
	
	/**
	 * PLAYER ACTION
	 * Builds a Building to a specific TableElement
	 * @param what Reference for a Building
	 * @param where Reference to the TableElement
	 * @throws GameEndsException Building Settlement or City increases Player's points.
	 */
	public void build(Building what, TableElement where) throws GameEndsException{
		Resource w = Resource.Wool;
		Resource o = Resource.Ore;
		Resource g = Resource.Grain;
		Resource l = Resource.Lumber;
		Resource b = Resource.Brick;
		
		if(what.getClass().equals(Road.class)){
			try {
				decResourceAmount(b, 1);
				decResourceAmount(l, 1);
			} catch (OutOfRangeException e1) {
				e1.printStackTrace();
			}
			availableBuildings.remove(what);
			
			erectedBuildings.add(what);
															//TODO Player can take it's new Road to map
															//Then TODO check if with this new Road have 5 continuous road segments (or longer then the previous longest road owner's)
			
		}
		if(what.getClass().equals(Settlement.class)){
			
			try {											//TODO maybe here check is there any place to built Settlement at all
				decResourceAmount(b, 1);
				decResourceAmount(l, 1);
				decResourceAmount(g, 1);
				decResourceAmount(w, 1);
			} catch (OutOfRangeException e1) {
				e1.printStackTrace();
			}
			availableBuildings.remove(what);
			
			//HERE											TODO Player can take it's new Settlement to map
			erectedBuildings.add(what);
			this.incPoints(1);
		}
		if(what.getClass().equals(City.class)){
			try {
				decResourceAmount(g, 2);
				decResourceAmount(o, 3);
			} catch (OutOfRangeException e1) {
				e1.printStackTrace();
			}
			availableBuildings.remove(what);
			
			//HERE											TODO Player can take it's new City to a Settlement's place
			erectedBuildings.add(what);
			this.incPoints(1);		//Inc 1, cause the Settlement became City (-1+2=+1)
		}
		
	}
	
	/**
	 * PLAYER ACTION
	 * Implements trading TODO
	 * Uses the PlayerController.query() function to make offers.
	 * Modifies resources accordingly.
	 */
	public boolean trade(int amountW, Resource What, int amountFW, Resource ForWhat,Player with){
		boolean succesfull=false;
		return succesfull;
	}
	
	public void change(Resource give , Resource get){}
	/**
	 * PLAYER ACTION
	 * Player can buy a random Development Card from the DevCardShop.
	 * It's added to the devCards List.
	 * @throws NotEnoughResourcesException if Player don't have enough resources to buy DevCard
	 */
	public void buyDevCard() throws NotEnoughResourcesException {
		Resource w = Resource.Wool;
		Resource o = Resource.Ore;
		Resource g = Resource.Grain;
		
		if((resourcePool.get(w)<0) | (resourcePool.get(o)<0) |  (resourcePool.get(g)<0)) throw new NotEnoughResourcesException("You dont't have enough resoureces to buy developement card.");
		try {
			decResourceAmount(w, 1);
			decResourceAmount(o, 1);
			decResourceAmount(g, 1);
		} catch (OutOfRangeException e1) {
			e1.printStackTrace();
		}
		
		try {
			devCards.add(DevCardShop.buyDevCard());
		} catch (OutOfRangeException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * PLAYER ACTION
	 * Uses one of the players Dev cards
	 * @param dc
	 * @throws Throwable VictoryPointCard increase player's score, so it can throw GameEndsException
	 */
	public void playDev(DevCard dc, Resource r) throws GameEndsException{
		if(dc.getClass().equals(MonopolyCard.class) | dc.getClass().equals(YearOfPlentyCard.class))
			dc.doCard(this, r);
		if(dc.getClass().equals(KnightCard.class) | dc.getClass().equals(RoadBuildingCard.class) | dc.getClass().equals(VictoryPointCard.class))
			dc.doCard(this);
		playedDevCards.add(dc);
		devCards.remove(dc);
	}
}


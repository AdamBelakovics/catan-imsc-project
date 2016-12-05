
package controller.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import controller.Game;
import controller.map.Buildable;
import controller.map.Dice;
import controller.map.Edge;
import controller.map.Hex;
import controller.map.Table;
import controller.map.TableElement;
import controller.map.Vertex;
import controller.player.devcards.DevCard;
import controller.player.devcards.DevCardShop;
import controller.player.devcards.KnightCard;
import controller.player.devcards.MonopolyCard;
import controller.player.devcards.RoadBuildingCard;
import controller.player.devcards.VictoryPointCard;
import controller.player.devcards.YearOfPlentyCard;

/**
 * @author szoke
 *
 */
/**
 * @author szoke
 *
 */
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
	ArrayList<Road> availableRoads = new ArrayList<Road>();
	ArrayList<Settlement> availableSettlements = new ArrayList<Settlement>();
	ArrayList<City> availableCities = new ArrayList<City>();
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
			availableRoads.add(new Road(this));
		}
		
		for(int i = 0; i < 5; i++){
			availableSettlements.add(new Settlement(this));
		}
		
		for(int i = 0; i < 4; i++){
			availableCities.add(new City(this));
		}
		
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
	public final void decResourceAmount(Resource r, int value) throws OutOfRangeException{
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
	 * Returns all existing buildings of the Player.
	 * @return List of Buildings
	 */
	public ArrayList<Building> getAllBuildings(){
		return erectedBuildings;
	}
	
	/**
	 * Getter for table where the Player in
	 * @return table
	 */
	public Table getTable(){
		return table;
	}
	
	/**
	 * Getter for ChangeLUT the Player currently has
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
	 * Getter for DevCards list
	 * @return devCards
	 */
	public ArrayList<DevCard> getDevCards(){
		return devCards;
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
	
	/**
	 * Calculates the players maximum length road.
	 * @return - length of the maximum road
	 * @author Gergely Olah
	 */
	public int calculateMaxRoad(){
		int max = 0;
		HashSet<Vertex> visitedNodes = new HashSet<Vertex>();
		for(Vertex n : table.getNodes()){
			visitedNodes.clear();
			int currentMax = calculateMaxRoadFromNode(n, visitedNodes);
			if(currentMax > max){
				max = currentMax;
			}
		}
		return max;
	}
	
	/**
	 * Recursive function, calculates longest road of the player from
	 * given node.
	 * @param fromNode - start of the road
	 * @param visitedNodes - the Nodes we visited
	 * @return - the length of the longest road
	 * @author Gergely Olah
	 */
	public int calculateMaxRoadFromNode(Vertex fromNode, HashSet<Vertex> visitedNodes){
		
		int dist, max = 0;
	    visitedNodes.add(fromNode);
	    for(Edge road : getRoadsFromNode(fromNode)){
	    	Vertex n1, n2;
	        ArrayList<Vertex> roadNodes = road.getEnds();
	        n1 = roadNodes.get(0);
	        n2 = roadNodes.get(1);
	        // if we havent visited the node yet
	        if(fromNode.equals(n1)){
	        	if(!visitedNodes.contains(n2)){
	        		dist = 1 + calculateMaxRoadFromNode(n2, visitedNodes);
		            if(dist>max)
		                max=dist;
	        	}
	        } else if(fromNode.equals(n2)){
	        	if(!visitedNodes.contains(n1)){
	        		dist = 1 + calculateMaxRoadFromNode(n1, visitedNodes);
		            if(dist>max)
		                max=dist;
	        	}
	        }
	    }

	    visitedNodes.remove(fromNode);
	    return max;
	}
	
	/**
	 * Lists roads starting from the given vertex
	 * owned by the player as list of Edge-s
	 * @return - the edges from the node the player has roads on,
	 * empty if no roads
	 */
	public ArrayList<Edge> getRoadsFromNode(Vertex v){
		ArrayList<Edge> result = new ArrayList<Edge>();
		Road tmpRoad = null;
		for(Edge e : table.getEdges()){
			tmpRoad = (Road)e.getBuilding();
			if(tmpRoad != null && tmpRoad.getOwner().equals(this)){
				if(e.getEnds().contains(v))
					result.add(e);
			}

		}
		return result;
	}
	
	//PLAYER ACTIONS---------------------------------------------------------------------->
	
	/**
	 * PLAYER ACTION
	 * Rolls the dice(e. g. generates two random integers (1-6), and adds them), and allocates new resources to each Player.
	 * If 7 is rolled calls the handleThief method and calls
	 * @throws OutOfRangeException 
	 */
	public void rollTheDice() {
		Resource w = Resource.Wool;
		Resource o = Resource.Ore;
		Resource g = Resource.Grain;
		Resource l = Resource.Lumber;
		Resource b = Resource.Brick;
		System.out.println(this.getName());
		System.out.println("W " + getResourceAmount(w));
		System.out.println("O " + getResourceAmount(o));
		System.out.println("G " + getResourceAmount(g));
		System.out.println("L " + getResourceAmount(l));
		System.out.println("B " + getResourceAmount(b));
		int result = (int)(Math.random()*6+1) + (int)(Math.random()*6+1);
		System.out.println("Hello from rollthedice"+result);
		Dice.setCurrentValue(result);
		if(result == 7){
			for(int i = 0; i < Game.players.size(); i++){
				Player a = Game.players.get(i);
				int sumAmountResources = (a.getResourceAmount(w) + a.getResourceAmount(o) + a.getResourceAmount(g) + a.getResourceAmount(l) + a.getResourceAmount(b));
				if(sumAmountResources>7){
					int cnt = (int)Math.floor(sumAmountResources/2);	
					while(cnt>0){
						int rnd = (int)(Math.random()*5);
						Resource randRes = Resource.values()[rnd];
						if(a.getResourceAmount(randRes)>0){
							try {
								a.decResourceAmount(randRes, 1);
								cnt--;
							} catch (OutOfRangeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else{
							
						}
					}		
				}
			}

			handleThief();
		}
		else{
		table.allocateResources(result);
		}
	}
	
	/**
	 * PLAYER ACTION
	 * The Thief goes to a random Hex, then the player get one Resource of the Hex
	 * 
	 */
	public void handleThief(){
		System.out.println("Hello from handlethief");
		for(int i = 0; i < table.getFields().size(); i++){
			if(table.getFields().get(i).hasThief() == true){
				table.getFields().get(i).setHasThief(false);
			}
		}
		
		Hex hex = table.getValidFields().get((int)(Math.random()*table.getValidFields().size()));
		hex.setHasThief(true);
		
		Resource Res = hex.getResource();
		
		try {
			this.incResourceAmount(Res, 1);
		} catch (OutOfRangeException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isFirstBuildPossible(Buildable what, TableElement where){
		System.out.println("Hello from is1bud");
		if(what == Buildable.Road){
			boolean rightInput = where != null;;
			if(rightInput){
				boolean isEmpty = where.getBuilding() == null;
				boolean isTheRightPlace = where.getClass().equals(Edge.class);
				boolean hasNeighbouringSettlement = (((Edge)where).getEnds().get(0).getBuilding() == null ? false :  (((Edge)where).getEnds().get(0).getBuilding().getClass() == Settlement.class && ((Edge)where).getEnds().get(0).getBuilding().getOwner().equals(this)))
													|| (((Edge)where).getEnds().get(1).getBuilding() == null ? false :  (((Edge)where).getEnds().get(1).getBuilding().getClass() == Settlement.class && ((Edge)where).getEnds().get(1).getBuilding().getOwner().equals(this)));
				if(isEmpty && isTheRightPlace && hasNeighbouringSettlement){
					return true;
				}
			}
			else
				throw new NullPointerException("From isFistBuildPossible(Road): the received TableElement is null.");
			return false;
		}
		else if(what == Buildable.Settlement){
			boolean rightInput = where != null;
			if (rightInput){
				boolean isEmpty = where.getBuilding() == null;
				boolean isTheRightPlace = where.getClass().equals(Vertex.class);
				boolean hasNeighbouringSettlement = false;
				for(Vertex v : ((Vertex)where).getNeighbours())									
					if(v.getBuilding() != null)
						hasNeighbouringSettlement = true;
				if(isEmpty && isTheRightPlace && !hasNeighbouringSettlement){
					return true;
				}
			}
			else
				throw new NullPointerException("From isFirstBuildPossible(Settlement): the received TableElement is null.");
			return false;
		}
		else if(what == Buildable.City){
			return false;
		}	
		return false;
	}
	
	public boolean firstBuild(Buildable what, TableElement where) throws GameEndsException{
		System.out.println("Hello from 1buid");
		boolean succesful = false;	
		if(what == Buildable.Road){
			succesful = isFirstBuildPossible(Buildable.Road, where);
			if(succesful){
				Road u = availableRoads.remove(0);
				where.setBuilding(u);
				erectedBuildings.add(u);
			}
		}
		else if(what == Buildable.Settlement){
			succesful = isFirstBuildPossible(Buildable.Settlement, where);;
			if(succesful){
				Settlement s = availableSettlements.remove(0);
				where.setBuilding(s);
				erectedBuildings.add(s);
				this.incPoints(1);
			}	
		}
		else if(what == Buildable.City){
			succesful = false;
		}
		return succesful;
	}
	
	public boolean isBuildPossible(Buildable what, TableElement where){
		System.out.println("Hello from isbuild");
		if(what == Buildable.Road){
			boolean hasEnoughAvailableRoads = (availableRoads.size() > 0);
			boolean isEmpty = where.getBuilding() == null;
			boolean isTheRightPlace = where.getClass().equals(Edge.class);
			boolean hasNeighbouringSettlement = (((Edge)where).getEnds().get(0).getBuilding() == null ? false :  (((Edge)where).getEnds().get(0).getBuilding().getClass() == Settlement.class && ((Edge)where).getEnds().get(0).getBuilding().getOwner().equals(this)))
											|| (((Edge)where).getEnds().get(1).getBuilding() == null ? false :  (((Edge)where).getEnds().get(1).getBuilding().getClass() == Settlement.class && ((Edge)where).getEnds().get(1).getBuilding().getOwner().equals(this)));
			boolean hasNeighbouringCity = (((Edge)where).getEnds().get(0).getBuilding() == null ? false :  (((Edge)where).getEnds().get(0).getBuilding().getClass() == City.class && ((Edge)where).getEnds().get(0).getBuilding().getOwner().equals(this)))
											|| (((Edge)where).getEnds().get(1).getBuilding() == null ? false :  (((Edge)where).getEnds().get(1).getBuilding().getClass() == City.class && ((Edge)where).getEnds().get(1).getBuilding().getOwner().equals(this)));
			boolean hasNeighbouringRoad = false;
			for(Vertex v : ((Edge)where).getEnds())
				if(v.getBuilding() == null ? true : v.getBuilding().getOwner().equals(this)){
					for(Edge e : v.getNeighbourEdges())
							if(e.getBuilding() == null ? false : e.getBuilding().getOwner().equals(this))
									hasNeighbouringRoad = true;
					}
			if(hasEnoughAvailableRoads && isEmpty && isTheRightPlace && (hasNeighbouringSettlement || hasNeighbouringCity || hasNeighbouringRoad)){
				return true;
			}
			return false;
		}
		else if(what == Buildable.Settlement){
			boolean hasEnoughAvailableSettlements = (availableSettlements.size() > 0);
			boolean isEmpty = where.getBuilding() == null;
			boolean isTheRightPlace = where.getClass().equals(Vertex.class);
			boolean hasNeighbouringSettlement = false;
			for(Vertex v : ((Vertex)where).getNeighbours())									
				if(v.getBuilding() != null)
					hasNeighbouringSettlement = true;
			boolean hasNeighbouringRoad = false;
			for(Edge e : ((Vertex)where).getNeighbourEdges()){
				if(e.getBuilding() == null ? false : (e.getBuilding().getClass().equals(Road.class) && e.getBuilding().getOwner().equals(this))){
					hasNeighbouringRoad = true;
				}
			}
			if(hasEnoughAvailableSettlements && isEmpty && isTheRightPlace && !hasNeighbouringSettlement && hasNeighbouringRoad){
				return true;
			}
			return false;
		}
		else if(what == Buildable.City){
			boolean hasEnoughAvailableCities = (availableCities.size() > 0);
			boolean hasSettlement = ((Vertex)where).getBuilding() == null ? false : ((Vertex)where).getBuilding().getClass().equals(Settlement.class) && ((Vertex)where).getBuilding().getOwner().equals(this); 
			if(hasEnoughAvailableCities && hasSettlement){
				return true;
			}
			return false;
		}	
		return false;
	}
	
	/**
	 * PLAYER ACTION
	 * Builds a Building to a specific TableElement
	 * @param what Reference for a Building
	 * @param where Reference to the TableElement
	 * @throws GameEndsException Building Settlement or City increases Player's points.
	 */
	public boolean build(Buildable what, TableElement where) throws GameEndsException{
		System.out.println("Hello from build " + what + " " + where);
		Resource w = Resource.Wool;
		Resource o = Resource.Ore;
		Resource g = Resource.Grain;
		Resource l = Resource.Lumber;
		Resource b = Resource.Brick;
		boolean succesful = false;
		
		if(what == Buildable.Road){
			if(getResourceAmount(b) >= 1 && getResourceAmount(l) >= 1){
				try {
					decResourceAmount(b, 1);
					decResourceAmount(l, 1);
				} catch (OutOfRangeException e1) {
					e1.printStackTrace();
				}
				Road u = availableRoads.remove(0);
				succesful = u.build(where);
				if(succesful)
					erectedBuildings.add(u);
				else{
					availableRoads.add(u);
					try {
						incResourceAmount(b, 1);
						incResourceAmount(l, 1);
					} catch (OutOfRangeException e) {
						e.printStackTrace();
					}
				}
			}
		}
		else if(what == Buildable.Settlement){
			if(getResourceAmount(b) >= 1 && getResourceAmount(l) >= 1 && getResourceAmount(g) >= 1 && getResourceAmount(w) >= 1){
				try {										
					decResourceAmount(b, 1);
					decResourceAmount(l, 1);
					decResourceAmount(g, 1);
					decResourceAmount(w, 1);
				} catch (OutOfRangeException e1) {
					e1.printStackTrace();
				}
				Settlement s = availableSettlements.remove(0);
				succesful = s.build(where);
				if(succesful){
					erectedBuildings.add(s);
					this.incPoints(1);
				}
				else{
					availableSettlements.add(s);
					try {										
						incResourceAmount(b, 1);
						incResourceAmount(l, 1);
						incResourceAmount(g, 1);
						incResourceAmount(w, 1);
					} catch (OutOfRangeException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		else if(what == Buildable.City){
			if(getResourceAmount(g) >= 2 && getResourceAmount(o) >= 3){
				try {
					decResourceAmount(g, 2);
					decResourceAmount(o, 3);
				} catch (OutOfRangeException e1) {
					e1.printStackTrace();
				}
				City c = availableCities.remove(0);
				succesful = c.build(where);
				if(succesful){
					availableSettlements.add((Settlement) where.getBuilding());
					erectedBuildings.remove(where.getBuilding());
					erectedBuildings.add(c);
					this.incPoints(1);
				}
				else{
					availableCities.add(c);
					try {										
						incResourceAmount(g, 2);
						incResourceAmount(o, 3);
					} catch (OutOfRangeException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return succesful;
	}
	
	/**
	 * PLAYER ACTION
	 * Implements trading TODO
	 * Uses the PlayerController.query() function to make offers.
	 * Modifies resources accordingly.
	 */
	public boolean trade(int amountW, Resource What, int amountFW, Resource ForWhat,Player with){
		System.out.println("Hello from trade");
		boolean succesfull=false;
		return succesfull;
	}
	
	public void change(Resource give , Resource get) {
		System.out.println("Hello from change");
		try {
			if(this.getResourceAmount(give) >= 4)
				this.decResourceAmount(give, 4);
		} catch (OutOfRangeException e) {
			e.printStackTrace();
		}
		try {
			this.incResourceAmount(get, 1);
		} catch (OutOfRangeException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * PLAYER ACTION
	 * Player can buy a random Development Card from the DevCardShop.
	 * It's added to the devCards List.
	 * @throws NotEnoughResourcesException if Player don't have enough resources to buy DevCard
	 */
	public void buyDevCard() throws NotEnoughResourcesException {
		System.out.println("Hello from buyDev");
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
		System.out.println("Hello from playDev");
		dc.doCard(this, r);
		playedDevCards.add(dc);
		devCards.remove(dc);
	}
}


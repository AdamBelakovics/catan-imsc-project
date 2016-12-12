package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aitest.AiParameter;
import aitest.GameForTest;
import controller.map.Buildable;
import controller.map.Edge;
import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.GameEndsException;
import controller.player.NotEnoughResourcesException;
import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.PlayerController;
import controller.player.Resource;
import controller.player.devcards.DevCard;
import controller.player.devcards.KnightCard;
import controller.player.devcards.MonopolyCard;
import controller.player.devcards.RoadBuildingCard;
import controller.player.devcards.VictoryPointCard;
import controller.player.devcards.YearOfPlentyCard;

public class AiController extends PlayerController {

	public Player me;
	ArrayList<Player> players = new ArrayList<Player>();
	private Table map;
	private int robberSum;

	public BuildCity buildCity;
	public BuildVillage buildVillage;
	public BuildRoad buildRoad;
	public BuildDevelopment buildDevelopment;

	private Set<Integer> numbers = new HashSet<Integer>();
	private Map<Resource, Material> resources = new HashMap<Resource, Material>();
	private Map<Resource, Integer> rAmount = new HashMap<Resource, Integer>();
	private Map<Resource, Integer> rLut = new HashMap<Resource, Integer>();

	// for testing
	private HashSet<AiParameter> params;
	private boolean isFirstTurn;
	private int turnCount;
	
	public static double minValueNode = Double.MAX_VALUE;
	public static double maxValueNode = - Double.MAX_VALUE;
	public static double sumValueNode = 0;
	public static int cntNode = 0;
	public static double minValueTerr = Double.MAX_VALUE;
	public static double maxValueTerr = - Double.MAX_VALUE;
	public static double sumValueTerr = 0;
	public static int cntTerr = 0;

	/**
	 * Constructor. Initializes attributes.
	 * @param t - the table
	 * @param p - the player's datas and control possibilities
	 * @param otherPlayers - the ather players data's
	 * @author @author Hollo-Szabo Akos
	 */
	public AiController(Table t, Player p, ArrayList<Player> otherPlayers, HashSet<AiParameter> prms) {
		map = t;
		me = p;
		players.addAll(otherPlayers);
		robberSum = 0;
		for(Resource r:Resource.values())
			resources.put(r,new Material(map,me,r, prms));
		// these should be here, so they don't initialize with null pointers
		buildCity = new BuildCity(t, this, p, otherPlayers);
		buildVillage = new BuildVillage(t, this, p, otherPlayers);
		buildRoad = new BuildRoad(t, this, p, otherPlayers);
		buildDevelopment = new BuildDevelopment(t, this, p, otherPlayers);
		
		params = prms;
		isFirstTurn = true;
		turnCount = 0;
	}

	/**
	 * getKnightDiff. Calculates:How money KnightCard do you need to get the 2 pints?
	 * @author Hollo-Szabo Akos
	 */
	public int getKnightDiff() {
		Map<Player, Integer> amountoKnightsperPlayer = new HashMap<Player, Integer>();
		int max = 3;

		for (Player p : players) {
			amountoKnightsperPlayer.put(p, p.getActiveKnights());
			if (amountoKnightsperPlayer.get(p) > max && !p.equals(me))
				max = amountoKnightsperPlayer.get(p);
		}

		return max - amountoKnightsperPlayer.get(me);
	}

	/**
	 * getResources. Get:the map of Metirials
	 * @author Hollo-Szabo Akos
	 */
	public Map<Resource, Material> getResources() {
		return resources;
	}

	/**
	 * @return true if robber is on a field the player has a village or city on
	 */
	public boolean isRobbed() {
		for(Vertex v : map.getNodes()){
			if((v.getBuilding() != null) && (v.getBuilding().getOwner().equals(me))){
				for(Hex h : v.getNeighbourHexes()){
					if(h.hasThief){
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * query. Calculates:Should we accept the offer?
	 * @param donor - The donor.
	 * @param offer - Map of the offered resources.
	 * @param demand - The demand in change.
	 * @author Hollo-Szabo Akos
	 */
	public boolean query(Player donor, Map<Resource, Integer> offer, Map<Resource, Integer> demand) {
		double offerValue = 0;
		double demandValue = 0;

		for (Resource res : Resource.values()){
			if(offer.containsKey(res)){
				offerValue += resources.get(res).personalValue() * offer.get(res);
			}
			if(demand.containsKey(res)){
				demandValue += resources.get(res).personalValue() * demand.get(res);
			}
		}

		if(demandValue > offerValue){
			return true;
		}

		return false;
	}

	/**
	 * turn. The main calculator of future planes, and the center of control. (Avagy egy fasza repter :D)
	 * @author Hollo-Szabo Akos
	 */
	public void turn() throws GameEndsException{
		me.rollTheDice();
		if(params.contains(AiParameter.Random)){
			turnRandom();
		} else {
			boolean playedCard = true;
			int index = 0;
			while(playedCard){
				//System.out.println("development card ciklus");
				playedCard = false;
				if(me.getDevCards().size() > index){
					if(me.getDevCards().get(index).getClass().equals(KnightCard.class)){
						if(isRobbed()){
							me.playDev(me.getDevCards().get(index), null);
							playedCard = true;
						} else {
							index++;
						}
					}
					else if (me.getDevCards().get(index).getClass().equals(YearOfPlentyCard.class)){
						me.playDev(me.getDevCards().get(index), null);
						playedCard = true;
					}
					else if (me.getDevCards().get(index).getClass().equals(MonopolyCard.class)){
						me.playDev(me.getDevCards().get(index), null);
						playedCard = true;
					}
					else if (me.getDevCards().get(index).getClass().equals(RoadBuildingCard.class)){
						me.playDev(me.getDevCards().get(index), null);
						playedCard = true;
					}
					else if (me.getDevCards().get(index).getClass().equals(VictoryPointCard.class)){
						int victoryPoints = 0;
						for(DevCard card : me.getDevCards()){
							if(card.getClass().equals(VictoryPointCard.class)){
								victoryPoints++;
							}
						}
						if(me.getPoints() + victoryPoints >= 10){
							me.playDev(me.getDevCards().get(index), null);
							//System.out.println("Victory point played");
							playedCard = true;
						}
					}
				}
			}
			//System.out.println("Development ciklus ended");

			boolean buildSuccesful = true;
			while(buildSuccesful){
				//System.out.println("Build ciklus");
				buildSuccesful = false;
				ArrayList<Buildable> choices = new ArrayList<Buildable>();
				choices.add(Buildable.Settlement);
				choices.add(Buildable.Road);
				choices.add(Buildable.City);
				choices.add(Buildable.Development);
				buildCity.refresh();
				buildVillage.refresh();
				buildRoad.refresh();
				buildDevelopment.refresh();
				choices.sort(new BuildableComparator(map, me, this, players));
				for(Buildable b : choices){
					if(b.equals(Buildable.City)){
						if(buildCity.getNode() != null){
							if(me.build(b, buildCity.getNode())){
								buildSuccesful = true;
								break;
							} else {
								buildSuccesful = false;
							}
						}
					}
					else if(b.equals(Buildable.Settlement)){
						if(buildVillage.getNode() != null){
							if(me.build(b, buildVillage.getNode())){
								buildSuccesful = true;
								break;
							} else {
								buildSuccesful = false;
							}
						}
					}
					else if(b.equals(Buildable.Road)){
						if(buildRoad.getEdge() != null){
							if(me.build(b, buildRoad.getEdge())){
								buildSuccesful = true;
								if(buildRoad.getBuildValue() > 30){
									//GameForTest.drawMap(buildRoad.getEdge().getBuilding(), buildRoad.getEdge());
								}
								break;
							} else {
								buildSuccesful = false;
							}
						}
					}
					else if(b.equals(Buildable.Development)){
						if(me.getResourceAmount(Resource.Grain) >= 1 &&
								me.getResourceAmount(Resource.Wool) >= 1 &&
								me.getResourceAmount(Resource.Ore) >= 1){
							if(buildDevelopment.getBuildValue() > 0 && me.buyDevCard()){
								buildSuccesful = true;
								//System.out.println("Successful development");
							} else {
								//System.out.println("Nulla az erteke a fejlesztesnek");
								buildSuccesful = false;
							}
						}
						else {
							buildSuccesful = false;
						}
					}
				}
				if(buildSuccesful){
					continue;
				}
				for(Buildable b : choices){
					String logmessage = "Ha ezt latod, akkor kuldd el GergelyO-nak pls!\n";
					HashMap<Resource, Integer> whatToChange = this.whatCanChange(b);
					if(whatToChange.isEmpty()){
						continue;
					}
					logmessage += ("Trying to build: " + b.toString() +"\n");
					ArrayList<Resource> neededRes = this.resourceMissing(b);
					logmessage += ("neededRes: " + neededRes.toString() + "\n");
					logmessage += ("whatToChange: " + whatToChange.toString() + "\n");
					logmessage += ("\tIn hand \t ChangeLUT\n");
					logmessage += ("Brick:\t" + me.getResourceAmount(Resource.Brick) + "\t\t" + me.getChangeLUT(Resource.Brick) + "\n");
					logmessage += ("Lumber:\t" + me.getResourceAmount(Resource.Lumber) + "\t\t" + me.getChangeLUT(Resource.Lumber) + "\n");
					logmessage += ("Grain:\t" + me.getResourceAmount(Resource.Grain) + "\t\t" + me.getChangeLUT(Resource.Grain) + "\n");
					logmessage += ("Wool:\t" + me.getResourceAmount(Resource.Wool) + "\t\t" + me.getChangeLUT(Resource.Wool) + "\n");
					logmessage += ("Ore:\t" + me.getResourceAmount(Resource.Ore) + "\t\t" + me.getChangeLUT(Resource.Ore) + "\n");
					logmessage += ("Egeszen idaig. Koszi :)\n");
					//System.out.println(logmessage);
					for(Map.Entry<Resource, Integer> item : whatToChange.entrySet()){
						while(item.getValue() > 0){
							//System.out.println("Change in ai needed " + neededRes.toString());
							//System.out.println("Change in ai to change " + whatToChange.toString());
							try{
								me.change(item.getKey(), neededRes.remove(0));
							} catch (IndexOutOfBoundsException e){
								//System.out.println("INDEXOUTOFBOUNDS!!!");
								//System.out.println(logmessage);
							}
							item.setValue(item.getValue() - me.getChangeLUT(item.getKey()));
						}
					}
					if(b.equals(Buildable.City)){
						if(buildCity.getNode() != null){		
							if(me.build(b, buildCity.getNode())){
								buildSuccesful = true;
								//System.out.println("Build succesful with change: " + b.toString());
								break;
							} else {
								buildSuccesful = false;
							}
						}
					}
					else if(b.equals(Buildable.Settlement)){
						if(buildVillage.getNode() != null){
							if(me.build(b, buildVillage.getNode())){
								buildSuccesful = true;
								//System.out.println("Build succesful with change: " + b.toString());
								break;
							} else {
								buildSuccesful = false;
							}
						}
					}
					else if(b.equals(Buildable.Road)){
						if(buildRoad.getEdge() != null){
							if(me.build(b, buildRoad.getEdge())){
								buildSuccesful = true;
								//System.out.println("Build succesful with change: " + b.toString());
								break;
							} else {
								buildSuccesful = false;
							}
						}
					}
					else if(b.equals(Buildable.Development)){
						if(me.getResourceAmount(Resource.Grain) >= 1 &&
								me.getResourceAmount(Resource.Wool) >= 1 &&
								me.getResourceAmount(Resource.Ore) >= 1){
							if(buildDevelopment.getBuildValue() > 0 && me.buyDevCard()){
								buildSuccesful = true;
								//System.out.println("Successful development");
							} else {
								buildSuccesful = false;
							}
						} else{
							buildSuccesful = false;
						}
					}
				}
			}
			//System.out.println("Build ciklus ended");
		}
	}

	// az adott building epiteshez megadja, hogy melyik nyersanyagbol
	// mennyit kell bevaltani az epiteshez
	private HashMap<Resource, Integer> whatCanChange(Buildable what){
		// ezt adom vissza
		HashMap<Resource, Integer> result = new HashMap<Resource, Integer>();

		// ez az, ami az adott building epiteshez mar megvan nyersanyag
		HashMap<Resource, Integer> ownedResources = new HashMap<Resource, Integer>();

		// ez a maradek nyersanyag, ezeket lehet cserelni
		HashMap<Resource, Integer> changableResources = new HashMap<Resource, Integer>();

		changableResources.put(Resource.Brick, me.getResourceAmount(Resource.Brick));
		changableResources.put(Resource.Lumber, me.getResourceAmount(Resource.Lumber));
		changableResources.put(Resource.Grain, me.getResourceAmount(Resource.Grain));
		changableResources.put(Resource.Ore, me.getResourceAmount(Resource.Ore));
		changableResources.put(Resource.Wool, me.getResourceAmount(Resource.Wool));
		int neededResCnt = 0;
		if(what.equals(Buildable.Road)){
			neededResCnt = 2;
			// ha van teglank
			if(me.getResourceAmount(Resource.Brick) > 0){
				ownedResources.put(Resource.Brick, 1);
				changableResources.put(Resource.Brick, changableResources.get(Resource.Brick) - 1);
				neededResCnt--;
			}
			// ha van fank :-)
			if(me.getResourceAmount(Resource.Lumber) > 0){
				ownedResources.put(Resource.Lumber, 1);
				changableResources.put(Resource.Lumber, changableResources.get(Resource.Lumber) - 1);
				neededResCnt--;
			}
		} else if(what.equals(Buildable.Settlement)){
			neededResCnt = 4;
			// ha van teglank
			if(me.getResourceAmount(Resource.Brick) > 0){
				ownedResources.put(Resource.Brick, 1);
				changableResources.put(Resource.Brick, changableResources.get(Resource.Brick) - 1);
				neededResCnt--;
			}
			// ha van fank :-)
			if(me.getResourceAmount(Resource.Lumber) > 0){
				ownedResources.put(Resource.Lumber, 1);
				changableResources.put(Resource.Lumber, changableResources.get(Resource.Lumber) - 1);
				neededResCnt--;
			}
			// ha van gyapjunk
			if(me.getResourceAmount(Resource.Wool) > 0){
				ownedResources.put(Resource.Wool, 1);
				changableResources.put(Resource.Wool, changableResources.get(Resource.Wool) - 1);
				neededResCnt--;
			}
			// ha van buzank
			if(me.getResourceAmount(Resource.Grain) > 0){
				ownedResources.put(Resource.Grain, 1);
				changableResources.put(Resource.Grain, changableResources.get(Resource.Grain) - 1);
				neededResCnt--;
			}
		} else if(what.equals(Buildable.City)){
			neededResCnt = 5;
			// ha van ercunk
			if(me.getResourceAmount(Resource.Ore) > 0){
				if(me.getResourceAmount(Resource.Ore) == 1){
					ownedResources.put(Resource.Ore, 1);
					changableResources.put(Resource.Ore, 0);
					neededResCnt--;
				} else if(me.getResourceAmount(Resource.Ore) == 2){
					ownedResources.put(Resource.Ore, 2);
					changableResources.put(Resource.Ore, 0);
					neededResCnt = neededResCnt - 2;
				} else{
					ownedResources.put(Resource.Ore, 3);
					changableResources.put(Resource.Ore, changableResources.get(Resource.Ore) - 3);
					neededResCnt = neededResCnt - 3;
				}
			}
			// ha van buzank
			if(me.getResourceAmount(Resource.Grain) > 0){
				if(me.getResourceAmount(Resource.Grain) == 1){
					ownedResources.put(Resource.Grain, 1);
					changableResources.put(Resource.Grain, 0);
					neededResCnt--;
				} else{
					ownedResources.put(Resource.Grain, 2);
					changableResources.put(Resource.Grain, changableResources.get(Resource.Grain) - 2);
					neededResCnt = neededResCnt - 2;
				}
			}
		} else if(what.equals(Buildable.Development)){
			neededResCnt = 3;
			// ha van ercunk
			if(me.getResourceAmount(Resource.Ore) > 0){
				ownedResources.put(Resource.Ore, 1);
				changableResources.put(Resource.Ore, changableResources.get(Resource.Ore) - 1);
				neededResCnt--;
			}
			// ha van gyapjunk
			if(me.getResourceAmount(Resource.Wool) > 0){
				ownedResources.put(Resource.Wool, 1);
				changableResources.put(Resource.Wool, changableResources.get(Resource.Wool) - 1);
				neededResCnt--;
			}
			// ha van buzank
			if(me.getResourceAmount(Resource.Grain) > 0){
				ownedResources.put(Resource.Grain, 1);
				changableResources.put(Resource.Grain, changableResources.get(Resource.Grain) - 1);
				neededResCnt--;
			}
		} else {

		}
		HashMap<Resource, Integer> obtainableRes = this.resourcesThatCanBeChangedFromThisResourcePool(changableResources);
		int obtainableCnt = 0;
		for(int item : obtainableRes.values()){
			obtainableCnt += item;
		}
		if(neededResCnt > obtainableCnt){
			return result;
		}
		// to initialize result map
		for(Resource res : Resource.values()){
			result.put(res, 0);
		}
		for(Resource res : Resource.values()){
			while(neededResCnt > 0 && obtainableRes.get(res) > 0){
				result.put(res, 1 + result.get(res));
				obtainableRes.put(res, obtainableRes.get(res) - 1);
				--neededResCnt;
			}
			if(neededResCnt <= 0) {
				break;
			}
		}
		// to remove resources that are not needed
		for(Resource res : Resource.values()){
			result.remove(res, 0);
		}
		return result;
	}
	// ennyi nyersanyag hianyzik a building megepitesehez
	private ArrayList<Resource> resourceMissing(Buildable what){
		// ez az, ami az adott building megepitesehez mar megvan nyersanyag
		HashMap<Resource, Integer> ownedResources = new HashMap<Resource, Integer>();
		// ezek a nyersanyagok hianyoznak meg az epitesehez
		HashMap<Resource, Integer> neededResources = new HashMap<Resource, Integer>();
		if(what.equals(Buildable.Road)){
			// ennyibe kerul az ut
			neededResources.put(Resource.Brick, 1);
			neededResources.put(Resource.Lumber, 1);
			int neededRes = 0;
			// ha van teglank
			if(me.getResourceAmount(Resource.Brick) > 0){
				neededResources.remove(Resource.Brick);
			}
			// ha van fank :-)
			if(me.getResourceAmount(Resource.Lumber) > 0){
				neededResources.remove(Resource.Lumber);
			}

		} else if(what.equals(Buildable.Settlement)){
			// ennyibe kerul a telepules
			neededResources.put(Resource.Brick, 1);
			neededResources.put(Resource.Grain, 1);
			neededResources.put(Resource.Wool, 1);
			neededResources.put(Resource.Lumber, 1);
			int neededRes = 0;
			// ha van teglank
			if(me.getResourceAmount(Resource.Brick) > 0){
				neededResources.remove(Resource.Brick);
			}
			// ha van fank :-)
			if(me.getResourceAmount(Resource.Lumber) > 0){
				neededResources.remove(Resource.Lumber);
			}
			// ha van buzank
			if(me.getResourceAmount(Resource.Grain) > 0){
				neededResources.remove(Resource.Grain);
			}
			// ha van baranyunk
			if(me.getResourceAmount(Resource.Wool) > 0){
				neededResources.remove(Resource.Wool);
			}
		} else if(what.equals(Buildable.City)){
			// ebbe kerul a city;
			neededResources.put(Resource.Grain, 2);
			neededResources.put(Resource.Ore, 3);
			// ha van ercunk
			if(me.getResourceAmount(Resource.Ore) > 0){
				if(me.getResourceAmount(Resource.Ore) == 1){
					neededResources.put(Resource.Ore, 2);
				} else if(me.getResourceAmount(Resource.Ore) == 2){
					neededResources.put(Resource.Ore, 1);
				} else{
					neededResources.put(Resource.Ore, 0);
				}
			}
			// ha van buzank
			if(me.getResourceAmount(Resource.Grain) > 0){
				if(me.getResourceAmount(Resource.Grain) == 1){
					neededResources.put(Resource.Grain, 1);
				} else{
					neededResources.put(Resource.Grain, 0);
				}
			}
		} else if(what.equals(Buildable.Development)){
			// ennyibe kerul a fejlesztes
			neededResources.put(Resource.Grain, 1);
			neededResources.put(Resource.Wool, 1);
			neededResources.put(Resource.Ore, 1);
			int neededRes = 0;
			// ha van ercunk
			if(me.getResourceAmount(Resource.Ore) > 0){
				neededResources.remove(Resource.Ore);
			}
			// ha van buzank
			if(me.getResourceAmount(Resource.Grain) > 0){
				neededResources.remove(Resource.Grain);
			}
			// ha van baranyunk
			if(me.getResourceAmount(Resource.Wool) > 0){
				neededResources.remove(Resource.Wool);
			}
		} else {

		}
		ArrayList<Resource> result = new ArrayList<Resource>();
		for(Map.Entry<Resource, Integer> item : neededResources.entrySet()){
			for(int i = 0; i < item.getValue(); i++){
				result.add(item.getKey());
			}
		}
		return result;
	}

	private HashMap<Resource, Integer> resourcesThatCanBeChangedFromThisResourcePool(HashMap<Resource, Integer> pool){
		HashMap<Resource, Integer> result = new HashMap<Resource, Integer>();
		for(Map.Entry<Resource, Integer> item : pool.entrySet()){
			result.put(item.getKey(), item.getValue() / me.getChangeLUT(item.getKey()));
		}
		return result;
	}

	/**
	 * firstturn. The main calculator of the first moves.
	 * @author Hollo-Szabo Akos 
	 */
	@Override
	public void firstturn(){
		try {
			if(params.contains(AiParameter.Random)){
				firstTurnRandom();
			} else {
				Vertex villageNode = buildVillage.getNodeFirstTurn();
				me.firstBuild(Buildable.Settlement, villageNode);
				Edge edgeRoad = buildRoad.getEdgeFirstTurn(villageNode);
				me.firstBuild(Buildable.Road, edgeRoad);
				turnCount++;
				if(turnCount == 2)
					isFirstTurn = false;
			}
		} catch(GameEndsException gee){

		}
	}
	public boolean isNodeValid(Vertex v){
		ArrayList<Vertex> neighbors = v.getNeighbours();
		for(Vertex next : neighbors){
			if(next.getBuilding() != null)
				return false;
		}
		return v.getBuilding() == null;
	}
	public double nodePersonalValue(Vertex v){
		double sum = 0;
		ArrayList<Hex> terrytories = v.getNeighbourHexes();
		for(Hex x : terrytories){
			sum += territoryPersonalValue(x);
		}
		if(sum > 0){
			sumValueNode += sum;
			cntNode++;
			if(sum > maxValueNode)
				maxValueNode = sum;
			if(sum < minValueNode)
				minValueNode = sum;
		}
		return sum;
	}

	public double territoryPersonalValue(Hex h){
		double buildValue = 0;
		ArrayList<Hex> valid = map.getValidFields();
		if((h.getProsperity() > 0 || h.getPort() != null)){					
			if(h.getPort() == null){
				// ha nem kikoto
				double numberValue = 1;
				if(params.contains(AiParameter.NewRes) && !numbers.contains(h.getProsperity()))
					numberValue = 1.8;
				double resourceValue = 1;
				if(params.contains(AiParameter.NewRes) && this.resources.get(h.getResource()).personalFrequency() < (1.0 / 36.0)){
					resourceValue = 2.5;
				}
				buildValue = 0.3 * Material.frequencyLUT(h.getProsperity()) * resources.get(h.getResource()).personalValue() * numberValue * resourceValue;
			}
			else if(!isFirstTurn && params.contains(AiParameter.Port)) {
				// ha kikoto
				if(h.getPort().getRes() == null){
					buildValue = threeToOnePortPersonalValue();
				} else{
					if(me.getChangeLUT(h.getPort().getRes()) > h.getPort().getChangeNumber()){
						buildValue = 0.1 * resources.get(h.getPort().getRes()).personalValue() * resources.get(h.getPort().getRes()).personalFrequency();
					}
				}
			}
		}
		if(buildValue > 0){
			sumValueTerr += buildValue;
			cntTerr++;
			if(buildValue > maxValueTerr)
				maxValueTerr = buildValue;
			if(buildValue < minValueTerr)
				minValueTerr = buildValue;
		}
		return buildValue;
	}

	/**
	 * The value of settling next to a three-to-one port.
	 * (Experimenting)
	 * @return - personal value as double
	 */
	private double threeToOnePortPersonalValue(){
		double result = 0;
		for(Resource res : Resource.values()){
			if(me.getChangeLUT(res) > 3)
				result +=  resources.get(res).personalValue() * 0.008;
		}
		return result;
	}
	
	/**
	 * The first turn method of the random ai. Builds a village
	 * to a random node then builds a random road.
	 */
	private void firstTurnRandom(){
		ArrayList<Vertex> nodes = new ArrayList<>(map.getNodes());
		Collections.shuffle(nodes);
		for(Vertex v : nodes){
			if(me.isFirstBuildPossible(Buildable.Settlement, v)){
				try {
					me.firstBuild(Buildable.Settlement, v);
				} catch (GameEndsException e) {
				}
				break;
			}
		}
		ArrayList<Edge> edges = new ArrayList<>(map.getEdges());
		Collections.shuffle(edges);
		for(Edge e : edges){
			if(me.isFirstBuildPossible(Buildable.Road, e)){
				try {
					me.firstBuild(Buildable.Road, e);
				} catch (GameEndsException ex) {
				}
				break;
			}
		}
	}
	
	/**
	 * The turn method of the random ai. Plays all the devcards, 
	 * then creates a build list in random order, and tries to buy
	 * them in that order from hand. If successful, rebuilds the
	 * list and starts again. When it can't buy any, tries the same
	 * thing but with changing of resources.
	 * @throws GameEndsException
	 */
	private void turnRandom() throws GameEndsException{
		while(me.getDevCards().size() > 0){
			me.playDev(me.getDevCards().get(0), null);
			//System.out.println("Cardshop " + me.getId() + "  " + me.getDevCards().get(0).toString());
		}
		
		boolean buildSuccessful = true;

		ArrayList<Vertex> nodes = new ArrayList<>(map.getNodes());

		ArrayList<Edge> edges = new ArrayList<>(map.getEdges());

		ArrayList<Buildable> builds = new ArrayList<>();
		for(Buildable b : Buildable.values())
			builds.add(b);

		while(buildSuccessful){
			buildSuccessful = false;
			Collections.shuffle(builds);
			Collections.shuffle(nodes);
			Collections.shuffle(edges);
			for(Buildable build : builds){
				if(build.equals(Buildable.Settlement) || build.equals(Buildable.City)){
					for(Vertex v : nodes){
						if(me.isBuildPossible(build, v)){
							if(me.build(build, v)){
								buildSuccessful = true;
								Collections.shuffle(nodes);
								break;
							}
						}
					}
				} else if(build.equals(Buildable.Road)){
					for(Edge e : edges){
						if(me.isBuildPossible(build, e)){
							if(me.build(build, e)){
								buildSuccessful = true;
								Collections.shuffle(edges);
								break;
							}
						}
					}
				} else {
					if(me.getResourceAmount(Resource.Grain) >= 1 &&
							me.getResourceAmount(Resource.Wool) >= 1 &&
							me.getResourceAmount(Resource.Ore) >= 1){
						if(me.buyDevCard()){
							buildSuccessful = true;
							//System.out.println("Successful development");
						}
					}
				}
			}
			if(buildSuccessful)
				continue;
			Collections.shuffle(builds);
			Collections.shuffle(nodes);
			Collections.shuffle(edges);
			for(Buildable build : builds){
				String logmessage = "Ha ezt latod, akkor kuldd el GergelyO-nak pls!\n";
				HashMap<Resource, Integer> whatToChange = this.whatCanChange(build);
				if(whatToChange.isEmpty()){
					continue;
				}
				//logmessage += ("Trying to build: " + b.toString() +"\n");
				ArrayList<Resource> neededRes = this.resourceMissing(build);
				/*logmessage += ("neededRes: " + neededRes.toString() + "\n");
				logmessage += ("whatToChange: " + whatToChange.toString() + "\n");
				logmessage += ("\tIn hand \t ChangeLUT\n");
				logmessage += ("Brick:\t" + me.getResourceAmount(Resource.Brick) + "\t\t" + me.getChangeLUT(Resource.Brick) + "\n");
				logmessage += ("Lumber:\t" + me.getResourceAmount(Resource.Lumber) + "\t\t" + me.getChangeLUT(Resource.Lumber) + "\n");
				logmessage += ("Grain:\t" + me.getResourceAmount(Resource.Grain) + "\t\t" + me.getChangeLUT(Resource.Grain) + "\n");
				logmessage += ("Wool:\t" + me.getResourceAmount(Resource.Wool) + "\t\t" + me.getChangeLUT(Resource.Wool) + "\n");
				logmessage += ("Ore:\t" + me.getResourceAmount(Resource.Ore) + "\t\t" + me.getChangeLUT(Resource.Ore) + "\n");
				logmessage += ("Egeszen idaig. Koszi :)\n");*/
				//System.out.println(logmessage);
				for(Map.Entry<Resource, Integer> item : whatToChange.entrySet()){
					while(item.getValue() > 0){
						//System.out.println("Change in ai needed " + neededRes.toString());
						//System.out.println("Change in ai to change " + whatToChange.toString());
						try{
							me.change(item.getKey(), neededRes.remove(0));
						} catch (IndexOutOfBoundsException e){
							//System.out.println("INDEXOUTOFBOUNDS!!!");
							//System.out.println(logmessage);
						}
						item.setValue(item.getValue() - me.getChangeLUT(item.getKey()));
					}
				}
				if(build.equals(Buildable.Settlement) || build.equals(Buildable.City)){
					for(Vertex v : nodes){
						if(me.isBuildPossible(build, v)){
							if(me.build(build, v)){
								buildSuccessful = true;
								Collections.shuffle(nodes);
								break;
							}
						}
					}
				} else if(build.equals(Buildable.Road)){
					for(Edge e : edges){
						if(me.isBuildPossible(build, e)){
							if(me.build(build, e)){
								buildSuccessful = true;
								Collections.shuffle(edges);
								break;
							}
						}
					}
				} else {
					if(me.getResourceAmount(Resource.Grain) >= 1 &&
							me.getResourceAmount(Resource.Wool) >= 1 &&
							me.getResourceAmount(Resource.Ore) >= 1){
						if(me.buyDevCard()) {							
							buildSuccessful = true;
							//System.out.println("Successful development");
						}
					}
				}
			}

		}
	}
	public void printStatus(){
		System.out.println("Materials:");
		for(Map.Entry<Resource, Material> item : resources.entrySet()){
			System.out.println("\t" + item.getKey() + "\t" + item.getValue().personalValue());
		}
		/*
		HashSet<Hex> ports = new HashSet<Hex>();
		for(Vertex v : map.getNodes()){
			for(Hex h : v.getNeighbourHexes()){
				if(h.getPort() != null)
					ports.add(h);
			}
		}
		System.out.println("Fields:");
		for(Hex h : map.getFields()){
			System.out.println("\t" + h.getResource() + "\t" + h.getProsperity() + "\t" + territoryPersonalValue(h));
		}
		System.out.println("Ports:");
		for(Hex h : ports){
			System.out.println("\t" + h.getPort().getRes() + "\t" + h.getPort().getChangeNumber() + "\t" + territoryPersonalValue(h));
		}*/
		
		System.out.println("Nodes:");
		for(Vertex v : map.getNodes()){
			System.out.println(v.getID() + "\t\t" + nodePersonalValue(v) + "\troad: " + buildRoad.nodePersonalValueForRoad(v, null));
			for(Hex h : v.getNeighbourHexes()){
				if(h.getPort() == null)
					System.out.println("\t(field)\t" + h.getResource() + "\t" + h.getProsperity() + "\t" + territoryPersonalValue(h));
				else
					System.out.println("\t(port)\t" + h.getPort().getRes() + "\t" + h.getPort().getChangeNumber() + "\t" + territoryPersonalValue(h));
			}
			System.out.println("");
		}
	}
}

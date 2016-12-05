package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import controller.map.Buildable;
import controller.map.Edge;
import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.GameEndsException;
import controller.player.Player;
import controller.player.PlayerController;
import controller.player.Resource;

public class AiController extends PlayerController {

	private Player me;
	ArrayList<Player> players = new ArrayList<Player>();
	private Table map;
	private int robberSum;

	private BuildCity buildCity;
	private BuildVillage buildVillage;
	private BuildRoad buildRoad;
	private BuildDevelopment buildDevelopment;

	private Set<Integer> numbers = new HashSet<Integer>();
	private Map<Resource, Material> resources = new HashMap<Resource, Material>();
	private Map<Resource, Integer> rAmount = new HashMap<Resource, Integer>();
	private Map<Resource, Integer> rLut = new HashMap<Resource, Integer>();
	
	/**
	 * Constructor. Initializes attributes.
	 * @param t - the table
	 * @param p - the player's datas and control possibilities
	 * @param otherPlayers - the ather players data's
	 * @author @author Hollo-Szabo Akos
	 */
	public AiController(Table t, Player p, ArrayList<Player> otherPlayers) {
		map = t;
		me = p;
		players.addAll(otherPlayers);
		robberSum = 0;
		for(Resource r:Resource.values())
			resources.put(r,new Material(map,me,r));
		// these should be here, so they don't initialize with null pointers
		buildCity = new BuildCity(t, this, p, otherPlayers);
		buildVillage = new BuildVillage(t, this, p, otherPlayers);
		buildRoad = new BuildRoad(t, this, p, otherPlayers);
		buildDevelopment = new BuildDevelopment(t, this, p, otherPlayers);
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
	 * query. Calculates:Should we accept the offer?
	 * @param donor - The donor.
	 * @param offer - Map of the offered resources.
	 * @param demand - The demand in change.
	 * @author Hollo-Szabo Akos
	 */
	public boolean query(Player donor, Map<Resource, Integer> offer, Map<Resource, Integer> demand) {
		double demValue=0;
		double ofValue=0;
		double materia;
		for(Resource r:Resource.values()){
			materia=resources.get(r).personalValue();
			demValue+=(materia*demand.get(r));
			ofValue+=(materia*offer.get(r));
		}
		if(ofValue>demValue)
			return true;
		else
			return false;
	}

		/**
	 * turn. The main calculator of future planes, and the center of control.
	 * @author Hollo-Szabo Akos
	 */
	public void turn() throws GameEndsException{
		me.rollTheDice();
		boolean buildSuccesful = true;
		while(buildSuccesful){
			buildSuccesful = false;
			ArrayList<Buildable> choices = new ArrayList<Buildable>();
			choices.add(Buildable.Settlement);
			choices.add(Buildable.Road);
			choices.add(Buildable.City);
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
							break;
						} else {
							buildSuccesful = false;
						}
					}
				}
			}
			if(buildSuccesful){
				continue;
			}
			for(Buildable b : choices){
				HashMap<Resource, Integer> whatToChange = this.whatCanChange(b);
				if(whatToChange.isEmpty()){
					continue;
				}
				ArrayList<Resource> neededRes = this.resourceMissing(b);
				for(Map.Entry<Resource, Integer> item : whatToChange.entrySet()){
					while(item.getValue() > 0){
						System.out.println("Change in ai needed " + neededRes.toString());
						System.out.println("Change in ai to change " + whatToChange.toString());
						me.change(item.getKey(), neededRes.remove(0));
						item.setValue(item.getValue() - me.getChangeLUT(item.getKey()));
					}
				}
				if(b.equals(Buildable.City)){
					if(buildCity.getNode() != null){		
						if(me.build(b, buildCity.getNode())){
							buildSuccesful = true;
							System.out.println("Build succesful with change: " + b.toString());
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
							System.out.println("Build succesful with change: " + b.toString());
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
							System.out.println("Build succesful with change: " + b.toString());
							break;
						} else {
							buildSuccesful = false;
						}
					}
				}
			}
		}
		
	}
	
	// az adott building �p�t�s�hez megadja, hogy melyik nyersanyagb�l
	// mennyit kell bev�ltani az �p�t�shez
	private HashMap<Resource, Integer> whatCanChange(Buildable what){
		// ezt adom vissza
		HashMap<Resource, Integer> result = new HashMap<Resource, Integer>();
		// ez az, ami az adott building �p�t�s�hez m�r megvan nyersanyag
		HashMap<Resource, Integer> ownedResources = new HashMap<Resource, Integer>();
		// ez a marad�k nyersanyag, ezeket lehet cser�lni
		HashMap<Resource, Integer> changableResources = new HashMap<Resource, Integer>();
		// ezek a nyersanyagok hi�nyoznak m�g az �p�t�shez
		HashMap<Resource, Integer> neededResources = new HashMap<Resource, Integer>();
		changableResources.put(Resource.Brick, me.getResourceAmount(Resource.Brick));
		changableResources.put(Resource.Lumber, me.getResourceAmount(Resource.Lumber));
		changableResources.put(Resource.Grain, me.getResourceAmount(Resource.Grain));
		changableResources.put(Resource.Ore, me.getResourceAmount(Resource.Ore));
		changableResources.put(Resource.Wool, me.getResourceAmount(Resource.Wool));
		int neededResCnt = 0;
		if(what.equals(Buildable.Road)){
			// ebbe ker�l az �t
			neededResources.put(Resource.Brick, 1);
			neededResources.put(Resource.Lumber, 1);
			neededResCnt = 2;
			// ha van t�gl�nk
			if(me.getResourceAmount(Resource.Brick) > 0){
				ownedResources.put(Resource.Brick, 1);
				changableResources.put(Resource.Brick, changableResources.get(Resource.Brick) - 1);
				neededResCnt--;
			}
			// ha van f�nk :-)
			if(me.getResourceAmount(Resource.Lumber) > 0){
				ownedResources.put(Resource.Lumber, 1);
				changableResources.put(Resource.Lumber, changableResources.get(Resource.Lumber) - 1);
				neededResCnt--;
			}
		} else if(what.equals(Buildable.Settlement)){
			// ebbe ker�l a telep�l�s
			neededResources.put(Resource.Brick, 1);
			neededResources.put(Resource.Grain, 1);
			neededResources.put(Resource.Wool, 1);
			neededResources.put(Resource.Lumber, 1);
			neededResCnt = 4;
			// ha van t�gl�nk
			if(me.getResourceAmount(Resource.Brick) > 0){
				ownedResources.put(Resource.Brick, 1);
				changableResources.put(Resource.Brick, changableResources.get(Resource.Brick) - 1);
				neededResCnt--;
			}
			// ha van f�nk :-)
			if(me.getResourceAmount(Resource.Lumber) > 0){
				ownedResources.put(Resource.Lumber, 1);
				changableResources.put(Resource.Lumber, changableResources.get(Resource.Lumber) - 1);
				neededResCnt--;
			}
			// ha van gyapj�nk
			if(me.getResourceAmount(Resource.Wool) > 0){
				ownedResources.put(Resource.Wool, 1);
				changableResources.put(Resource.Wool, changableResources.get(Resource.Wool) - 1);
				neededResCnt--;
			}
			// ha van b�z�nk
			if(me.getResourceAmount(Resource.Grain) > 0){
				ownedResources.put(Resource.Grain, 1);
				changableResources.put(Resource.Grain, changableResources.get(Resource.Grain) - 1);
				neededResCnt--;
			}
		} else if(what.equals(Buildable.City)){
			// ebbe ker�l a city;
			neededResources.put(Resource.Grain, 2);
			neededResources.put(Resource.Ore, 3);
			neededResCnt = 5;
			// ha van �rc�nk
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
			// ha van b�z�nk
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
			// ebbe ker�l a fejleszt�s
			neededResources.put(Resource.Grain, 1);
			neededResources.put(Resource.Wool, 1);
			neededResources.put(Resource.Ore, 1);
			neededResCnt = 3;
			// ha van �rc�nk
			if(me.getResourceAmount(Resource.Ore) > 0){
				ownedResources.put(Resource.Ore, 1);
				changableResources.put(Resource.Ore, changableResources.get(Resource.Ore) - 1);
				neededResCnt--;
			}
			// ha van gyapj�nk
			if(me.getResourceAmount(Resource.Wool) > 0){
				ownedResources.put(Resource.Wool, 1);
				changableResources.put(Resource.Wool, changableResources.get(Resource.Wool) - 1);
				neededResCnt--;
			}
			// ha van b�z�nk
			if(me.getResourceAmount(Resource.Grain) > 0){
				ownedResources.put(Resource.Grain, 1);
				changableResources.put(Resource.Grain, changableResources.get(Resource.Grain) - 1);
				neededResCnt--;
			}
		} else {
			
		}
		HashMap<Resource, Integer> obtainableRes = this.resourcesThatCanBeChangedFromThisResourcePool(changableResources);
		if(neededResCnt > obtainableRes.size()){
			return result;
		}
		for(Map.Entry<Resource, Integer> item : obtainableRes.entrySet()){
			while(neededResCnt > 0 && item.getValue() > 0){
				result.put(item.getKey(), item.getValue());
				item.setValue(item.getValue() - 1);
				--neededResCnt;
			}
			if(neededResCnt <= 0) {
				return result;
			}
		}
		return result;
	}
	// ennyi nyersanyag hi�nyzik a building meg�p�t�s�hez
	private ArrayList<Resource> resourceMissing(Buildable what){
		// ez az, ami az adott building �p�t�s�hez m�r megvan nyersanyag
		HashMap<Resource, Integer> ownedResources = new HashMap<Resource, Integer>();
		// ezek a nyersanyagok hi�nyoznak m�g az �p�t�shez
		HashMap<Resource, Integer> neededResources = new HashMap<Resource, Integer>();
		if(what.equals(Buildable.Road)){
			// ennyibe ker�l az �t
			neededResources.put(Resource.Brick, 1);
			neededResources.put(Resource.Lumber, 1);
			int neededRes = 0;
			// ha van t�gl�nk
			if(me.getResourceAmount(Resource.Brick) > 0){
				neededResources.remove(Resource.Brick);
			}
			// ha van f�nk :-)
			if(me.getResourceAmount(Resource.Lumber) > 0){
				neededResources.remove(Resource.Lumber);
			}
			
		} else if(what.equals(Buildable.Settlement)){
			// ennyibe ker�l a telep�l�s
			neededResources.put(Resource.Brick, 1);
			neededResources.put(Resource.Grain, 1);
			neededResources.put(Resource.Wool, 1);
			neededResources.put(Resource.Lumber, 1);
			int neededRes = 0;
			// ha van t�gl�nk
			if(me.getResourceAmount(Resource.Brick) > 0){
				neededResources.remove(Resource.Brick);
			}
			// ha van f�nk :-)
			if(me.getResourceAmount(Resource.Lumber) > 0){
				neededResources.remove(Resource.Lumber);
			}
			// ha van b�z�nk
			if(me.getResourceAmount(Resource.Grain) > 0){
				neededResources.remove(Resource.Grain);
			}
			// ha van b�r�nyunk
			if(me.getResourceAmount(Resource.Wool) > 0){
				neededResources.remove(Resource.Wool);
			}
		} else if(what.equals(Buildable.City)){
			// ebbe ker�l a city;
			neededResources.put(Resource.Grain, 2);
			neededResources.put(Resource.Ore, 3);
			// ha van �rc�nk
			if(me.getResourceAmount(Resource.Ore) > 0){
				if(me.getResourceAmount(Resource.Ore) == 1){
					neededResources.put(Resource.Ore, 2);
				} else if(me.getResourceAmount(Resource.Ore) == 2){
					neededResources.put(Resource.Ore, 1);
				} else{
					neededResources.put(Resource.Ore, 0);
				}
			}
			// ha van b�z�nk
			if(me.getResourceAmount(Resource.Grain) > 0){
				if(me.getResourceAmount(Resource.Grain) == 1){
					neededResources.put(Resource.Grain, 1);
				} else{
					neededResources.put(Resource.Grain, 0);
				}
			}
		} else if(what.equals(Buildable.Development)){
			// ennyibe ker�l a fejleszt�s
			neededResources.put(Resource.Grain, 1);
			neededResources.put(Resource.Wool, 1);
			neededResources.put(Resource.Ore, 1);
			int neededRes = 0;
			// ha van �rc�nk
			if(me.getResourceAmount(Resource.Ore) > 0){
				neededResources.remove(Resource.Ore);
			}
			// ha van b�z�nk
			if(me.getResourceAmount(Resource.Grain) > 0){
				neededResources.remove(Resource.Grain);
			}
			// ha van b�r�nyunk
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
			List<Double> Values=new ArrayList<Double>();
			List<Vertex> settlementPlace=map.getNodes();
			Vertex placeofSettlement=new Vertex(null);
			List<Hex> sourceOfNewNumbers=new ArrayList<Hex>();
			List<Edge> roadPlaces=new ArrayList<Edge>();
			List<Vertex> settlementPlaceNN=new ArrayList<Vertex>();
			List<Edge> edgesinNeighbour=new ArrayList<Edge>();
			Vertex villageNode = buildVillage.getNodeFirstTurn();
			me.firstBuild(Buildable.Settlement, villageNode);
			Edge edgeRoad = buildRoad.getEdgeFirstTurn(villageNode);
			me.firstBuild(Buildable.Road, edgeRoad);
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
		return sum;
	}
	
	public double territoryPersonalValue(Hex h){
		ArrayList<Hex> valid = map.getValidFields();
		if(valid.contains(h)){
			double numberValue = 1;
			if(!numbers.contains(h.getProsperity()))
				numberValue = 2;
			double resourceValue = 1;
			if(this.resources.get(h.getResource()).personalFrequency() < (1.0 / 36.0))
				resourceValue = 3;
			return Material.frequencyLUT(h.getProsperity()) * resources.get(h.getResource()).personalValue() * numberValue * resourceValue;
		}
		return 0;
	}

}

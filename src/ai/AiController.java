package ai;

import java.util.*;
import controller.map.*;
import controller.player.*;

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
	 * @author Holló-Szabó Ákos
	 */
	public AiController(Table t, Player p, ArrayList<Player> otherPlayers) {
		map = t;
		me = p;
		players.addAll(otherPlayers);
		robberSum = 0;
		// these should be here, so they don't initialize with null pointers
		buildCity = new BuildCity(t, this, p, otherPlayers);
		buildVillage = new BuildVillage(t, this, p, otherPlayers);
		buildRoad = new BuildRoad(t, this, p, otherPlayers);
		buildDevelopment = new BuildDevelopment(t, this, p, otherPlayers);
	}

	/**
	 * getKnightDiff. Calculates:How money KnightCard do you need to get the 2 pints?
	 * @author Holló-Szabó Ákos
	 */
	public int getKnightDiff() {
		Map<Player, Integer> amountoKnightsperPlayer = new HashMap<Player, Integer>();
		int max = 3;

		for (Player p : players) {
			amountoKnightsperPlayer.put(p, p.getActiveKnights());
			if (amountoKnightsperPlayer.get(p) > max && !p.equals(me))
				max = amountoKnightsperPlayer.get(p);
		}

		if (amountoKnightsperPlayer.get(me) <= max)
			return max - amountoKnightsperPlayer.get(me);
		else
			return -1;
	}

	/**
	 * isNodeBuildable. Calculates:Can we build there a Village?
	 * @param vIn - The node for check.
	 * @author Holló-Szabó Ákos
	 */
	public Boolean isNodeBuildable(Vertex vIn) {
		if (!vIn.isBuildPossible(new Settlement(me)))
			return false;

		List<Vertex> neighbours = new ArrayList<Vertex>();
		List<Vertex> neighboursoNeioghbours = new ArrayList<Vertex>();
		List<Edge> edges = new ArrayList<Edge>();

		neighbours = vIn.getNeighbours();
		for (Vertex v : neighbours)
			neighboursoNeioghbours.addAll(v.getNeighbours());

		for (int i = 0; i < neighboursoNeioghbours.size(); i++) {
			if (neighbours.contains(neighboursoNeioghbours.get(i)) || neighboursoNeioghbours.get(i).equals(vIn)) {
				neighboursoNeioghbours.remove(i);
				i--;
			}
		}

		for (Vertex v : neighboursoNeioghbours)
			if (!v.getBuilding().equals(null))
				return false;

		edges = vIn.getNeighbourEdges();
		for (Edge e : edges)
			if (e.getRoad() != null && e.getRoad().getOwner().equals(me))
				return true;

		return false;
	}

	/**
	 * isRobbed. Calculates:Is the robber on a field next to one of our town?
	 * @author Holló-Szabó Ákos
	 */
	public Boolean isRobbed() {
		List<Vertex> all = new ArrayList<Vertex>();
		List<Hex> neighbours = new ArrayList<Hex>();
		all.addAll(map.getNodes());

		for (Vertex v : all) {
			if (!v.getBuilding().equals(null) && v.getBuilding().getOwner().equals(me)) {
				neighbours = v.getNeighbourHexes();
				/*
				 * for(Hex h:neighbours){ if(map.getRobberPossision().equals(h))
				 * return true; }
				 */}
		}
		return false;
	}

	/**
	 * nodePersonalDistance. Calculates:How far is an exact node?
	 * @param vIn - The node for check.
	 * @author Holló-Szabó Ákos
	 */
	public int nodePersonalDistance(Vertex vIn) {
		List<Edge> edges = map.getEdges();
		List<Vertex> myVertexs = new ArrayList<Vertex>();
		List<Vertex> VertexsNeighbours1 = new ArrayList<Vertex>();
		List<Vertex> VertexsNeighbours2 = new ArrayList<Vertex>();

		for (Edge e : edges)
			if (!e.getRoad().equals(null) && e.getRoad().getOwner().equals(me))
				myVertexs.addAll(e.getEnds());
		if (myVertexs.contains(vIn))
			return 0;
		for (Vertex v : myVertexs)
			VertexsNeighbours1.addAll(v.getNeighbours());
		if (VertexsNeighbours1.contains(vIn))
			return 1;
		int i = 1;
		while (i < 20) {
			for (Vertex v : VertexsNeighbours1)
				VertexsNeighbours2.addAll(v.getNeighbours());
			if (VertexsNeighbours2.contains(vIn))
				return ++i;
			for (Vertex v : VertexsNeighbours2)
				VertexsNeighbours1.addAll(v.getNeighbours());
			if (VertexsNeighbours1.contains(vIn))
				return ++i;
		}
		return -1;
	}

	/**
	 * getResources. Get:the map of Metirials
	 * @author Holló-Szabó Ákos
	 */
	public Map<Resource, Material> getResources() {
		return resources;
	}

	/**
	 * query. Calculates:Should we accept the offer?
	 * @param donor - The donor.
	 * @param offer - Map of the offered resources.
	 * @param demand - The demand in change.
	 * @author Holló-Szabó Ákos
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
	 * AllNeededDataForTurn. Class:To assist the complicate turn()
	 * @author Holló-Szabó Ákos
	 */
	private class AllNeededDataForTurn {
		public Map<Resource, Integer> seged = new HashMap<Resource, Integer>();

		/**
		 * Value1. Class:To make simple the use of setPossibilities
		 * @author Holló-Szabó Ákos
		 */
		private class Value1 {
			public Map<Resource, Integer> hand;
			public List<List<Resource>> how;

			public Value1(Map<Resource, Integer> ha, List<List<Resource>> ho) {
				hand = ha;
				how = ho;
			}

			public Value1(Value1 v) {
				hand = new HashMap<Resource, Integer>();
				hand.putAll(v.hand);
				how = new ArrayList<List<Resource>>();
				how.addAll(v.how);
			}

			public void add(List<Resource> change) {
				how.add(change);
			}
		}

		public Map<Integer, Value1> possibilities = new HashMap<Integer, Value1>();

		public Map<Buildable, Map<Resource, Integer>> resourceNeed = new HashMap<Buildable, Map<Resource, Integer>>();

		public Map<Buildable, Double> options = new HashMap<Buildable, Double>();
		public List<Buildable> bList = new ArrayList<Buildable>();
		List<Double> values = new ArrayList<Double>();

		public Map<Integer, List<Buildable>> toDoList = new HashMap<Integer, List<Buildable>>();
		public List<Buildable> buildPlan = new ArrayList<Buildable>();

		public Integer buildPlanMax = 0;

		public Map<Buildable, Map<Resource, Integer>> needToBuild = new HashMap<Buildable, Map<Resource, Integer>>();

		/**
		 * Constructor. Initialize main datarelations
		 */
		public AllNeededDataForTurn() {
			for (Resource r : Resource.values()) {
				rAmount.put(r, me.getResourceAmount(r));
				System.out.println(me.getChangeLUT(r));
				rLut.put(r, me.getChangeLUT(r));
				seged.put(r, rAmount.get(r));
				// mPValue.put(r, resources.get(r).personalValue());
			}

			seged = new HashMap<Resource, Integer>();
			seged.put(Resource.Brick, 1);
			seged.put(Resource.Lumber, 1);
			seged.put(Resource.Ore, 0);
			seged.put(Resource.Grain, 1);
			seged.put(Resource.Wool, 1);
			resourceNeed.put(Buildable.Village, seged);
			seged = new HashMap<Resource, Integer>();
			seged.put(Resource.Brick, 0);
			seged.put(Resource.Lumber, 0);
			seged.put(Resource.Ore, 3);
			seged.put(Resource.Grain, 2);
			seged.put(Resource.Wool, 0);
			resourceNeed.put(Buildable.City, seged);
			seged = new HashMap<Resource, Integer>();
			seged.put(Resource.Brick, 0);
			seged.put(Resource.Lumber, 0);
			seged.put(Resource.Ore, 0);
			seged.put(Resource.Grain, 1);
			seged.put(Resource.Wool, 1);
			resourceNeed.put(Buildable.Development, seged);
			seged = new HashMap<Resource, Integer>();
			seged.put(Resource.Brick, 1);
			seged.put(Resource.Lumber, 1);
			seged.put(Resource.Ore, 0);
			seged.put(Resource.Grain, 0);
			seged.put(Resource.Wool, 0);
			resourceNeed.put(Buildable.Road, seged);
		}
		
		/**
		 * Refresh. Resets main datarelations
		 * @author Holló-Szabó Ákos
		 */
		public void Refresh() {
			for (Resource r : Resource.values()) {
				rAmount.put(r, me.getResourceAmount(r));
				rLut.put(r, me.getChangeLUT(r));
				seged.put(r, rAmount.get(r));
			}
			possibilities = new HashMap<Integer, Value1>();
			options = new HashMap<Buildable, Double>();
			bList = new ArrayList<Buildable>();
			values = new ArrayList<Double>();
			toDoList = new HashMap<Integer, List<Buildable>>();
			buildPlan = new ArrayList<Buildable>();
			needToBuild = new HashMap<Buildable, Map<Resource, Integer>>();
		}
		
		/**
		 * setPossibilities. Calculates:What Hand can we reach and how?
		 * @author Holló-Szabó Ákos
		 */
		public void setPossibilities() {
			Value1 data = new Value1(rAmount, new ArrayList<List<Resource>>());
			int i = 0;
			for (Resource r : Resource.values())
				i = i * 100 + rAmount.get(r);
			possibilities.put(i, data);

			for (Resource forChange : Resource.values()) {
				for (Resource getAble : Resource.values()) {
					if (forChange != getAble)
						setPossibilitiesRec(data, forChange, getAble);
				}
			}
		}
		
		/**
		 * setPossibilitiesRec. Assists: setPossibilities
		 * @author Holló-Szabó Ákos
		 */
		private void setPossibilitiesRec(Value1 data, Resource forChange, Resource getAble) {
			if (data.hand.get(forChange) < rLut.get(forChange))
				return;

			Value1 newData = new Value1(data);
			List<Resource> change = new ArrayList<Resource>();
			change.add(forChange);
			change.add(getAble);
			newData.add(change);

			newData.hand.replace(forChange, newData.hand.get(forChange) - rLut.get(forChange));
			newData.hand.replace(getAble, newData.hand.get(getAble) + 1);

			int i = 0;
			for (Resource r : Resource.values())
				i = i * 100 + newData.hand.get(r);

			possibilities.put(i, newData);

			for (Resource forChange2 : Resource.values()) {
				for (Resource getAble2 : Resource.values()) {
					if (forChange2 != getAble2)
						setPossibilitiesRec(newData, forChange2, getAble2);
				}
			}
		}
		
		/**
		 * setBList. Calculates: What is the order of buildinges by value?
		 * @author Holló-Szabó Ákos
		 */
		public void setBList() {

			values.add(buildVillage.getBuildValue());
			options.put(Buildable.Village, values.get(0));
			values.add(buildCity.getBuildValue());
			options.put(Buildable.City, values.get(1));
			values.add(buildRoad.getBuildValue());
			options.put(Buildable.Road, values.get(2));
			values.add(buildDevelopment.getBuildValue());
			options.put(Buildable.Development, values.get(3));
			values.add(0.0);
			options.put(Buildable.None, values.get(4));

			Collections.sort(values, new ComperatorD());
			for (Double d : values)
				for (Buildable b : Buildable.values()) {
					if (d == options.get(b))
						bList.add(b);
				}
		}
		
		/**
		 * setToDoList. Calculates: What could we build from an factual hand?
		 * @author Holló-Szabó Ákos
		 */
		public void setToDoList() {
			int i = 2;
			int j = 0;
			boolean enough = true;
			for (Integer key : possibilities.keySet()) {
				seged = new HashMap<Resource, Integer>();
				seged.putAll(possibilities.get(key).hand);
				while (j < 4 && i > 0) {
					for (Buildable b : Buildable.values()) {
						if (bList.get(j).equals(b)) {
							for (Resource r : Resource.values()) {
								if (seged.get(r) < resourceNeed.get(b).get(r))
									enough = false;
							}
							if (enough == true) {
								for (Resource r : Resource.values()) {
									seged.replace(r, seged.get(r) - resourceNeed.get(b).get(r));
									buildPlan.add(b);
								}
								i--;
							} else
								j++;
						}
					}
					enough = true;
				}
				while (i > 0) {
					buildPlan.add(Buildable.None);
					i--;
				}

				toDoList.put(key, buildPlan);
				buildPlan = new ArrayList<Buildable>();
				j = 0;
				i = 2;
			}
		}
		
		/**
		 * findMaxToDoList. Calculates: Which hand is the most advantageous?
		 * @author Holló-Szabó Ákos
		 */
		public boolean findMaxToDoList() {
			boolean found = false;
			for (int i = 0; i < 4; i++)
				for (int j = i; j < 4; j++) {
					for (Integer key : toDoList.keySet())
						if (toDoList.get(key).get(0).equals(bList.get(i)) && toDoList.get(key).get(1).equals(bList.get(j))
								&& found == false) {
							buildPlanMax = key;
							found = true;
						}
				}
			if (toDoList.get(buildPlanMax).get(0).equals(Buildable.None))
				return false;
			else
				return true;
		}
		
		/**
		 * inChange. Calculates,does: What change do we have to do by the bank to get the most advantageous hand?
		 * @author Holló-Szabó Ákos
		 */
		public void inChange() {
			Value1 job = possibilities.get(buildPlanMax);
			buildPlan = toDoList.get(buildPlanMax);
			for (Integer key : possibilities.keySet())
				if (possibilities.get(key).how.size() < job.how.size() && toDoList.get(key).get(0).equals(buildPlan.get(0))
						&& toDoList.get(key).get(1).equals(buildPlan.get(1))) {
					job = possibilities.get(key);
					buildPlan = toDoList.get(key);
					buildPlanMax = key;
				}

			for (List<Resource> lr : job.how)
				me.change(lr.get(0), lr.get(1));
		}
		
		/**
		 * letsBuild. Calculates,does: What should we build?
		 * @author Holló-Szabó Ákos
		 */
		public boolean letsBuild() {
			Building toBuild;
			TableElement t;
			Vertex tf;
			Vertex tt;
			List<Edge> roadWhere=map.getEdges();
			List<Vertex> roadWhereSeged;
			boolean outOfBuildable = true;

				switch (buildPlan.get(0)) {
				case Village:
					toBuild = new Settlement(me);
					t = buildVillage.getNode();
					try {
						me.build(toBuild, t);
					} catch (GameEndsException e1) {
					}
					break;
				case City:
					toBuild = new City(me);
					t = buildCity.getNode();
					try {
						me.build(toBuild, t);
					} catch (GameEndsException e1) {
					}
					break;
				case Development:
					try {
						me.buyDevCard();
					} catch (NotEnoughResourcesException e) {
					}
					break;
				case Road:
					toBuild = new Road(me);
					tf = buildRoad.getNodeFrom();
					tt = buildRoad.getNodeTo();
					t=roadWhere.get(0);
					for(Edge e:roadWhere){
						roadWhereSeged=e.getEnds();
						if(roadWhereSeged.contains(tt)&&roadWhereSeged.contains(tf))
							t=e;
					}
					try {
						me.build(toBuild, t);
					} catch (GameEndsException e1) {
					}
					break;
				case None:
					outOfBuildable = false;
					break;
				}
			return outOfBuildable;
		}
		
		/**
		 * trade. Calculates,does: What should we trade to get another hand to build again?
		 * @author Holló-Szabó Ákos
		 */
		public boolean trade() {

			for (Resource r : Resource.values()) {
				rAmount.put(r, me.getResourceAmount(r));
			}
			for (Buildable b : Buildable.values()) {
				seged.putAll(resourceNeed.get(b));
				needToBuild.put(b, seged);
				for (Resource r : Resource.values()) {
					if (needToBuild.get(b).get(r) >= rAmount.get(r))
						needToBuild.get(b).replace(r, 0);
					else
						needToBuild.get(b).replace(r, rAmount.get(r) - needToBuild.get(b).get(r));
				}
			}

			for (int i = 0; i < bList.size(); i++)
				if (values.get(i) == 0) {
					needToBuild.remove(bList.get(i));
					bList.remove(i);
					values.remove(i);
					i--;
				}
			int hSum = 0;
			for (Resource r : Resource.values()) {
				hSum += rAmount.get(r);
			}

			int ntbSum;
			for (int i = 0; i < bList.size(); i++) {
				ntbSum = 0;
				for (Resource r : Resource.values()) {
					ntbSum += needToBuild.get(bList.get(i)).get(r);
				}
				if (ntbSum >= hSum / 3) {
					needToBuild.remove(bList.get(i));
					bList.remove(i);
					values.remove(i);
					i--;
				} else{
					values.set(i, values.get(i) * (hSum - ntbSum));
					options.replace(bList.get(i), values.get(i));}
			}
			if (bList.size() == 0)
				return false;

			Collections.sort(values, new ComperatorD());
			bList.clear();
			for (Double d : values){
				for (Buildable b : Buildable.values()) {
					if (d == options.get(b))
						bList.add(b);
				}}
			
			int next=0;
			boolean doneBusiness = false;
			boolean success = false;
			for(Resource r:Resource.values()){
				rAmount.replace(r, rAmount.get(r)-needToBuild.get(bList.get(0)).get(r));
				seged.putAll(needToBuild.get(bList.get(0)));}
			for(Resource r1:Resource.values()){
				for(Resource r2:Resource.values()){
					if(rAmount.get(r1)!=0&&seged.get(r2)!=0){
						success=true;
						while(rAmount.get(r1)!=0&&seged.get(r2)!=0&&success){
						success=false;
						next=0;
					while(success==false&&next!=players.size()){
						success=me.trade(1, r1, 1, r2,players.get(next));
						next++;
					}
					if(success){
						rAmount.replace(r1, rAmount.get(r1)-1);
						seged.replace(r2, seged.get(r2)+1);
						doneBusiness=true;
					}}}}}
			return doneBusiness;
		}	
		}
	
	/**
	 * turn. The main calculator of future planes, and the center of control.
	 * @author Holló-Szabó Ákos
	 */
	public void turn() {
		me.rollTheDice();
		AllNeededDataForTurn datas = new AllNeededDataForTurn();
		boolean canBuild = true;
		boolean canTryAgain = true;

		while (canTryAgain == true) {
			datas.Refresh();
			datas.setPossibilities();
			datas.setBList();
			datas.setToDoList();
			canBuild = datas.findMaxToDoList();
			datas.inChange();
			canBuild = datas.letsBuild();
			if (canBuild == false)
				canTryAgain = datas.trade();
		}
	}
	
	public double nodePersonalValue(Vertex v){
		if(!(v.isBuildPossible(new Settlement(me)))){
			return 0;
		}
		double sum = 0;
		ArrayList<Hex> terrytories = v.getNeighbourHexes();
		for(Hex x : terrytories){
			sum += territoryPersonalValue(x);
		}
		return sum;
	}
	
	public double territoryPersonalValue(Hex h){
		return Material.frequencyLUT(h.getProsperity()) * resources.get(h.getResource()).personalValue(); 
	}

}

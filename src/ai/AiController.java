package ai;

import java.util.*;

import controller.Game;
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

		if (amountoKnightsperPlayer.get(me) <= max)
			return max - amountoKnightsperPlayer.get(me);
		else
			return -1;
	}

	/**
	 * isNodeBuildable. Calculates:Can we build there a Village?
	 * @param vIn - The node for check.
	 * @author @author Hollo-Szabo Akos
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
			if (e.getBuilding() != null && e.getBuilding().getOwner().equals(me))
				return true;

		return false;
	}

	/**
	 * isRobbed. Calculates:Is the robber on a field next to one of our town?
	 * @author Hollo-Szabo Akos
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
	 * @author Hollo-Szabo Akos
	 */
	public int nodePersonalDistance(Vertex vIn) {
		List<Edge> edges = map.getEdges();
		List<Vertex> myVertexs = new ArrayList<Vertex>();
		List<Vertex> VertexsNeighbours1 = new ArrayList<Vertex>();
		List<Vertex> VertexsNeighbours2 = new ArrayList<Vertex>();

		for (Edge e : edges)
			if (!e.getBuilding().equals(null) && e.getBuilding().getOwner().equals(me))
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
	 * AllNeededDataForTurn. Class:To assist the complicate turn()
	 * @author Hollo-Szabo Akos
	 */
	private class AllNeededDataForTurn {
		public Map<Resource, Integer> seged = new HashMap<Resource, Integer>();

		/**
		 * Value1. Class:To make simple the use of setPossibilities
		 * @author Hollo-Szabo Akos
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
		 * @author Hollo-Szabo Akos
		 */
		public AllNeededDataForTurn() {
			for (Resource r : Resource.values()) {
				if(!r.equals(Resource.Desert)){
				rAmount.put(r, me.getResourceAmount(r));
				rLut.put(r, me.getChangeLUT(r));
				seged.put(r, rAmount.get(r));
			}}

			seged = new HashMap<Resource, Integer>();
			seged.put(Resource.Brick, 1);
			seged.put(Resource.Lumber, 1);
			seged.put(Resource.Ore, 0);
			seged.put(Resource.Grain, 1);
			seged.put(Resource.Wool, 1);
			resourceNeed.put(Buildable.Settlement, seged);
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
			seged = new HashMap<Resource, Integer>();
			seged.put(Resource.Brick, 0);
			seged.put(Resource.Lumber, 0);
			seged.put(Resource.Ore, 0);
			seged.put(Resource.Grain, 0);
			seged.put(Resource.Wool, 0);
			resourceNeed.put(Buildable.None, seged);
		}
		
		/**
		 * Refresh. Resets main datarelations
		 * @author Hollo-Szabo Akos
		 */
		public void Refresh() {
			for (Resource r : Resource.values()) {
				if(!r.equals(Resource.Desert)){
				rAmount.put(r, me.getResourceAmount(r));
				rLut.put(r, me.getChangeLUT(r));
				seged.put(r, rAmount.get(r));
			}}
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
		 * @author Hollo-Szabo Akos
		 */
		public void setPossibilities() {
			Value1 data = new Value1(rAmount, new ArrayList<List<Resource>>());
			int i = 0;
			for (Resource r : Resource.values())
				if(!r.equals(Resource.Desert)){
				i = i * 100 + rAmount.get(r);}
			possibilities.put(i, data);

			for (Resource forChange : Resource.values()) {
				if(!forChange.equals(Resource.Desert)){
				for (Resource getAble : Resource.values()) {
					if(!getAble.equals(Resource.Desert)){
					if (forChange != getAble)
						setPossibilitiesRec(data, forChange, getAble);
				}}
			}}
		}
		
		/**
		 * setPossibilitiesRec. Assists: setPossibilities
		 * @author Hollo-Szabo Akos
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
				if(!r.equals(Resource.Desert)){
				i = i * 100 + newData.hand.get(r);}

			possibilities.put(i, newData);

			for (Resource forChange2 : Resource.values()) {
				if(!forChange2.equals(Resource.Desert)){
				for (Resource getAble2 : Resource.values()) {
					if(!getAble2.equals(Resource.Desert)){
					if (forChange2 != getAble2)
						setPossibilitiesRec(newData, forChange2, getAble2);
				}}
			}}
		}
		
		/**
		 * setBList. Calculates: What is the order of buildinges by value?
		 * @author Hollo-Szabo Akos
		 */
		public void setBList() {

			values.add(buildVillage.getBuildValue());
			options.put(Buildable.Settlement, values.get(0));
			values.add(buildCity.getBuildValue());
			options.put(Buildable.City, values.get(1));
			values.add(buildRoad.getBuildValue());
			options.put(Buildable.Road, values.get(2));
			//values.add(buildDevelopment.getBuildValue());
			//options.put(Buildable.Development, values.get(3));
			values.add(0.0);
			options.put(Buildable.Development, 0.0);
			values.add(0.0);
			options.put(Buildable.None, values.get(4));

			Collections.sort(values, new ComperatorD());
			for (Double d : values)
				for (Buildable b : Buildable.values()) {
					if (d == options.get(b)&&!bList.contains(b))
						bList.add(b);
				}
		}
		
		/**
		 * setToDoList. Calculates: What could we build from an factual hand?
		 * @author Hollo-Szabo Akos
		 */
		public void setToDoList() {
			int i = 2;
			int j = 0;
			boolean enough = true;
			boolean jPlus = false;
			for (Integer key : possibilities.keySet()) {
				seged = new HashMap<Resource, Integer>();
				seged.putAll(possibilities.get(key).hand);
				
				while (j < 4 && i > 0) {
					for (Buildable b : Buildable.values()) {
						if (bList.get(j).equals(b)) {
							for (Resource r : Resource.values()) {
								if(!r.equals(Resource.Desert)){
								if (seged.get(r) < resourceNeed.get(b).get(r))
									enough = false;
							}}
							if (enough == true) {
								for (Resource r : Resource.values()) {
									if(!r.equals(Resource.Desert)){
									seged.replace(r, seged.get(r) - resourceNeed.get(b).get(r));
									buildPlan.add(b);
								}}
								i--;
							} else
								jPlus=true;
						}
						enough = true;
					}
					if(jPlus==true){
						j++;
						jPlus=false;}
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
		 * @author Hollo-Szabo Akos
		 */
		public boolean findMaxToDoList() {
			boolean found = false;
			for (int i = 0; i < bList.size(); i++)
				for (int j = i; j < bList.size(); j++) {
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
		 * @author Hollo-Szabo Akos
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
		 * @author Hollo-Szabo Akos
		 */
		public boolean letsBuild() throws GameEndsException{
			TableElement t;
			Vertex tf;
			Vertex tt;
			List<Edge> roadWhere=map.getEdges();
			List<Vertex> roadWhereSeged;
			List<Hex> sourceOfNewNumbers=new ArrayList<Hex>();
			boolean outOfBuildable = true;

				switch (buildPlan.get(0)) {
				case Settlement:
					t = buildVillage.getNode();
					if(t != null){
						me.build(Buildable.Settlement, t);
						sourceOfNewNumbers=((Vertex)t).getNeighbourHexes();
						for(Hex h:sourceOfNewNumbers)
							if(!numbers.contains(h.getProsperity()))
								numbers.add(h.getProsperity());
					}
					break;
				case City:
					t = buildCity.getNode();
					if(t != null){
						me.build(Buildable.City, t);
						sourceOfNewNumbers=((Vertex)t).getNeighbourHexes();
						for(Hex h:sourceOfNewNumbers)
							if(!numbers.contains(h.getProsperity()))
								numbers.add(h.getProsperity());
					}
					break;
				case Development:
					try {
						me.buyDevCard();
					} catch (NotEnoughResourcesException e1) {
					}
					break;
				case Road:
					tf = buildRoad.getNodeFrom();
					tt = buildRoad.getNodeTo();
					t=roadWhere.get(0);
					for(Edge e:roadWhere){
						roadWhereSeged=e.getEnds();
						if(roadWhereSeged.contains(tt)&&roadWhereSeged.contains(tf))
							t=e;
					}
						me.build(Buildable.Road, t);
					break;
				case None:
					outOfBuildable = false;
					break;
				}
			return outOfBuildable;
		}
		
		/**
		 * trade. Calculates,does: What should we trade to get another hand to build again?
		 * @author Hollo-Szabo Akos
		 */
		public boolean trade() {

			for (Resource r : Resource.values()) {if(!r.equals(Resource.Desert)){
				rAmount.put(r, me.getResourceAmount(r));
			}}
			for (Buildable b : Buildable.values()) {
				seged.putAll(resourceNeed.get(b));
				needToBuild.put(b, seged);
				for (Resource r : Resource.values()) {
					if(!r.equals(Resource.Desert)){
					if (needToBuild.get(b).get(r) >= rAmount.get(r))
						needToBuild.get(b).replace(r, 0);
					else
						needToBuild.get(b).replace(r, rAmount.get(r) - needToBuild.get(b).get(r));
				}}
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
				if(!r.equals(Resource.Desert)){
				hSum += rAmount.get(r);
			}}

			int ntbSum;
			for (int i = 0; i < bList.size(); i++) {
				ntbSum = 0;
				for (Resource r : Resource.values()) {
					if(!r.equals(Resource.Desert)){
					ntbSum += needToBuild.get(bList.get(i)).get(r);
				}}
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
				if(!r.equals(Resource.Desert)){
				rAmount.replace(r, rAmount.get(r)-needToBuild.get(bList.get(0)).get(r));
				seged.putAll(needToBuild.get(bList.get(0)));}}
			for(Resource r1:Resource.values()){
				if(!r1.equals(Resource.Desert)){
				for(Resource r2:Resource.values()){
					if(!r2.equals(Resource.Desert)){
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
				}}
			return doneBusiness;
		}}
	
	/**
	 * turn. The main calculator of future planes, and the center of control.
	 * @author Hollo-Szabo Akos
	 */
	public void turn() throws GameEndsException{
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
			canTryAgain = datas.trade();
		}
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
		
		for(int i=0;i<settlementPlace.size();i++){
			if(me.isFirstBuildPossible(Buildable.Settlement, settlementPlace.get(i))){
				Values.add(nodeFirstPersonalValue(settlementPlace.get(i)));}
			else{settlementPlace.remove(i);
				i--;}}
		double max=-1;
		int index=-1;
		for(double d:Values)
		if(d>max){
			max=d;
			index=Values.indexOf(d);
			placeofSettlement=settlementPlace.get(index);
		}
		
			me.firstBuild(Buildable.Settlement, placeofSettlement);
			sourceOfNewNumbers=placeofSettlement.getNeighbourHexes();
			for(Hex h:sourceOfNewNumbers)
				if(!numbers.contains(h.getProsperity()))
					numbers.add(h.getProsperity());
			
			settlementPlace=placeofSettlement.getNeighbours();
			for(Vertex v:settlementPlace)
				settlementPlaceNN.addAll(v.getNeighbours());
			for(int i=0;i<6;i++)
				settlementPlaceNN.remove(placeofSettlement);
			Values=new ArrayList<Double>();
			
			for(int i=0;i<settlementPlaceNN.size();i++){
				if(me.isFirstBuildPossible(Buildable.Settlement, settlementPlaceNN.get(i))){
					Values.add(nodeFirstPersonalValue(settlementPlaceNN.get(i)));}
				else{settlementPlaceNN.remove(i);
					i--;}}
			max=-1;
			index=-1;
			for(double d:Values)
			if(d>max){
				max=d;
				index=Values.indexOf(d);
			}
			
			roadPlaces=placeofSettlement.getNeighbourEdges();
			edgesinNeighbour=settlementPlaceNN.get(index).getNeighbourEdges();
			for(Edge e1:roadPlaces)
				for(Edge e2:edgesinNeighbour)
					if(e1.getEnds().contains(placeofSettlement)&&(e1.getEnds().contains(e2.getEnds().get(0))||e1.getEnds().contains(e2.getEnds().get(1))))
						me.firstBuild(Buildable.Road, e1);
			} catch (GameEndsException e) {
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
	
	public double nodeFirstPersonalValue(Vertex v){
		if(!(v.isFirstBuildPossible(new Settlement(me)))){
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
		ArrayList<Hex> valid = map.getValidFields();
		if(valid.contains(h)){
		return Material.frequencyLUT(h.getProsperity()) * resources.get(h.getResource()).personalValue();
		}
		return 0;
	}

}

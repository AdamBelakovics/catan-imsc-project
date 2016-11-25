package ai;

import java.util.*;
import controller.map.*;
import controller.player.*;

public class AiController extends PlayerController {

	private Player me;
	ArrayList<Player> players=new ArrayList<Player>();
	private Table map;
	private int robberSum;

	private BuildCity buildCity= new BuildCity(map,me,players);
	private BuildVillage buildVillage= new BuildVillage(map,me,players);
	private BuildRoad buildRoad= new BuildRoad(map,me,players);
	private BuildDevelopment buildDevelopment= new BuildDevelopment(map,robberSum, me,players);

	private Set<Integer> numbers=new HashSet<Integer>();
	private Map<Resource, Material> resources = new HashMap<Resource, Material>();
	private Map<Resource, Integer> rAmount = new HashMap<Resource, Integer>();
	private Map<Resource, Integer> rLut = new HashMap<Resource, Integer>();

	public AiController(Table t, Player p, ArrayList<Player> otherPlayers){
		map=t;
		me=p;
		for(Player pl:otherPlayers)
			players.put(pl.getId(), pl);
		robberSum=0;
	}

	public int getKnightDiff() {
		return 0;
	}

	public Boolean isNodeBuildable() {
		return false;
	}

	public Boolean isRobbed() {
		return false;
	}

	public int nodePersonalDistance() {
		return 0;
	}

	public double nodePersonalValue() {
		return 0;
	}

	public double territoryPersonalValue() {
		return 0;
	}

	public Map<Resource, Material> getResources() {
		return resources;
	}

	public boolean query(Player donor, Map<Resource, Integer> offer, Map<Resource, Integer> demand) {
		return false;
	}

	private class AllNeededDataForTurn {
		public Map<Resource, Integer> rAmount = new HashMap<Resource, Integer>();
		public Map<Resource, Integer> seged = new HashMap<Resource, Integer>();
		public Map<Resource, Double> mPValue = new HashMap<Resource, Double>();
		
		private class Value1{
			public Map<Resource, Integer> hand;
			public List<Map<Resource, Integer>> how;
			public Value1(Map<Resource, Integer> ha,List<Map<Resource, Integer>>ho){
				hand=ha;
				how=ho;
			}
			public Value1(Value1 v){
				hand=new HashMap<Resource, Integer>();
				hand.putAll(v.hand);
				how=new ArrayList<Map<Resource, Integer>>();
				how.addAll(v.how);
			}
			public void add(Map<Resource, Integer> change){
				how.add(change);
			}
		}
		public Map<Integer,Value1> possibilities = new HashMap<Integer,Value1>();
		
		public Map<Buildable, Map<Resource, Integer>> resourceNeed = new HashMap<Buildable, Map<Resource, Integer>>();

		public Map<Buildable, Double> options = new HashMap<Buildable, Double>();
		public List<Buildable> bList;

		public Map<Integer, List<Buildable>> toDoList = new HashMap<Integer, List<Buildable>>();
		public List<Buildable> buildPlan = new ArrayList<Buildable>();

		public List<Buildable> buildPlanMax;

		
		
		public AllNeededDataForTurn() {
			for (Resource r : Resource.values()) {
				rAmount.put(r, me.getResourceAmount(r));
				System.out.println(me.getChangeLUT(r));
				rLut.put(r, me.getChangeLUT(r));
				seged.put(r, rAmount.get(r));
				//mPValue.put(r, resources.get(r).personalValue());
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

		public void setPossibilities() {
			Value1 data=new Value1(rAmount,new ArrayList<Map<Resource, Integer>>());
			Map< Map<Resource, Integer>,List<Map<Resource, Integer>>> changes=new HashMap< Map<Resource, Integer>,List<Map<Resource, Integer>>>();
			List<Map<Resource, Integer>> how=new ArrayList<Map<Resource, Integer>>();
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

		private void setPossibilitiesRec(Value1 data, Resource forChange, Resource getAble) {
			if (data.hand.get(forChange) < rLut.get(forChange))
				return;
			
			Value1 newData=new Value1(data);
			Map<Resource, Integer> change = new HashMap<Resource, Integer>();
			change.put(forChange, rLut.get(forChange));
			change.put(getAble, 1);
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

		public void setBList() {
			List<Buildable> bList = new ArrayList<Buildable>();
			List<Double> values = new ArrayList<Double>();

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

			Collections.sort(values, new Comperator());
			for (Double d : values)
				for (Buildable b : Buildable.values()) {
					if (d == options.get(b))
					bList.add(b);
				}
		}

		public void setToDoList() {
			int i = 2;
			int j = 0;
			boolean enough = true;
			for (Integer key : possibilities.keySet()) {
				seged = new HashMap<Resource, Integer>();
				seged.putAll(possibilities.get(key).hand);
				while (j <= 3 && i > 0) {
					for (Buildable b : Buildable.values()) {
						if (bList.get(j) == b) {
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
				while(i > 0){
					buildPlan.add(Buildable.None);
					i--;
				}
				
				toDoList.put(key, buildPlan);
				buildPlan = new ArrayList<Buildable>();
				j = 0;
				i = 2;
			}
		}

		public boolean findMaxToDoList() {
			buildPlanMax = new ArrayList<Buildable>();
			boolean found = false;
			for (int i = 0; i < 4; i++)
				for (int j = i; j < 4; j++) {
					for (Integer key : toDoList.keySet())
						if (toDoList.get(key).get(0) == bList.get(i) && toDoList.get(key).get(1) == bList.get(j)
								&& found == false) {
							buildPlanMax.add(bList.get(i));
							buildPlanMax.add(bList.get(j));
							found = true;
						}		
				}
			return found;
		}
	
		public void build(){
		}
		
		public void trade(){}
	}

	public void turn() {
		AllNeededDataForTurn datas = new AllNeededDataForTurn();

		datas.setPossibilities();
		datas.setBList();
		datas.setToDoList();
		boolean by=datas.findMaxToDoList();
		

	}
	
	private double nodePersonalValue(Vertex v){
		if(!(v.isBuildPossile(Settlement.class))){
			return 0;
		}
		double sum = 0;
		ArrayList<Hex> terrytories = v.getNeighbourHexes();
		for(Hex x : terrytories){
			sum += territoryPersonalValue(x);
		}
		return sum;
	}
	
	private double territoryPersonalValue(Hex h){
		return Material.frequencyLUT(h.getProsperity()) * resources.get(h.getResource()).personalValue(); 
	}

}

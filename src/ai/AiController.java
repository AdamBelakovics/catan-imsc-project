package ai;

import java.util.*;
import controller.map.*;
import controller.player.*;

public class AiController extends PlayerController {
	

	private Player me;
	Map<Colour, Player> players;
	private Table map;
	
	private BuildCity buildCity;
	private BuildVillage buildVillage;
	private BuildRoad buildRoad;
	private BuildDevelopment buildDevelopment;
	
	private int robberSum;
	private Set<Integer> numbers;
	private Map<Resource, Material> resources;
	private Map<Resource, Integer> rAmount;
	private Map<Resource, Integer> rLut;

	
	public AiController(Table t, Colour c,Map<Colour, Player> p){
		map=t;
		me=players.get(c);
		players=p;
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
	
	public boolean query(Player donor, Map<Resource, Integer> offer, Map<Resource, Integer> demand)  {
		return false;
	}
	
	private void Change(Map<Integer,Map<Resource,Integer>> possibilities, Map<Resource, Integer> seged,
			Resource forChange,Resource getAble){
		if(seged.get(forChange)<rLut.get(forChange))
		return;
		
		Map<Resource, Integer> segedMasolat=new HashMap<Resource, Integer>();
		for (Resource r : Resource.values())
			segedMasolat.put(r, seged.get(r));
		
		segedMasolat.replace(forChange, segedMasolat.get(forChange)-rLut.get(forChange));
		segedMasolat.replace(getAble, segedMasolat.get(getAble)+1);
		
		int i=0;
		for (Resource r : Resource.values()) 
			i=i*100+segedMasolat.get(r);
		
		possibilities.put(i,segedMasolat);
		
		for (Resource forChange2 : Resource.values()) {
			for (Resource getAble2 : Resource.values()) {
				if(forChange2!=getAble2)
					Change(possibilities,segedMasolat,forChange2,getAble2);
			}
		}
	}
	
	private List<Buildable> Order(Map<Buildable,Double> options) {
		List<Buildable> bList=new ArrayList<Buildable>();
		List<Double> values=new ArrayList<Double>();
		
		values.add(buildVillage.getBuildValue());
		options.put(Buildable.Village, values.get(0));
		values.add(buildCity.getBuildValue());
		options.put(Buildable.City,values.get(1));
		values.add(buildRoad.getBuildValue());
		options.put(Buildable.Road, values.get(2));
		values.add(buildDevelopment.getBuildValue());
		options.put(Buildable.Development, values.get(3));
		
		Collections.sort(values,new Comperator());
		for(Double d:values)
			for(Buildable b:Buildable.values()){
			if(d==options.get(b));
				bList.add(b);}
		
		
		return bList;
	}
	
	public void turn() {
		Map<Resource, Integer> rAmount=new HashMap<Resource, Integer>();
		Map<Resource, Integer> seged1=new HashMap<Resource, Integer>();
		Map<Resource, Integer> seged2=new HashMap<Resource, Integer>();
		Map<Resource, Integer> seged3=new HashMap<Resource, Integer>();
		Map<Resource, Double> mPValue=new HashMap<Resource, Double>();
		for (Resource r : Resource.values()) {
			rAmount.put(r,me.getResourceAmount(r));
			rLut.put(r,me.getChangeLUT(r));
			seged1.put(r,rAmount.get(r));
			seged2.put(r,0);
			seged3.put(r,0);
			mPValue.put(r,resources.get(Resource.Brick).personalValue());
			}
		
		Map<Integer,Map<Resource,Integer>> possibilities=new HashMap<Integer,Map<Resource,Integer>>();
		int i=0;
		for (Resource r : Resource.values()) 
			i=i*10+rAmount.get(r);
		possibilities.put(i,rAmount);
		
		for (Resource forChange : Resource.values()) {
			for (Resource getAble : Resource.values()) {
				if(forChange!=getAble)
					Change(possibilities,rAmount,forChange,getAble);
			}
		}
		
		Map<Buildable,Double> options= new HashMap<Buildable,Double>();
		List<Buildable> bList;
		bList=Order(options);
		
		Map<Buildable,Map<Resource,Integer> > resourceNeed=new HashMap<Buildable,Map<Resource,Integer> >();
		//Brick, Lumber, Ore, Grain, Wool
		seged1=new HashMap<Resource,Integer> ();
		seged1.put(Resource.Brick, 1);
		seged1.put(Resource.Lumber, 1);
		seged1.put(Resource.Ore, 0);
		seged1.put(Resource.Grain, 1);
		seged1.put(Resource.Wool, 1);
		resourceNeed.put(Buildable.Village,seged1);
		seged1=new HashMap<Resource,Integer> ();
		seged1.put(Resource.Brick, 0);
		seged1.put(Resource.Lumber, 0);
		seged1.put(Resource.Ore, 3);
		seged1.put(Resource.Grain, 2);
		seged1.put(Resource.Wool, 0);
		resourceNeed.put(Buildable.City,seged1);
		seged1=new HashMap<Resource,Integer> ();
		seged1.put(Resource.Brick, 0);
		seged1.put(Resource.Lumber, 0);
		seged1.put(Resource.Ore, 0);
		seged1.put(Resource.Grain, 1);
		seged1.put(Resource.Wool, 1);
		resourceNeed.put(Buildable.Development,seged1);
		seged1=new HashMap<Resource,Integer> ();
		seged1.put(Resource.Brick, 1);
		seged1.put(Resource.Lumber, 1);
		seged1.put(Resource.Ore, 0);
		seged1.put(Resource.Grain, 0);
		seged1.put(Resource.Wool, 0);
		resourceNeed.put(Buildable.Road,seged1);
		
		Map<Integer,List<Buildable>> ToDoList=new HashMap<Integer,List<Buildable>>();
		List<Buildable> seged=new ArrayList<Buildable>();
		int j=0;
		boolean enough=true;
		for(Integer key:possibilities.keySet()){
			seged2=new HashMap<Resource, Integer>();
			seged2.putAll(possibilities.get(key));
			while(j<=3){
			for(Buildable b:Buildable.values()){
				if(bList.get(j)==b){
					for(Resource r:Resource.values()){
						if(seged2.get(r)<resourceNeed.get(b).get(r))
							enough=false;
					}
					if(enough==true){
						for(Resource r:Resource.values()){
							seged2.replace(r, seged2.get(r)-resourceNeed.get(b).get(r));
							seged.add(b);
						}
					}
					else
						j++;
				}
			}
			enough=true;}
			ToDoList.put(key, seged);
			seged.clear();
		}
		
		
		
		
		
		
		
	}
	
}
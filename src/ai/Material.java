package ai;

import java.util.HashSet;

import aitest.AiParameter;
import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.City;
import controller.player.Player;
import controller.player.Resource;
import controller.player.Settlement;

/**
 * @author Mate Sebestyen
 *
 * Class for calculating values of materials in Catan, used by the AI player
 */
public class Material {
	// for testing, collects statistics of personal value
	public static double minValue = Double.MAX_VALUE;
	public static double maxValue = - Double.MAX_VALUE;
	public static double sumValue = 0;
	public static int cnt = 0;
	
	/**
	 * the resource type this material represents
	 */
	private Resource myresource;
	
	/**
	 * base value of specific material
	 */
	private double baseValue;
	
	/**
	 * the chance of gaining the material in a turn on all the territories combined
	 */
	@SuppressWarnings("unused")
	private double baseFrequency;
	
	/**
	 * the playing board 
	 */
	private Table board;
	
	/**
	 * the Player for whom the values are calculated 
	 */
	private Player me;
	
	/**
	 * constructor, initializes board me and myresource 
	 * @param t - board
	 * @param p - me
	 * @param r - myresource
	 */
	public Material(Table t, Player p, Resource r, HashSet<AiParameter> params){
		
		me = p;
		board = t;
		myresource = r;
		
		/*
		 * initializing baseValue
		 */
		switch (myresource) {
		case Brick:
			if(params.contains(AiParameter.Lumbrick))
				baseValue = 7;
			else if(params.contains(AiParameter.Orain))
				baseValue = 2;
			else 
				baseValue = 5;
			break;
		case Lumber:
			if(params.contains(AiParameter.Lumbrick))
				baseValue = 8;
			else if(params.contains(AiParameter.Orain))
				baseValue = 3;
			else 
				baseValue = 5;
			break;
		case Ore:
			if(params.contains(AiParameter.Lumbrick))
				baseValue = 2;
			else if(params.contains(AiParameter.Orain))
				baseValue = 8;
			else 
				baseValue = 5;
			break;
		case Grain:
			if(params.contains(AiParameter.Lumbrick))
				baseValue = 3;
			else if(params.contains(AiParameter.Orain))
				baseValue = 7;
			else 
				baseValue = 5;
			break;
		case Wool:
			if(params.contains(AiParameter.Lumbrick))
				baseValue = 3;
			else if(params.contains(AiParameter.Orain))
				baseValue = 6;
			else 
				baseValue = 5;
			break;
		case Desert:
			baseValue = 0;
			break;
		default:
			baseValue = 0;
			System.out.println("Failed to initialize baseValue to proper ammount set to 0");
			break;
		}
		
		/*
		 * initializing baseFrequency
		 */
		baseFrequency = 0;
		for(Hex x : board.getFields()){
			if(x.getResource().equals(myresource)){
				baseFrequency += frequencyLUT(x.getProsperity());
			}
		}
	}
	
	/**
	 * method for calculating the chance of the player to get the material in the turn 
	 * @return the chance
	 */
	public double personalFrequency(){
		double sum = 0;
		for(Hex field : board.getFields()){
			if(field.getResource().equals(myresource)){
				for(Vertex node : field.getNeighbouringVertices()){
					if((node.getBuilding() != null) && (node.getBuilding().getOwner().equals(me))){
						if(node.getBuilding().getClass().equals(Settlement.class)){
						sum += frequencyLUT(field.getProsperity());
						}else if(node.getBuilding().getClass().equals(City.class)){
						sum += 2.0 * frequencyLUT(field.getProsperity());
						}
					}
				}
			}
		}
		return sum;
	}
	
	/**
	 * method for calculating the value of the given material for the player
	 * @return the value
	 */
	public double personalValue(){
		double result = baseValue * factorByNumberInHandLUT(me.getResourceAmount(myresource)) + 2 * globalFrequency();
		// for testing
		if(result > 0){
			sumValue += result;
			cnt++;
			if(result > maxValue)
				maxValue = result;
			if(result < minValue)
				minValue = result;
		}
		return result;
	}
	
	/**
	 * the chance of all players getting the material combined
	 * @return the chance
	 */
	public double globalFrequency(){
		double sum = 0;
		for(Hex field : board.getFields()){
			if(field.getResource().equals(myresource)){
				for(Vertex node : field.getNeighbouringVertices()){
					if(node.getBuilding() != null){
						if(node.getBuilding().getClass().equals(Settlement.class)){
							sum += frequencyLUT(field.getProsperity());
						}else if(node.getBuilding().getClass().equals(City.class)){
							sum += 2.0 * frequencyLUT(field.getProsperity());
						}
					}
				}
			}
		}
		return sum;
	}
	
	/**
	 * LUT for the chance of rolling a given number if we roll two dice
	 * @param a the rolled number
	 * @return the chance of rolling it
	 */
	public static double frequencyLUT(int a){
		switch (a) {
		case 2:
		case 12:		//intended fallthrough
			return 1.0/36.0;
		case 3:
		case 11:		//intended fallthrough
			return 2.0/36.0;
		case 4:
		case 10:		//intended fallthrough
			return 3.0/36.0;
		case 5:
		case 9:			//intended fallthrough
			return 4.0/36.0;
		case 6:
		case 8:			//intended fallthrough
			return 5.0/36.0;
		case 7:
			return 6.0/36.0;
		case 0:
			return 0;
		default:
			System.out.println("Material.frequencyLUT: argument is not a valid dice roll.");
			return 1;
		}
	}
	
	/**
	 * look up table for factors in calculating personal vale magic constants
	 * @param number of given material in hand
	 * @return a factor for the calculation of the value of the material
	 */
	private double factorByNumberInHandLUT(int inhand){
		switch(inhand){
		case 0:
			return 1;
		case 2:
			return 0.7;
		case 3:
			return 0.3;
		case 4:
			return 0.1;
		default:
			return 0;
		}
	}
}

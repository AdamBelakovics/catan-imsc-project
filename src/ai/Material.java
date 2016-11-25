package ai;

import java.util.ArrayList;
import java.util.List;

import controller.map.*;
import controller.player.*;

/**
 * @author Máté Sebestyén
 *
 * Class for representing materials in the game Catan
 */
public class Material {
	
	/**
	 * variable for storing the resource type this material represents
	 */
	private Resource myresource;
	
	/**
	 * variable to store base value of specific material
	 * basically a magic number by the creators
	 */
	private double baseValue;
	
	/**
	 * the chance of getting the material in a turn on all the territories combined 
	 */
	private double baseFrequency;
	
	private Table board;
	private Player me;
	
	public Material(Table t, Player pc, Resource r){
		
		me = pc;
		board = t;
		myresource = r;
		
		/*
		 * initializing baseValue
		 */
		switch (myresource) {
		case Brick:
			baseValue = 2;
			break;
		case Lumber:
			baseValue = 3;
			break;
		case Ore:
			baseValue = 5;
			break;
		case Grain:
			baseValue = 6;
			break;
		case Wool:
			baseValue = 4;
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
		ArrayList<Hex> fields = board.getFields();
		for(Hex x : fields){
			if(x.getResource().equals(myresource)){
				baseFrequency += frequencyLUT(x.getProsperity());
			}
		}
	}
	
	/**
	 * method for calculating the chance of the player to get the material in the turn 
	 * @return the chance
	 * not available or implemented methods are needed
	 */
	public double personalFrequency(){
		
		double sum = 0;
		ArrayList<Hex> fields = board.getFields();
		for(Hex x : fields){
			List<Vertex> ver = x.getNeighbouringVertices();
			for(Vertex y : ver){
				if(y.getSettlement().getOwner().equals(me)){
					if(x.getResource().equals(myresource)){
						sum += frequencyLUT(x.getProsperity());
					}
				}
			}
		}
		return sum;
		
	}
	
	/**
	 * method for calculating the value of the given material for the player
	 * @return the value (5 for now)
	 */
	public double personalValue(){
		double value = 0;
		value = baseValue * factorByNumberInHandLUY(me.getResourceAmount(myresource)) + 2 * globalFrequency();
		
		
		//returning a value randomly chosen for now
		return value;
	}
	
	/**
	 * the chance of all players getting the material combined
	 * @return the chance
	 * not available or implemented methods are needed
	 */
	private double globalFrequency(){
		double sum = 0;
		ArrayList<Hex> fields = board.getFields();
		for(Hex x : fields){
			List<Vertex> ver = x.getNeighbouringVertices();
			for(Vertex y : ver){
				if(!(y.getSettlement().getOwner().equals(me))){
					if(x.getResource().equals(myresource)){
						sum += frequencyLUT(x.getProsperity());
					}
				}
			}
		}
		return sum;
	}
	
	
	/**
	 * method for getting the chance of rolling a given number if we roll two dice
	 * @param a the number in question
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

		default:
			System.out.println("argument is not a valid dice roll from Material.frequencyLUT");
			return 1;
		}
	}
	
	/**
	 * look up table for factors in calculating personal vale
	 * @param inhand number of given material in hand
	 * @return a factor for the calculation
	 */
	private double factorByNumberInHandLUY(int inhand){
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

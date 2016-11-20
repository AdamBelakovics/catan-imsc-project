package ai;

import java.util.ArrayList;

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
	
	public Material(Table t, Resource r){
		
		board = t;
		myresource = r;
		
		/*
		 * initializing baseValue
		 * TODO magic constants need to be calculated
		 */
		switch (myresource) {
		case Brick:
			baseValue = 0;
			break;
		case Lumber:
			baseValue = 0;
			break;
		case Ore:
			baseValue = 0;
			break;
		case Grain:
			baseValue = 0;
			break;
		case Wool:
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
		
		/*
		 * algorithm iterates through all territories and adds up the income rates
		 * if the material on a given territory matches this material
		 * TODO getFields is needed to be visible
		 * TODO get number needs to be implemented
		 */ 
		baseFrequency = 0;
		ArrayList<Hex> fields = board.getFields();
		for(Hex x : fields){
			if(x.getResource().equals(myresource)){
				baseFrequency += frequencyLUT(x.getNumber());
			}
			
		}
	}
	
	/**
	 * method for calculating the chance of the player to get the material in the turn 
	 * @return the chance
	 * not available or implemented methods are needed
	 */
	public double personalFrequency(){
		// TODO implement method body
		
		double sum = 0;
		ArrayList<Hex> fields = board.getFields();
		for(Hex x : fields){
			if(x.weHaveBuildingOnThisField()){
				if(x.getResource().equals(myresource)){
					sum += frequencyLUT(x.getNumber());
				}
			}
		}
		return sum;
		
	}
	
	/**
	 * method for calculating the value of the given material for the player
	 * @return the value (5 for now)
	 */
	public double personalvalue(){
		 /*
		  * TODO implement method body
		  * my idea for calculation is to decide the base values multiply that with a polynom calculated with
		  * interpolating polynomial calculation in wolfram alpha to match certain criteria based on the number of
		  * the given material in hand (eg if we have zero it should be higher than the base value, if we have 1 it should be
		  * the base value ... and if we have 6 or more it should be 0) than add a number based on how much other players have
		  * and get form the resource
		  * exact numbers to be determined
		  */
		
		//returning a value randomly chosen for now
		return 5;
	}
	
	/**
	 * the chance of all players getting the material combined
	 * @return the chance
	 * not available or implemented methods are needed
	 */
	private double globalFrequency(){
		// TODO implement method body
		double sum = 0;
		ArrayList<Hex> fields = board.getFields();
		for(Hex x : fields){
			if(x.otherPlayersHaveBuildingOnThisField()){
				if(x.getResource().equals(myresource)){
					sum += frequencyLUT(x.getNumber());
				}
			}
		}
		return sum;
	}
	
	
	/**
	 * method for geting the chance of rolling a given number if we roll two dice
	 * @param a the number in question
	 * @return the chance of rolling it
	 */
	private double frequencyLUT(int a){
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
}

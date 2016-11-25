package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.map.Table;
import controller.player.devcards.DevCard;
import controller.player.Player;
import controller.player.Resource;


/**
 * Buying a development card
 * 
 * This class is used to calculate the different
 * development cards' values according to the current
 * state of the table. The combined values of these
 * cards determines the value of a development card.
 * 
 * @author Gergely
 */
public class BuildDevelopment {
	private Table map;
	// TODO ai
	private AiController owner;
	private int player;
	private Player aiPlayer;
	private ArrayList<Player> otherPlayers;
	
	/**
	 * Constructor
	 * @param map - the table
	 * @param o - the ai player who uses this class
	 * @author Gergely Olah
	 */
	public BuildDevelopment(Table map, AiController owner, Player aiPlayer, ArrayList<Player> otherPlayers){
		this.map = map;
		// TODO ai
		//this.owner = owner;
		this.aiPlayer = aiPlayer;
		this.otherPlayers = otherPlayers;
	}
	/**
	 * Calculates the combined values of all development
	 * cards at the current state of the game. This does
	 * not require a refresh function before unlike other
	 * BuildXXX classes.
	 * @return - the value of a development card
	 * @author Gergely Olah
	 */
	public double getBuildValue(){
		return calculateInventionProbability() * calculateInventionValue() + 
				calculateKnightProbability() * calculateKnightValue() + 
				calculateMonopolyProbability() * calculateMonopolyValue() + 
				calculatePlusPointProbability() * calculatePlusPointValue() +
				calculateTwoRoadProbability() * calculateTwoRoadValue();
	}
	/**
	 * Calculates the value of getting a single two-road development card.
	 * @return - the value of a two-road development card
	 * @author Gergely Olah
	 */
	private double calculateTwoRoadValue(){
		// TODO ai
		//BuildRoad r = new BuildRoad(map, owner);
		BuildRoad r = new BuildRoad(map, owner, aiPlayer, otherPlayers);
		int dif = r.calculateMaxRoadDifference();
		int difVal = 1;
		if(dif < 2 && dif >= -2)
			difVal = 5;
		else if(dif < -2)
			difVal = 3;
		r.refresh();
		return r.getBuildValue() + difVal;
	}
	
	/**
	 * Calculates the value of getting a single plus-point development card.
	 * @return - the value of a plus-point development card
	 * @author Gergely Olah
	 */
	private double calculatePlusPointValue(){
		return 5;
	}
	
	/**
	 * Calculates the value of getting a single invention development card.
	 * @return - the value of a invention development card, between 0 and 10
	 * @author Gergely Olah
	 */
	private double calculateInventionValue(){
		double minResourceFrequency = -1;
		// TODO need Material, ai 
		/*HashMap<Resource, Material> res = owner.getResources();
		for(Map.Entry<Resource, Material> it : res.entrySet()){
			double currentFrequency = it.getValue().personalFrequency();
			if(minResourceFrequency == -1 || currentFrequency < minResourceFrequency){
				minResourceFrequency = currentFrequency;
			}
		}*/
		// the return value is between 0 and 10
		if(minResourceFrequency > 0)
			return Math.max(10 / minResourceFrequency, 10);
		else
			return 0;
	}
	
	/**
	 * Calculates the value of getting a single monopoly development card.
	 * @return - the value of a monopoly development card, between 0 and 10
	 * @author Gergely Olah
	 */
	private double calculateMonopolyValue(){
		double maxMaterialValue = 0;
		// TODO need Material, ai
		/*HashMap<Resource, Material> res = owner.getResources();
		for(Map.Entry<Resource, Material> it : res.entrySet()){
			double currentValue;
			// handling division by zero
			try{
				currentValue = it.getValue().personalValue() * it.getValue().globalFrequency() / it.getValue().personalFrequency();
			} catch(IllegalArgumentException e){
				currentValue = 0;
			}
			if(maxMaterialValue < currentValue){
				maxMaterialValue = currentValue;
			}
		}*/
		return Math.max(maxMaterialValue, 10);
	}
	
	/**
	 * Calculates the value of getting a single knight development card.
	 * @return - the value of a knight development card, between 0 and 10
	 * @author Gergely Olah
	 */
	private double calculateKnightValue(){
		int robbed = 1;
		// TODO ai
		/*if(owner.isRobbed())
			robbed = 2;*/
		int dif, difVal;
		dif = 0;
		// TODO ai
		//dif = owner.getKnightDiff();
		difVal = 1;
		if(dif < 2 && dif >= -2)
			difVal = 3;
		else if(dif < -2)
			difVal = 2;
		// TODO ai
		return 0;
		//return Math.max(robbed * difVal + owner.getRobbedSum() + 2.5, 10);
	}
	
	/**
	 * Calculates the probability of getting a two-road development
	 * card. It only counts the cards that are played, it does not
	 * counts the ones which are not in the pack but not used.
	 * @return probability of getting a two-road development card
	 * @author Gergely Olah
	 */
	private double calculateTwoRoadProbability(){
		double result = 0.08;
		ArrayList<DevCard> playedCards;
		for(Player player : otherPlayers){
			playedCards = player.getPlayedDevelopmentCards();
			// TODO count how many were played
			// result -= playedCards.get("tworoad") * 0.04;
		}
		// TODO need devcard
		//playedCards = map.getPlayedDevelopmentCards(owner.getPlayerID());
		//result -= playedCards.get("tworoad") * 0.04;
		return result;
	}
	
	/**
	 * Calculates the probability of getting a monopoly development
	 * card. It only counts the cards that are played, it does not
	 * counts the ones which are not in the pack but not used.
	 * @return probability of getting a monopoly development card
	 * @author Gergely Olah
	 */
	private double calculateMonopolyProbability(){
		double result = 0.08;
		// TODO need DevCard
		/*
		ArrayList<DevCard> playedCards;
		for(Player player : otherPlayers){
			playedCards = player.getPlayedDevelopmentCards();
			// TODO count how many were played
			result -= playedCards.get("monopoly") * 0.04;
		}
		playedCards = map.getPlayedDevelopmentCards(owner.getPlayerID());
		result -= playedCards.get("monopoly") * 0.04;*/
		return result;
	}
	
	/**
	 * Calculates the probability of getting a invention development
	 * card. It only counts the cards that are played, it does not
	 * counts the ones which are not in the pack but not used.
	 * @return probability of getting a invention development card
	 * @author Gergely Olah
	 */
	private double calculateInventionProbability(){
		double result = 0.08;
		// TODO need DevCard
		/*
		ArrayList<DevCard> playedCards;
		for(Player player : otherPlayers){
			playedCards = player.getPlayedDevelopmentCards();
			// TODO count how many were played
			result -= playedCards.get("invention") * 0.04;
		}
		playedCards = map.getPlayedDevelopmentCards(owner.getPlayerID());
		result -= playedCards.get("invention") * 0.04;
		*/
		return result;
	}
	
	/**
	 * Calculates the probability of getting a plus-point development
	 * card. It only counts the cards that are played, it does not
	 * counts the ones which are not in the pack but not used.
	 * @return probability of getting a plus-point development card
	 * @author Gergely Olah
	 */
	private double calculatePlusPointProbability(){
		double result = 0.2;
		// TODO need DevCard
		/*
		ArrayList<DevCard> playedCards;
		for(Player player : otherPlayers){
			playedCards = player.getPlayedDevelopmentCards();
			// TODO count how many were played
			result -= playedCards.get("plus-point") * 0.04;
		}
		playedCards = map.getPlayedDevelopmentCards(owner.getPlayerID());
		result -= playedCards.get("plus-point") * 0.04;
		*/
		return result;
	}
	
	/**
	 * Calculates the probability of getting a knight development
	 * card. It only counts the cards that are played, it does not
	 * counts the ones which are not in the pack but not used.
	 * @return probability of getting a knight development card
	 * @author Gergely Olah
	 */
	private double calculateKnightProbability(){
		double result = 0.56;
		// TODO need DevCard
		/*
		ArrayList<DevCard> playedCards;
		for(Player player : otherPlayers){
			playedCards = player.getPlayedDevelopmentCards();
			// TODO count how many were played
			result -= playedCards.get("knight") * 0.04;
		}
		playedCards = map.getPlayedDevelopmentCards(owner.getPlayerID());
		result -= playedCards.get("knight") * 0.04;
		*/
		return result;
	}
}

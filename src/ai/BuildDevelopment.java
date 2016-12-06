package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.map.Table;
import controller.player.devcards.DevCard;
import controller.player.devcards.KnightCard;
import controller.player.devcards.MonopolyCard;
import controller.player.devcards.RoadBuildingCard;
import controller.player.devcards.VictoryPointCard;
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
	private AiController owner;
	private int player;
	private Player aiPlayer;
	private ArrayList<Player> otherPlayers;
	
	private double pMonopoly;
	private double pInvention;
	private double pKnight;
	private double pPlusPoint;
	private double pTwoRoad;
	
	/**
	 * Constructor
	 * @param map - the table
	 * @param o - the ai player who uses this class
	 * @author Gergely Olah
	 */
	public BuildDevelopment(Table map, AiController owner, Player aiPlayer, ArrayList<Player> otherPlayers){
		this.map = map;
		this.owner = owner;
		this.aiPlayer = aiPlayer;
		this.otherPlayers = otherPlayers;
		pMonopoly = 0.08;
		pInvention = 0.08;
		pKnight = 0.56;
		pPlusPoint = 0.2;
		pTwoRoad = 0.08;
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
		calculateProbabilities();
		/*double inv = calculateInventionValue();
		double kni = calculateKnightValue();
		double mon = calculateMonopolyValue();
		double pls = calculatePlusPointValue();
		double two = calculateTwoRoadValue();
		System.out.println("-");
		System.out.println("Invention value: " + inv + ", prob: " + pInvention);
		System.out.println("Knight value: " + kni + ", prob: " + pKnight);
		System.out.println("Monopoly value: " + mon + ", prob: " + pMonopoly);
		System.out.println("PlusPoint value: " + pls + ", prob: " + pPlusPoint);
		System.out.println("TwoRoad value: " + two + ", prob: " + pTwoRoad);*/
		double result =
				pInvention * calculateInventionValue() + 
				pKnight * calculateKnightValue() + 
				pMonopoly * calculateMonopolyValue() + 
				pPlusPoint * calculatePlusPointValue() +
				pTwoRoad * calculateTwoRoadValue();
		//System.out.println("Development value: " + result);
		//System.out.println("-");
		return result;
	}
	/**
	 * Calculates the value of getting a single two-road development card.
	 * @return - the value of a two-road development card
	 * @author Gergely Olah
	 */
	private double calculateTwoRoadValue(){
		BuildRoad r = new BuildRoad(map, owner, aiPlayer, otherPlayers);
		int dif = r.calculateMaxRoadDifference();
		int difVal = 1;
		if(dif < 2 && dif >= -2)
			difVal = 5;
		else if(dif < -2)
			difVal = 3;
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
		Map<Resource, Material> res = owner.getResources();
		for(Map.Entry<Resource, Material> it : res.entrySet()){
			double currentFrequency = it.getValue().personalFrequency();
			if(minResourceFrequency == -1 || currentFrequency < minResourceFrequency){
				minResourceFrequency = currentFrequency;
			}
		}
		// the return value is between 0 and 10
		if(minResourceFrequency > 0)
			return 10.0 / minResourceFrequency;
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
		Map<Resource, Material> res = owner.getResources();
		for(Map.Entry<Resource, Material> it : res.entrySet()){
			double currentValue;
			// handling division by zero
			try{
				if(it.getValue().personalFrequency() > 0)
					currentValue = 0.1 * it.getValue().personalValue() * it.getValue().globalFrequency() / it.getValue().personalFrequency();
				else
					currentValue = 0;
			} catch(IllegalArgumentException e){
				currentValue = 0;
			}
			if(maxMaterialValue < currentValue){
				maxMaterialValue = currentValue;
			}
		}
		return maxMaterialValue;
	}
	
	/**
	 * Calculates the value of getting a single knight development card.
	 * @return - the value of a knight development card, between 0 and 10
	 * @author Gergely Olah
	 */
	private double calculateKnightValue(){
		int robbed = 1;
		if(owner.isRobbed())
			robbed = 2;
		int dif, difVal;
		dif = 0;
		dif = owner.getKnightDiff();
		difVal = 1;
		if(dif < 2 && dif >= -2)
			difVal = 3;
		else if(dif < -2)
			difVal = 2;
		// TODO getRobbedSum() in AiController, not very important
		//return Math.max(robbed * difVal + owner.getRobbedSum() + 2.5, 10);
		return robbed * difVal + 2.5;
	}
	
	/**
	 * Calculates the probabilites for each devcard.
	 * Doesn't count the ones that are not in the pack, but unplayed.
	 * @author Gergely Olah
	 */
	private void calculateProbabilities(){
		pMonopoly = 0.08;
		pInvention = 0.08;
		pKnight = 0.56;
		pPlusPoint = 0.2;
		pTwoRoad = 0.08;
		
		int monopolyCnt = 0;
		int inventionCnt = 0;
		int knightCnt = 0;
		int plusPointCnt = 0;
		int twoRoadCnt = 0;
		int allCnt = 0;
		
		// counting how many cards other players played
		for(Player player : otherPlayers){
			if(player.getPlayedDevelopmentCards() != null){
				for(DevCard dc : player.getPlayedDevelopmentCards()){
					if(dc.getClass().equals(KnightCard.class)){
						knightCnt++;
					} else if(dc.getClass().equals(MonopolyCard.class)){
						monopolyCnt++;
					} else if(dc.getClass().equals(RoadBuildingCard.class)){
						twoRoadCnt++;
					} else if(dc.getClass().equals(VictoryPointCard.class)){
						plusPointCnt++;
					} else {
						inventionCnt++;
					}
					allCnt++;
				}
			}
		}
		if(aiPlayer.getPlayedDevelopmentCards() != null){
			// counting how many cards we (ai) played
			for(DevCard dc : aiPlayer.getPlayedDevelopmentCards()){
				if(dc.getClass().equals(KnightCard.class)){
					knightCnt++;
				} else if(dc.getClass().equals(MonopolyCard.class)){
					monopolyCnt++;
				} else if(dc.getClass().equals(RoadBuildingCard.class)){
					twoRoadCnt++;
				} else if(dc.getClass().equals(VictoryPointCard.class)){
					plusPointCnt++;
				} else {
					inventionCnt++;
				}
				allCnt++;
			}
		}
		for(Player player : otherPlayers){
			allCnt += player.getDevCards().size();
		}
		allCnt += aiPlayer.getDevCards().size();
		
		if(allCnt == 25){
			pKnight = 0;
			pInvention = 0;
			pMonopoly = 0;
			pTwoRoad = 0;
			pPlusPoint = 0;
		}
		// to avoid divide by zero
		else if(allCnt > 0){
			pKnight = (14 - knightCnt) / (25.0 - allCnt);
			pInvention = (2 - inventionCnt) / (25.0 - allCnt);
			pMonopoly = (2 - monopolyCnt) / (25.0 - allCnt);
			pTwoRoad = (2 - twoRoadCnt) / (25.0 - allCnt);
			pPlusPoint = (5 - plusPointCnt) / (25.0 - allCnt);
		}
	}
}

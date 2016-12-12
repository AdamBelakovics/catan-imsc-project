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
	private double buildValue;
	private Player aiPlayer;
	private ArrayList<Player> otherPlayers;
	
	private double pMonopoly;
	private double pInvention;
	private double pKnight;
	private double pPlusPoint;
	private double pTwoRoad;
	
	// for testing, collects statistics of build value
	public static double minValue = Double.MAX_VALUE;
	public static double maxValue = - Double.MAX_VALUE;
	public static double sumValue = 0;
	public static int cnt = 0;
	
	public static double minValueKnight = Double.MAX_VALUE;
	public static double maxValueKnight = - Double.MAX_VALUE;
	public static double sumValueKnight = 0;
	public static int cntKnight = 0;
	
	public static double minValueTwoRoad = Double.MAX_VALUE;
	public static double maxValueTwoRoad = - Double.MAX_VALUE;
	public static double sumValueTwoRoad = 0;
	public static int cntTwoRoad = 0;
	
	public static double minValueMonopoly = Double.MAX_VALUE;
	public static double maxValueMonopoly = - Double.MAX_VALUE;
	public static double sumValueMonopoly = 0;
	public static int cntMonopoly = 0;
	
	public static double minValueInvention = Double.MAX_VALUE;
	public static double maxValueInvention = - Double.MAX_VALUE;
	public static double sumValueInvention = 0;
	public static int cntInvention = 0;
	
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
		buildValue = 0;
		pMonopoly = 0.08;
		pInvention = 0.08;
		pKnight = 0.56;
		pPlusPoint = 0.2;
		pTwoRoad = 0.08;
	}
	/**
	 * Calculates the combined values of all development
	 * cards at the current state of the game. Refresh
	 * should be called before, otherwise it may return
	 * with an outdated value
	 * @return - the value of a development card
	 * @author Gergely Olah
	 */
	public double getBuildValue(){
		return buildValue;
		//refresh();
		/*calculateProbabilities();
		double inv = calculateInventionValue();
		double kni = calculateKnightValue();
		double mon = calculateMonopolyValue();
		double pls = calculatePlusPointValue();
		double two = calculateTwoRoadValue();
		/*System.out.println("-");
		System.out.println("Invention value: " + inv + ", prob: " + pInvention);
		System.out.println("Knight value: " + kni + ", prob: " + pKnight);
		System.out.println("Monopoly value: " + mon + ", prob: " + pMonopoly);
		System.out.println("PlusPoint value: " + pls + ", prob: " + pPlusPoint);
		System.out.println("TwoRoad value: " + two + ", prob: " + pTwoRoad);
		double buildValue =
				pInvention * calculateInventionValue() + 
				pKnight * calculateKnightValue() + 
				pMonopoly * calculateMonopolyValue() + 
				pPlusPoint * calculatePlusPointValue() +
				pTwoRoad * calculateTwoRoadValue();
		//System.out.println("Development value: " + buildValue);
		//System.out.println("-");
			sumValue += buildValue;
			cnt++;
			if(buildValue > maxValue){
				maxValue = buildValue;
				/*System.out.println("-");
				System.out.println("Invention value: " + inv + ", prob: " + pInvention);
				System.out.println("Knight value: " + kni + ", prob: " + pKnight);
				System.out.println("Monopoly value: " + mon + ", prob: " + pMonopoly);
				System.out.println("PlusPoint value: " + pls + ", prob: " + pPlusPoint);
				System.out.println("TwoRoad value: " + two + ", prob: " + pTwoRoad);
				System.out.println("Development value: " + buildValue);
			}
			if(buildValue < minValue)
				minValue = buildValue;
		return buildValue;*/
	}
	public void refresh(){
		calculateProbabilities();
		buildValue =
				pInvention * calculateInventionValue() + 
				pKnight * calculateKnightValue() + 
				pMonopoly * calculateMonopolyValue() + 
				pPlusPoint * calculatePlusPointValue() +
				pTwoRoad * calculateTwoRoadValue();
		// for testing, collects statistics of build value
		if(buildValue > 0){
			sumValue += buildValue;
			cnt++;
			if(buildValue > maxValue)
				maxValue = buildValue;
			if(buildValue < minValue)
				minValue = buildValue;
		}
	}
	/**
	 * Calculates the value of getting a single two-road development card.
	 * @return - the value of a two-road development card
	 * @author Gergely Olah
	 */
	private double calculateTwoRoadValue(){
		BuildRoad r = new BuildRoad(map, owner, aiPlayer, otherPlayers);
		r.refresh();
		int dif = r.calculateMaxRoadDifference();
		int difVal = 1;
		if(dif < 2 && dif >= -2)
			difVal = 5;
		else if(dif < -2)
			difVal = 3;
		double result = r.getBuildValue() + difVal;
		// for testing, collects statistics of build value
		if(result > 0){
			sumValueTwoRoad += result;
			cntTwoRoad++;
			if(result > maxValueTwoRoad)
				maxValueTwoRoad = result;
			if(result < minValueTwoRoad)
				minValueTwoRoad = result;
		}
		return result;
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
		Map<Resource, Material> res = owner.getResources();
		double minResourceFrequency = res.get(Resource.Brick).personalFrequency();
		for(Map.Entry<Resource, Material> it : res.entrySet()){
			double currentFrequency = it.getValue().personalFrequency();
			if(!it.getKey().equals(Resource.Desert) && currentFrequency < minResourceFrequency){
				minResourceFrequency = currentFrequency;
			}
		}
		double result = 0;
		// the return value is between 0 and 10
		if(minResourceFrequency > 0.025)
			result = Math.max(3, 0.25 / minResourceFrequency);
		else
			result = 8;
		
		// for testing, collects statistics of build value
		if(result > 0){
			sumValueInvention += result;
			cntInvention++;
			if(result > maxValueInvention)
				maxValueInvention = result;
			if(result < minValueInvention)
				minValueInvention = result;
		}
		return result;
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
			double currentValue = 0;
			// handling division by zero
			try{
				if(it.getValue().personalFrequency() > 0.025)
					currentValue = Math.min(7, 0.2 * it.getValue().personalValue() * it.getValue().globalFrequency() / it.getValue().personalFrequency());
			} catch(IllegalArgumentException e){
				currentValue = 0;
			}
			if(maxMaterialValue < currentValue){
				maxMaterialValue = currentValue;
			}
		}
		// for testing, collects statistics of build value
		if(maxMaterialValue > 0){
			sumValueMonopoly += maxMaterialValue;
			cntMonopoly++;
			if(maxMaterialValue > maxValueMonopoly)
				maxValueMonopoly = maxMaterialValue;
			if(maxMaterialValue < minValueMonopoly)
				minValueMonopoly = maxMaterialValue;
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
		double result = robbed * difVal + 2.5;
		// for testing, collects statistics of build value
		if(result > 0){
			sumValueKnight += result;
			cntKnight++;
			if(result > maxValueKnight)
				maxValueKnight = result;
			if(result < minValueKnight)
				minValueKnight = result;
		}
		return result;
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
		/*if(aiPlayer.getPlayedDevelopmentCards() != null){
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
		}*/
		int monopolyCntCheat = 0;
		int inventionCntCheat = 0;
		int knightCntCheat = 0;
		int plusPointCntCheat = 0;
		int twoRoadCntCheat = 0;
		int allCntCheat = 0;
		for(Player player : otherPlayers){
			for(DevCard dc : player.getDevCards()){
				if(dc.getClass().equals(KnightCard.class)){
					knightCntCheat++;
				} else if(dc.getClass().equals(MonopolyCard.class)){
					monopolyCntCheat++;
				} else if(dc.getClass().equals(RoadBuildingCard.class)){
					twoRoadCntCheat++;
				} else if(dc.getClass().equals(VictoryPointCard.class)){
					plusPointCntCheat++;
				} else {
					inventionCntCheat++;
				}
				allCnt++;
				allCntCheat++;
			}
		}
		//allCnt += aiPlayer.getDevCards().size();
		
		if(allCnt == 25){
			pKnight = 0;
			pInvention = 0;
			pMonopoly = 0;
			pTwoRoad = 0;
			pPlusPoint = 0;
		}
		// to avoid divide by zero
		else if(allCnt > 0){
			pKnight = Math.max(14 - knightCnt - knightCntCheat, 0) / (25.0 - allCnt);
			pInvention = Math.max(2 - inventionCnt - inventionCntCheat, 0) / (25.0 - allCnt);
			pMonopoly = Math.max(2 - monopolyCnt - monopolyCntCheat, 0) / (25.0 - allCnt);
			pTwoRoad = Math.max(2 - twoRoadCnt - twoRoadCntCheat, 0) / (25.0 - allCnt);
			pPlusPoint = Math.max(5 - plusPointCnt - plusPointCntCheat, 0) / (25.0 - allCnt);
		}
		
		if(allCnt > 25 || pKnight > 1 || pMonopoly > 1 || pInvention > 1 || pPlusPoint > 1 || pTwoRoad > 1){
			System.out.println("");
			System.out.println("pKnight: " + pKnight + "\t" + "pMonopoly: " + pMonopoly + "\t" + "pInvention: " + pInvention + "\t" + "pPlusPoint: " + pPlusPoint + "\t" + "pTwoRoad: " + pTwoRoad);
			System.out.println("allCnt: " + allCnt + "\t" + "knightCnt: " + knightCnt + "\t" + "inventionCnt: " + inventionCnt + "\t" + "monopolyCnt: " + monopolyCnt + "\t" + "twoRoadCnt: " + twoRoadCnt + "\t" + "plusPointCnt: " + plusPointCnt);
			System.out.println("allCntCheat: " + allCntCheat + "\t" + "knightCntCheat: " + knightCntCheat + "\t" + "inventionCntCheat: " + inventionCntCheat + "\t" + "monopolyCntCheat: " + monopolyCntCheat + "\t" + "twoRoadCntCheat: " + twoRoadCntCheat + "\t" + "plusPointCntCheat: " + plusPointCntCheat);
			for(Player player : otherPlayers){
				System.out.println("Devcards: " + player.getDevCards().size());
				System.out.println("Played devcards: " + player.getPlayedDevelopmentCards().size());
			}
			System.out.println("");
		}
	}
}

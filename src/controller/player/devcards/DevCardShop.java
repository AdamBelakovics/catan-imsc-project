package controller.player.devcards;

import java.util.ArrayList;

import controller.player.OutOfRangeException;

public class DevCardShop {
	static ArrayList<DevCard> devCardList = new ArrayList<DevCard>();
	
	/**
	 * Initialize DevCardList to:	- 14 KnightCard
	 * 								- 5 VictoryPointCard
	 *								- 2 MonopolyCard
	 *								- 2 YearOfPlentyCard
	 *								- 2 RoadBuildingCard
	 */
	
	
	static public void initializeShop(){
		for(int i = 0; i < 14; i++){
			devCardList.add(new KnightCard());
		}
		for(int i = 0; i < 5; i++){
			devCardList.add(new VictoryPointCard());
		}
		for(int i = 0; i < 2; i++){
			devCardList.add(new MonopolyCard());
			devCardList.add(new YearOfPlentyCard());
			devCardList.add(new RoadBuildingCard());
		}
	}
	
	/**
	 * Return a random card from devCardList then remove it.
	 * @throws OutOfRangeException if devCardList is empty
	 * @return random DevCard from devCardList
	 */
	
	public static DevCard buyDevCard() {
		if(!devCardList.isEmpty()){
			DevCard d = devCardList.remove((int)((Math.random()*devCardList.size())));
			return d;
		}
		return null;
	}
	
	/**
	 * Getter for devCardList is Empty
	 * @return devCardList is Empty
	 */
	static public boolean isShopEmpty(){
		return devCardList.isEmpty();
	}
	
	
}

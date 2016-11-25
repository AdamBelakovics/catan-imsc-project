package controller.player.devcards;

import java.util.ArrayList;

import controller.player.OutOfRangeException;

public class DevCardShop {
	static ArrayList<DevCard> devCardList;
	
	/**
	 * Initialize DevCardList to:	- 25 KnightCard
	 * 								- 5 VictoryPointCard
	 *								- 2 MonopolyCard
	 *								- 2 YearOfPlentyCard
	 *								- 2 RoadBuildingCard
	 */
	public DevCardShop(){
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
	
	public static DevCard buyDevCard() throws OutOfRangeException{
		if(devCardList.isEmpty()) throw new OutOfRangeException("DevCardList is empty.");
		DevCard d = devCardList.remove((int)((Math.random()*devCardList.size())));
		return d;
	}
	
	
}

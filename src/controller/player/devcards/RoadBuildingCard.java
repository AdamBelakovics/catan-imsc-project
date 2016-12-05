package controller.player.devcards;

import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.Resource;

public class RoadBuildingCard extends DevCard {
	/**
	 * Get 2-2 Brick and Lumber, Player can spend them to 2 roads
	 */
	@Override
	public void doCard(Player p) {
		try {
			p.incResourceAmount(Resource.Brick, 2);
		} catch (OutOfRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			p.incResourceAmount(Resource.Lumber, 2);
		} catch (OutOfRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package controller.player.devcards;

import controller.player.GameEndsException;
import controller.player.Player;
import controller.player.Resource;

abstract public class DevCard {

	/**
	 * 2 types of doCard() methods, for different parameters
	 * @throws GameEndsException  KnightCard can increase player's point
	 *
	 */
	public void doCard(Player p, Resource r) throws GameEndsException {
		
	}		
}


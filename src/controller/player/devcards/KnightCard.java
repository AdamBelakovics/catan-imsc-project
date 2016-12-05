package controller.player.devcards;

import controller.player.Player;
import controller.player.Resource;

public class KnightCard extends DevCard{
	
	/**
	 * Increases player's ActiveKnights then player can handle the thief.
	 */
	@Override
	public void doCard(Player p, Resource r) {
		p.incActiveKnights(1);
		p.handleThief();
	}

}

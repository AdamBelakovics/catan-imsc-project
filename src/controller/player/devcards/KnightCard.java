package controller.player.devcards;

import controller.player.Player;

public class KnightCard extends DevCard{
	
	/**
	 * Increases player's ActiveKnights then player can handle the thief.
	 */
	@Override
	public void doCard(Player p) {
		p.incActiveKnights(1);
		p.handleThief();
	}

}

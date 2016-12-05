package controller.player.devcards;

import controller.player.GameEndsException;
import controller.player.Player;
import controller.player.Resource;

public class VictoryPointCard extends DevCard{
	
	@Override
	public void doCard(Player p) {
		try {
			p.incPoints(1);
		} catch (GameEndsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

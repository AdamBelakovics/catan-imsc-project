package controller.player.devcards;

import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.Resource;

public class YearOfPlentyCard extends DevCard{
	
	@Override
	public void doCard(Player p, Resource r) {
		try {
			p.incResourceAmount(r, 2);
		} catch (OutOfRangeException e) {
			e.printStackTrace();
		}
		
	}

}

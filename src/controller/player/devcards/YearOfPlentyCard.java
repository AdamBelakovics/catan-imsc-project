package controller.player.devcards;

import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.Resource;

public class YearOfPlentyCard extends DevCard{
	
	@Override
	public void doCard(Player p) {
		int rnd = (int)(Math.random()*5);
		Resource randRes = Resource.values()[rnd];
		try {
			p.incResourceAmount(randRes, 2);
		} catch (OutOfRangeException e) {
			e.printStackTrace();
		}
		
	}

}

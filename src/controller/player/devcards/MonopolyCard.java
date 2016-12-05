package controller.player.devcards;

import controller.Game;
import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.Resource;

public class MonopolyCard extends DevCard {
	
	@Override
	public void doCard(Player p) {
		int rnd = (int)(Math.random()*5);
		Resource randRes = Resource.values()[rnd];
		for(int i = 0; i < Game.players.size(); i++){
			Player a = Game.players.get(i);
			if(!a.equals(p)){
				int resAmount = a.getResourceAmount(randRes);
				try {
					a.decResourceAmount(randRes, resAmount);
				} catch (OutOfRangeException e) {
					e.printStackTrace();
				}
				try {
					p.incResourceAmount(randRes, resAmount);
				} catch (OutOfRangeException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

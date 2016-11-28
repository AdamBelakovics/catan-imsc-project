package controller.player.devcards;

import controller.Game;
import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.Resource;

public class MonopolyCard extends DevCard {
	
	@Override
	public void doCard(Player p, Resource r) {
		for(int i = 0; i < Game.players.size(); i++){
			Player a = Game.players.get(i);
			if(!a.equals(p)){
				int resAmount = a.getResourceAmount(r);
				try {
					a.decResourceAmount(r, resAmount);
				} catch (OutOfRangeException e) {
					e.printStackTrace();
				}
				try {
					p.incResourceAmount(r, resAmount);
				} catch (OutOfRangeException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

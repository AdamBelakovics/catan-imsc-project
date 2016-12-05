package controller.player.devcards;

import controller.Game;
import controller.player.GameEndsException;
import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.Resource;

public class KnightCard extends DevCard{
	
	/**
	 * Increases player's ActiveKnights then player can handle the thief.
	 * @throws GameEndsException 
	 */
	@Override
	public void doCard(Player p) throws GameEndsException {
		boolean anyoneHaveBiggestArmy = false;
		p.incActiveKnights(1);
		p.handleThief();
		if(p.getActiveKnights()>2 && p.isBiggestArmy() == false){
			for(int i = 0; i < Game.players.size(); i++){
				Player a = Game.players.get(i);
				if(a.isBiggestArmy()){
					anyoneHaveBiggestArmy = true;
				}
			}
			if(!anyoneHaveBiggestArmy){
				p.setBiggestArmy(true);
				p.incPoints(2);
			}
			for(int i = 0; i < Game.players.size(); i++){
				Player a = Game.players.get(i);
				
				if(anyoneHaveBiggestArmy && a.isBiggestArmy() && a.getActiveKnights() < p.getActiveKnights()){
					a.setBiggestArmy(false);
					try {
						a.decPoints(2);
					} catch (OutOfRangeException e) {
						e.printStackTrace();
					}
					p.incPoints(2);
				}
			}
		}
	}

}

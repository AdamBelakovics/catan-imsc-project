package ai;

import java.util.ArrayList;
import java.util.Comparator;

import controller.map.Buildable;
import controller.map.Table;
import controller.player.Player;
import controller.player.PlayerController;

public class BuildableComparator implements Comparator<Buildable> {

	Table board;
	Player me;
	AiController ac;
	ArrayList<Player> playerList;
	
	public BuildableComparator(Table board, Player me, AiController pc, ArrayList<Player> playerList) {
		super();
		this.board = board;
		this.me = me;
		this.ac = pc;
		this.playerList = playerList;
	}

	@Override
	public int compare(Buildable arg0, Buildable arg1) {
		if((arg0 == Buildable.Settlement && arg1 == Buildable.City) || (arg1 == Buildable.Settlement && arg0 == Buildable.City)){
			BuildVillage bv = new BuildVillage(board, ac, me, playerList);
			BuildCity bc = new BuildCity(board, ac, me, playerList);
			return Double.compare(bv.getBuildValue(), bc.getBuildValue());
		}else if((arg0 == Buildable.Settlement && arg1 == Buildable.Road) || (arg1 == Buildable.Settlement && arg0 == Buildable.Road)){
			BuildVillage bv = new BuildVillage(board, ac, me, playerList);
			BuildRoad br = new BuildRoad(board, ac, me, playerList);
			return Double.compare(bv.getBuildValue(), br.getBuildValue());
		}else if((arg0 == Buildable.City && arg1 == Buildable.Road) || (arg1 == Buildable.City && arg0 == Buildable.Road)){
			BuildCity bc = new BuildCity(board, ac, me, playerList);
			BuildRoad br = new BuildRoad(board, ac, me, playerList);
			return Double.compare(bc.getBuildValue(), br.getBuildValue());
		}
		return 0;
	}
	

}

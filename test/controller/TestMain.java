package controller;

import java.util.ArrayList;

import controller.map.Buildable;
import controller.map.Table;
import controller.player.GameEndsException;
import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.Resource;

public class TestMain {

	public static void main(String[] args) throws OutOfRangeException {
		Table t = new Table();
		Player p = new Player("Bela", 0, t);
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(p);
		Game theGame = new Game(t, players);
		p.incResourceAmount(Resource.Lumber, 100);
		p.incResourceAmount(Resource.Brick, 100);
		p.incResourceAmount(Resource.Wool, 100);
		p.incResourceAmount(Resource.Grain, 100);
		try {
			p.build(Buildable.Settlement, t.getNodes().get(0));
			p.build(Buildable.Settlement, t.getNodes().get(4));
			p.build(Buildable.Settlement, t.getNodes().get(5));
			p.build(Buildable.Settlement, t.getNodes().get(6));
			p.build(Buildable.Settlement, t.getNodes().get(7));
		} catch (GameEndsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(p.getResourceAmount(Resource.Lumber));
		System.out.println(p.getResourceAmount(Resource.Brick));
		System.out.println(p.getResourceAmount(Resource.Wool));
		System.out.println(p.getResourceAmount(Resource.Grain));
	}

}

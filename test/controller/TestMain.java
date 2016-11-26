package controller;

import java.util.ArrayList;

import controller.map.Table;

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
		System.out.println(Game.table.hexList.get(0).neighbourHexes.get(2).getResource());
	}

}

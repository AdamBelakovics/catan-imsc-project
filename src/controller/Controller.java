package controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import ai.AiController;
import controller.map.Table;
import controller.player.Player;
import ux.graphics.Renderer;
import ux.ui.UIController;

public class Controller {
	public static void main(String[] args){
		//MENÜ létrehoz
		//MENÜnek átadjuk az irányítást
		//MENÜ visszatér TODO kezelni
		Table gameTable = new Table();
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(new Player("Alfonz", 0, gameTable));
		players.add(new Player("Bela", 1, gameTable));
		players.add(new Player("Cela", 2,  gameTable));
		players.add(new Player("Dalma", 3, gameTable));
		players.get(0).setPlayerController(new AiController(gameTable, players.get(0), players));
		players.get(1).setPlayerController(new AiController(gameTable, players.get(1), players));
		players.get(2).setPlayerController(new AiController(gameTable, players.get(2), players));
		players.get(3).setPlayerController(new UIController());
		Renderer renderer = new Renderer((UIController) players.get(3).getPlayerController(), gameTable, 800, 600);
		while(true){
			for(Player p : players)
				p.controller.turn();
		}
	}
}


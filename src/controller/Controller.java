package controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import ai.AiController;
import controller.map.Hex;
import controller.map.Table;
import controller.player.Player;
import controller.player.Resource;
import ux.Renderer;
import ux.ui.UIController;

public class Controller {
	public static void main(String[] args) throws InterruptedException{
		//MENĂś lĂ©trehoz
		//MENĂśnek Ăˇtadjuk az irĂˇnyĂ­tĂˇst
		//MENĂś visszatĂ©r TODO kezelni
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
		for(Hex h : gameTable.hexList){
			h.setResource(Resource.values()[(int) (Math.random()*5)]);		
		}
		while(true){
			for(Player p : players)
				p.controller.turn();
		}
	}
}


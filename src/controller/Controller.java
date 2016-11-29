package controller;

import java.io.File;
import java.util.ArrayList;

import java.util.Map;
import java.util.TreeMap;

import ai.AiController;
import controller.map.Hex;
import controller.map.MapXMLParser;
import controller.map.Table;
import controller.player.GameEndsException;
import controller.player.Player;
import controller.player.PlayerController;
import controller.player.Resource;
import ux.Renderer;
import ux.ui.UIController;

public class Controller {
	public static void main(String[] args) throws InterruptedException{
		Table board = new Table();
		MapXMLParser.readCatanMap(new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "catan_base_map.xml"), board);
		
		Player AI01 = new Player("AI01", 01, board);
		Player AI02 = new Player("AI02", 02, board);
		Player AI03 = new Player("AI03", 03, board);
		Player HUMAN = new Player("HUMAN", 04, board);
		
		ArrayList<Player> playerList = new ArrayList<Player>();
		playerList.add(AI01);
		playerList.add(AI02);
		playerList.add(AI03);
		playerList.add(HUMAN);
		
		Game game = new Game(board, playerList);
		
		AiController AICONT01 = new AiController(board, AI01, playerList);
		AiController AICONT02 = new AiController(board, AI02, playerList);
		AiController AICONT03 = new AiController(board, AI03, playerList);
		
		UIController HUMCONT = new UIController(HUMAN);
		
		ArrayList<PlayerController> pclist = new ArrayList<PlayerController>();
		pclist.add(HUMCONT);
		pclist.add(AICONT01);
		pclist.add(AICONT02);
		pclist.add(AICONT03);
		
		AI01.setPlayerController(AICONT01);
		AI02.setPlayerController(AICONT02);
		AI03.setPlayerController(AICONT03);
		HUMAN.setPlayerController(HUMCONT);
		
		Renderer rend = new Renderer(HUMCONT, board, 1024, 768);
		
		for(int i = 0; i < pclist.size(); i++){
			Thread.sleep(100);
			pclist.get(i).firstturn();
		}
		
		for(int i = pclist.size()-1; i >= 0; i--){
			Thread.sleep(100);
			pclist.get(i).firstturn();
		}
		
		int i = 0;
		while(true){
			for(PlayerController pc : pclist){
				try {
					Thread.sleep(100);
					pc.turn();
				} catch (GameEndsException e) {
					e.printStackTrace();
					
				}
			}
		}
	}
}


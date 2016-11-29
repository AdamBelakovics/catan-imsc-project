package controller;

import java.io.File;

import java.util.ArrayList;

import controller.map.MapXMLParser;
import controller.map.Table;
import controller.player.GameEndsException;
import controller.player.Player;
import controller.player.PlayerController;
import ux.Renderer;
import ux.ui.UIController;

/**
 * Alternate Controller for the game.
 * This class creates a basic game with four different players. They are controlled through
 * the UI.
 * @author AdamBelakovics
 *
 */
public class HotSeatController {
	public static void main(String[] args) throws InterruptedException{
		Table board = new Table();
		MapXMLParser.readCatanMap(new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "catan_base_map.xml"), board);
		ArrayList<Player> playerList = new ArrayList<Player>();
		Game game = new Game(board, playerList);
		ArrayList<UIController> pclist = new ArrayList<UIController>();
		for (int i = 0; i < 4; i++) {
			playerList.add(new Player("Belam", i, board));
			pclist.add(new UIController(playerList.get(i)));
			playerList.get(i).setPlayerController(pclist.get(i));
			Renderer rend = new Renderer(pclist.get(i), board, 700, 400);
		}
		for(int i = 0; i < pclist.size(); i++){
			pclist.get(i).firstturn();
		}
		for(int i = pclist.size()-1; i > 0; i--){
			pclist.get(i).firstturn();
		}
		while(true){
			for(PlayerController pc : pclist){
				try {
					pc.turn();
				} catch (GameEndsException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

package controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import controller.map.Table;
import controller.player.Player;
import ux.ui.UIController;

public class Controller {
	public static void main(String[] args){
		//MENÜ létrehoz
		//MENÜnek átadjuk az irányítást
		//MENÜ visszatér TODO kezelni
		Table gameTable = new Table();
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(new Player("Alfonz", 0, null));
		
	}
}


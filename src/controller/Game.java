package controller;

import java.util.ArrayList;

import controller.map.Table;
import controller.player.Player;


/**
 * class Game is for storing vital information about the current Game. It stores static reference to
 * Table and the List of all Players
 * @author AdamBelakovics
 *
 */
public class Game {
	public static Table table;
	public static ArrayList<Player> players;
	public static int turn_number;
	
	public Game(Table t, ArrayList<Player> p){
		table = t;
		players = p;
		turn_number = 0;
	}
}

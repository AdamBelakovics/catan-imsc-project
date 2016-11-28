package controller.player;

import controller.map.TableElement;

abstract public class Building {
	private final Player owner;
	
	public Building(Player p){
		owner = p;
	}
	
	public Player getOwner(){
		return owner;
	}
	
	abstract public boolean build(TableElement t);
	
}

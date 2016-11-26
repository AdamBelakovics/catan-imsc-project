package controller.player;

abstract public class Building {
	private final Player owner;
	
	public Building(Player p){
		owner = p;
	}
	
	public Player getOwner(){
		return owner;
	}
	
}

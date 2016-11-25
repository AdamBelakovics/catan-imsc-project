package controller.player;

abstract public class Building {
	Player owner;
	
	public Player getOwner(){
		return owner;
	}
	
	void setOwner(Player p){
		owner = p;
	}
	
}

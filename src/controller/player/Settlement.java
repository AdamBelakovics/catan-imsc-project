package controller.player;

public class Settlement extends Building{
	Player owner;
	
	public Player getOwner(){
		return owner;
	}
	
	public void setOwner(Player p){
		owner = p;
	}
}

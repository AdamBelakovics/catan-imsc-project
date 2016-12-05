package controller.player;

public class GameEndsException extends Exception {
	int playerId;
	
	public GameEndsException(int id){
		playerId = id;
	}
	
	public int getPlayerID(){
		return playerId;
	}
}

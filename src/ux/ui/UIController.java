package ux.ui;

import java.util.ArrayList;
import java.util.Map;

import controller.player.Player;
import controller.player.PlayerController;
import controller.player.Resource;
import controller.player.devcards.*;
import ux.FirstTurnState;

public class UIController extends PlayerController {
	
	public Player controlledPlayer;
	public volatile boolean active;
	public volatile boolean firstturnactive;

	public FirstTurnState state=FirstTurnState.NULL;
	
	public UIController(Player _controlledPlayer) {
		controlledPlayer=_controlledPlayer;
	}

	public int getPlayerResource(Resource r) {
		return controlledPlayer.getResourceAmount(r);
	}
	
	public int getPoints(){
		//TODO
		return 0;
	}
	
	public int getActiveKnights(){
		//TODO
		return 0;
	}
	
	public ArrayList<DevCard> getDevCards(){
		return controlledPlayer.getDevCards();
	}
	
	public ArrayList<DevCard> getPlayedDevCards(){
		return controlledPlayer.getPlayedDevelopmentCards();
	}
		
	@Override
	public void turn() {
		controlledPlayer.rollTheDice();
		active=true;
		while (active);
	}
	
	@Override
	public boolean query(Player donor, Map<Resource, Integer> offer, Map<Resource, Integer> demand) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public UIController() {
		active=false;
	}

	@Override
	public void firstturn() {
		firstturnactive = true;
		state=FirstTurnState.STARTED;
		while (firstturnactive);
		state=FirstTurnState.NULL;
	}
}

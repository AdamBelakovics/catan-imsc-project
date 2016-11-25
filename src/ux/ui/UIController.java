package ux.ui;

import java.util.ArrayList;
import java.util.Map;

import controller.player.Player;
import controller.player.PlayerController;
import controller.player.Resource;
import controller.player.devcards.*;

public class UIController extends PlayerController {
	
	Player controlledPlayer;
	
	public void setControlledPlayer(Player _controlledPlayer) {
		controlledPlayer=_controlledPlayer;
	}

	public int getPlayerResource(Resource r) {
		//TODO
		return 10+r.ordinal();
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
		//TODO
		ArrayList<DevCard> tempArrayList=new ArrayList();
		for (int i=0;i<2;i++)
			tempArrayList.add(new KnightCard());
		
		return tempArrayList;
	}
	
	public ArrayList<DevCard> getPlayedDevCards(){
		//TODO
		ArrayList<DevCard> tempArrayList=new ArrayList();
		for (int i=0;i<2;i++)
			tempArrayList.add(new KnightCard());
		
		return tempArrayList;
	}
		

	@Override
	public void turn() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean query(Player donor, Map<Resource, Integer> offer, Map<Resource, Integer> demand) {
		// TODO Auto-generated method stub
		return false;
	}

}

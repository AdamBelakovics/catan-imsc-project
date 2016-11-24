package ux.ui;

import java.util.ArrayList;
import java.util.Map;

import controller.player.*;

public class UIController extends PlayerController {

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
		for (int i=0;i<8;i++)
			tempArrayList.add(new DevCard());
		
		return tempArrayList;
	}
	
	public ArrayList<DevCard> getPlayedDevCards(){
		//TODO
		ArrayList<DevCard> tempArrayList=new ArrayList();
		for (int i=0;i<5;i++)
			tempArrayList.add(new DevCard());
		
		return tempArrayList;
	}
		
	@Override
	public boolean query(Player donor, Map<Resource, Integer> offer, Map<Resource, Integer> demand) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void turn() {
		// TODO Auto-generated method stub
	}

}

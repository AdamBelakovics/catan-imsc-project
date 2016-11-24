package controller.player;

import java.util.Map;

import controller.map.*;

public abstract class PlayerController {
	Player controllerPlayer;
	Table board;
	abstract public boolean query(Player donor, Map<Resource,Integer> offer, Map<Resource,Integer> demand);
	abstract public void turn();
	
}

package ux.graphics;

import controller.map.*;
import controller.player.*;

public class Renderer {
	Table board;
	public void draw(){}; // minden k�r v�g�n �jrarajzol�s
	public void updateDice(Dice dice){};
	public void updateTableElement(TableElement element){};
	public void updateDevCards(Player actualPlayer){};
	public void updateResources(Player actualPlayer){};
	public void updateThief(Hex to) {};
}

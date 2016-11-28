package controller.map;

import controller.player.Building;

public interface TableElement {
	public boolean isBuildPossible(Building what);

	abstract public void setBuilding(Building b);

	public Building getBuilding();
}

package controller.map;

import controller.player.Building;
import controller.player.Road;

public interface TableElement {
	public boolean isBuildPossible(Building what);

	abstract public void setBuilding(Building b);

	public Building getBuilding();

	public boolean isFirstBuildPossible(Building r);
}

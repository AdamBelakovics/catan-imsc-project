package controller.player;

import controller.map.Edge;
import controller.map.Hex;
import controller.map.TableElement;
import controller.map.Vertex;

public class Road extends Building {

	public Road(Player p) {
		super(p);
	}

	@Override
	public boolean build(TableElement t) {
		if(t.getBuilding() == null && t.isBuildPossible(this) && t.getClass().equals(Edge.class)){
			t.setBuilding(this);
			return true;
		}
		return false;
	}

}

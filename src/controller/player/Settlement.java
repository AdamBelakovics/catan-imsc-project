package controller.player;

import controller.map.TableElement;
import controller.map.Vertex;

public class Settlement extends Building{

	public Settlement(Player p) {
		super(p);
	}

	@Override
	public boolean build(TableElement t) {
		if(t.getBuilding() == null && t.isBuildPossible(this) && t.getClass().equals(Vertex.class)){
			t.setBuilding(this);
		}
		return false;
	}

}

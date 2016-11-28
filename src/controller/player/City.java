package controller.player;

import controller.map.TableElement;
import controller.map.Vertex;

public class City extends Building{

	public City(Player p) {
		super(p);
	}

	@Override
	public boolean build(TableElement t) {
			if(t.getBuilding().getClass().equals(Settlement.class) && t.isBuildPossible(this) && t.getClass().equals(Vertex.class)){
				t.setBuilding(this);
			}
		return false;
	}

}

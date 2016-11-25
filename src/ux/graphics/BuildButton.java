package ux.graphics;

import java.util.HashMap;

import controller.player.Building;
import controller.player.Resource;

public class BuildButton extends Button {

	Building building;
	HashMap<Resource,Integer> buildCost;
	
	BuildButton(String _text, Building _building, int _x, int _y, int _height, int _width) {
		super(_text, _x, _y, _height, _width);
		building=_building;
		buildCost=ResourceXMLReader.readResourceXML("resources.xml", building);
		for (HashMap.Entry e : buildCost.entrySet()) {
			System.out.println(e.getKey()+" "+e.getValue());
		}
	}
	
	@Override
	public void press() {
		System.out.println(this + " pressed");
		//TODO
	}
}

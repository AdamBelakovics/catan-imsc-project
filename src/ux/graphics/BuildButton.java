package ux.graphics;

import java.util.HashMap;

import controller.player.Building;
import controller.player.Resource;

public class BuildButton extends Button {

	BuildingEnum building;
	HashMap<Resource,Integer> buildCost;
	
	BuildButton(String _text, BuildingEnum _building, int _x, int _y, int _height, int _width) {
		super(_text, _x, _y, _height, _width);
		building=_building;
		buildCost=ResourceXMLReader.readResourceXML("resources.xml", building);
	}
	
	@Override
	public void press() {
		System.out.println(this + " pressed");
		//TODO
	}
}

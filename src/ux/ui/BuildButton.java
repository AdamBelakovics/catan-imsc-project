package ux.ui;

import java.util.HashMap;

import controller.player.Building;
import controller.player.Resource;
import ux.BuildingEnum;
import ux.ResourceXMLReader;

public class BuildButton extends Button {

	public BuildingEnum building;
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

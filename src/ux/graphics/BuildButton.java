package ux.graphics;

import controller.player.Building;

public class BuildButton extends Button {

	Building building;
	
	BuildButton(String _text, Building building, int _x, int _y, int _height, int _width) {
		super(_text, _x, _y, _height, _width);
	}
	
	@Override
	public void press() {
		selected=true;
		//TODO
	}
}

package ux.ui;

import controller.player.NotEnoughResourcesException;
import ux.RendererDataStore;

public class DevButton extends Button {
	RendererDataStore ds;

	DevButton(RendererDataStore _ds, String _text, int _x, int _y, int _width, int _height) {
		super(_text, _x, _y, _width, _height);
		ds=_ds;
	}
	
	@Override
	public void press() {
		System.out.println("Pressed devbutton");
		try {
			ds.currUIC.controlledPlayer.buyDevCard();
		} catch (NotEnoughResourcesException e) {}
	}

}

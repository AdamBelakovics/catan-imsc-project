package ux.ui;

import ux.RendererDataStore;

public class TradeButton extends Button {
	
	RendererDataStore ds;
	TradeButton(RendererDataStore _ds, String _text, int _x, int _y, int _width, int _height) {
		super(_text, _x, _y, _width, _height);
		ds=_ds;
	}
	
	@Override
	void press() {
		ds.changeActive=!(ds.changeActive);
	}

}

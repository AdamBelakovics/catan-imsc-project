package ux.ui;

import ux.FirstTurnState;

public class EndTurnButton extends Button {
	UIController uiController;

	EndTurnButton(UIController currUIC,String _text, int _x, int _y, int _width, int _height) {
		super(_text, _x, _y, _width, _height);
		uiController=currUIC;
	}
	
	@Override
	public void press() {
		if (uiController.state==FirstTurnState.ROADBUILT || uiController.active) {
			uiController.active=false;
			this.setSelected(false);
			uiController.firstturnactive=false;
		}
	}
}

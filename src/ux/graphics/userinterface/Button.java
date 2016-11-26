package ux.graphics.userinterface;

public abstract class Button {
	int x,y,width,height;
	String text;
	private boolean selected;
	
	Button(String _text, int _x, int _y, int _width, int _height) {
		text=_text;
		x=_x;
		y=_y;
		width=_width;
		height=_height;
		setSelected(false);
	}
	
	void press() {}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	};
}

package ux.graphics;

public abstract class Button {
	int x,y,width,height;
	String text;
	boolean selected;
	
	Button(String _text, int _x, int _y, int _width, int _height) {
		text=_text;
		x=_x;
		y=_y;
		width=_width;
		height=_height;
		selected=false;
	}
	
	void press() {};
}

package ux.graphics;

import java.awt.Graphics;

public abstract class ImageRenderer {
	int width;
	int height;
	ImageRenderer(int _width, int _height) {
		width=_width;
		height=_height;
	}
	protected ImageRenderer parentRenderer;
	public int getWidth() {return width;}
	public int getHeight() {return height; }
	abstract public void paint(Graphics g);
}
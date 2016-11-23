package ux.graphics;

import java.awt.Graphics;

public abstract class ImageRenderer {
	int width;
	int height;
	protected ImageRenderer parentRenderer;
	public int getWidth() {return width;}
	public int getHeight() {return height; }
	abstract public void paint(Graphics g);
}
package ux;

import java.awt.Graphics;

/**
 * Abstract superclass of subrenderers for double buffering
 * @author Kiss Lorinc
 *
 */
public abstract class ImageRenderer {
	protected int width;
	protected int height;
	protected ImageRenderer(int _width, int _height) {
		width=_width;
		height=_height;
	}
	protected ImageRenderer parentRenderer;
	public int getWidth() {return width;}
	public int getHeight() {return height; }
	abstract public void paint(Graphics g);
}
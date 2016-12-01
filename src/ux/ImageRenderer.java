package ux;

import java.awt.Graphics;

/**
 * Abstract superclass of subrenderers for double buffering
 * @author Kiss Lorinc
 *
 */
public abstract class ImageRenderer {
	protected ImageRenderer parentRenderer;
	abstract public void paint(Graphics g);
}
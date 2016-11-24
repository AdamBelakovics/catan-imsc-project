package ux.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class HUDRenderer extends ImageRenderer {
	Graphics2D hudCanvas;
	BufferedImage interfaceImage;
	InterfaceRenderer interfaceRenderer;
	

	public HUDRenderer(int _width, int _height) {
		width=_width;
		height=_height;
		interfaceRenderer=new InterfaceRenderer(width,height);
		interfaceImage=new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	public void paint(Graphics g) {
		hudCanvas=(Graphics2D)g;
		interfaceRenderer.paint(interfaceImage.getGraphics());
		if (interfaceImage!=null) hudCanvas.drawImage(interfaceImage, 0, 0, null);
	}
}

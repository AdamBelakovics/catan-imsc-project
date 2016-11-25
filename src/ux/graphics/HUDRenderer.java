package ux.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ux.ui.UIController;

public class HUDRenderer extends ImageRenderer {
	Graphics2D hudCanvas;
	BufferedImage interfaceImage;
	InterfaceRenderer interfaceRenderer;
	CardRenderer cardRenderer;
	ResourceRenderer resRenderer;
	UIController uiPlayer;
	

	public HUDRenderer(UIController currUIC, int _width, int _height) {
		super(_width,_height);
		uiPlayer=currUIC;
		interfaceRenderer=new InterfaceRenderer(uiPlayer,width,height);
		cardRenderer=new CardRenderer(uiPlayer,width,height);
		resRenderer=new ResourceRenderer(uiPlayer,width,height);
		interfaceImage=new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	public void paint(Graphics g) {
		hudCanvas=(Graphics2D)g;
		interfaceRenderer.paint(interfaceImage.getGraphics());
		cardRenderer.paint(interfaceImage.getGraphics());
		resRenderer.paint(interfaceImage.getGraphics());
		if (interfaceImage!=null) hudCanvas.drawImage(interfaceImage, 0, 0, null);
	}

	public void pressButton(Button selectedButton) {
		interfaceRenderer.pressButton(selectedButton);
		
	}
}

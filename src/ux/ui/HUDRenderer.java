package ux.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ux.ImageRenderer;
import ux.RendererDataStore;
import ux.board.BoardRenderer.BoardOrientation;

public class HUDRenderer extends ImageRenderer {
	Graphics2D hudCanvas;
	BufferedImage interfaceImage;
	public InterfaceRenderer interfaceRenderer;
	public CardRenderer cardRenderer;
	private ResourceRenderer resRenderer;
	private RendererDataStore ds;
	

	public HUDRenderer(RendererDataStore _ds) {
		ds=_ds;

		interfaceRenderer=new InterfaceRenderer(ds);
		cardRenderer=new CardRenderer(ds);
		resRenderer=new ResourceRenderer(ds);
		interfaceImage=new BufferedImage(ds.width, ds.height, BufferedImage.TYPE_INT_ARGB);
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
	
	public void resetInterfaceSelection() {
		interfaceRenderer.deselectAllButtons();
		cardRenderer.deselectDevCards();
	}
}

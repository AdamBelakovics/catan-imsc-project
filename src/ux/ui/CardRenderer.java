package ux.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import controller.player.devcards.DevCard;
import ux.ImageRenderer;
import ux.RendererDataStore;

public class CardRenderer extends ImageRenderer {
	Graphics2D cardCanvas;
	HashMap<DevCard,Rectangle> devCards;
	HashMap<DevCard,Rectangle> usedDevCards;
	RendererDataStore ds;

	DevCard selectedDevCard;
	

	public CardRenderer(RendererDataStore _ds) {
		ds=_ds;
		calculateCardMetrics();
		selectedDevCard=null;
		}

	private void calculateCardMetrics() {
		CardMetrics cardMetrics=new CardMetrics();
		int i=1;
		
		devCards=new HashMap();
		ArrayList<DevCard> devCardsArray=ds.currUIC.getDevCards();
		for (DevCard dc : devCardsArray) {
			devCards.put(dc, 
					new Rectangle(ds.width*13/40+i*ds.width*14/40/(devCardsArray.size()+1)-cardMetrics.width/2, ds.height*17/20-cardMetrics.height/2, cardMetrics.width, cardMetrics.height));
			i++;
		}
		
		usedDevCards=new HashMap();
		i=1;
		ArrayList<DevCard> usedDevCardsArray=ds.currUIC.getPlayedDevCards();
		for (DevCard udc : ds.currUIC.getPlayedDevCards()) {
			usedDevCards.put(udc, 
					new Rectangle(ds.width*13/40+i*ds.width*14/40/(usedDevCardsArray.size()+1)-cardMetrics.width/2, ds.height*15/20-cardMetrics.height/2, cardMetrics.width, cardMetrics.height));		
			i++;
		}
}

	public void paint(Graphics g) {
		cardCanvas=(Graphics2D)g;
		paintCards();
	}

	private void paintCards() {

		//TODO print card info
		
		for (HashMap.Entry<DevCard,Rectangle> dc : devCards.entrySet()) {
			cardCanvas.setColor(
					dc.getKey().equals(selectedDevCard)?
							InterfaceColorProfile.selectedColor:
							ds.currUIC.active?
								InterfaceColorProfile.bgColor:
								InterfaceColorProfile.inactiveColor);
			cardCanvas.fill(dc.getValue());
			String cardText;
			
			StringPainter.printString(cardCanvas, "C", dc.getValue().x+dc.getValue().width/2, dc.getValue().y+dc.getValue().height/2);

			cardCanvas.setColor(InterfaceColorProfile.fgColor);
			cardCanvas.draw(dc.getValue());			
		}
		
		//TODO print card info
		
		for (HashMap.Entry<DevCard,Rectangle> udc : usedDevCards.entrySet()) {
			cardCanvas.setColor(InterfaceColorProfile.bgColor);
			cardCanvas.fill(udc.getValue());

			cardCanvas.setColor(InterfaceColorProfile.fgColor);
			cardCanvas.draw(udc.getValue());			
				}
	}
	
	private void paintOpponentCards() {
		// TODO
	}
	
	public DevCard getDevCardUnderCursor(int x, int y) {
		for (HashMap.Entry<DevCard,Rectangle> dc : devCards.entrySet()) {
			if (dc.getValue().contains(new Point(x,y))) return dc.getKey();
		}
		return null;
	}
	
	public void selectDevCard(DevCard selected) {
		selectedDevCard=selected;
	}
	
	public void deselectDevCards() {
		selectDevCard(null);
	}
	
	private class CardMetrics {
		public final int height=ds.height*1/10-20;
		public final int width=height*11/16;
	}
}

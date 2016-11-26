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

public class CardRenderer extends ImageRenderer {
	Graphics2D cardCanvas;
	UIController uiPlayer;
	HashMap<DevCard,Rectangle> devCards;
	HashMap<DevCard,Rectangle> usedDevCards;

	DevCard selectedDevCard;
	

	public CardRenderer(UIController _uiPlayer, int _width, int _height) {
		super(_width,_height);
		uiPlayer=_uiPlayer;
		calculateCardMetrics();
		selectedDevCard=null;
		}

	private void calculateCardMetrics() {
		CardMetrics cardMetrics=new CardMetrics();
		int i=1;
		
		devCards=new HashMap();
		ArrayList<DevCard> devCardsArray=uiPlayer.getDevCards();
		for (DevCard dc : devCardsArray) {
			devCards.put(dc, 
					new Rectangle(width*13/40+i*width*14/40/(devCardsArray.size()+1)-cardMetrics.width/2, height*17/20-cardMetrics.height/2, cardMetrics.width, cardMetrics.height));
			i++;
		}
		
		usedDevCards=new HashMap();
		i=1;
		ArrayList<DevCard> usedDevCardsArray=uiPlayer.getPlayedDevCards();
		for (DevCard udc : uiPlayer.getPlayedDevCards()) {
			usedDevCards.put(udc, 
					new Rectangle(width*13/40+i*width*14/40/(usedDevCardsArray.size()+1)-cardMetrics.width/2, height*15/20-cardMetrics.height/2, cardMetrics.width, cardMetrics.height));		
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
							uiPlayer.active?
								InterfaceColorProfile.bgColor:
								InterfaceColorProfile.inactiveColor);
			cardCanvas.fill(dc.getValue());

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
		public final int height=CardRenderer.this.height*1/10-20;
		public final int width=height*11/16;
	}
}

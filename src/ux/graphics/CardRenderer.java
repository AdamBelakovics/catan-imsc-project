package ux.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import controller.player.DevCard;
import ux.ui.UIController;

public class CardRenderer extends ImageRenderer {
	Graphics2D cardCanvas;
	UIController uiPlayer;
	ArrayList<DevCard> devCards;
	ArrayList<DevCard> usedDevCards;
	CardMetrics cardMetrics;
	

	public CardRenderer(UIController _uiPlayer, int _width, int _height) {
		super(_width,_height);
		uiPlayer=_uiPlayer;
		devCards=uiPlayer.getDevCards();
		usedDevCards=uiPlayer.getPlayedDevCards();
		cardMetrics=new CardMetrics();
		}

	public void paint(Graphics g) {
		cardCanvas=(Graphics2D)g;
		paintUnusedCards();
		paintUsedCards();
	}

	private void paintUnusedCards() {
		cardCanvas.setColor(Color.black);
		cardCanvas.drawRect(width*3/10, height*8/10, width*4/10, height*1/10);
		int i=0;
		//TODO get card info
		
		for (DevCard c : devCards) {
			cardCanvas.setColor(Color.white);
			cardCanvas.fillRect(width*13/40+i*width*14/40/(devCards.size()-1)-cardMetrics.width/2, height*17/20-cardMetrics.height/2, cardMetrics.width, cardMetrics.height);

			cardCanvas.setColor(Color.black);
			cardCanvas.drawRect(width*13/40+i*width*14/40/(devCards.size()-1)-cardMetrics.width/2, height*17/20-cardMetrics.height/2, cardMetrics.width, cardMetrics.height);
			
			i++;
		}
	}
	
	private void paintUsedCards() {
		cardCanvas.setColor(Color.black);
		cardCanvas.drawRect(width*3/10, height*7/10, width*4/10, height*1/10);
		int i=0;
		
		//TODO get card info
		
		for (DevCard c : usedDevCards) {
			cardCanvas.setColor(Color.white);
			cardCanvas.fillRect(width*13/40+i*width*14/40/(usedDevCards.size()-1)-cardMetrics.width/2, height*15/20-cardMetrics.height/2, cardMetrics.width, cardMetrics.height);

			cardCanvas.setColor(Color.black);
			cardCanvas.drawRect(width*13/40+i*width*14/40/(usedDevCards.size()-1)-cardMetrics.width/2, height*15/20-cardMetrics.height/2, cardMetrics.width, cardMetrics.height);
			
			i++;
		}
	}
	
	private class CardMetrics {
		public final int height=CardRenderer.this.height*1/10-20;
		public final int width=height*11/16;
		
	}
}

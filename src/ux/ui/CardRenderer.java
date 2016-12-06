package ux.ui;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import controller.player.devcards.*;
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
	
	private void refreshCardMap() {
		devCards=new HashMap();
		usedDevCards=new HashMap();
		CardMetrics cardMetrics=new CardMetrics();
		int i=1;
		ArrayList<DevCard> devCardsArray=ds.currUIC.getDevCards();
		for (DevCard dc : devCardsArray) {
			devCards.put(dc, 
					new Rectangle(ds.width*13/40+i*ds.width*14/40/(devCardsArray.size()+1)-cardMetrics.width/2, ds.height*17/20-cardMetrics.height/2, cardMetrics.width, cardMetrics.height));
			i++;
		}
		
		i=1;
		ArrayList<DevCard> usedDevCardsArray=ds.currUIC.getPlayedDevCards();
		for (DevCard udc : ds.currUIC.getPlayedDevCards()) {
			usedDevCards.put(udc, 
					new Rectangle(ds.width*13/40+i*ds.width*14/40/(usedDevCardsArray.size()+1)-cardMetrics.width/2, ds.height*15/20-cardMetrics.height/2, cardMetrics.width, cardMetrics.height));		
			i++;
		}
		
		
	}

	private void calculateCardMetrics() {				
		refreshCardMap();
		
}

	public void paint(Graphics g) {
		cardCanvas=(Graphics2D)g;
		cardCanvas.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		cardCanvas.fillRect(ds.width*3/10, ds.height*7/10, ds.width*4/10, ds.height*2/10);

		//reset composite
		cardCanvas.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		paintCards();
		paintCardInfo();
	}

	private void paintCards() {

		refreshCardMap();
		
		//cardCanvas.setColor(InterfaceColorProfile.bgWaterColor);
		//cardCanvas.fillRect(ds.width*3/10, ds.height*7/10, ds.width*4/10, ds.height*2/10);
		
		for (HashMap.Entry<DevCard,Rectangle> dc : devCards.entrySet()) {
			cardCanvas.setColor(
					dc.getKey().equals(selectedDevCard)?
							InterfaceColorProfile.selectedColor:
							ds.currUIC.active?
								InterfaceColorProfile.bgColor:
								InterfaceColorProfile.inactiveColor);
			cardCanvas.fill(dc.getValue());
			String cardText="C";
			if (dc.getKey() instanceof KnightCard) cardText="KNI";
			if (dc.getKey() instanceof MonopolyCard) cardText="MON";
			if (dc.getKey() instanceof RoadBuildingCard) cardText="RBU";
			if (dc.getKey() instanceof VictoryPointCard) cardText="VIC";
			if (dc.getKey() instanceof YearOfPlentyCard) cardText="YOP";
			
			StringPainter.printString(cardCanvas, cardText, dc.getValue().x+dc.getValue().width/2, dc.getValue().y+dc.getValue().height/2);
			if (dc.getKey().equals(selectedDevCard) && ds.currUIC.active)
				StringPainter.printString(cardCanvas, "Play", dc.getValue().x+dc.getValue().width/2, dc.getValue().y+dc.getValue().height/2+15);
			
			cardCanvas.setColor(InterfaceColorProfile.fgColor);
			cardCanvas.draw(dc.getValue());			
		}
		
		for (HashMap.Entry<DevCard,Rectangle> udc : usedDevCards.entrySet()) {
			cardCanvas.setColor(InterfaceColorProfile.bgColor);
			cardCanvas.fill(udc.getValue());
			
			cardCanvas.fill(udc.getValue());
			String cardText="C";
			if (udc.getKey() instanceof KnightCard) cardText="KNI";
			if (udc.getKey() instanceof MonopolyCard) cardText="MON";
			if (udc.getKey() instanceof RoadBuildingCard) cardText="RBU";
			if (udc.getKey() instanceof VictoryPointCard) cardText="VIC";
			if (udc.getKey() instanceof YearOfPlentyCard) cardText="YOP";
			
			StringPainter.printString(cardCanvas, cardText, udc.getValue().x+udc.getValue().width/2, udc.getValue().y+udc.getValue().height/2);
			
			cardCanvas.setColor(InterfaceColorProfile.inactiveColor);
			cardCanvas.draw(udc.getValue());	
				}
	}
	
	private void paintCardInfo() {
		if (selectedDevCard!=null) {
			String cardText1="null1";
			String cardText2="null2";
			
			if (selectedDevCard instanceof KnightCard) {
				cardText1="lets the player move the robber";
				cardText2="";
			}
			if (selectedDevCard instanceof MonopolyCard) {
				cardText1="player can claim all resource";
				cardText2="cards of a specific declared type";
			}
			if (selectedDevCard instanceof RoadBuildingCard) {
				cardText1="player can place 2 roads as";
				cardText2="is they just built them";
			}
			if (selectedDevCard instanceof VictoryPointCard) {
				cardText1="1 additional Victory Point";
				cardText2="is added to the owners total";
			}
			if (selectedDevCard instanceof YearOfPlentyCard) {
				cardText1="the player can draw 2 resource";
				cardText2="cards of their choice from the bank";
			}
			
			StringPainter.printString(cardCanvas, cardText1, ds.width*3/20, ds.height*7/10+30);
			StringPainter.printString(cardCanvas, cardText2, ds.width*3/20, ds.height*7/10+45);

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
	
	public DevCard getSelectedDevCard() {
		return selectedDevCard;
	}
	
	private class CardMetrics {
		public final int height=ds.height*1/10-20;
		public final int width=height*11/16;
	}
}

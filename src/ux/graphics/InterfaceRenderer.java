package ux.graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import controller.map.Hex;
import controller.player.Building;

public class InterfaceRenderer extends ImageRenderer {
	Graphics2D intCanvas;
	private Hex activeHex;
	ArrayList<Button> buttonsList;
	
	public InterfaceRenderer(int _width, int _height) {
		width=_width;
		height=_height;
		activeHex=null;
		generateButtons();
	}
	private void generateButtons() {
		buttonsList=new ArrayList();
		buttonsList.add(new BuildButton("Village",Building.Settlement, width*31/40, height*15/20, width*3/20-20, 30));
		buttonsList.add(new BuildButton("City", Building.City, width*31/40, height*16/20, width*3/20-20, 30));
		buttonsList.add(new BuildButton("Road",Building.Road, width*31/40, height*17/20, width*3/20-20, 30));
		
		buttonsList.add(new TradeButton("Trade", width*37/40, height*61/80, width*3/20-20, 40));
		buttonsList.add(new TradeButton("Dev Card", width*37/40, height*67/80, width*3/20-20, 40));
		
	}
	@Override
	public void paint(Graphics g) {
		intCanvas=(Graphics2D)g;
		intCanvas.setColor(Color.black);
		paintFrames();
		paintHexInfo();
		paintButtons();
	}

	private void paintButtons() {
		
		for (Button b : buttonsList) {
			intCanvas.setColor(b.selected?Color.red:Color.white);
			intCanvas.fillRect(b.x-b.width/2, b.y-b.height/2, b.width, b.height);
			
			intCanvas.setColor(Color.black);
			intCanvas.drawRect(b.x-b.width/2, b.y-b.height/2, b.width, b.height);
			
			printString(b.text, b.x, b.y);
			
		}
	}
	
	private void printString(String str,int x, int y) {
		FontMetrics metrics=intCanvas.getFontMetrics();
		intCanvas.drawString(str, x-metrics.stringWidth(str)/2, y-metrics.getHeight()/2+12);

	}
	
	private void paintHexInfo() {
		if (activeHex!=null) {
			intCanvas.drawString("Selected "+activeHex.getID(), (width*3/10)+20, (height*9/10)+30);
		} else {
			intCanvas.drawString("No field selected", (width*3/10)+20, (height*9/10)+30);
		}
		
	}
	
	private void paintFrames() {
		intCanvas.setBackground(Color.white);
		Rectangle leftRect=new Rectangle(0, height*7/10, width*3/10, height*3/10);
		Rectangle centerRect=new Rectangle(width*3/10, height*9/10, width*4/10, height*1/10);
		Rectangle rightRect=new Rectangle(width*7/10, height*7/10, width*3/10, height*3/10);
		
		intCanvas.setColor(Color.white);
		intCanvas.fill(leftRect);
		intCanvas.fill(centerRect);
		intCanvas.fill(rightRect);
		
		intCanvas.setColor(Color.black);
		intCanvas.draw(leftRect);
		intCanvas.draw(centerRect);
		intCanvas.draw(rightRect);
		
		paintHexInfo();
		printString("Build",width*31/40, height*57/80);
		
		paintButtons();
	}
	public void setActiveHex(Hex _activeHex) {
		activeHex=_activeHex;
	}
	
	public void resetActiveHex() {
		activeHex=null;
	}
	public Button getButtonUnderCursor(int x, int y) {
		for (Button b : buttonsList)
			if (Math.abs(b.x-x)<b.width/2 && Math.abs(b.y-y)<b.height/2) {
				for (Button allB : buttonsList) allB.selected=b.equals(allB);
				return b;
			}
		return null;
	}

}

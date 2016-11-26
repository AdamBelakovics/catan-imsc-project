package ux.graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import controller.map.Hex;
import controller.player.Building;
import ux.ui.UIController;

public class InterfaceRenderer extends ImageRenderer {
	
	UIController currUIC;
	Graphics2D intCanvas;
	
	private Hex activeHex=null;;
	ArrayList<Button> buttonsList;
	FrameMetrics frameMetrics=new FrameMetrics();
	
	
	public InterfaceRenderer(UIController _currUIC, int _width, int _height) {
		super(_width,_height);
		currUIC=_currUIC;
		generateButtons();
	}
	
	private void generateButtons() {
		buttonsList=new ArrayList();
		buttonsList.add(new BuildButton("Settlement",BuildingEnum.Settlement, width*31/40, height*15/20, width*3/20-20, 30));
		buttonsList.add(new BuildButton("City", BuildingEnum.City, width*31/40, height*16/20, width*3/20-20, 30));
		buttonsList.add(new BuildButton("Road",BuildingEnum.Road, width*31/40, height*17/20, width*3/20-20, 30));
		
		buttonsList.add(new TradeButton("Trade", width*37/40, height*61/80, width*3/20-20, 40));
		buttonsList.add(new TradeButton("Dev Card", width*37/40, height*67/80, width*3/20-20, 40));
		
		buttonsList.add(new EndTurnButton(currUIC,"End turn", width*17/20,height*26/40,width*3/10-10,50));
		
	}
	@Override
	public void paint(Graphics g) {
		intCanvas=(Graphics2D)g;
		intCanvas.setColor(InterfaceColorProfile.fgColor);
		paintFrames();
		paintHexInfo();
		paintButtons();
	}

	private void paintButtons() {
		
		for (Button b : buttonsList) {
			if (b.selected) {
				intCanvas.setColor(InterfaceColorProfile.selectedColor);
				if (b instanceof BuildButton) {
					int i=0;
					for (HashMap.Entry e : ((BuildButton)b).buildCost.entrySet())
						StringPainter.printString(intCanvas, e.getKey().toString()+ ": "+e.getValue(), 
								frameMetrics.leftRect.width/2, 
								frameMetrics.leftRect.y+50+frameMetrics.leftRect.height*(i++)/8);
				}
			}
			
			else intCanvas.setColor(b.selected?InterfaceColorProfile.selectedColor:currUIC.active?InterfaceColorProfile.bgColor:InterfaceColorProfile.inactiveColor);
			intCanvas.fillRect(b.x-b.width/2, b.y-b.height/2, b.width, b.height);
			
			intCanvas.setColor(InterfaceColorProfile.fgColor);
			intCanvas.drawRect(b.x-b.width/2, b.y-b.height/2, b.width, b.height);
			
			StringPainter.printString(intCanvas, b.text, b.x, b.y);
		}
	}
	
	private void paintHexInfo() {
		if (activeHex!=null) {
			intCanvas.drawString("Selected "+activeHex.getID()+", resource: "+ activeHex.getResource().toString(), (width*3/10)+20, (height*9/10)+30);
		} else {
			intCanvas.drawString("No field selected", (width*3/10)+20, (height*9/10)+30);
		}
		
	}
	
	private void paintFrames() {
		intCanvas.setBackground(InterfaceColorProfile.bgColor);
		
		intCanvas.setColor(InterfaceColorProfile.bgColor);
		intCanvas.fill(frameMetrics.leftRect);
		intCanvas.fill(frameMetrics.centerRect);
		intCanvas.fill(frameMetrics.rightRect);
		
		intCanvas.setColor(InterfaceColorProfile.fgColor);
		intCanvas.draw(frameMetrics.leftRect);
		intCanvas.draw(frameMetrics.centerRect);
		intCanvas.draw(frameMetrics.rightRect);
		
		paintHexInfo();
		StringPainter.printString(intCanvas, "Build",width*31/40, height*57/80);
		
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
				return b;
			}
		return null;
	}
	
	public void deselectAllButtons() {
		for (Button b : buttonsList) b.selected=false;
	}
	
	public void pressButton(Button b) {
		for (Button allB : buttonsList) allB.selected=b.equals(allB)&&!b.selected;
		if (b.selected) b.press();
	}
	
	private class FrameMetrics {
		Rectangle leftRect=new Rectangle(0, height*7/10, width*3/10, height*3/10);
		Rectangle centerRect=new Rectangle(width*3/10, height*9/10, width*4/10, height*1/10);
		Rectangle rightRect=new Rectangle(width*7/10, height*7/10, width*3/10, height*3/10);
	}

}

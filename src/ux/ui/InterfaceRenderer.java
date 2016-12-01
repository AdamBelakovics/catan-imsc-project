package ux.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import controller.map.Buildable;
import controller.map.Hex;
import controller.player.Building;
import ux.FirstTurnState;
import ux.ImageRenderer;
import ux.RendererDataStore;

public class InterfaceRenderer extends ImageRenderer {
	
	Graphics2D intCanvas;
	
	private Hex activeHex=null;
	ArrayList<Button> buttonsList;
	FrameMetrics frameMetrics;
	RendererDataStore ds;

	public InterfaceRenderer(RendererDataStore _ds) {
		ds=_ds;
		frameMetrics=new FrameMetrics();
		generateButtons();
	}
	
	private void generateButtons() {
		buttonsList=new ArrayList();
		buttonsList.add(new BuildButton("Settlement",Buildable.Settlement, ds.width*31/40, ds.height*15/20, ds.width*3/20-20, 30));
		buttonsList.add(new BuildButton("City", Buildable.City, ds.width*31/40, ds.height*16/20, ds.width*3/20-20, 30));
		buttonsList.add(new BuildButton("Road",Buildable.Road, ds.width*31/40, ds.height*17/20, ds.width*3/20-20, 30));
		
		buttonsList.add(new TradeButton("Trade", ds.width*37/40, ds.height*61/80, ds.width*3/20-20, 40));
		buttonsList.add(new TradeButton("Dev Card", ds.width*37/40, ds.height*67/80, ds.width*3/20-20, 40));

		buttonsList.add(new EndTurnButton(ds.currUIC,"End turn", ds.width*17/20,ds.height*26/40,ds.width*3/10-10,50));
		
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
			if (!ds.currUIC.active) intCanvas.setColor(InterfaceColorProfile.inactiveColor);
			if (ds.currUIC.firstturnactive && 
					((b.text.equals("Settlement") && ds.currUIC.state==FirstTurnState.STARTED) || 
					((b.text.equals("Road") && ds.currUIC.state==FirstTurnState.CITYBUILT))|| 
					b.text.equals("End turn")))
				intCanvas.setColor(InterfaceColorProfile.bgColor);
			if (ds.currUIC.active) intCanvas.setColor(InterfaceColorProfile.bgColor);
			
			
			if (b.isSelected()) {
				intCanvas.setColor(InterfaceColorProfile.selectedColor);
				if (b instanceof BuildButton) {
					int i=0;
					for (HashMap.Entry e : ((BuildButton)b).buildCost.entrySet())
						StringPainter.printString(intCanvas, e.getKey().toString()+ ": "+e.getValue(), 
								frameMetrics.leftRect.width/2, 
								frameMetrics.leftRect.y+50+frameMetrics.leftRect.height*(i++)/8);
				}
			}
			
			intCanvas.fillRect(b.x-b.width/2, b.y-b.height/2, b.width, b.height);
			
			intCanvas.setColor(InterfaceColorProfile.fgColor);
			intCanvas.drawRect(b.x-b.width/2, b.y-b.height/2, b.width, b.height);
			
			StringPainter.printString(intCanvas, b.text, b.x, b.y);
		}
	}
	
	private void paintHexInfo() {
		if (activeHex!=null && activeHex.getResource()!=null) {
			intCanvas.drawString("Selected "+activeHex.getID()+", resource: "+ activeHex.getResource().toString(), (ds.width*3/10)+20, (ds.height*9/10)+30);
		} else {
			intCanvas.drawString("No field selected", (ds.width*3/10)+20, (ds.height*9/10)+30);
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
		StringPainter.printString(intCanvas, "Build",ds.width*31/40, ds.height*57/80);
		
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
		for (Button b : buttonsList) b.setSelected(false);
	}
	
	public void pressButton(Button b) {
		for (Button allB : buttonsList) allB.setSelected(b.equals(allB)&&!b.isSelected());
		if (b.isSelected()) b.press();
	}
	
	private class FrameMetrics {
		Rectangle leftRect=new Rectangle(0, ds.height*7/10, ds.width*3/10, ds.height*3/10);
		Rectangle centerRect=new Rectangle(ds.width*3/10, ds.height*9/10,ds.width*4/10, ds.height*1/10);
		Rectangle rightRect=new Rectangle(ds.width*7/10, ds.height*7/10, ds.width*3/10, ds.height*3/10);
	}

}

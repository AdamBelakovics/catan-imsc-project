package ux.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

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
	TextureManager textureManager;
	RendererDataStore ds;

	public InterfaceRenderer(RendererDataStore _ds) {
		ds=_ds;
		textureManager=new TextureManager(30, 50);
		generateButtons();
	}
	
	private void generateButtons() {
		buttonsList=new ArrayList();
		buttonsList.add(new BuildButton("Settlement",Buildable.Settlement, ds.width*31/40, ds.height*15/20, ds.width*3/20-20, 30));
		buttonsList.add(new BuildButton("City", Buildable.City, ds.width*31/40, ds.height*16/20, ds.width*3/20-20, 30));
		buttonsList.add(new BuildButton("Road",Buildable.Road, ds.width*31/40, ds.height*17/20, ds.width*3/20-20, 30));
		
		buttonsList.add(new TradeButton("Trade", ds.width*37/40, ds.height*61/80, ds.width*3/20-20, 30));
		buttonsList.add(new TradeButton("Dev Card", ds.width*37/40, ds.height*67/80, ds.width*3/20-20, 30));

		buttonsList.add(new EndTurnButton(ds.currUIC,"End turn", ds.width*17/20,ds.height*26/40,ds.width*3/10-10,50));
		
	}
	@Override
	public void paint(Graphics g) {
		intCanvas=(Graphics2D)g;
		intCanvas.setColor(InterfaceColorProfile.fgColor);
		paintFrames();
		paintHexInfo();
		paintButtons();
		paintDice();
	}

	private void paintButtons() {
		
		
		for (Button b : buttonsList) {
			boolean active=false;
			//if (!ds.currUIC.active)
			//	intCanvas.setColor(InterfaceColorProfile.inactiveColor);
			if (ds.currUIC.firstturnactive && 
					((b.text.equals("Settlement") && ds.currUIC.state==FirstTurnState.STARTED) || 
					((b.text.equals("Road") && ds.currUIC.state==FirstTurnState.CITYBUILT))|| 
					b.text.equals("End turn")) || ds.currUIC.active)
				active=true;
				//intCanvas.setColor(InterfaceColorProfile.bgColor);
			
			
			if (b.isSelected()) {
				intCanvas.setColor(InterfaceColorProfile.selectedColor);
				if (b instanceof BuildButton) {
					int i=0;
					for (HashMap.Entry e : ((BuildButton)b).buildCost.entrySet())
						StringPainter.printString(intCanvas, e.getKey().toString()+ ": "+e.getValue(), 
								textureManager.leftRect.width/2, 
								textureManager.leftRect.y+50+textureManager.leftRect.height*(i++)/8);
				}
			}

			if (textureManager.fallback) {
				intCanvas.fillRect(b.x-b.width/2, b.y-b.height/2, b.width, b.height);
				intCanvas.setColor(InterfaceColorProfile.fgColor);
				intCanvas.drawRect(b.x-b.width/2, b.y-b.height/2, b.width, b.height);

				StringPainter.printString(intCanvas, b.text, b.x, b.y);
			} else {
				if (b instanceof EndTurnButton)
					textureManager.drawEndTurnButton(intCanvas, b.isSelected(), b.x-b.width/2, b.y-b.height/2, b.width);
				else textureManager.drawButton(intCanvas, b.isSelected(),active, b.x-b.width/2, b.y-b.height/2, b.width);
				StringPainter.printString(intCanvas, b.text, b.x, b.y);

			}
		}
	}
	
	private void paintDice() {
		intCanvas.setColor(InterfaceColorProfile.bgColor);
		StringPainter.printString(intCanvas, "3 4", ds.width-30, 45);
	}
	
	private void paintHexInfo() {
		if (activeHex!=null && activeHex.getResource()!=null && activeHex.getProsperity()!=0) {
			StringPainter.printString(intCanvas, "Resource: "+ activeHex.getResource().toString()+", prosperity: "+activeHex.getProsperity(), ds.width/2, ds.height*9/10+30);
			}		
	}
	
	private void paintFrames() {
		intCanvas.setBackground(InterfaceColorProfile.bgColor);
		
		if (textureManager.fallback) {
			intCanvas.setColor(InterfaceColorProfile.bgColor);
			intCanvas.fill(textureManager.leftRect);
			intCanvas.fill(textureManager.centerRect);
			intCanvas.fill(textureManager.rightRect);
		
			intCanvas.setColor(InterfaceColorProfile.fgColor);
			intCanvas.draw(textureManager.leftRect);
			intCanvas.draw(textureManager.centerRect);
			intCanvas.draw(textureManager.rightRect);
		} else {
			intCanvas.drawImage(textureManager.rectImg,0,0,null);
		}
		
		
		paintHexInfo();
		StringPainter.printString(intCanvas, "Build",ds.width*31/40, ds.height*57/80+5);
		
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
	
	private class TextureManager {
		boolean fallback=false;
		BufferedImage rectImg = new BufferedImage(ds.width, ds.height,BufferedImage.TYPE_INT_ARGB);
		
		Rectangle leftRect=new Rectangle(0, ds.height*7/10, ds.width*3/10, ds.height*3/10);
		Rectangle centerRect=new Rectangle(ds.width*3/10, ds.height*9/10,ds.width*4/10, ds.height*1/10);
		Rectangle rightRect=new Rectangle(ds.width*7/10, ds.height*7/10, ds.width*3/10, ds.height*3/10);
		Rectangle diceRect=new Rectangle(ds.width-60, 0, 60, 70);
		
		int buttonHeight;
		Image leftunsel;
		Image centerunsel;
		Image rightunsel;
		Image leftsel;
		Image centersel;
		Image rightsel;
		Image leftinact;
		Image centerinact;
		Image rightinact;
		
		int bigButtonHeight;
		Image bigleft;
		Image bigcenter;
		Image bigright;
		Image bigleftsel;
		Image bigcentersel;
		Image bigrightsel;
		Image bigleftinact;
		Image bigcenterinact;
		Image bigrightinact;
		
		public TextureManager(int _buttonHeight, int _bigButtonHeight) {
			buttonHeight=_buttonHeight;
			bigButtonHeight=_bigButtonHeight;
			drawFrame(rectImg.getGraphics(),ds.width*3/10-5, ds.height*9/10,ds.width*4/10+10, ds.height*1/10);
			drawFrame(rectImg.getGraphics(),0,ds.height*7/10, ds.width*3/10, ds.height*3/10);
			drawFrame(rectImg.getGraphics(),ds.width*7/10, ds.height*7/10, ds.width*3/10, ds.height*3/10);
			drawFrame(rectImg.getGraphics(),ds.width-60, 0, 60, 70);
			
			try {
				leftunsel=ImageIO.read(new File("assets/button_left_unselected.png")).getScaledInstance(buttonHeight, -1, Image.SCALE_SMOOTH);
				centerunsel=ImageIO.read(new File("assets/button_center_unselected.png")).getScaledInstance(buttonHeight, -1, Image.SCALE_SMOOTH);
				rightunsel=ImageIO.read(new File("assets/button_right_unselected.png")).getScaledInstance(buttonHeight, -1, Image.SCALE_SMOOTH);
				leftsel=ImageIO.read(new File("assets/button_left_selected.png")).getScaledInstance(buttonHeight, -1, Image.SCALE_SMOOTH);
				centersel=ImageIO.read(new File("assets/button_center_selected.png")).getScaledInstance(buttonHeight, -1, Image.SCALE_SMOOTH);
				rightsel=ImageIO.read(new File("assets/button_right_selected.png")).getScaledInstance(buttonHeight, -1, Image.SCALE_SMOOTH);
				leftinact=ImageIO.read(new File("assets/button_left_inactive.png")).getScaledInstance(buttonHeight, -1, Image.SCALE_SMOOTH);
				centerinact=ImageIO.read(new File("assets/button_center_inactive.png")).getScaledInstance(buttonHeight, -1, Image.SCALE_SMOOTH);
				rightinact=ImageIO.read(new File("assets/button_right_inactive.png")).getScaledInstance(buttonHeight, -1, Image.SCALE_SMOOTH);
				
				bigleft=ImageIO.read(new File("assets/button_big_left.png")).getScaledInstance(-1, bigButtonHeight, Image.SCALE_SMOOTH);
				bigcenter=ImageIO.read(new File("assets/button_big_center.png")).getScaledInstance(-1, bigButtonHeight, Image.SCALE_SMOOTH);
				bigright=ImageIO.read(new File("assets/button_big_right.png")).getScaledInstance(-1, bigButtonHeight, Image.SCALE_SMOOTH);
				
				bigleftsel=ImageIO.read(new File("assets/button_big_left_pressed.png")).getScaledInstance(-1, bigButtonHeight, Image.SCALE_SMOOTH);
				bigcentersel=ImageIO.read(new File("assets/button_big_center_pressed.png")).getScaledInstance(-1, bigButtonHeight, Image.SCALE_SMOOTH);
				bigrightsel=ImageIO.read(new File("assets/button_big_right_pressed.png")).getScaledInstance(-1, bigButtonHeight, Image.SCALE_SMOOTH);
				
				bigleftinact=ImageIO.read(new File("assets/button_big_left_inactive.png")).getScaledInstance(-1, bigButtonHeight, Image.SCALE_SMOOTH);
				bigcenterinact=ImageIO.read(new File("assets/button_big_center_inactive.png")).getScaledInstance(-1, bigButtonHeight, Image.SCALE_SMOOTH);
				bigrightinact=ImageIO.read(new File("assets/button_big_right_inactive.png")).getScaledInstance(-1, bigButtonHeight, Image.SCALE_SMOOTH);
				
				
				
			} catch (IOException e) {
				System.out.println("[InterfaceRenderer] Problem with textures, activating fallback");
				fallback=true;
			}
		
		}
		
		public void drawEndTurnButton(Graphics canvas, boolean selected, int x, int y, int width) {
			Image left,center,right;
			left=ds.currUIC.state!=FirstTurnState.ROADBUILT && ds.currUIC.state!=FirstTurnState.NULL?
					bigleftinact:selected?bigleftsel:bigleft;
			center=ds.currUIC.state!=FirstTurnState.ROADBUILT && ds.currUIC.state!=FirstTurnState.NULL?
					bigcenterinact:selected?bigcentersel:bigcenter;
			right=ds.currUIC.state!=FirstTurnState.ROADBUILT && ds.currUIC.state!=FirstTurnState.NULL?
					bigrightinact:selected?bigrightsel:bigright;
			
			canvas.drawImage(left, x, y, null);
			int lefttogo=right.getWidth(null);
			while (lefttogo<width-right.getWidth(null)) {
				canvas.drawImage(center, x+lefttogo, y, null);
				lefttogo+=right.getWidth(null);
			}
			canvas.drawImage(right, x+width-right.getWidth(null), y, null);
		}
		
		public void drawButton(Graphics canvas, boolean selected, boolean active, int x, int y, int width) {
			Image left;
			Image center;
			Image right;
			
			if (!active) {
				left=leftinact;
				center=centerinact;
				right=rightinact;
			} else if (selected) {
				left=leftsel;
				center=centersel;
				right=rightsel;
			} else {
				left=leftunsel;
				center=centerunsel;
				right=rightunsel;
			}
			
			canvas.drawImage(left, x, y, null);
			int lefttogo=buttonHeight;
			while (lefttogo<width-buttonHeight) {
				canvas.drawImage(center, x+lefttogo, y, null);
				lefttogo+=buttonHeight;
			}
			canvas.drawImage(right, x+width-buttonHeight, y, null);
			
		}
		
		private void drawFrame(Graphics canvas, int x, int y, int width, int height) {
			final int textureSize=32;
			try {				
				// loading textures
				Image lefttop=ImageIO.read(new File("assets/ui_left_top.png"));
				Image centertop=ImageIO.read(new File("assets/ui_center_top.png"));
				Image righttop=ImageIO.read(new File("assets/ui_right_top.png"));
				Image leftmiddle=ImageIO.read(new File("assets/ui_left_middle.png"));
				Image centermiddle=ImageIO.read(new File("assets/ui_center_middle.png"));
				Image rightmiddle=ImageIO.read(new File("assets/ui_right_middle.png"));
				Image leftbottom=ImageIO.read(new File("assets/ui_left_bottom.png"));
				Image centerbottom=ImageIO.read(new File("assets/ui_center_bottom.png"));
				Image rightbottom=ImageIO.read(new File("assets/ui_right_bottom.png"));
							
				
				// first row
				canvas.drawImage(lefttop, x, y, null);
				int lefttogo=textureSize;
				while (lefttogo<width-textureSize) {
					canvas.drawImage(centertop, x+lefttogo, y, null);
					lefttogo+=textureSize;
				}
				canvas.drawImage(righttop, x+width-textureSize, y, null);
				
				// rest of the rows
				int bottomtogo=textureSize;
				while (bottomtogo<height-textureSize) {
					canvas.drawImage(leftmiddle, x, y+bottomtogo, null);
					lefttogo=textureSize;
					while (lefttogo<width-textureSize) {
						canvas.drawImage(centermiddle, x+lefttogo,y+bottomtogo, null);
						lefttogo+=textureSize;
					}
					canvas.drawImage(rightmiddle, x+width-textureSize, y+bottomtogo, null);
					bottomtogo+=textureSize;
				}
						
				// bottom row
				canvas.drawImage(leftbottom, x, y+height-32, null);
				lefttogo=textureSize;
				while (lefttogo<width-textureSize) {
					canvas.drawImage(centerbottom, x+lefttogo, y+height-32, null);
					lefttogo+=textureSize;
				}
				canvas.drawImage(rightbottom, x+width-textureSize, y+height-32, null);
				
				
			} catch (IOException e) {
				System.out.println("[InterfaceRenderer] Problem with textures, activating fallback");
				fallback=true;
			}
		}
	}

}

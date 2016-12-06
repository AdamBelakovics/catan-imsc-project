package ux.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import controller.player.Resource;
import ux.RendererDataStore;

public class TradeWindow {
	Resource col1=Resource.Lumber;
	Resource col2=Resource.Brick;
	RendererDataStore ds;
	InterfaceRenderer ir;
	BufferedImage img;
	ChangeButton button;
	Image left;
	Image right;
	
	public TradeWindow(RendererDataStore _ds,InterfaceRenderer _ir) {
		ds=_ds;
		ir=_ir;
		img=new BufferedImage(ds.width,ds.height,BufferedImage.TYPE_INT_ARGB);
		button=new ChangeButton("Change", ds.width*3/20, ds.height*19/20, ds.width*3/20-20, 30);
		
		try {
			left=ImageIO.read(new File("assets/switch_button_left.png"));
			Image center=ImageIO.read(new File("assets/button_center_selected.png"));
			right=ImageIO.read(new File("assets/switch_button_right.png"));
			
			img.getGraphics().drawImage(left, ds.width*3/20-60-left.getWidth(null)/2, ds.height*15/20-left.getHeight(null)/2, null);
			int rightleft=ds.width*3/20-60+left.getWidth(null)/2;
			while (rightleft<=ds.width*3/20+60-right.getWidth(null)/2-center.getWidth(null)) {
				img.getGraphics().drawImage(center, rightleft, ds.height*15/20-center.getHeight(null)/2, null);
				rightleft+=center.getWidth(null);
			}
			img.getGraphics().drawImage(center, ds.width*3/20+60-right.getWidth(null)/2-center.getWidth(null), ds.height*15/20-center.getHeight(null)/2, null);
			img.getGraphics().drawImage(right, ds.width*3/20+60-right.getWidth(null)/2, ds.height*15/20-right.getHeight(null)/2, null);
			
			img.getGraphics().drawImage(left, ds.width*3/20-60-left.getWidth(null)/2, ds.height*17/20-left.getHeight(null)/2, null);
			rightleft=ds.width*3/20-60+left.getWidth(null)/2;
			while (rightleft<=ds.width*3/20+60-right.getWidth(null)/2-center.getWidth(null)) {
				img.getGraphics().drawImage(center, rightleft, ds.height*17/20-center.getHeight(null)/2, null);
				rightleft+=center.getWidth(null);
			}
			img.getGraphics().drawImage(center, ds.width*3/20+60-right.getWidth(null)/2-center.getWidth(null), ds.height*17/20-center.getHeight(null)/2, null);
			img.getGraphics().drawImage(right, ds.width*3/20+60-right.getWidth(null)/2, ds.height*17/20-right.getHeight(null)/2, null);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void paint(Graphics2D g) {
		
		g.drawImage(img, 0, 0, null);
		StringPainter.printString(g, "Change "+ds.currUIC.controlledPlayer.getChangeLUT(col1), ds.width*3/20, ds.height*29/40);
		StringPainter.printString(g, col1.toString(), ds.width*3/20, ds.height*15/20);

		StringPainter.printString(g, "for a(n)", ds.width*3/20, ds.height*33/40);
		StringPainter.printString(g, col2.toString(), ds.width*3/20, ds.height*17/20);
		ir.paintButton(button);
	}
	
	public void click(int x, int y) {
		final int eps=8;
		do {
			if (Math.abs(x-ds.width*3/20-50-left.getWidth(null)/2)<eps) {
				if (Math.abs(y-ds.height*15/20+10-left.getHeight(null)/2)<eps) {
					col1=Resource.values()[(col1.ordinal()+4)%5];
				}
				else if (Math.abs(y-ds.height*17/20+10-left.getHeight(null)/2)<eps)
					col2=Resource.values()[(col2.ordinal()+4)%5];
			} else if (Math.abs(x-ds.width*3/20+46+right.getWidth(null)/2)<eps) {
				if (Math.abs(y-ds.height*15/20+10-left.getHeight(null)/2)<eps)
					col1=Resource.values()[(col1.ordinal()+1)%5];
				else if (Math.abs(y-ds.height*17/20+10-left.getHeight(null)/2)<eps)
					col2=Resource.values()[(col2.ordinal()+1)%5];
			}
			button.active=ds.currUIC.controlledPlayer.getResourceAmount(col1)>=ds.currUIC.controlledPlayer.getChangeLUT(col1);

		} while (col1.equals(col2));
		if (Math.abs(button.x-x)<button.width/2 && Math.abs(button.y-y)<button.height/2) {
			button.press();
		}
		
	}
	
	private class ChangeButton extends Button {
		boolean active=true;

		ChangeButton(String _text, int _x, int _y, int _width, int _height) {
			super(_text, _x, _y, _width, _height);
		}
		
		@Override
		void press() {
			if (active)	ds.currUIC.controlledPlayer.change(col1, col2);
		}
	}
}

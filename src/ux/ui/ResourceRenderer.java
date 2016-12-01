package ux.ui;

import java.awt.Graphics;

import javax.swing.JPanel;

import controller.player.Resource;
import ux.ImageRenderer;
import ux.RendererDataStore;

/**
 * Responsible for rendering the player's resources
 * @author Kiss Lorinc
 *
 */
public class ResourceRenderer extends ImageRenderer {
	RendererDataStore ds;
	
	public ResourceRenderer(RendererDataStore _ds) {
		ds=_ds;
	}

	@Override
	public void paint(Graphics g) {
		StringPainter.printString(g,
				"Brick: " + ds.currUIC.getPlayerResource(Resource.Brick) + 
				" Lumber: " + ds.currUIC.getPlayerResource(Resource.Lumber) + 
				" Ore: " + ds.currUIC.getPlayerResource(Resource.Ore),
				ds.width*17/20,ds.height*37/40);
		
		StringPainter.printString(g,
				"Grain: " + ds.currUIC.getPlayerResource(Resource.Grain) + 
				" Wool: " + ds.currUIC.getPlayerResource(Resource.Wool),
				ds.width*17/20,ds.height*38/40);
		
	}

}

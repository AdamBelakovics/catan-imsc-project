package ux;

import java.awt.Graphics;

import javax.swing.JPanel;

import controller.player.Resource;
import ux.ui.StringPainter;
import ux.ui.UIController;

/**
 * Responsible for rendering the player's resources
 * @author Kiss Lorinc
 *
 */
public class ResourceRenderer extends ImageRenderer {
	UIController uiController;
	
	public ResourceRenderer(UIController _uiController, int _width, int _height) {
		super(_width, _height);
		uiController=_uiController;
	}

	@Override
	public void paint(Graphics g) {
		StringPainter.printString(g,
				"Brick: " + uiController.getPlayerResource(Resource.Brick) + 
				" Lumber: " + uiController.getPlayerResource(Resource.Lumber) + 
				" Ore: " + uiController.getPlayerResource(Resource.Ore),
				width*17/20,height*37/40);
		
		StringPainter.printString(g,
				"Grain: " + uiController.getPlayerResource(Resource.Grain) + 
				" Wool: " + uiController.getPlayerResource(Resource.Wool),
				width*17/20,height*38/40);
		
	}

}

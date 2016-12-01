package ux.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import controller.map.Hex;
import controller.map.Orientation;
import controller.map.Table;
import ux.ImageRenderer;
import ux.Renderer;
import ux.RendererDataStore;
import ux.ui.UIController;

/**
 * Parent renderer of the Hex, Edge and Vertex renderers
 * @author Kiss Lorinc
 *
 */
public class BoardRenderer extends ImageRenderer {
	Graphics2D boardCanvas;
	BufferedImage hexImage;
	RendererDataStore ds;
	
	public BoardHexRenderer hexRenderer;
	public BoardVertexRenderer vertexRenderer;
	public BoardEdgeRenderer edgeRenderer;

	/**
	 * Enumerator representing the direction of the board currently facing
	 * @author Kiss Lorinc
	 *
	 */
	public enum BoardOrientation {
		NORTH,
		NORTHEAST,
		SOUTHEAST,
		SOUTH,
		SOUTHWEST,
		NORTHWEST
	}


	/**
	 * Initializes the renderer
	 * @param _currUIC the current UIController in use
	 * @param _board the game board
	 * @param _width width of the window
	 * @param _height height of the window
	 */
	public BoardRenderer(RendererDataStore _ds) {
		
		ds=_ds;
		hexImage=new BufferedImage(ds.width, ds.height, BufferedImage.TYPE_INT_ARGB);
		hexRenderer=new BoardHexRenderer(ds);
		vertexRenderer=new BoardVertexRenderer(ds);
		edgeRenderer=new BoardEdgeRenderer(ds);
	}

	@Override
	public void paint(Graphics g) {
		boardCanvas=(Graphics2D)g;
		hexRenderer.paint(hexImage.getGraphics());
		edgeRenderer.paint(hexImage.getGraphics());
		vertexRenderer.paint(hexImage.getGraphics());
		
		if (hexImage!=null && boardCanvas!=null) boardCanvas.drawImage(hexImage, 0, 0, null);
	
		boardCanvas.translate(ds.width/2, ds.height/2);
	}
	
	/**
	 * Resets the selected hexes and vertices
	 * @author Kiss Lorinc
	 */
	public void resetBoardSelection() {
		hexRenderer.deselectHexes();
		vertexRenderer.deselectVertices();
	}
}
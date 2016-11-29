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
import ux.ui.UIController;

/**
 * Parent renderer of the Hex, Edge and Vertex renderers
 * @author Kiss Lorinc
 *
 */
public class BoardRenderer extends ImageRenderer {
	Table board;
	Graphics2D boardCanvas;
	BufferedImage hexImage;
	UIController currUIC;
	
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

	public BoardOrientation boardOrientation;

	/**
	 * Initializes the renderer
	 * @param _currUIC the current UIController in use
	 * @param _board the game board
	 * @param _width width of the window
	 * @param _height height of the window
	 */
	public BoardRenderer(UIController _currUIC,Table _board,int _width, int _height) {
		super(_width,_height);
		board=_board;
		boardOrientation=BoardOrientation.NORTH;
		currUIC=_currUIC;
		hexImage=new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		hexRenderer=new BoardHexRenderer(this, board ,width,height);
		vertexRenderer=new BoardVertexRenderer(_board, hexRenderer, _currUIC, width, height);
		edgeRenderer=new BoardEdgeRenderer(vertexRenderer,hexRenderer, board, width, height);
	}

	@Override
	public void paint(Graphics g) {
		boardCanvas=(Graphics2D)g;
		hexRenderer.paint(hexImage.getGraphics());
		edgeRenderer.paint(hexImage.getGraphics());
		vertexRenderer.paint(hexImage.getGraphics());
		
		if (hexImage!=null && boardCanvas!=null) boardCanvas.drawImage(hexImage, 0, 0, null);
	
		boardCanvas.translate(getWidth()/2, getHeight()/2);
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
package ux.graphics;

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

/**
 * @author Kiss Lorinc
 *
 */
public class BoardRenderer extends ImageRenderer {
	Table board;
	Graphics2D boardCanvas;
	BufferedImage hexImage;
	
	public BoardHexRenderer hexRenderer;

	public enum BoardOrientation {
		NORTH,
		NORTHEAST,
		SOUTHEAST,
		SOUTH,
		SOUTHWEST,
		NORTHWEST
	}

	public BoardOrientation boardOrientation;

	public BoardRenderer(Table _board,int _width, int _height) {
		width=_width;
		height=_height;
		board=_board;
		boardOrientation=BoardOrientation.NORTH;
		hexImage=new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		hexRenderer=new BoardHexRenderer(this, board ,width,height);
	}

	public void paint(Graphics g) {
		boardCanvas=(Graphics2D)g;
		hexRenderer.paint(hexImage.createGraphics());
		if (hexImage!=null && boardCanvas!=null) boardCanvas.drawImage(hexImage, 0, 0, null);
		boardCanvas.translate(getWidth()/2, getHeight()/2);
	}
}
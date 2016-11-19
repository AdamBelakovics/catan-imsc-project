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
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import controller.map.Hex;
import controller.map.Orientation;
import controller.map.Table;

/**
 * @author kisss
 *
 */
public class BoardRenderer extends JPanel {
	Table board;
	Graphics2D canvas;
	double rotationLeft;
	HashMap<Hex,HexPoly> hexMap;
	AffineTransform currentCanvasTransform;
	private enum BoardOrientation {
		NORTH,
		NORTHEAST,
		SOUTHEAST,
		SOUTH,
		SOUTHWEST,
		NORTHWEST
	}
	
	BoardOrientation boardOrientation;
	
	
	//Constants
	final double eps=0.01;
	final double rotationStep=0.01;
	
	public BoardRenderer(Table _board) {
		rotationLeft=0;
		board=_board;
		hexMap=new HashMap<Hex, HexPoly>();
		generateHexes();
		boardOrientation=BoardOrientation.NORTH;
	}

	@Override
	public void paintComponent(Graphics g) {
		canvas=(Graphics2D)g;
		translateBoardCanvas();
		paintHexes();
	}
	
	/**
	 * Generates hexes in the HashMap
	 * 
	 * @author      Kiss Lorinc
	 */
	private void generateHexes() {
		// TODO hexes from table
		for(int i=-3;i<=3;i++)
			for(int j=-3;j<=3;j++)
				if (!(i+4 <=j || i-4 >=j))
				hexMap.put(new Hex(i,j),HexPolyFactory.getHexPoly(new Hex(i,j),j));
	}
	
	/**
	 * Paints all hex fields from the HashMap
	 * @author Kiss Lorinc
	 */
	private void paintHexes() {
		for (Map.Entry<Hex, HexPoly> entry : hexMap.entrySet()){
			canvas.draw(entry.getValue());
			if (entry.getValue().selected) canvas.fill(entry.getValue());
		}
	}
	
	public void selectHex(Hex h) {
		System.out.println("[Renderer]Selected "+hexMap.get(h).toString());
			hexMap.get(h).selected=!(hexMap.get(h).selected);
	}
	
	public Map.Entry<Hex, HexPoly> getHexUnderCursor(int x, int y) {
		
		for (Map.Entry<Hex, HexPoly> entry : hexMap.entrySet()){
			try {
				if (entry.getValue().contains(canvas.getTransform().createInverse().transform(new Point(x,y), null))) return entry;
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Transforms the canvas.
	 * Sets the origin to the center of the screen, utilizes
	 * scaling to simulate isometric tilt effect, and applies view rotation
	 * 
	 * @author      Kiss Lorinc
	 */
	private void translateBoardCanvas() {
		//Setting origin to center
		canvas.translate(getWidth()/2, getHeight()/2);
		
		//Isometric tilt
		canvas.scale(1, 0.5);
		
		//Applying rotation, if needed
		if (Math.abs(rotationLeft)>eps) {
			canvas.rotate((Math.signum(rotationLeft)+boardOrientation.ordinal()-1)*Math.PI/3-rotationLeft);
			rotationLeft-=Math.signum(rotationLeft)*rotationStep;
		} else canvas.rotate(boardOrientation.ordinal()*Math.PI/3);
	}

	/**
	 * Rotates the board
	 * @param i direction of rotation
	 * 
	 * @author Kiss Lorinc
	 */
	public void cycleOrientation(int i) {
		if (Math.abs(rotationLeft)<eps) {
			boardOrientation=BoardOrientation.values()[((boardOrientation.ordinal()+i)%6)];
			rotationLeft+=i*Math.PI/3;
		}
	}
}
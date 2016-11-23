package ux.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import controller.map.Hex;
import ux.graphics.BoardRenderer.BoardOrientation;

public class BoardHexRenderer extends ImageRenderer {
	double rotationLeft;
	Graphics2D hexCanvas;
	double zoomLevel;

	//Constants
	final double eps=0.01;
	final double rotationStep=0.01;

	public BoardHexRenderer(ImageRenderer _parentRenderer, HashMap<Hex,HexPoly> _hexMap,int _width,int _height) {

		parentRenderer=_parentRenderer;
		width=parentRenderer.getWidth();
		height=parentRenderer.getHeight();
		width=_width;
		height=_height;
		generateHexes();
		rotationLeft=0;
		zoomLevel=1;
	}

	public void paint(Graphics g) {
		hexCanvas=(Graphics2D)g;
		hexCanvas.setBackground(Color.white);
		hexCanvas.clearRect(0, 0, width, height);
		translateBoardCanvas();
		paintHexes();
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
		hexCanvas.translate(width/2, height/2);

		//Isometric tilt
		hexCanvas.scale(1*zoomLevel, 0.5*zoomLevel);

		//Applying rotation, if needed
		if (Math.abs(rotationLeft)>eps) {
			hexCanvas.rotate((Math.signum(rotationLeft)+((BoardRenderer)parentRenderer).boardOrientation.ordinal()-1)*Math.PI/3-rotationLeft);
			rotationLeft-=Math.signum(rotationLeft)*rotationStep;
		} else hexCanvas.rotate(((BoardRenderer)parentRenderer).boardOrientation.ordinal()*Math.PI/3);
	}

	/**
	 * Rotates the board
	 * @param i direction of rotation
	 * 
	 * @author Kiss Lorinc
	 */
	public void cycleOrientation(int i) {
		if (Math.abs(rotationLeft)<eps) {
			((BoardRenderer)parentRenderer).boardOrientation=BoardOrientation.values()[((((BoardRenderer)parentRenderer).boardOrientation.ordinal()+i)%6)];
			rotationLeft+=i*Math.PI/3;
		}
	}

	/**
	 * Handles board zoom.
	 * 
	 * @param i mouse wheel value from the event
	 * @author Kiss Lorinc
	 */
	public void zoomBoard(int i) {
		zoomLevel+=0.1*i;
		if (zoomLevel>1.6) zoomLevel=1.6;
		if (zoomLevel<0.8) zoomLevel=0.8;
	}

	public Map.Entry<Hex, HexPoly> getHexUnderCursor(int x, int y) {

		for (Map.Entry<Hex, HexPoly> entry : ((BoardRenderer)parentRenderer).hexMap.entrySet()){
			try {
				if (entry.getValue().contains(hexCanvas.getTransform().createInverse().transform(new Point(x,y), null))) return entry;
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Paints all hex fields from the HashMap
	 * @author Kiss Lorinc
	 */
	private void paintHexes() {
		for (Map.Entry<Hex, HexPoly> entry : ((BoardRenderer)parentRenderer).hexMap.entrySet()){
			hexCanvas.setPaint(Color.BLACK);
			hexCanvas.draw(entry.getValue());
			if (entry.getValue().selected) hexCanvas.fill(entry.getValue());
		}
	}

	public void selectHex(Hex h) {
		System.out.println("[Renderer]Selected "+((BoardRenderer)parentRenderer).hexMap.get(h).toString());
		((BoardRenderer)parentRenderer).hexMap.get(h).selected=!(((BoardRenderer)parentRenderer).hexMap.get(h).selected);
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
					((BoardRenderer)parentRenderer).hexMap.put(new Hex(i,j),HexPolyFactory.getHexPoly(new Hex(i,j),j));
	}
}

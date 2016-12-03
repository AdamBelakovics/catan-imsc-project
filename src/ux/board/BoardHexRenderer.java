package ux.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import controller.map.Buildable;
import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.Building;
import controller.player.Resource;
import ux.ImageRenderer;
import ux.RendererDataStore;
import ux.ResourceXMLReader;
import ux.board.BoardRenderer.BoardOrientation;
import ux.ui.InterfaceColorProfile;
import ux.ui.StringPainter;

/**
 * Renderer responsible for rendering the hex fields
 * @author Kiss Lorinc
 *
 */
public class BoardHexRenderer extends ImageRenderer {
	private double rotationLeft;
	private Graphics2D hexCanvas;
	private double zoomLevel;
	
	private RendererDataStore ds;

	//Constants
	final double eps=0.01;
	final double rotationStep=0.01;

	/**
	 * Initializes the renderer
	 * @param _dataStore the RendererDataStore object belonging to the parent Renderer
	 */
	public BoardHexRenderer(RendererDataStore _dataStore) {
		
		ds=_dataStore;
		generateHexes();
		rotationLeft=0;
		zoomLevel=1;
		
	}
	
	public void paint(Graphics g) {
		hexCanvas=(Graphics2D)g;
		hexCanvas.setFont(hexCanvas.getFont().deriveFont(18.0f));
		hexCanvas.setColor(InterfaceColorProfile.bgWaterColor);
		hexCanvas.fillRect(0, 0, ds.width, ds.height);
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
		ds.boardTransformation=new AffineTransform();
		//Setting origin to center
		ds.boardTransformation.translate(ds.width/2, ds.height/2);

		//Isometric tilt
		ds.boardTransformation.scale(1*zoomLevel, 0.5*zoomLevel);

		//Applying rotation, if needed
		if (Math.abs(rotationLeft)>eps) {
			ds.boardTransformation.rotate(ds.boardOrientation.ordinal()*Math.PI/3-rotationLeft);
			rotationLeft-=Math.signum(rotationLeft)*rotationStep;
		} 
		else ds.boardTransformation.rotate(ds.boardOrientation.ordinal()*Math.PI/3);
		hexCanvas.setTransform(ds.boardTransformation);
	}

	/**
	 * Rotates the board
	 * @param i direction of rotation
	 * 
	 * @author Kiss Lorinc
	 */
	public void cycleOrientation(int i) {
		if (Math.abs(rotationLeft)<eps) {
			ds.boardOrientation=BoardOrientation.values()[((ds.boardOrientation.ordinal()+i+6)%6)];
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

	public Hex getHexUnderCursor(int x, int y) {

		for (Map.Entry<Hex, HexPoly> entry : ds.hexMap.entrySet()){
			try {
				if (entry.getValue().contains(ds.boardTransformation.inverseTransform(new Point(x,y), null))) return entry.getKey();
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
		Hex currHex;
		HexPoly currHexPoly;
		for (Map.Entry<Hex, HexPoly> entry : ds.hexMap.entrySet()){
			currHex=entry.getKey();
			currHexPoly=entry.getValue();
			if (entry.getKey().getResource()==null)	hexCanvas.setPaint(InterfaceColorProfile.waterColor);
			else hexCanvas.setPaint(ds.colorMap.get(currHex.getResource()));
			hexCanvas.draw(currHexPoly);
			hexCanvas.fillPolygon(currHexPoly);
			
			if (currHex.getProsperity()!=0) StringPainter.printString(hexCanvas, new Integer(currHex.getProsperity()).toString(), 14, currHexPoly.x, currHexPoly.y);
			if (currHex.hasThief) {
				hexCanvas.setColor(Color.black);
				hexCanvas.fillOval(currHexPoly.x-10, currHexPoly.y-10, 14, 14);
			}
			
			if (currHexPoly.selected) ds.selectedTile=currHex;
		}
		
		if (ds.selectedTile!=null) {
			hexCanvas.setPaint(InterfaceColorProfile.fgColor);
			hexCanvas.draw(ds.hexMap.get(ds.selectedTile));
		}
	}
	
	

	/**
	 * Selects or deselects given hex
	 * @param h the hex to select
	 * @author Kiss Lorinc
	 */
	public void selectHex(Hex h) {
		System.out.println("[Renderer]Selected "+ds.hexMap.get(h).toString());
		deselectHexes();
		ds.hexMap.get(h).selected=!(ds.hexMap.get(h).selected);
	}
	
	public void deselectHexes() {
		for (Map.Entry<Hex, HexPoly> entry : ds.hexMap.entrySet()) entry.getValue().selected=false;
		ds.selectedTile=null;
	}
	
	
	/**
	 * Generates hexes in the HashMap
	 * 
	 * @author      Kiss Lorinc
	 */
	private void generateHexes() {
		int x=0;
		int y=0;
		Table.IteratorHex iterator=ds.board.hexIterator();
		do {
			Hex currHex=iterator.next();
			ds.hexMap.put(currHex, HexPolyFactory.getHexPoly(currHex, x-3, y-3));
			do {
				if (y < 6)
					y++;
				else if(x < 6){
					x++;
					y = 0;
				}
			}
			while(x+4 <= y || x-4 >= y);
		}
		while (iterator.hasNext());
	}
}

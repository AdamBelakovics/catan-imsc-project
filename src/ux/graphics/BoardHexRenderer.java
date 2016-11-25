package ux.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.Building;
import controller.player.Resource;
import ux.graphics.BoardRenderer.BoardOrientation;

public class BoardHexRenderer extends ImageRenderer {
	private Table board;
	private double rotationLeft;
	private Graphics2D hexCanvas;
	private double zoomLevel;
	private HashMap<Hex,HexPoly> hexMap;
	private HashMap<Resource,Color> colorMap;
	private HashMap<Point,Vertex> vertexMap;
	Map.Entry<Hex, HexPoly> selectedTile=null;
	
	BuildingEnum currentlyBuilding;

	//Constants
	final double eps=0.01;
	final double rotationStep=0.01;

	public BoardHexRenderer(ImageRenderer _parentRenderer,Table _board,int _width,int _height) {
		
		super(_width,_height);

		parentRenderer=_parentRenderer;
		width=parentRenderer.getWidth();
		height=parentRenderer.getHeight();
		board=_board;
		hexMap=new HashMap<Hex,HexPoly>();
		colorMap=ResourceXMLReader.readTextureXML("textures.xml");
		generateHexes();
		generateVertices();
		currentlyBuilding=null;
		rotationLeft=0;
		zoomLevel=1;
	}

	private void generateVertices() {
		for (Vertex v : board.getNodes())
			vertexMap.put(pointFromVertex(v), v);		
	}

	private Point pointFromVertex(Vertex v) {
			//TODO
		return new Point();
	}

	public void paint(Graphics g) {
		hexCanvas=(Graphics2D)g;
		hexCanvas.setColor(Color.white);
		hexCanvas.fillRect(0, 0, width, height);
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
			hexCanvas.rotate((((BoardRenderer)parentRenderer).boardOrientation.ordinal())*Math.PI/3-rotationLeft);
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
			((BoardRenderer)parentRenderer).boardOrientation=BoardOrientation.values()[((((BoardRenderer)parentRenderer).boardOrientation.ordinal()+i+6)%6)];
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

		for (Map.Entry<Hex, HexPoly> entry : hexMap.entrySet()){
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
		for (Map.Entry<Hex, HexPoly> entry : hexMap.entrySet()){
			hexCanvas.setPaint(colorMap.get(entry.getKey().getResource()));
			hexCanvas.draw(entry.getValue());
			hexCanvas.fillPolygon(entry.getValue());
			if (entry.getValue().selected) selectedTile=entry;
		}
		if (selectedTile!=null) {
			hexCanvas.setPaint(Color.black);
			hexCanvas.draw(selectedTile.getValue());
		}
	}

	/**
	 * Selects or deselects given hex
	 * @param h the hex to select
	 * @author Kiss Lorinc
	 */
	public void selectHex(Hex h) {
		System.out.println("[Renderer]Selected "+hexMap.get(h).toString());
		for (Map.Entry<Hex, HexPoly> entry : hexMap.entrySet())	if (!entry.getKey().equals(h)) entry.getValue().selected=false;
		hexMap.get(h).selected=!(hexMap.get(h).selected);
		
	}
	/**
	 * Generates hexes in the HashMap
	 * 
	 * @author      Kiss Lorinc
	 */
	private void generateHexes() {
		int x=0;
		int y=0;
		Table.IteratorHex iterator=board.hexIterator();
		do {
			Hex currHex=iterator.next();
			hexMap.put(currHex, HexPolyFactory.getHexPoly(currHex, x-3, y-3));
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

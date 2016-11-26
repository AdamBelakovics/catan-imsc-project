package ux.graphics.board;

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

import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.Building;
import controller.player.Resource;
import ux.graphics.BuildingEnum;
import ux.graphics.ImageRenderer;
import ux.graphics.ResourceXMLReader;
import ux.graphics.board.BoardRenderer.BoardOrientation;
import ux.graphics.userinterface.InterfaceColorProfile;

public class BoardHexRenderer extends ImageRenderer {
	private Table board;
	private double rotationLeft;
	private Graphics2D hexCanvas;
	private double zoomLevel;
	private HashMap<Hex,HexPoly> hexMap;
	private HashMap<Resource,Color> colorMap;
	private Hex selectedTile=null;
	
	AffineTransform boardTransformation;
	
	public BuildingEnum currentlyBuilding;

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
		
		currentlyBuilding=null;
		rotationLeft=0;
		zoomLevel=1;
	}
	
	/**
	 * Returns with the HexPoly of the given hex from the hex map
	 * @param hex required hex
	 * @return HexPoly of the given hex
	 */
	public HexPoly getHexPolyFromHex(Hex hex) {
		return hexMap.get(hex);
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
		boardTransformation=new AffineTransform();
		//Setting origin to center
		boardTransformation.translate(width/2, height/2);

		//Isometric tilt
		boardTransformation.scale(1*zoomLevel, 0.5*zoomLevel);

		//Applying rotation, if needed
		if (Math.abs(rotationLeft)>eps) {
			boardTransformation.rotate((((BoardRenderer)parentRenderer).boardOrientation.ordinal())*Math.PI/3-rotationLeft);
			rotationLeft-=Math.signum(rotationLeft)*rotationStep;
		} 
		else boardTransformation.rotate(((BoardRenderer)parentRenderer).boardOrientation.ordinal()*Math.PI/3);
		hexCanvas.setTransform(boardTransformation);
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

	public Hex getHexUnderCursor(int x, int y) {

		for (Map.Entry<Hex, HexPoly> entry : hexMap.entrySet()){
			try {
				if (entry.getValue().contains(boardTransformation.inverseTransform(new Point(x,y), null))) return entry.getKey();
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
			if (entry.getValue().selected) selectedTile=entry.getKey();
		}
		
		if (selectedTile!=null) {
			hexCanvas.setPaint(InterfaceColorProfile.fgColor);
			hexCanvas.draw(hexMap.get(selectedTile));
		}
	}
	
	

	/**
	 * Selects or deselects given hex
	 * @param h the hex to select
	 * @author Kiss Lorinc
	 */
	public void selectHex(Hex h) {
		System.out.println("[Renderer]Selected "+hexMap.get(h).toString());
		deselectHexes();
		hexMap.get(h).selected=!(hexMap.get(h).selected);
	}
	
	public void deselectHexes() {
		for (Map.Entry<Hex, HexPoly> entry : hexMap.entrySet()) entry.getValue().selected=false;
		selectedTile=null;
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

package ux.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import controller.Game;
import controller.map.Buildable;
import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.Building;
import controller.player.City;
import controller.player.Player;
import controller.player.Settlement;
import ux.ImageRenderer;
import ux.Renderer;
import ux.ui.InterfaceColorProfile;
import ux.ui.UIController;

public class BoardVertexRenderer extends ImageRenderer {

	private UIController uiController;
	private BoardHexRenderer hexRenderer; 
	HashMap<Vertex,Point> vertexMap=new HashMap();
	private Vertex selectedVertex=null;
	private Graphics2D vertexCanvas;
	private Table board;
	private ArrayList<Player> playersList;
	

	public Buildable currentlyBuilding=null;
	
	private final int eps=10;

	BoardVertexRenderer(Table _board, BoardHexRenderer _hexRenderer, UIController _currUIC, int _width, int _height) {
		super(_width, _height);
		board=_board;
		uiController=_currUIC;
		hexRenderer=_hexRenderer;
		generateVertices();
	}

	@Override
	public void paint(Graphics g) {
		vertexCanvas=(Graphics2D)g;
		paintVertices();

	}

	private void paintVertices() {

		for (Entry<Vertex,Point> v : vertexMap.entrySet()) {
			
			Point2D transformedPoint=new Point();
			hexRenderer.boardTransformation.transform(v.getValue(), transformedPoint);
			
			Building detectedBuilding=v.getKey().getBuilding();
			if (detectedBuilding!=null) {			
				InterfaceColorProfile.setPlayerColor(vertexCanvas,detectedBuilding);
				if (detectedBuilding instanceof Settlement) {
					vertexCanvas.fillOval((int)transformedPoint.getX()-7, (int)transformedPoint.getY()-7, 10, 10);
				}
				else if (detectedBuilding instanceof City) {
					vertexCanvas.fillOval((int)transformedPoint.getX()-14, (int)transformedPoint.getY()-14, 20, 20);
				}
			} else if (selectedVertex!=null && uiController.controlledPlayer.isBuildPossible(currentlyBuilding, selectedVertex)) {
				if (selectedVertex!=null && v.getKey().equals(selectedVertex))
					vertexCanvas.setColor(InterfaceColorProfile.vertexColor);
				else vertexCanvas.setColor(InterfaceColorProfile.selectedColor);
				vertexCanvas.fillOval((int)transformedPoint.getX()-4, (int)transformedPoint.getY()-4, 5, 5);
			}
		}
	}
	
	/**
	 * Generates vertex center point from neighbour hexes center points to vertexMap
	 */
	private void generateVertices() {
		for (Vertex v : board.getNodes()) {
			ArrayList<Hex> neighbourHexes=v.getNeighbourHexes();
			
			HexPoly A=hexRenderer.getHexPolyFromHex(neighbourHexes.get(0));
			HexPoly B=hexRenderer.getHexPolyFromHex(neighbourHexes.get(1));
			HexPoly C=hexRenderer.getHexPolyFromHex(neighbourHexes.get(2));
			vertexMap.put(v,new Point((A.x+B.x+C.x)/3,(A.y+B.y+C.y)/3));
		}
	}
	
	public Vertex getVertexUnderCursor(int x, int y) {
		Point transformedPoint=new Point();
		try {
			hexRenderer.boardTransformation.inverseTransform(new Point(x,y), transformedPoint);
		} catch (NoninvertibleTransformException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for (HashMap.Entry<Vertex,Point> e : vertexMap.entrySet()) {
			if (Math.abs(e.getValue().getX()-transformedPoint.x)<eps && Math.abs(e.getValue().getY()-transformedPoint.y)<eps) return e.getKey();
		}
		return null;
	}
	
	public void selectVertex(Vertex v) {	
		selectedVertex=v;
	}
	
	public void deselectVertices() {
		selectedVertex=null;
	}
}
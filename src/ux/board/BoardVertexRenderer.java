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

import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.Building;
import controller.player.Settlement;
import ux.ImageRenderer;
import ux.ui.InterfaceColorProfile;

public class BoardVertexRenderer extends ImageRenderer {

	private BoardHexRenderer hexRenderer;
	private HashMap<Point,Vertex> vertexMap=new HashMap();
	private Vertex selectedVertex=null;
	private Graphics2D vertexCanvas;
	private Table board;
	
	private final int eps=10;

	BoardVertexRenderer(Table _board, BoardHexRenderer _hexRenderer, int _width, int _height) {
		super(_width, _height);
		board=_board;
		hexRenderer=_hexRenderer;
		
		generateVertices();
	}

	@Override
	public void paint(Graphics g) {
		vertexCanvas=(Graphics2D)g;
		paintVertices();

	}

	private void paintVertices() {

		for (Entry<Point, Vertex> v : vertexMap.entrySet()) {
			if (/*board.isBuildPossibleAt(new Settlement(), v.getValue())*/true) {
				if (selectedVertex!=null && v.getValue().equals(selectedVertex))
					vertexCanvas.setColor(InterfaceColorProfile.vertexColor);
				else vertexCanvas.setColor(InterfaceColorProfile.selectedColor);
				Point2D transformedPoint=new Point();
					hexRenderer.boardTransformation.transform(v.getKey(), transformedPoint);
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
			vertexMap.put(new Point((A.x+B.x+C.x)/3,(A.y+B.y+C.y)/3),v);
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
		
		for (HashMap.Entry<Point,Vertex> e : vertexMap.entrySet()) {
			if (Math.abs(e.getKey().getX()-transformedPoint.x)<eps && Math.abs(e.getKey().getY()-transformedPoint.y)<eps) return e.getValue();
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
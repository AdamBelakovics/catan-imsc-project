package ux.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.Building;
import controller.player.Settlement;

public class BoardVertexRenderer extends ImageRenderer {

	private BoardHexRenderer hexRenderer;
	private HashMap<Point,Vertex> vertexMap=new HashMap();
	private Vertex selectedVertex=null;
	private Graphics2D vertexCanvas;
	private Table board;
	
	private final int eps=3;

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
			if (board.isBuildPossibleAt(new Settlement(), v.getValue())) {
				if (selectedVertex!=null && v.getValue().equals(selectedVertex))
					vertexCanvas.setColor(InterfaceColorProfile.vertexColor);
				else vertexCanvas.setColor(InterfaceColorProfile.selectedColor);
				Point transformedPoint=new Point();
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
		for (HashMap.Entry<Point,Vertex> e : vertexMap.entrySet()) {
			if (Math.abs(e.getKey().getX()-x)<eps && Math.abs(e.getKey().getY()-y)<eps) return e.getValue();
		}
		return null;
	}
	
	public void setSelectedVertex(Vertex v) {
		System.out.println("[BoardVertexRenderer] Selected "+v);		
		selectedVertex=v;
	}
}
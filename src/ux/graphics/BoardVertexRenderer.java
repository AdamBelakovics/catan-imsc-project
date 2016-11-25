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

public class BoardVertexRenderer extends ImageRenderer {

	private BoardHexRenderer hexRenderer;
	private HashMap<Point,Vertex> vertexMap;
	private Graphics2D vertexCanvas;
	private Table board;

	BoardVertexRenderer(Table _board, BoardHexRenderer _hexRenderer, int _width, int _height) {
		super(_width, _height);
		board=_board;
		hexRenderer=_hexRenderer;
		
		vertexMap=new HashMap();
		generateVertices();
	}

	@Override
	public void paint(Graphics g) {
		vertexCanvas=(Graphics2D)g;
		paintVertices();

	}

	private void paintVertices() {
		for (Entry<Point, Vertex> v : vertexMap.entrySet()) {
			vertexCanvas.setColor(Color.red);
			Point transformedPoint=new Point();
			hexRenderer.boardTransformation.transform(v.getKey(), transformedPoint);
			vertexCanvas.fillOval((int)transformedPoint.getX()-4, (int)transformedPoint.getY()-4, 5, 5);
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
}
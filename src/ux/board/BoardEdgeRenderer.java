package ux.board;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.map.Edge;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.Road;
import ux.ImageRenderer;
import ux.ui.InterfaceColorProfile;

public class BoardEdgeRenderer extends ImageRenderer {

	private BoardVertexRenderer vertexRenderer;
	private BoardHexRenderer hexRenderer;
	private Graphics2D edgeCanvas;
	private Table board;
	private HashMap<Edge, ArrayList<Vertex> > edgeMap=new HashMap();
	
	private final int eps=10;
	
	protected BoardEdgeRenderer(BoardVertexRenderer _vertexRenderer, BoardHexRenderer _hexRenderer, Table _board, int _width, int _height) {
		super(_width, _height);
		vertexRenderer=_vertexRenderer;
		hexRenderer=_hexRenderer;
		board=_board;
		generateEdges();
	}

	@Override
	public void paint(Graphics g) {
		edgeCanvas=(Graphics2D)g;

		edgeCanvas.setTransform(hexRenderer.boardTransformation);
		paintEdges();
	}
	
	

	private void paintEdges() {
		for (HashMap.Entry<Edge, ArrayList<Vertex> > e : edgeMap.entrySet() ) {
			if (e.getKey().getBuilding() instanceof Road) {
				edgeCanvas.setPaint(InterfaceColorProfile.selectedColor);
				edgeCanvas.drawLine(
						vertexRenderer.vertexMap.get(e.getValue().get(0)).x,
						vertexRenderer.vertexMap.get(e.getValue().get(0)).y,
						vertexRenderer.vertexMap.get(e.getValue().get(1)).x,
						vertexRenderer.vertexMap.get(e.getValue().get(1)).y
						);
			}
		}
		
	}

	private void generateEdges() {
		for (Edge e : board.getEdges()) {
			edgeMap.put(e, e.getEnds());
		}
	}
	
	

}

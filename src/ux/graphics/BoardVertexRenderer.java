 package ux.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;

public class BoardVertexRenderer extends ImageRenderer {
	Table board;
	HashMap<Vertex,Point2D> vertexMap;
	Graphics2D vertexCanvas;
	BoardHexRenderer hexRenderer;

	/**
	 * Creates the BoardVertexRenderer and generates vertex map
	 * @param _board game board
	 * @param _hexRenderer hex renderer of the parent renderer
	 * @param _width window width
	 * @param _height window height
	 */
	BoardVertexRenderer(Table _board, BoardHexRenderer _hexRenderer, int _width, int _height) {
		super(_width, _height);
		hexRenderer=_hexRenderer;
		board=_board;
		generateVertexes();
	}

	/**
	 * Generates vertex center point from neighbour hexes center points to vertexMap
	 */
	private void generateVertexes() {
		for (Vertex v : board.getNodes()) {
			ArrayList<Hex> neighbourHexes=v.getNeighbourHexes();
			
			double P1=hexRenderer.getHexPolyFromHex(neighbourHexes.get(0)).x;
			double P2=hexRenderer.getHexPolyFromHex(neighbourHexes.get(0)).y;
			double Q1=hexRenderer.getHexPolyFromHex(neighbourHexes.get(1)).x;
			double Q2=hexRenderer.getHexPolyFromHex(neighbourHexes.get(1)).y;
			double R1=hexRenderer.getHexPolyFromHex(neighbourHexes.get(2)).x;
			double R2=hexRenderer.getHexPolyFromHex(neighbourHexes.get(2)).y;
			
			double Pabs=Math.sqrt(P1*P1+P2*P2);
			double Qabs=Math.sqrt(Q1*Q1+Q2*Q2);
			double Rabs=Math.sqrt(R1*R1+R2*R2);
			
			double M11=P1*(Q2-R2)+Q1*(R2-P2)+R1*(P2-Q2);
			double M12=Pabs*(Q2-R2)+Qabs*(R2-P2)+Rabs*(P2-Q2);
			double M13=Pabs*(Q1-R1)+Qabs*(R1-P1)+Rabs*(P1-Q1);
			
			vertexMap.put(v, new Point((int)(Math.floor(0.5*M12/M11)),(int)(Math.floor(-0.5*M13/M11))));
		}
		
	}


	/* (non-Javadoc)
	 * @see ux.graphics.ImageRenderer#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		vertexCanvas=(Graphics2D)g;
		for (HashMap.Entry<Vertex,Point2D> v : vertexMap.entrySet()) {
			vertexCanvas.setColor(Color.red);
			vertexCanvas.fillOval((int)v.getValue().getX(), (int)v.getValue().getY(), 30, 30);
		}
		
	}
}

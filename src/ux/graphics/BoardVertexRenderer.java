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

			/*double P1=hexRenderer.getHexPolyFromHex(neighbourHexes.get(0)).x;
			double P2=hexRenderer.getHexPolyFromHex(neighbourHexes.get(0)).y;
			double Q1=hexRenderer.getHexPolyFromHex(neighbourHexes.get(1)).x;
			double Q2=hexRenderer.getHexPolyFromHex(neighbourHexes.get(1)).y;
			double R1=hexRenderer.getHexPolyFromHex(neighbourHexes.get(2)).x;
			double R2=hexRenderer.getHexPolyFromHex(neighbourHexes.get(2)).y;

			double Pabs=Math.sqrt(P1*P1+P2*P2);
		System.out.println("Pabs:" + Pabs);
		double Qabs=Math.sqrt(Q1*Q1+Q2*Q2);
		System.out.println("Qabs:" + Qabs);
		double Rabs=Math.sqrt(R1*R1+R2*R2);
		System.out.println("Rabs:" + Rabs);

		double M11=P1*(Q2-R2)+Q1*(R2-P2)+R1*(P2-Q2);
		System.out.println("M11:" + M11);
		double M12=Pabs*(Q2-R2)+Qabs*(R2-P2)+Rabs*(P2-Q2);
		System.out.println("M12:" + M12);
		double M13=Pabs*(Q1-R1)+Qabs*(R1-P1)+Rabs*(P1-Q1);
		System.out.println("M13:" + M13);

		System.out.println(0.5*M12/M11+" "+-0.5*M13/M11);

		vertexMap.put(v, new Point((int)(Math.floor(0.5*M12/M11)),(int)(Math.floor(-0.5*M13/M11))));

			double m1=(Q2-P2)/(Q1-P1);
			System.out.println("m1: "+ m1);
			double m2=(R2-Q2)/(R1-Q1);
			System.out.println("m2: "+ m2);

			double x=(m1*m2*(R2-P2)+m2*(Q1-R1)-m1*(P1-Q1))/(2*(m2-m1));
			System.out.println("x: "+ x);
			double y=(P2+Q2)/2-(x-(P1+Q1)/2)/m1;
			System.out.println("y: "+ y);


			vertexMap.put(new Point((int)(Math.floor(x)),(int)(Math.floor(y))),v);*/


		}

	}
}
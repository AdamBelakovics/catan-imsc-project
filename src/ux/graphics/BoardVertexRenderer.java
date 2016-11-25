 package ux.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;

public class BoardVertexRenderer extends ImageRenderer {
	Table board;
	HashMap<Vertex,Point2D> vertexMap;
	Graphics2D vertexCanvas;

	BoardVertexRenderer(Table _board, int _width, int _height) {
		super(_width, _height);
		board=_board;
		generateVertexes();
	}

	private void generateVertexes() {
		for (Vertex v : board.getNodes()) {
			
			Point2D P=new Point();
			Point2D Q=new Point();
			Point2D R=new Point();
			
			double Pabs;
			double Qabs;
			double Rabs;
			
			double M11=0;
			double M12=0;
			double M13=0;
			
			vertexMap.put(v, new Point((int)Math.floor(0.5*M12/M11),(int)Math.floor(-0.5*M13/M11)));
		}
		
	}

	@Override
	public void paint(Graphics g) {
		vertexCanvas=(Graphics2D)g;
		// TODO Auto-generated method stub
		
	}
}

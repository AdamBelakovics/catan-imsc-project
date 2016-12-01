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
import ux.RendererDataStore;
import ux.ui.InterfaceColorProfile;
import ux.ui.UIController;

/**
 * Renderer responsible for rendering vertices
 * @author Kiss Lorinc
 *
 */
public class BoardVertexRenderer extends ImageRenderer {


	private Graphics2D vertexCanvas;
	private RendererDataStore ds;	
	private final int eps=10;

	BoardVertexRenderer(RendererDataStore _ds) {
		ds=_ds;
		generateVertices();
	}

	@Override
	public void paint(Graphics g) {
		vertexCanvas=(Graphics2D)g;
		paintVertices();

	}

	/**
	 * Paints vertices on the canvas
	 * @author Kiss Lorinc
	 */
	private void paintVertices() {

		for (Entry<Vertex,Point> v : ds.vertexMap.entrySet()) {
			
			Point2D transformedPoint=new Point();
			ds.boardTransformation.transform(v.getValue(), transformedPoint);
			
			Building detectedBuilding=v.getKey().getBuilding();
			if (detectedBuilding!=null) {			
				InterfaceColorProfile.setPlayerColor(vertexCanvas,detectedBuilding);
				if (detectedBuilding instanceof Settlement) {
					vertexCanvas.fillOval((int)transformedPoint.getX()-7, (int)transformedPoint.getY()-7, 10, 10);
				}
				else if (detectedBuilding instanceof City) {
					vertexCanvas.fillOval((int)transformedPoint.getX()-14, (int)transformedPoint.getY()-14, 20, 20);
				}
			} else if (ds.selectedVertex!=null && ds.currUIC.controlledPlayer.isBuildPossible(ds.currentlyBuilding, ds.selectedVertex)) {
				if (ds.selectedVertex!=null && v.getKey().equals(ds.selectedVertex))
					vertexCanvas.setColor(InterfaceColorProfile.vertexColor);
				else vertexCanvas.setColor(InterfaceColorProfile.selectedColor);
				vertexCanvas.fillOval((int)transformedPoint.getX()-4, (int)transformedPoint.getY()-4, 5, 5);
			}
		}
	}
	
	/**
	 * Generates vertex center point from neighbour hexes center points to vertexMap
	 * @author Kiss Lorinc
	 */
	private void generateVertices() {
		for (Vertex v : ds.board.getNodes()) {
			ArrayList<Hex> neighbourHexes=v.getNeighbourHexes();
			
			HexPoly A=ds.hexMap.get(neighbourHexes.get(0));
			HexPoly B=ds.hexMap.get(neighbourHexes.get(1));
			HexPoly C=ds.hexMap.get(neighbourHexes.get(2));
			ds.vertexMap.put(v,new Point((A.x+B.x+C.x)/3,(A.y+B.y+C.y)/3));
		}
	}
	
	/**
	 * Returns with the vertex of the given point
	 * @author Kiss Lorinc
	 * @param x x coordinate of the point
	 * @param y y coordinate of the point
	 * @return Vertex under the given point
	 */
	public Vertex getVertexUnderCursor(int x, int y) {
		Point transformedPoint=new Point();
		try {
			ds.boardTransformation.inverseTransform(new Point(x,y), transformedPoint);
		} catch (NoninvertibleTransformException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for (HashMap.Entry<Vertex,Point> e : ds.vertexMap.entrySet()) {
			if (Math.abs(e.getValue().getX()-transformedPoint.x)<eps && Math.abs(e.getValue().getY()-transformedPoint.y)<eps) return e.getKey();
		}
		return null;
	}
	
	/**
	 * Selects the given vertex
	 * @param v the vertex to select
	 * @author Kiss Lorinc
	 */
	public void selectVertex(Vertex v) {	
		ds.selectedVertex=v;
	}
	
	/**
	 * Deselects the vertex
	 * @author Kiss Lorinc
	 */
	public void deselectVertices() {
		ds.selectedVertex=null;
	}
}
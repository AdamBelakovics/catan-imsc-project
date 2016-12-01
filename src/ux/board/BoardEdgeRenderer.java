package ux.board;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.Game;
import controller.map.Edge;
import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.Building;
import controller.player.Player;
import controller.player.Road;
import ux.ImageRenderer;
import ux.RendererDataStore;
import ux.ui.InterfaceColorProfile;

/**
 * Renderer responsible for painting edges
 * @author Kiss Lorinc
 *
 */
public class BoardEdgeRenderer extends ImageRenderer {

	private Graphics2D edgeCanvas;
	private RendererDataStore ds;
	private HashMap<Edge, ArrayList<Vertex> > edgeMap=new HashMap();
	private HashMap<Edge, Polygon> edgeClickMap=new HashMap();

	
	private final int eps=10;
	
	/**
	 * Initializes the Edge Renderer
	 * @param _vertexRenderer renderer responsible for vertices
	 * @param _hexRenderer renderer responsible for hex fields
	 * @param _board playing board
	 * @param _width width of the window
	 * @param _height height of the window
	 */
	protected BoardEdgeRenderer(RendererDataStore _dataStore) {
		ds=_dataStore;

		generateEdges();
	}

	@Override
	public void paint(Graphics g) {
		edgeCanvas=(Graphics2D)g;

		edgeCanvas.setTransform(ds.boardTransformation);
		paintEdges();
	}
	
	

	/**
	 * Paints the edges, then restores the previous drawing color
	 * @author Kiss Lorinc
	 */
	private void paintEdges() {
		Stroke prevStroke = edgeCanvas.getStroke();
		edgeCanvas.setStroke(new BasicStroke(5));
		for (HashMap.Entry<Edge, ArrayList<Vertex> > e : edgeMap.entrySet() ) {
			if (e.getKey().getBuilding()!=null) {
				InterfaceColorProfile.setPlayerColor(edgeCanvas, e.getKey().getBuilding());
				edgeCanvas.drawLine(
						ds.vertexMap.get(e.getValue().get(0)).x,
						ds.vertexMap.get(e.getValue().get(0)).y,
						ds.vertexMap.get(e.getValue().get(1)).x,
						ds.vertexMap.get(e.getValue().get(1)).y
						);
			}
		}		
		edgeCanvas.setStroke(prevStroke);
		
	}

	/**
	 * Generates all edges and calculates the clicking map polygons
	 */
	private void generateEdges() {
		for (Edge e : ds.board.getEdges()) {
			edgeMap.put(e, e.getEnds());
			
			double eps=0.3;
			
			Polygon clickPoly=new Polygon();
			Point v1=ds.vertexMap.get(e.getEnds().get(0));
			Point v2=ds.vertexMap.get(e.getEnds().get(1));

			clickPoly.addPoint(v1.x,v1.y);
			int p1=(v1.x+v2.x)/2;
			int p2=(v1.y+v2.y)/2;
			int n1=Math.abs(v1.y-v2.y);
			int n2=Math.abs(v1.x-v2.x);
			clickPoly.addPoint((int)Math.floor(p1+eps*n1), (int)Math.floor(p2+eps*n2));

			clickPoly.addPoint(v2.x,v2.y);
			
			clickPoly.addPoint((int)Math.floor(p1-eps*n1), (int)Math.floor(p2-eps*n2));
			
			edgeClickMap.put(e, clickPoly);
		}
	}
	
	/**
	 * Gets the edge (defined by the click map) under the given coordinates
	 * @param x x coordinate of the click
	 * @param y y coordinate of the click
	 * @return the Edge under the point specified
	 */
	public Edge getEdgeUnderCursor(int x, int y) {
		for (Map.Entry<Edge, Polygon> entry : edgeClickMap.entrySet()){
			try {
				if (
						entry.getValue().contains(ds.boardTransformation.inverseTransform(new Point(x,y), null))) return entry.getKey();
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}

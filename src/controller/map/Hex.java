//By Adam Belakovics 2016. 10. 25

package controller.map;

import java.awt.Graphics;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.Map;

import controller.player.Building;


public class Hex implements TableElement {
	HashMap<Orientation, Vertex> vertices = new HashMap<Orientation, Vertex>(6);
	int xpos;
	int ypos;
	String id;
	
	public Hex (int x, int y){
		id = Integer.toString(x) + Integer.toString(y);
		xpos = x;
		ypos = y;
	}
	
	public int getx(){
		return xpos;
	}
	
	public int gety(){
		return ypos;
	}
	
	private void generateVertices() {
		for(Orientation o : Orientation.values()){
			if(o == Orientation.EAST || o == Orientation.WEST){
				//do nothing, we dont need vertices there
			}
			else{
				
			}
		}
	}
	
	
//for testing
	static public Polygon drawableHex(double x, double y, double r){
	    double pi = Math.PI;
	    int[] xPoints = new int[6];
	    int[] yPoints = new int[6];
	    int nPoints = 6;
		for(int i=0; i<6; i++) {
		    xPoints[i] = (int) (x + r*Math.cos(i*2*pi/6 + pi/6));
		    yPoints[i] = (int) (y + r*Math.sin(i*2*pi/6 + pi/6));
		}
		return new Polygon(xPoints, yPoints, nPoints);
	}
//for testing end

	@Override
	public boolean isBuildPossible(Building what) {
		// TODO Auto-generated method stub
		return false;
	}

}
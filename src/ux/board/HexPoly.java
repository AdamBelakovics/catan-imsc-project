package ux.board;

import java.awt.Polygon;

/**
 * Represents the polygon of the hex fields
 * @author Kiss Lorinc
 *
 */
public class HexPoly extends Polygon {
	int x,y;
	public boolean selected;
	public HexPoly(int _x, int _y) {
		x=_x;
		y=_y;
		selected=false;
	}

}

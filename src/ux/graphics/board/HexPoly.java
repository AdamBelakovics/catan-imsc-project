package ux.graphics.board;

import java.awt.Polygon;

public class HexPoly extends Polygon {
	int x,y;
	public boolean selected;
	public HexPoly(int _x, int _y) {
		x=_x;
		y=_y;
		selected=false;
	}

}

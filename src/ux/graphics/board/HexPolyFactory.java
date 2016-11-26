package ux.graphics.board;

import java.awt.Polygon;

import controller.map.Hex;

/**
 * Polygon generator class, with a single static function
 * @author Kiss Lorinc
 *
 */
public class HexPolyFactory {
	/**
	 * Returns the complete hex field to be drawn
	 * @param sourceHex the source Hex object
	 * @param offset the offset of the hexes from the left
	 * @return drawable Polygon
	 */
	public static HexPoly getHexPoly(Hex sourceHex,int x, int y) {
		final int radius=50;
		
		// distance between hex center points
		double distance=2*radius*Math.cos(Math.toRadians(30));
		
		// center point of the hex
		int offset=y;
		double baseX=x*distance-offset*distance/2;
		double baseY=y*(distance*Math.sin(Math.toRadians(30))/2+radius+4);
		
		HexPoly hexPoly=new HexPoly((int)Math.round(baseX),(int)Math.round(baseY));
		
		for (int i=0;i!=360;i+=60) hexPoly.addPoint(
				(int)(baseX+(radius+1)*Math.sin(Math.toRadians(i))), 
				(int)(baseY+(radius+1)*Math.cos(Math.toRadians(i))));
		return hexPoly;
	}
}

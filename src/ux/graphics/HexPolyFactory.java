package ux.graphics;

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
	public static HexPoly getHexPoly(Hex sourceHex,int offset) {
		final int radius=50;
		HexPoly hexPoly=new HexPoly();
		// distance between hex center points
		double distance=2*radius*Math.cos(Math.toRadians(30));
		
		// center point of the hex
		double baseX=sourceHex.getx()*distance-offset*distance/2;
		double baseY=sourceHex.gety()*(distance*Math.sin(Math.toRadians(30))/2+radius+4);
		
		for (int i=0;i!=360;i+=60) hexPoly.addPoint(
				(int)(baseX+radius*Math.sin(Math.toRadians(i))), 
				(int)(baseY+radius*Math.cos(Math.toRadians(i))));
		// TODO Texturing
		return hexPoly;
	}
}

package ux.graphics.userinterface;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class StringPainter {
	public static void printString(Graphics canvas, String str,int x, int y) {
		FontMetrics metrics=canvas.getFontMetrics();
		Color previousColor=canvas.getColor();
		canvas.setColor(InterfaceColorProfile.fgColor);
		canvas.drawString(str, x-metrics.stringWidth(str)/2, y-metrics.getHeight()/2+12);
		canvas.setColor(previousColor);
	}
}

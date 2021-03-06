package ux.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StringPainter {
	private static Font mainFont;
	private static boolean fallback=false;
	private static int fontHeight=12;
	public static void initializeStringPainter() {
		try {
			mainFont=Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("assets/PressStart2P.ttf"));
			mainFont=mainFont.deriveFont(mainFont.getStyle(), 8);
			fontHeight=(int)Math.floor(mainFont.getSize2D());
		} catch (Exception e) {
			System.out.println("[StringPainter] Problem opening font, activating fallback");
			fallback=true;
		}
	}
	
	public static void printString(Graphics canvas, String str, int x, int y) {
		printString(canvas, str, InterfaceColorProfile.fgColor,x,y);
	}

	public static void printString(Graphics canvas, String str, int size, int x, int y) {
		if (!fallback) canvas.setFont(mainFont.deriveFont(Font.PLAIN,size));
		else canvas.setFont(canvas.getFont().deriveFont(Font.PLAIN,size));
		int prevFontHeight=fontHeight;
		fontHeight=size;
		Color previousColor=canvas.getColor();
		canvas.setColor(InterfaceColorProfile.fgColor);
		paintString(canvas, str, x, y);
		canvas.setColor(previousColor);
		fontHeight=prevFontHeight;
	}
	
	public static void printString(Graphics canvas, String str, Color c, int x, int y) {
		if (!fallback) canvas.setFont(mainFont);
		Color previousColor=canvas.getColor();
		canvas.setColor(c);
		paintString(canvas, str, x, y);
		canvas.setColor(previousColor);
	}
	
	private static void paintString(Graphics canvas, String str, int x, int y) {
		FontMetrics metrics=canvas.getFontMetrics();
		canvas.drawString(str, x-metrics.stringWidth(str)/2, y-metrics.getHeight()/2 + fontHeight);

	}
}

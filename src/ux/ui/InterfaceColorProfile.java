package ux.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import controller.Game;
import controller.player.Building;

public class InterfaceColorProfile {
	public static Color bgColor=Color.white;
	public static Color fgColor=Color.black;
	public static Color inactiveColor=Color.gray;
	public static Color selectedColor=Color.cyan;
	public static Color vertexColor=Color.red;
	public static Color waterColor=new Color(Integer.decode("0x4d79cc"));
	public static Color bgWaterColor=new Color(Integer.decode("0x214587"));
	
	public static Color player1Color=new Color(Integer.decode("0xAA3939"));
	public static Color player2Color=new Color(Integer.decode("0xAA7F39"));
	public static Color player3Color=new Color(Integer.decode("0x2B4970"));
	public static Color player4Color=new Color(Integer.decode("0x2D882D"));
	
	public static void setPlayerColor(Graphics2D canvas, Building b) {
		if (b.getOwner().equals(Game.players.get(0))) canvas.setPaint(InterfaceColorProfile.player1Color);
		else if (b.getOwner().equals(Game.players.get(1))) canvas.setPaint(InterfaceColorProfile.player2Color);
		else if (b.getOwner().equals(Game.players.get(2))) canvas.setPaint(InterfaceColorProfile.player3Color);
		else if (b.getOwner().equals(Game.players.get(3))) canvas.setPaint(InterfaceColorProfile.player4Color);
	}
}

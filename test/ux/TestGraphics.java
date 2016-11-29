package ux;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.Game;
import controller.map.Table;
import controller.player.Building;
import controller.player.City;
import controller.player.Player;
import controller.player.Resource;
import ux.Renderer;
import ux.ResourceXMLReader;
import ux.ui.InterfaceColorProfile;
import ux.ui.UIController;

public class TestGraphics {
	Graphics2D testCanvas;
	Player p;
	ArrayList<Player> testArray;
	Game testGame;
	
	@Before
	public void staticTestGameInit() {
		testCanvas=(new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB)).createGraphics();
		p=new Player("testPlayer", 0, new Table());
		testArray=new ArrayList();
		testArray.add(p);
		testGame= new Game(new Table(), testArray);
		Game.players.add(p);

	}
	
	@Test
	public void colorChangeTest() {
		Building testBuilding=new City(p);
		InterfaceColorProfile.setPlayerColor(testCanvas, testBuilding);
		assertEquals(InterfaceColorProfile.player1Color,testCanvas.getColor());
	}
	
	@Test
	public void XMLParseTest() {
		HashMap<Resource,Color> testResourceMap=ResourceXMLReader.readTextureXML("textures.xml");
		assertEquals(testResourceMap.get(Resource.Wool),new Color(Integer.decode("0x87ce40")));
		
	}
}

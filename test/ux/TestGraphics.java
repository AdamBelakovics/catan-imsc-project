package ux;

import controller.map.Table;
import controller.player.Player;
import ux.graphics.Renderer;
import ux.graphics.ResourceXMLReader;
import ux.ui.UIController;

public class TestGraphics {
	public static void main(String[] args) {
		UIController testUIC=new UIController();
		Table testBoard=new Table();
		testUIC.setControlledPlayer(new Player("test_player", 0, testBoard));
		Renderer testRenderer=new Renderer(testUIC,testBoard,1024,768);
		testUIC.turn();
		System.out.println("ended turn");
		//TextureXMLReader.readXML("textures.xml");
	}

}

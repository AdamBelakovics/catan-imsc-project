package ux;

import controller.map.Table;
import controller.player.Player;
import ux.Renderer;
import ux.ResourceXMLReader;
import ux.ui.UIController;

public class TestGraphics {
	public static void main(String[] args) {
		UIController testUIC=new UIController();
		Table testBoard=new Table();
		testUIC.setControlledPlayer(new Player("test_player", 0, testBoard));
		Renderer testRenderer=new Renderer(testUIC,testBoard,1024,768);
		testUIC.firstturn();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testUIC.turn();
		System.out.println("ended turn");
		//TextureXMLReader.readXML("textures.xml");
	}

}

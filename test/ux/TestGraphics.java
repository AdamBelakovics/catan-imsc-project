package ux;

import controller.map.Table;
import ux.graphics.Renderer;
import ux.graphics.ResourceXMLReader;
import ux.ui.UIController;

public class TestGraphics {
	public static void main(String[] args) {
		UIController testUIC=new UIController();
		Renderer testRenderer=new Renderer(testUIC,new Table(),1024,768);
		testUIC.turn();
		System.out.println("ended turn");
		//TextureXMLReader.readXML("textures.xml");
	}

}

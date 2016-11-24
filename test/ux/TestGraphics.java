package ux;

import controller.map.Table;
import ux.graphics.Renderer;
import ux.graphics.ResourceXMLReader;
import ux.ui.UIController;

public class TestGraphics {
	public static void main(String[] args) {
		Renderer testRenderer=new Renderer(new UIController(),new Table(),1024,768);
		//TextureXMLReader.readXML("textures.xml");
	}

}

package ux;

import controller.map.Table;
import ux.graphics.Renderer;

public class TestGraphics {
	static final long refreshRate=5;
	public static void main(String[] args) {
		Renderer testRenderer=new Renderer(new Table());
		while (true){
			testRenderer.draw();
			try {
				Thread.sleep(refreshRate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

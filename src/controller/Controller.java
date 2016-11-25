package controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import ai.AiController;
import ai.TEST_AIController;
import controller.map.Hex;
import controller.map.Table;
import controller.player.GameEndsException;
import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.Resource;
import ux.graphics.Renderer;
import ux.ui.UIController;

public class Controller {
	public static void main(String[] args) throws InterruptedException{
		Table board = new Table();
		
		Player AI01 = new Player("AI01", 01, board);
		Player AI02 = new Player("AI02", 02, board);
		Player AI03 = new Player("AI03", 03, board);
		Player Human = new Player("Human", 04, board);
		
		TEST_AIController AI01Controller = new TEST_AIController(AI01, board);
		TEST_AIController AI02Controller = new TEST_AIController(AI02, board);
		TEST_AIController AI03Controller = new TEST_AIController(AI03, board);
		UIController HumanController = new UIController();
		
		AI01.setPlayerController(AI01Controller);
		AI02.setPlayerController(AI02Controller);
		AI03.setPlayerController(AI03Controller);
		Human.setPlayerController(HumanController);
		
		Renderer renderer = new Renderer(HumanController, board, 1080, 720);
		
		for(Hex h : board.hexList){
			h.setResource(Resource.values()[(int)Math.random()*5]);
		}
		
		try {
			for(Resource r : Resource.values()){		
				AI01.incResourceAmount(r, 10);
			}
			for(Resource r : Resource.values()){		
				AI02.incResourceAmount(r, 10);
			}
			for(Resource r : Resource.values()){		
				AI03.incResourceAmount(r, 10);
			}
			for(Resource r : Resource.values()){		
				Human.incResourceAmount(r, 10);
			}
		} catch (OutOfRangeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(true){
			try {
				AI01Controller.turn();
				System.out.println("AI01 finished turn");
				for(Resource r : Resource.values()){
					System.out.println(r.toString()+ ": " + AI01.getResourceAmount(r));
				}
//				AI02Controller.turn();
//				System.out.println("AI02 finished turn");
//				AI03Controller.turn();
//				System.out.println("AI03 finished turn");
				HumanController.turn();
				System.out.println("Human finished turn");
			} catch (GameEndsException e) {
				e.printStackTrace();
				break;
			}
			
		}
		System.out.println("gg wp");
	}
}


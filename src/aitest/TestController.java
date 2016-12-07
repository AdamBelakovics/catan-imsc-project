package aitest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import ai.AiController;
import controller.Game;
import controller.map.Hex;
import controller.map.MapXMLParser;
import controller.map.Table;
import controller.player.GameEndsException;
import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.PlayerController;
import controller.player.Resource;
import controller.player.devcards.DevCardShop;
import ux.Renderer;
import ux.ui.UIController;

public class TestController {
	public static void main(String[] args) throws InterruptedException, OutOfRangeException{
		run(2500);
	}
	
	private static void run(int turns){
		int ai1Wins = 0;
		int ai2Wins = 0;
		int ai3Wins = 0;
		int ai4Wins = 0;
		int ai1Points = 0;
		int ai2Points = 0;
		int ai3Points = 0;
		int ai4Points = 0;
		int gameTurns = 0;
		int minTurns = 1000;
		int maxTurns = 0;
		int hibak = 0;
		for(int j = 0; j < turns; j++){
			System.out.println(j);
			Table board = new Table();
			MapXMLParser.readCatanMap(new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "catan_base_map.xml"), board);
			
			Player AI01 = new Player("AI01", 01, board);
			Player AI02 = new Player("AI02", 02, board);
			Player AI03 = new Player("AI03", 03, board);
			Player AI04 = new Player("AI04", 04, board);
			
			ArrayList<Player> playerList = new ArrayList<Player>();
			playerList.add(AI01);
			playerList.add(AI02);
			playerList.add(AI03);
			playerList.add(AI04);
			
			Game.initializeGame(board, playerList);
			DevCardShop.initializeShop();
			
			HashSet<AiParameter> paramTest1 = new HashSet<AiParameter>();
			paramTest1.add(AiParameter.NewRes);
			HashSet<AiParameter> paramTest2 = new HashSet<AiParameter>();
			paramTest2.add(AiParameter.Port);
			HashSet<AiParameter> paramTest3 = new HashSet<AiParameter>();
			paramTest3.add(AiParameter.NewRes);
			paramTest3.add(AiParameter.Port);
			HashSet<AiParameter> paramBase = new HashSet<AiParameter>();
			//paramBase.add(AiParameter.Port);
			
			AiController AICONT01 = new AiController(board, AI01, playerList, paramTest1);
			AiController AICONT02 = new AiController(board, AI02, playerList, paramTest2);
			AiController AICONT03 = new AiController(board, AI03, playerList, paramTest3);
			AiController AICONT04 = new AiController(board, AI04, playerList, paramBase);
			
			ArrayList<PlayerController> pclist = new ArrayList<PlayerController>();
			pclist.add(AICONT01);
			pclist.add(AICONT02);
			pclist.add(AICONT03);
			pclist.add(AICONT04);
			
			AI01.setPlayerController(AICONT01);
			AI02.setPlayerController(AICONT02);
			AI03.setPlayerController(AICONT03);
			AI04.setPlayerController(AICONT04);
			
			Collections.shuffle(pclist);
			
			for(int i = 0; i < pclist.size(); i++){
				pclist.get(i).firstturn();
			}
			
			for(int i = pclist.size()-1; i >= 0; i--){
				pclist.get(i).firstturn();
			}
			
			int tmpTurns = 0;
			try {				
				while(true){
					for(PlayerController pc : pclist){
						pc.turn();
					}
					tmpTurns++;
				}
			} catch (GameEndsException e) {
				ai1Points += AI01.getPoints();
				ai2Points += AI02.getPoints();
				ai3Points += AI03.getPoints();
				ai4Points += AI04.getPoints();
				if(AI01.getPoints() >= 10){
					ai1Wins++;
				} else if(AI02.getPoints() >= 10){
					ai2Wins++;
				} else if(AI03.getPoints() >= 10){
					ai3Wins++;
				} else if(AI04.getPoints() >= 10){
					ai4Wins++;
				} else {
					hibak++;
				}
				gameTurns += tmpTurns;
				if(tmpTurns > maxTurns)
					maxTurns = tmpTurns;
				if(tmpTurns < minTurns)
					minTurns = tmpTurns;
				if(AI01.getPoints() > 10)
					hibak++;
				if(AI02.getPoints() > 10)
					hibak++;
				if(AI03.getPoints() > 10)
					hibak++;
				if(AI04.getPoints() > 10)
					hibak++;
			}	
		}
		System.out.println("\nAI01 wins: " + ai1Wins + "\tpoints / turns: " + (double)(ai1Points) / (double)(gameTurns));
		System.out.println("AI02 wins: " + ai2Wins + "\tpoints / turns: " + (double)(ai2Points) / (double)(gameTurns));
		System.out.println("AI03 wins: " + ai3Wins + "\tpoints / turns: " + (double)(ai3Points) / (double)(gameTurns));
		System.out.println("AI04 wins: " + ai4Wins + "\tpoints / turns: " + (double)(ai4Points) / (double)(gameTurns));
		System.out.println("\nTurns:");
		System.out.println("\tMin: " + minTurns);
		System.out.println("\tMax: " + maxTurns);
		System.out.println("\tAvg: " + gameTurns / turns);
		System.out.println("\nHibak: " + hibak);
	}
}


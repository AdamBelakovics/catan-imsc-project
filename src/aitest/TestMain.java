package aitest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import ai.AiController;
import ai.BuildCity;
import ai.BuildDevelopment;
import ai.BuildRoad;
import ai.BuildVillage;
import ai.Material;
import controller.Game;
import controller.map.Hex;
import controller.map.MapXMLParser;
import controller.map.Table;
import controller.player.GameEndsException;
import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.PlayerController;
import controller.player.Resource;
import controller.player.devcards.DevCard;
import controller.player.devcards.DevCardShop;
import controller.player.devcards.KnightCard;
import controller.player.devcards.VictoryPointCard;
import ux.Renderer;
import ux.ui.UIController;

public class TestMain {
	public static void main(String[] args) throws InterruptedException, OutOfRangeException{
		ArrayList<HashSet<AiParameter>> paramSets = new ArrayList<>();
		HashSet<AiParameter> paramSet1 = new HashSet<>();
		//paramSet1.add(AiParameter.NewRes);
		paramSet1.add(AiParameter.Port);
		paramSet1.add(AiParameter.Orain);
		HashSet<AiParameter> paramSet2 = new HashSet<>();
		paramSet2.add(AiParameter.NewRes);
		paramSet2.add(AiParameter.Port);
		paramSet2.add(AiParameter.Orain);
		HashSet<AiParameter> paramSet3 = new HashSet<>();
		//paramSet3.add(AiParameter.NewRes);
		paramSet3.add(AiParameter.Port);
		paramSet3.add(AiParameter.Lumbrick);
		HashSet<AiParameter> paramSet4 = new HashSet<>();
		paramSet4.add(AiParameter.NewRes);
		paramSet4.add(AiParameter.Port);
		paramSet4.add(AiParameter.Lumbrick);
		paramSets.add(paramSet1);
		paramSets.add(paramSet2);
		paramSets.add(paramSet3);
		paramSets.add(paramSet4);
		// turns, players count, random, is there a stupid player, paramsets
		//run(100, 4, true, false, paramSets);
		interactiveGameplay(4, false, false, paramSets);
		//printStatus();
		System.out.println("Clever: " + Player.cleverUpdateCount + "\tstupid:" + Player.stupidUpdateCount);
	}
	private static void interactiveGameplay(int playerCnt, boolean random, boolean isStupid, ArrayList<HashSet<AiParameter>> paramSets){
		Table board = new Table();
		MapXMLParser.readCatanMap(new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "catan_base_map.xml"), board);
		
		ArrayList<Player> playerList = new ArrayList<Player>();
		ArrayList<PlayerController> pclist = new ArrayList<PlayerController>();
		for(int i = 0; i < playerCnt; i++) {
			paramSets.get(i).add(AiParameter.Interactive);
			Player p = new Player("AI0" + (i + 1), (i + 1), board);
			playerList.add(p);
			p.setStupidity(isStupid);
		}
		
		ArrayList<AiController> ais = new ArrayList<>();
		
		for(int i = 0; i < playerCnt; i++) {
			AiController ai = new AiController(board, playerList.get(i), playerList, paramSets.get(i));
			pclist.add(ai);
			ais.add(ai);
			playerList.get(i).setPlayerController(ai);
		}
		
		Game.initializeGame(board, playerList);
		DevCardShop.initializeShop();
		
		GameForTest.initialize(playerList, ais, board);
		GameVisualizer.initialize(ais, board);
		
		if(random)
			Collections.shuffle(pclist);
		for(int i = 0; i < pclist.size(); i++){
			pclist.get(i).firstturn();
		}
		
		for(int i = pclist.size()-1; i >= 0; i--){
			pclist.get(i).firstturn();
		}
		GameVisualizer.firstTurnEnded();
		try {				
			while(true){
				for(PlayerController pc : pclist){
					pc.turn();
				}
			}
		} catch (GameEndsException e) {
			
		}
	}
	/**
	 * Ais play a number of games in a row. Some statistics
	 * are printed to console after the games.
	 * @param turns - number of games the ais play
	 * @param playerCnt - number of ais
	 * @param random - if random, the ais play each game in randomized order
	 * @param isStupid - must be true only if any of the ais is random
	 * @param paramSets - list of the ais' parameter sets, size must be same as playerCnt
	 */
	private static void run(int turns, int playerCnt, boolean random, boolean isStupid, ArrayList<HashSet<AiParameter>> paramSets){
		GregorianCalendar startTime = new GregorianCalendar();
		
		ArrayList<Integer> aiWins = new ArrayList<>();
		ArrayList<Integer> aiPoints = new ArrayList<>();
		ArrayList<Integer> aiGameTurns = new ArrayList<>();
		ArrayList<Integer> aiMinTurns = new ArrayList<>();
		ArrayList<Integer> aiMaxTurns = new ArrayList<>();
		
		for(int i = 0; i < playerCnt; i++){
			aiWins.add(0);
			aiPoints.add(0);
			aiGameTurns.add(0);
			aiMinTurns.add(Integer.MAX_VALUE);
			aiMaxTurns.add(Integer.MIN_VALUE);
		}
		
		int gameTurns = 0;
		int hibak = 0;
		
		Table board = null;
		
		ArrayList<Player> playerList = new ArrayList<Player>();
		ArrayList<PlayerController> pclist = new ArrayList<PlayerController>();

		for(int j = 0; j < turns; j++){
			System.out.println(j);
			board = new Table();
			MapXMLParser.readCatanMap(new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "catan_base_map.xml"), board);
			
			playerList.clear();
			pclist.clear();
			for(int i = 0; i < playerCnt; i++) {
				Player p = new Player("AI0" + (i + 1), (i + 1), board);
				playerList.add(p);
				p.setStupidity(isStupid);
			}
			
			ArrayList<AiController> ais = new ArrayList<>();
			
			for(int i = 0; i < playerCnt; i++) {
				AiController ai = new AiController(board, playerList.get(i), playerList, paramSets.get(i));
				pclist.add(ai);
				ais.add(ai);
				playerList.get(i).setPlayerController(ai);
			}
			
			Game.initializeGame(board, playerList);
			DevCardShop.initializeShop();
			
			GameForTest.initialize(playerList, ais, board);
			
			if(random)
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
					if(tmpTurns == 20){
						//GameForTest.drawMap();
					}
					if(tmpTurns > 100){
						Renderer rend = new Renderer(new UIController(new Player("", -1, board)), board, 1280, 700);
						break;
					}
					//System.out.println("Turn " + tmpTurns);
					tmpTurns++;
				}
			} catch (GameEndsException e) {
				gameTurns += tmpTurns;
				for(int i = 0; i < playerCnt; i++){
					int currentPoint = playerList.get(i).getPoints();
					aiPoints.set(i, aiPoints.get(i) + currentPoint);
					if(currentPoint >= 10){
						aiWins.set(i, aiWins.get(i) + 1);
						aiGameTurns.set(i, aiGameTurns.get(i) + tmpTurns);
						if(aiMaxTurns.get(i) < tmpTurns){
							aiMaxTurns.set(i, tmpTurns);
						}
						if(aiMinTurns.get(i) > tmpTurns){
							aiMinTurns.set(i, tmpTurns);
						}
						if(currentPoint > 10)
							hibak++;
					}
				}
			}
		}
		if(turns == 1){
			ArrayList<Integer> vicPts = new ArrayList<>();
			ArrayList<Integer> knights = new ArrayList<>();
			for (int i = 0; i < playerCnt; i++) {
				vicPts.add(0);
				knights.add(0);
			}
			for(int i = 0; i < playerCnt; i++){
				for(DevCard card : playerList.get(i).getPlayedDevelopmentCards()){
					if(card.getClass().equals(VictoryPointCard.class))
						vicPts.set(i, vicPts.get(i) + 1);
					else if(card.getClass().equals(KnightCard.class))
						knights.set(i, knights.get(i) + 1);
				}
			}
			
			Renderer rend = new Renderer(new UIController(new Player("", -1, board)), board, 1280, 700);
			System.out.println("Max road: " + GameForTest.maxRoadLength() + " " + GameForTest.longestRoadKing);
			for(int i = 0; i  < playerCnt; i++){
				System.out.println(playerList.get(i) + " road: " + GameForTest.maxRoadLength(playerList.get(i)) + " " + playerList.get(i).longestRoad + "\tVicPt: " + vicPts.get(i) + "\tknights: " + knights.get(i) + " " + playerList.get(i).isBiggestArmy());
			}
		}
		GregorianCalendar endTime = new GregorianCalendar();
		System.out.println("");
		for(int i = 0; i < playerCnt; i++){
			if(aiWins.get(i) > 0)
				System.out.println(playerList.get(i) + " wins: " + aiWins.get(i) + " (" + (int)((aiWins.get(i) / (double)turns) * 100) + "%)" + "\tmin: " + aiMinTurns.get(i) + "\t\tmax: " + aiMaxTurns.get(i) + "\t\tavg: " + aiGameTurns.get(i) / aiWins.get(i) + "\t\tpoints / turns: " + (double)(aiPoints.get(i)) / (double)(gameTurns) + "\t" + paramSets.get(i).toString());
		}
		System.out.println("\nBuildRoad\tmin: " + BuildRoad.minValue + "\tmax: " + BuildRoad.maxValue + "\tavg: " + BuildRoad.sumValue / BuildRoad.cnt);
		System.out.println("BuildVillage\tmin: " + BuildVillage.minValue + "\tmax: " + BuildVillage.maxValue + "\tavg: " + BuildVillage.sumValue / BuildVillage.cnt);
		System.out.println("BuildCity\tmin: " + BuildCity.minValue + "\tmax: " + BuildCity.maxValue + "\tavg: " + BuildCity.sumValue / BuildCity.cnt);
		System.out.println("BuildDev\tmin: " + BuildDevelopment.minValue + "\tmax: " + BuildDevelopment.maxValue + "\tavg: " + BuildDevelopment.sumValue / BuildDevelopment.cnt);
		System.out.println("Knight\tmin: " + BuildDevelopment.minValueKnight + "\tmax: " + BuildDevelopment.maxValueKnight + "\tavg: " + BuildDevelopment.sumValueKnight / BuildDevelopment.cntKnight);
		System.out.println("Monopoly\tmin: " + BuildDevelopment.minValueMonopoly + "\tmax: " + BuildDevelopment.maxValueMonopoly + "\tavg: " + BuildDevelopment.sumValueMonopoly / BuildDevelopment.cntMonopoly);
		System.out.println("Invention\tmin: " + BuildDevelopment.minValueInvention + "\tmax: " + BuildDevelopment.maxValueInvention + "\tavg: " + BuildDevelopment.sumValueInvention / BuildDevelopment.cntInvention);
		System.out.println("TwoRoad\tmin: " + BuildDevelopment.minValueTwoRoad + "\tmax: " + BuildDevelopment.maxValueTwoRoad + "\tavg: " + BuildDevelopment.sumValueTwoRoad / BuildDevelopment.cntTwoRoad);
		System.out.println("Material\tmin: " + Material.minValue + "\tmax: " + Material.maxValue + "\tavg: " + Material.sumValue / Material.cnt);
		System.out.println("Node value\tmin: " + AiController.minValueNode + "\tmax: " + AiController.maxValueNode + "\tavg: " + AiController.sumValueNode / AiController.cntNode);
		System.out.println("Territory\tmin: " + AiController.minValueTerr + "\tmax: " + AiController.maxValueTerr + "\tavg: " + AiController.sumValueTerr / AiController.cntTerr);
//		System.out.println("\nHibak: " + hibak);
//		System.out.println("SumRoad: " + BuildRoad.sumValue);
//		System.out.println("SumVillage: " + BuildVillage.sumValue);
//		System.out.println("SumCity: " + BuildCity.sumValue);
//		System.out.println("SumDevelopment: " + BuildDevelopment.sumValue);
		long difTime = endTime.getTimeInMillis() - startTime.getTimeInMillis();
		int hour = (int)TimeUnit.MILLISECONDS.toHours(difTime);
		int min = (int)TimeUnit.MILLISECONDS.toMinutes(difTime);
		int sec = (int)TimeUnit.MILLISECONDS.toSeconds(difTime);
		System.out.println("\nRuntime: " + hour + ":" + min % 60 + ":" + sec % 60);
		System.out.println("Start: " + startTime.get(Calendar.HOUR) + ":" + startTime.get(Calendar.MINUTE) + ":" + startTime.get(Calendar.SECOND) + "\tend: " + endTime.get(Calendar.HOUR) + ":" + endTime.get(Calendar.MINUTE) + ":" + endTime.get(Calendar.SECOND));
	}
	
	/**
	 * This won't be needed, cooler features are about to release. ;)
	 */
	private static void printStatus(){
		Table board = new Table();
		MapXMLParser.readCatanMap(new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "catan_base_map.xml"), board);
		
		Player AI01 = new Player("AI01", 01, board);
		Player AI02 = new Player("AI02", 02, board);
		Player AI03 = new Player("AI03", 03, board);
		Player AI04 = new Player("AI04", 04, board);
		
		AI01.setStupidity(false);
		AI02.setStupidity(false);
		AI03.setStupidity(false);
		AI04.setStupidity(false);
		
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
		
		/*for(int i = 0; i < pclist.size(); i++){
			pclist.get(i).firstturn();
		}
		
		for(int i = pclist.size()-1; i >= 0; i--){
			pclist.get(i).firstturn();
		}*/
		//AICONT01.printStatus();
		AICONT02.printStatus();
		//AICONT03.printStatus();
		//AICONT04.printStatus();
		
		Renderer rend = new Renderer(new UIController(new Player("", -1, board)), board, 1280, 700);
	}
}


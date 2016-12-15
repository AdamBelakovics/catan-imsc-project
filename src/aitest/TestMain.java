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
import javafx.print.PageLayout;
import ux.Renderer;
import ux.ui.UIController;

public class TestMain {
	public static void main(String[] args) throws InterruptedException, OutOfRangeException{
		ArrayList<HashSet<AiParameter>> paramSets = new ArrayList<>();
		HashSet<AiParameter> paramSet1 = new HashSet<>();
		paramSet1.add(AiParameter.Random);
		//paramSet1.add(AiParameter.Port);
		//paramSet1.add(AiParameter.Orain);
		HashSet<AiParameter> paramSet2 = new HashSet<>();
		//paramSet2.add(AiParameter.NewRes);
		//paramSet2.add(AiParameter.Port);
		paramSet2.add(AiParameter.Random);
		HashSet<AiParameter> paramSet3 = new HashSet<>();
		paramSet3.add(AiParameter.Random);
		//paramSet3.add(AiParameter.Port);
		//paramSet3.add(AiParameter.Lumbrick);
		HashSet<AiParameter> paramSet4 = new HashSet<>();
		//paramSet4.add(AiParameter.NewRes);
		paramSet4.add(AiParameter.Port);
		//paramSet4.add(AiParameter.Lumbrick);
		HashSet<AiParameter> paramSet5 = new HashSet<>();
		paramSet5.add(AiParameter.NewRes);
		paramSet5.add(AiParameter.Port);
		//paramSet5.add(AiParameter.Orain);
		HashSet<AiParameter> paramSet6 = new HashSet<>();
		//paramSet6.add(AiParameter.NewRes);
		//paramSet6.add(AiParameter.Port);
		paramSet6.add(AiParameter.Orain);
		HashSet<AiParameter> paramSet7 = new HashSet<>();
		paramSet7.add(AiParameter.Lumbrick);
		HashSet<AiParameter> paramSet8 = new HashSet<>();
		paramSet8.add(AiParameter.NewRes);
		paramSet8.add(AiParameter.Orain);
		HashSet<AiParameter> paramSet9 = new HashSet<>();
		paramSet9.add(AiParameter.NewRes);
		paramSet9.add(AiParameter.Lumbrick);
		HashSet<AiParameter> paramSet10 = new HashSet<>();
		paramSet10.add(AiParameter.Orain);
		paramSet10.add(AiParameter.Port);
		HashSet<AiParameter> paramSet11 = new HashSet<>();
		paramSet11.add(AiParameter.Lumbrick);
		paramSet11.add(AiParameter.Port);
		HashSet<AiParameter> paramSet12 = new HashSet<>();
		paramSet12.add(AiParameter.NewRes);
		paramSet12.add(AiParameter.Port);
		paramSet12.add(AiParameter.Orain);
		HashSet<AiParameter> paramSet13 = new HashSet<>();
		paramSet13.add(AiParameter.NewRes);
		paramSet13.add(AiParameter.Lumbrick);
		paramSet13.add(AiParameter.Port);
		paramSets.add(paramSet1);
		paramSets.add(paramSet2);
		paramSets.add(paramSet3);
		/*paramSets.add(paramSet4);
		paramSets.add(paramSet5);
		paramSets.add(paramSet6);
		paramSets.add(paramSet7);
		paramSets.add(paramSet8);
		paramSets.add(paramSet9);
		paramSets.add(paramSet10);
		paramSets.add(paramSet11);
		paramSets.add(paramSet12);
		paramSets.add(paramSet13);*/
		// turns, players count, random, is there a stupid player, paramsets
		//run(1, 3, true, true, paramSets);
		interactiveGameplay(3, false, true, paramSets);
		//printStatus();
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
		int competitorCnt = paramSets.size();
		if(playerCnt > competitorCnt){
			System.out.println("Legfeljebb annyian jatszhatnak, ahany parameterhalmazt megadtal!");
			return;
		}
		GregorianCalendar startTime = new GregorianCalendar();
		
		ArrayList<Integer> aiWins = new ArrayList<>();
		ArrayList<Integer> aiPoints = new ArrayList<>();
		ArrayList<Integer> aiGames = new ArrayList<>();
		ArrayList<Integer> aiGameTurns = new ArrayList<>();
		ArrayList<Integer> aiWinGameTurns = new ArrayList<>();
		ArrayList<Integer> aiMinTurns = new ArrayList<>();
		ArrayList<Integer> aiMaxTurns = new ArrayList<>();
		
		for(int i = 0; i < competitorCnt; i++){
			aiWins.add(0);
			aiPoints.add(0);
			aiGames.add(0);
			aiGameTurns.add(0);
			aiWinGameTurns.add(0);
			aiMinTurns.add(Integer.MAX_VALUE);
			aiMaxTurns.add(Integer.MIN_VALUE);
		}
		
		//int gameTurns = 0;
		int hibak = 0;
		
		Table board = null;
		
		ArrayList<Player> playerList = new ArrayList<Player>();
		ArrayList<PlayerController> pclist = new ArrayList<PlayerController>();

		for(int j = 0; j < turns; j++){
			System.out.println(j);
			board = new Table();
			MapXMLParser.readCatanMap(new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "catan_base_map.xml"), board);
			
			ArrayList<Integer> competitorList = new ArrayList<>();
			for(int i = 0; i < competitorCnt; i++) {
				competitorList.add(i);
			}
			if(random){
				Collections.shuffle(competitorList);
			}
			while(competitorList.size() > playerCnt){
				competitorList.remove(competitorList.size() - 1);
			}
			
			playerList.clear();
			pclist.clear();
			for(int i = 0; i < playerCnt; i++) {
				Player p = new Player("AI0" + (competitorList.get(i)), competitorList.get(i), board);
				playerList.add(p);
				p.setStupidity(isStupid);
			}
			ArrayList<AiController> ais = new ArrayList<>();
			
			for(int i = 0; i < playerCnt; i++) {
				AiController ai = new AiController(board, playerList.get(i), playerList, paramSets.get(competitorList.get(i)));
				pclist.add(ai);
				ais.add(ai);
				playerList.get(i).setPlayerController(ai);
			}
			
			Game.initializeGame(board, playerList);
			DevCardShop.initializeShop();
			
			GameForTest.initialize(playerList, ais, board);
			
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
				//gameTurns += tmpTurns;
				for(int i = 0; i < playerCnt; i++){
					int currentPoint = playerList.get(i).getPoints();
					int currentId = playerList.get(i).getId();
					aiGames.set(currentId, aiGames.get(currentId) + 1);
					aiGameTurns.set(currentId, aiGameTurns.get(currentId) + tmpTurns);
					aiPoints.set(currentId, aiPoints.get(currentId) + currentPoint);
					if(currentPoint >= 10){
						aiWins.set(currentId, aiWins.get(currentId) + 1);
						aiWinGameTurns.set(currentId, aiWinGameTurns.get(currentId) + tmpTurns);
						if(aiMaxTurns.get(currentId) < tmpTurns){
							aiMaxTurns.set(currentId, tmpTurns);
						}
						if(aiMinTurns.get(currentId) > tmpTurns){
							aiMinTurns.set(currentId, tmpTurns);
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
		for(int i = 0; i < competitorCnt; i++){
			if(aiGames.get(i) == 0)
				System.out.println("AI" + i + " nem kerult sorra");
			else if(aiWins.get(i) == 0)
				System.out.println("AI" + i + " games: " + aiGames.get(i) + " wins: 0 (0%)" + "\tmin: -\tmax: -\tavg: -\t\tpoints / turns: " + (double)(aiPoints.get(i)) / (double)(aiGameTurns.get(i)) + "\t" + paramSets.get(i).toString());
			else 
				System.out.println("AI" + i + " games: " + aiGames.get(i) + " wins: " + aiWins.get(i) + " (" + (int)((aiWins.get(i) / (double)aiGames.get(i)) * 100) + "%)" + "\tmin: " + aiMinTurns.get(i) + "\tmax: " + aiMaxTurns.get(i) + "\tavg: " + aiWinGameTurns.get(i) / aiWins.get(i) + "\t\tpoints / turns: " + (double)(aiPoints.get(i)) / (double)(aiGameTurns.get(i)) + "\t" + paramSets.get(i).toString());
		}
		long difTime = endTime.getTimeInMillis() - startTime.getTimeInMillis();
		int hour = (int)TimeUnit.MILLISECONDS.toHours(difTime);
		int min = (int)TimeUnit.MILLISECONDS.toMinutes(difTime);
		int sec = (int)TimeUnit.MILLISECONDS.toSeconds(difTime);
		System.out.println("\nRuntime: " + hour + ":" + min % 60 + ":" + sec % 60);
		System.out.println("Start: " + startTime.get(Calendar.HOUR) + ":" + startTime.get(Calendar.MINUTE) + ":" + startTime.get(Calendar.SECOND) + "\tend: " + endTime.get(Calendar.HOUR) + ":" + endTime.get(Calendar.MINUTE) + ":" + endTime.get(Calendar.SECOND));
		printStats();
	}
	
	/**
	 * Prints max, min and avg stats of personal values
	 */
	private static void printStats(){
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
		System.out.println("3 to 1 port\tmin: " + AiController.minValuePort3 + "\tmax: " + AiController.maxValuePort3 + "\tavg: " + AiController.sumValuePort3 / AiController.cntPort3);
		System.out.println("2 to 1 port\tmin: " + AiController.minValuePort2 + "\tmax: " + AiController.maxValuePort2 + "\tavg: " + AiController.sumValuePort2 / AiController.cntPort2);
		System.out.println("Clever: " + Player.cleverUpdateCount + "\tstupid:" + Player.stupidUpdateCount);
	}
}


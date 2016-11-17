package controller.map;

import java.util.HashMap;
import java.util.Map;

import controller.player.Building;
import controller.map.TableElement;

public class Table{
	static Hex[][] hexgrid = new Hex[7][7];
	Map<String, Vertex> vertexMap = new HashMap<>();
	static Hex iteratorHex = new Hex(3,3);
	
	public Table(){
		this.generateHexGrid();
	}
	
	private void generateHexGrid(){
		for(int x = 0; x < 7; x++){
			for (int y = 0; y < 7; y++) {
				if(!(x+4 <=y || x-4 >=y)){
					hexgrid[x][y] = new Hex(x, y);
				}
				else{
					hexgrid[x][y] = null;
				}
			}
		}	
	}
	
	public static Hex getIteratorHex(){
		return iteratorHex;
	}
	
	static void moveToHex(Orientation to){
		int newX = iteratorHex.xpos;
		int newY = iteratorHex.ypos;
		System.out.println(to);
		switch(to){
		case NORTHWEST:
			newX--;
			newY--;
			break;
		case NORTHEAST:
			newX--;
			break;
		case EAST:
			newY++;
			break;
		case SOUTHEAST:
			newX++;
			newY++;
			break;
		case SOUTHWEST:
			newX++;
			break;
		case WEST:
			newY--;
			break;
		default:
			System.out.println("Ervenytelen irany!!!");
			break;
		}
		if (newX+4 <=newY || newX-4 >=newY || newX > 6 || newY > 6 || newX < 0 || newY < 0)
			//precheck if the next hex would be out of the map
			System.out.println("Ervenytelen lepes!!!");
		else{
			//if there's no problem we set the new values
			iteratorHex.xpos = newX;
			iteratorHex.ypos = newY;
		}
	}
	
	public boolean isBuildPossibleAt(Building what, TableElement where) {
		return where.isBuildPossible(what);
	};
}

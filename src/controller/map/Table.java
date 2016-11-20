package controller.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import controller.player.Building;
import controller.map.TableElement;


/**
 * class Table
 * It represents the map of the the game. It stores all the Hexes, Vertices and Edges.
 * The constructor generates every part of the map. The methods are providing the self-constructing functionality
 * for the table.
 * @author AdamBelakovics
 */
public class Table{
	
	/**
	 * hexGrid
	 * A two-dimensional array storing the Hexes of the Game's Table
	 */
	private Hex[][] hexGrid = new Hex[7][7];
	
	/**
	 * iteratorHex
	 * The iterator of hexGrid. The IteratorHex class is implemented as an inner class of Table.
	 */
	private IteratorHex iteratorHex = new IteratorHex();
	
	/**
	 * vertexMap
	 * A Map for storing all Vertices of the Table. The String part of the data is a unique ID.
	 */
	public Map<String, Vertex> vertexMap = new HashMap<>();
	
	/**
	 * EMPTY container
	 * Work in progress TODO
	 * @return List of all Vertexes
	 */
	ArrayList<Vertex> getNodes(){ //TODO convert inner vertexMap to List
		return new ArrayList<Vertex>(); 
	}
	
	/**
	 * EMPTY container
	 * Work in progress TODO
	 * @return List of all Hexes
	 */
	ArrayList<Hex> getFields(){ //TODO convert inner hexgrid to List
		return new ArrayList<Hex>(); 
	}
	
	/**
	 * EMPTY container
	 * Work in progress TODO
	 * @return List of all Edges
	 */
	ArrayList<Edge> getEdges(){ //TODO create inner container for Edges
		return new ArrayList<Edge>(); 
	}
	
	/**
	 * hexIterator
	 * @return iteratorHex The iterator of hexGrid. The next() method returns reference for current Hex.
	 */
	public IteratorHex hexIterator() {
		return iteratorHex;
	};
	
	/**
	 * The constructor generates all the Hexes, Vertices and Edges of the Table.
	 */
	public Table(){
		this.generateHexGrid();
	}
	
	/**
	 * This private function generates Hex grid this way:
	 * 
	 * 00 01 02 03 XX XX XX 
	 * 10 11 12 13 14 XX XX 
	 * 20 21 22 23 24 25 XX 
	 * 30 31 32 33 34 35 36 
	 * XX 41 42 43 44 45 46 
	 * XX XX 52 53 54 55 56 
	 * XX XX XX 63 64 65 66
	 * 
	 * The grid is stored in hexGrid. The positions marked with XX are null pointers.
	 * @author AdamBelakovics
	 */
	private void generateHexGrid(){
		for(int x = 0; x < 7; x++){
			for (int y = 0; y < 7; y++) {
				if(!(x+4 <=y || x-4 >=y)){
					hexGrid[x][y] = new Hex(x + "" + y);
				}
				else{
					hexGrid[x][y] = null;
				}
			}
		}	
	}
	
	
	/**
	 * class IteratorHex
	 * It provides basic Iterator functionality for hexGrid, and other functionality which are used by the Table's constructor.
	 * @author AdamBelakovics
	 */
	public class IteratorHex implements Iterator<Hex> {
		
		int i = 0;
		int j = 0;
		

		@Override
		public boolean hasNext() {
			if(i < 6 || j < 6)
				return true;
			return false;
		}

		@Override
		public Hex next() {	
			do{
				if (j < 6)
					j++;
				else if(i < 6){
					i++;
					j = 0;
				}
			}
			while(i+4 <= j || i-4 >= j);
			return Table.this.hexGrid[i][j];
		}
		
		/**
		 * Sets the Iterator's new position to the selected coordinates.
		 * @param x row
		 * @param y column
		 */
		private void jumptoHex(int x, int y){
			if (i < 7 || j < 7){
				i = x;
				j = y;
			}
			System.out.println("Invalid Jump"); //TODO throw sthg
		}
		
		/**
		 * Returns the neighboring Hex in the selected direction
		 * @param to An Orientation representing the direction.
		 * @return The Hex in the selected direction.
		 */
		public Hex getNeighboringHex(Orientation to){
			int newX = this.i;
			int newY = this.j;
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
			if (newX+4 <=newY || newX-4 >=newY || newX > 6 || newY > 6 || newX < 0 || newY < 0){
				//precheck if the next hex would be out of the map
				System.out.println("Ervenytelen lepes!!!"); //todo throw sth
				return Table.this.hexGrid[this.i][this.j];
			}
			else{
				//if there's no problem we return the required Hex 
				return Table.this.hexGrid[newX][newY];
			}
		}

	}
	
	
	public boolean isBuildPossibleAt(Building what, TableElement where) {
		return where.isBuildPossible(what);
	}
}

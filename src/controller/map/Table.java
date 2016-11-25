package controller.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import controller.player.Building;
import controller.map.TableElement;
import controller.map.Table.IteratorHex;


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
	 * vertexMap
	 * A Map for storing all Vertices of the Table. The String part of the data is a unique ID.
	 */
	public Map<String, Vertex> vertexMap = new HashMap<>();
	
	/**
	 * List of Hexes
	 */
	public ArrayList<Hex> hexList = new ArrayList<Hex>();
	
	/**
	 * List of Vertexes
	 */
	public ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
	
	/**
	 * Returns All Vertexes as a List
	 * @return List of all Vertexes
	 */
	public ArrayList<Vertex> getNodes(){ 
		return vertexList; 
	}
	
	/**
	 * Returns all Hexes as a List
	 * @return List of all Hexes
	 */
	public ArrayList<Hex> getFields(){ 
		return hexList; 
	}
	
	/**
	 * EMPTY container
	 * Work in progress TODO
	 * @return List of all Edges
	 */
	public ArrayList<Edge> getEdges(){ //TODO create inner container for Edges
		return new ArrayList<Edge>(); 
	}
	
	/**
	 * hexIterator
	 * @return iteratorHex The iterator of hexGrid. The next() method returns reference for current Hex.
	 */
	public IteratorHex hexIterator() {
		return new IteratorHex();
	};
	
	/**
	 * The constructor generates all the Hexes, Vertices and Edges of the Table.
	 */
	public Table(){
		generateHexGrid();
		generateVertices();
		//generateEdges();
		generateLists();
		generateAllNeigbours();
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
	 * This private function generates the Vertices for the Hexgrid. References are stored in Table.vertexMap, 
	 * in Hex.vertices and in Vertex.hexes
	 */
	private void generateVertices(){

		Orientation[] oarray =  Orientation.values();
		ArrayList<Orientation> oList = new ArrayList<Orientation>();
		ArrayList<Orientation> shiftedOList = new ArrayList<Orientation>();
		for (int i = 0; i < oarray.length; i++) {
			oList.add(oarray[i]);
		}
		oList.remove(0); //remove NORTH
		oList.remove(3); //remove SOUTH
		shiftedOList.add(oList.get(oList.size() - 1));
		for (int i = 0; i < oList.size() - 1; i++) {
			shiftedOList.add(oList.get(i));
		}
		IteratorHex i = hexIterator();
		while(i.hasNext()){
			Hex h0 = i.next();
			for (int j = 0; j < shiftedOList.size(); j++) {
				Hex h1 = i.getNeighboringHex(shiftedOList.get(j));
				Hex h2 = i.getNeighboringHex(oList.get(j));
				Map<Integer, String> mapForSorting = new TreeMap<Integer, String>();
				mapForSorting.put(new Integer(h0.getID()), h0.getID());
				mapForSorting.put(new Integer(h1.getID()), h1.getID());
				mapForSorting.put(new Integer(h2.getID()), h2.getID());
				String vertexID = "";
				for(Map.Entry<Integer, String> entry : mapForSorting.entrySet()){
					vertexID = vertexID + entry.getValue();
				}
				if (vertexID.length() >= 6){ //az olyan Vertexek amelyeknek az IDje 6 karakternĂ©l rĂ¶videbbek invalidak
					Vertex v = new Vertex(vertexID);
					if(vertexMap.containsKey(vertexID)){ //ha benne van akkor csak a pointereket ĂˇllĂ­tjuk be
						h0.vertices.put(vertexID, vertexMap.get(vertexID));
						vertexMap.get(vertexID).hexes.put(h0.getID(), h0);
					}
					else{//ha nincs benne hozzĂˇadjuk a TablehĂ¶z is
						vertexMap.put(vertexID, v); //ha valid beletesszĂĽk
						h0.vertices.put(vertexID, v);
						v.hexes.put(h0.getID(), h0);
					}
				}
			}
		}
		
		//Fontos teszt
		
		/*for(Map.Entry<String, Vertex> v : vertexMap.entrySet()){
			System.out.println(vertexMap.size());
		}*/
		
		
		/*IteratorHex j = hexIterator();
		while(j.hasNext())
			System.out.println(j.next().vertices);*/
	}
	
	private void generateLists(){
		IteratorHex i = this.hexIterator();
		while(i.hasNext()){
			hexList.add(i.next());
		}
		for(Map.Entry<String, Vertex> e : vertexMap.entrySet()){
			vertexList.add(e.getValue());
		}
	}
	
	public void generateAllNeigbours(){
		
		//for Hexes
		IteratorHex i = hexIterator();
		ArrayList<Hex> tmpNeighbourHexList = new ArrayList<Hex>();
		for(Hex h: hexList){
			i.next();
			for(Orientation o : Orientation.values())
				tmpNeighbourHexList.add(i.getNeighboringHex(o));
			h.generateNeighbours(tmpNeighbourHexList);
		}
		
		//for Vertices
		for(Vertex v: vertexList){
			v.generateNeighbours();
		}
		
	}
	
	
	/**
	 * class IteratorHex
	 * It provides basic Iterator functionality for hexGrid, and other functionality which are used by the Table's constructor.
	 * @author AdamBelakovics
	 */
	public class IteratorHex implements Iterator<Hex> {
		
		int i = 0;
		int j = -1;
		

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
				//TODO throw stg wrong direction
				break;
			}
			if (newX+4 <=newY || newX-4 >=newY || newX > 6 || newY > 6 || newX < 0 || newY < 0){
				//precheck if the next hex would be out of the map
				//TODO throw sth wrong move
				return Table.this.hexGrid[this.i][this.j];
			}
			else{
				//if there's no problem we return the required Hex 
				return Table.this.hexGrid[newX][newY];
			}
		}

	}
	
	public void allocateResources(int result){
		
	}
	
	public boolean isBuildPossibleAt(Building what, TableElement where) {
		return where.isBuildPossible(what);
	}
}


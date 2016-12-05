package ux;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;

import controller.map.Buildable;
import controller.map.Hex;
import controller.map.Table;
import controller.map.Vertex;
import controller.player.Player;
import controller.player.Resource;
import ux.board.HexPoly;
import ux.board.BoardRenderer.BoardOrientation;
import ux.ui.UIController;

public class RendererDataStore {
	public int width;
	public int height;
	public Table board;
	public UIController currUIC;
	public AffineTransform boardTransformation;
	public HashMap<Vertex,Point> vertexMap=new HashMap();
	public Vertex selectedVertex=null;
	public ArrayList<Player> playersList;
	public Buildable currentlyBuilding=null;
	public HashMap<Hex,HexPoly> hexMap=new HashMap<Hex,HexPoly>();
	public HashMap<Resource,Color> colorMap=ResourceXMLReader.readTextureXML("textures.xml");
	public Hex selectedTile=null;
	public BoardOrientation boardOrientation=BoardOrientation.NORTH;
	public boolean changeActive=false;
}

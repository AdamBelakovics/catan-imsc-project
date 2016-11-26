package ai;

import controller.player.*;

import java.util.ArrayList;

import controller.map.*;

public class FunctionsToAdd {
	
	public static ArrayList<Vertex> getPlayerSettlements(Table board, Player me){
		ArrayList<Vertex> nodes = board.getNodes();
		ArrayList<Vertex> ret = new ArrayList<Vertex>();
		for(Vertex node : nodes){
			if(node.getSettlement() == null){
				continue;
			}
			if(node.getSettlement().getOwner().equals(me)){
				ret.add(node);
			}
		}
		return ret;
	}
	
	//not implementable at this point
//	public static ArrayList<Edge> getPlayerRoadsFromVertex(Vertex v, Player me){
//	}

}

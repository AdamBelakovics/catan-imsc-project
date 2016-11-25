package ai;

import java.util.ArrayList;
import java.util.Map;

import controller.map.*;
import controller.player.*;

public class TEST_AIController extends PlayerController {
	
	Player me;
	Table board;

	public TEST_AIController(Player me, Table board) {
		this.me = me;
		this.board = board;
	}

	@Override
	public boolean query(Player donor, Map<Resource, Integer> offer, Map<Resource, Integer> demand) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void turn() throws GameEndsException {
		if((me.getResourceAmount(Resource.Brick) >= 1) && (me.getResourceAmount(Resource.Grain) >= 1) &&
			(me.getResourceAmount(Resource.Lumber) >= 1) && (me.getResourceAmount(Resource.Wool) >= 1)){
			ArrayList<Vertex> nodelist = board.getNodes();
			for(Vertex v : nodelist){
				if(v.isBuildPossible(new Settlement())){
					System.out.println(me.getName() + ": i build a Settlement at: " + v.getID());
					me.build(new Settlement(), v);
					break;
				}
			}
		}else if ((me.getResourceAmount(Resource.Ore) >= 3) && (me.getResourceAmount(Resource.Grain) >= 2)) {
			ArrayList<Vertex> nodelist = board.getNodes();
			for(Vertex v : nodelist){
				if(v.isBuildPossible(new City())){
					System.out.println(me.getName() + ": i build a City");
					me.build(new City(), v);
					break;
				}
			}
		}else if ((me.getResourceAmount(Resource.Lumber) >= 1) && (me.getResourceAmount(Resource.Brick) >= 1)) {
			ArrayList<Edge> edgelist = board.getEdges();
			for(Edge e : edgelist){
				if(e.isBuildPossible(new Road())){
					System.out.println(me.getName() + ": i build a Road");
					me.build(new Road(), e);
					break;
				}
			}
		}
	}

}

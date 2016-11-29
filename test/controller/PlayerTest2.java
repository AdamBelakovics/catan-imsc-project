package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.map.Buildable;
import controller.map.Table;
import controller.player.GameEndsException;
import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.Resource;

public class PlayerTest2 {
	static Player p;
	
	/**
	 * Fill players some resources
	 * @throws OutOfRangeException 
	 * @throws GameEndsException 
	 */
	@BeforeClass
	static public void setUpFull() throws OutOfRangeException, GameEndsException{
		p = new Player("Adolf", 1, new Table());
		p.incResourceAmount(Resource.Wool, 10);
		p.incResourceAmount(Resource.Ore, 10);
		p.incResourceAmount(Resource.Grain, 10);
		p.incResourceAmount(Resource.Brick, 10);
		p.incResourceAmount(Resource.Lumber, 10);
		p.firstBuild(Buildable.Road, p.getTable().edgeList.get(1));
		p.firstBuild(Buildable.Settlement, p.getTable().vertexList.get(1));
		p.build(Buildable.Road, p.getTable().edgeList.get(3));
		p.build(Buildable.Road, p.getTable().edgeList.get(4));
		//p.build(Buildable.Settlement, p.getTable().vertexList.get(2));
		
	}
	
	
	@Test
	public void buildTest() throws GameEndsException{

		assertEquals(1, p.getPoints());
		assertEquals(2, p.getAllBuildings().size());
		p.firstBuild(Buildable.Settlement, p.getTable().vertexList.get(3));
		assertEquals(2, p.getPoints());
		
	}
	
	@Test
	public void resourcesAfterBuildTest() throws GameEndsException{
		p.build(Buildable.Road, p.getTable().edgeList.get(5));
		p.build(Buildable.Road, p.getTable().edgeList.get(6));
		p.build(Buildable.Settlement, p.getTable().vertexList.get(3));
		assertEquals(10, p.getResourceAmount(Resource.Lumber));
		assertEquals(10, p.getResourceAmount(Resource.Wool));
	}
	
	

}

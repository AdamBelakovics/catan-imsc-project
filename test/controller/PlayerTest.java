package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


import controller.map.Table;

import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.Resource;


public class PlayerTest {
	Player p;
	
	
	@Before
	public void setUpTest(){
		p = new Player("Adolf", 1, new Table());
	}

	
	/**
	 * Testing Player constructor, getters, setters
	 */
	@Test
	public void getSetTest() {
		assertEquals("Adolf", p.getName());
		assertEquals(1, p.getId());
		assertEquals(0, p.getPoints());
	}
	
	@Test
	public void resourcesTest() throws OutOfRangeException{
		assertEquals(0, p.getResourceAmount(Resource.Wool));
		assertEquals(0, p.getResourceAmount(Resource.Grain));
		assertEquals(0, p.getResourceAmount(Resource.Lumber));
		assertEquals(0, p.getResourceAmount(Resource.Ore));
		assertEquals(0, p.getResourceAmount(Resource.Brick));
		p.incResourceAmount(Resource.Wool, 10);
		p.incResourceAmount(Resource.Ore, 10);
		p.incResourceAmount(Resource.Grain, 10);
		p.incResourceAmount(Resource.Brick, 10);
		p.incResourceAmount(Resource.Lumber, 10);
		assertEquals(10, p.getResourceAmount(Resource.Wool));
		assertEquals(10, p.getResourceAmount(Resource.Grain));
		assertEquals(10, p.getResourceAmount(Resource.Lumber));
		assertEquals(10, p.getResourceAmount(Resource.Ore));
		assertEquals(10, p.getResourceAmount(Resource.Brick));
		
	}
	
	
	
	

}

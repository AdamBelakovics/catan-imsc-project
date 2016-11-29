package controller;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.player.NotEnoughResourcesException;
import controller.player.OutOfRangeException;
import controller.player.Player;
import controller.player.Resource;
import controller.player.devcards.DevCard;
import controller.player.devcards.DevCardShop;



public class TestDevCards {

	Player player = new Player("Adolf", 1, null);
	Resource w = Resource.Wool;
	Resource o = Resource.Ore;
	Resource g = Resource.Grain;
	
	
	@Before
	public void setUp() throws OutOfRangeException, NotEnoughResourcesException{
	

	DevCardShop.initializeShop();
	player.incResourceAmount(w, 100);
	player.incResourceAmount(g, 100);
	player.incResourceAmount(o, 100);
	player.buyDevCard();
	player.buyDevCard();
	player.buyDevCard();
	player.buyDevCard();
	player.buyDevCard();
	

	}
	
	@Test
	public void testWoolAmount() {
		assertEquals(95, player.getResourceAmount(w) );
	}
	
	@Test
	public void testGrainAmount() {
		assertEquals(95, player.getResourceAmount(g) );
	}
	
	@Test
	public void testDevCardAmount(){
		assertEquals(5, player.getDevCards().size());
	}
}

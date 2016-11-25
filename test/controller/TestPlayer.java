package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.player.Player;
import controller.player.PlayerController;

public class TestPlayer extends Player{
	public TestPlayer(String name, int id, PlayerController controller) {
		super("Bela", 1, controller);
	}
	@Test
	public void incResourceTest() {
		assertEquals("Bela", this.getName());
	}

}

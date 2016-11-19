package ux.graphics;

import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import controller.map.*;
import controller.player.*;
import ux.ui.UIController;

public class Renderer {
	UIController currUIC;
	Table board;
	JFrame mainPanel;
	BoardRenderer boardPanel;
	HUDRenderer HUDPanel;
	
	/**
	 * @param _board
	 */
	public Renderer(Table _board) {
		board=_board;
		mainPanel=new JFrame("JCatan");
		mainPanel.setSize(800, 600);
		boardPanel=new BoardRenderer(board);
		HUDPanel=new HUDRenderer();
		mainPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel.add(boardPanel);
		
		mainPanel.addKeyListener(new BoardKeyListener());
		mainPanel.addMouseListener(new BoardMouseListener());
		
		mainPanel.setVisible(true);
	}
	
	public void draw(){
		mainPanel.repaint();
	};
	public void updateDice(Dice dice){};
	public void updateTableElement(TableElement element){};
	public void updateDevCards(Player actualPlayer){};
	public void updateResources(Player actualPlayer){};
	public void updateThief(Hex to) {};
	
	class BoardMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent ev) {
			Map.Entry<Hex,HexPoly> selected=boardPanel.getHexUnderCursor(ev.getX(), ev.getY()-mainPanel.getInsets().top);
			if (selected!=null) {
				boardPanel.selectHex(selected.getKey());
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {}

		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent arg0) {}

		@Override
		public void mouseReleased(MouseEvent arg0) {}

	}
	
	class BoardKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent ev) {
			switch (ev.getKeyChar()) {
			case 'q':
			case 'Q':
				System.out.println("[BoardRenderer]Rotated board to left");
				boardPanel.cycleOrientation(-1);
				break;
			case 'e':
			case 'E':
				System.out.println("[BoardRenderer]Rotated board to right");
				boardPanel.cycleOrientation(1);
				break;
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {}
	}

}

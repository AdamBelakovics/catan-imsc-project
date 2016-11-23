package ux.graphics;

import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import controller.map.*;
import controller.player.*;
import ux.ui.UIController;

public class Renderer {
	UIController currUIC;
	Table board;
	JFrame mainFrame;
	JPanel mainPanel;
	BoardRenderer boardPanel;
	HUDRenderer HUDPanel;
	Timer updateTimer;
	
	/**
	 * @param _board
	 */
	public Renderer(Table _board, int _width, int _height) {
		board=_board;
		mainFrame=new JFrame("JCatan");
		mainFrame.setSize(_width, _height);
		boardPanel=new BoardRenderer(board,_width,_height);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel=new JPanel();
		mainFrame.add(mainPanel);
		
		updateTimer=new Timer(15, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				draw();
			}
		});
		updateTimer.start();

		mainFrame.addKeyListener(new BoardKeyListener());
		mainFrame.addMouseListener(new BoardMouseListener());
		mainFrame.addMouseWheelListener(new BoardWheelListener());
		
		mainFrame.setVisible(true);
	}
	
	public void draw(){
		boardPanel.paint(mainFrame.getGraphics());
	};
	public void updateDice(Dice dice){};
	public void updateTableElement(TableElement element){};
	public void updateDevCards(Player actualPlayer){};
	public void updateResources(Player actualPlayer){};
	public void updateThief(Hex to) {};
	
	class BoardMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent ev) {
			Map.Entry<Hex,HexPoly> selected=boardPanel.hexRenderer.getHexUnderCursor(ev.getX(), ev.getY());
			if (selected!=null) {
				boardPanel.hexRenderer.selectHex(selected.getKey());
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
				boardPanel.hexRenderer.cycleOrientation(-1);
				break;
			case 'e':
			case 'E':
				System.out.println("[BoardRenderer]Rotated board to right");
				boardPanel.hexRenderer.cycleOrientation(1);
				break;
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {}
	}

	class BoardWheelListener implements MouseWheelListener {

		@Override
		public void mouseWheelMoved(MouseWheelEvent ev) {
			System.out.println("[BoardWheelListener] Used mousewheel for "+ev.getWheelRotation()+" clicks.");
			boardPanel.hexRenderer.zoomBoard(ev.getWheelRotation());			
		}
		
	}
}

package ux;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import controller.map.*;
import controller.player.*;
import controller.player.devcards.*;
import ux.board.BoardRenderer;
import ux.ui.BuildButton;
import ux.ui.Button;
import ux.ui.HUDRenderer;
import ux.ui.UIController;

public class Renderer {
	public UIController currUIC;
	Table board;
	JFrame mainFrame;
	public BoardRenderer boardPanel;
	public HUDRenderer hudPanel;
	Timer updateTimer;
	Image bufferImg;

	/**
	 * Initializes the renderer
	 * @param uiController 
	 * @param _board the Table element currently in the game
	 * @param _width width of the window
	 * @param _height height of the window
	 */
	public Renderer(UIController uiController, Table _board, int _width, int _height) {
		board=_board;
		currUIC=uiController;
		mainFrame=new JFrame("JCatan");
		
		boardPanel=new BoardRenderer(uiController, board,_width,_height);
		hudPanel=new HUDRenderer(currUIC, _width,_height);

		mainFrame.setSize(_width, _height);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		updateTimer=new Timer(30, new ActionListener() {
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
		bufferImg=mainFrame.createImage(mainFrame.getWidth(), mainFrame.getHeight());
		if (bufferImg!=null) {
			boardPanel.paint(bufferImg.getGraphics());
			hudPanel.paint(bufferImg.getGraphics());
			mainFrame.getGraphics().drawImage(bufferImg, 0, 0, null);
		}
	};
	public void updateDice(Dice dice){};
	public void updateTableElement(TableElement element){};
	public void updateDevCards(Player actualPlayer){};
	public void updateResources(Player actualPlayer){};
	public void updateThief(Hex to) {};

	
	class BoardMouseListener implements MouseListener {
		
		@Override
		public void mouseClicked(MouseEvent ev) {
			try {
			// Selecting board elements
			Hex selectedHex=boardPanel.hexRenderer.getHexUnderCursor(ev.getX(), ev.getY());
			if (selectedHex!=null) {
				boardPanel.resetBoardSelection();
				hudPanel.resetInterfaceSelection();
				boardPanel.hexRenderer.selectHex(selectedHex);
				hudPanel.interfaceRenderer.setActiveHex(selectedHex);
			}
			
			Vertex selectedVertex=boardPanel.vertexRenderer.getVertexUnderCursor(ev.getX(), ev.getY());
			if (selectedVertex!=null) {
				if (boardPanel.vertexRenderer.currentlyBuilding!=null) {
					if (currUIC.firstturnactive && currUIC.state==FirstTurnState.STARTED) {
						
							currUIC.state=FirstTurnState.CITYBUILT;
							currUIC.controlledPlayer.firstBuild(Buildable.Settlement, selectedVertex);
							
					} else if (currUIC.active) {
						switch (boardPanel.vertexRenderer.currentlyBuilding) {
						case Settlement:
							if (currUIC.controlledPlayer.isBuildPossible(Buildable.Settlement, selectedVertex))
								currUIC.controlledPlayer.build(Buildable.Settlement, selectedVertex);
							break;
						case City:
							if (currUIC.controlledPlayer.isBuildPossible(Buildable.Settlement, selectedVertex))
								currUIC.controlledPlayer.build(Buildable.Settlement, selectedVertex);
							break;		
						default:
							break;
						}
						
					}
				}
			}
			Edge selectedEdge=boardPanel.edgeRenderer.getEdgeUnderCursor(ev.getX(), ev.getY());
			if (selectedEdge!=null && boardPanel.vertexRenderer.currentlyBuilding==Buildable.Road) {
					if (currUIC.firstturnactive && currUIC.state==FirstTurnState.CITYBUILT) {
						
							currUIC.state=FirstTurnState.ROADBUILT;
							currUIC.controlledPlayer.firstBuild(Buildable.Road, selectedEdge);
						
					}
					else if (currUIC.active && currUIC.controlledPlayer.isBuildPossible(Buildable.Road, selectedEdge))
						currUIC.controlledPlayer.build(Buildable.Road, selectedEdge);
			}
			 
			
			// Player activity check
			if (!currUIC.active && !currUIC.firstturnactive) return;
			
			
			// Selecting interface elements
			Button selectedButton=hudPanel.interfaceRenderer.getButtonUnderCursor(ev.getX(), ev.getY());
			if (selectedButton!=null) {
				boardPanel.resetBoardSelection();
				hudPanel.resetInterfaceSelection();
				hudPanel.pressButton(selectedButton);
				if (selectedButton instanceof BuildButton && selectedButton.isSelected())
					boardPanel.vertexRenderer.currentlyBuilding=((BuildButton)selectedButton).building;
			}
			
			DevCard selectedCard=hudPanel.getCardRenderer().getDevCardUnderCursor(ev.getX(), ev.getY());
			if (selectedCard!=null) {
				boardPanel.resetBoardSelection();
				hudPanel.resetInterfaceSelection();
				
				hudPanel.getCardRenderer().selectDevCard(selectedCard);
			}		
			} catch (GameEndsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				//System.out.println("[BoardRenderer]Rotated board to left");
				boardPanel.hexRenderer.cycleOrientation(-1);
				break;
			case 'e':
			case 'E':
				//System.out.println("[BoardRenderer]Rotated board to right");
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
			//System.out.println("[BoardWheelListener] Used mousewheel for "+ev.getWheelRotation()+" clicks.");
			boardPanel.hexRenderer.zoomBoard(ev.getWheelRotation());			
		}
		
	}
}

package ux;

import java.awt.Color;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
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

import controller.Game;
import controller.map.*;
import controller.player.*;
import controller.player.devcards.*;
import ux.board.BoardRenderer;
import ux.ui.BuildButton;
import ux.ui.Button;
import ux.ui.HUDRenderer;
import ux.ui.InterfaceColorProfile;
import ux.ui.StringPainter;
import ux.ui.UIController;

/**
 * Main class of rendering. Creates the JFrame and image renderers
 * @author Kiss Lorinc
 *
 */
public class Renderer {
	JFrame mainFrame;
	public BoardRenderer boardPanel;
	public HUDRenderer hudPanel;
	RendererDataStore dataStore=new RendererDataStore();
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
		dataStore.board=_board;
		dataStore.currUIC=uiController;
		mainFrame=new JFrame("JCatan");
		
		dataStore.width=_width;
		dataStore.height=_height;
		
		boardPanel=new BoardRenderer(dataStore);
		hudPanel=new HUDRenderer(dataStore);

		mainFrame.setSize(_width, _height);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		StringPainter.initializeStringPainter();
		
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
	
	/**
	 * Calls the paint() method of all subrenderers
	 * @author Kiss Lorinc
	 */
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
	
	public void displayGameEndScreen(Player p) {
		System.out.println(p.getName().toUpperCase());
		updateTimer.stop();
		Graphics gr = bufferImg.getGraphics();
			Color c = new Color(InterfaceColorProfile.getPlayerColor(p).getRed(), InterfaceColorProfile.getPlayerColor(p).getGreen(),
					InterfaceColorProfile.getPlayerColor(p).getBlue(), 150);
			c = c.darker();
			gr.setColor(c);
			gr.fillRect(0, 0, dataStore.width, dataStore.height);
			StringPainter.printString(gr, "Game Over " + p.getName() + " you won!!!", InterfaceColorProfile.getPlayerColor(p).brighter(), dataStore.width/2, (int)(dataStore.height * 0.25));
			mainFrame.getGraphics().drawImage(bufferImg, 0, 0, null);		
	}

	
	/**
	 * Handles mouse clicks on elements
	 * @author Kiss Lorinc
	 *
	 */
	class BoardMouseListener implements MouseListener {
		
		/**
		 * Checks clicked elements in the following order:
		 * hex fields, vertices, edges, buttons, cards
		 * @author Kiss Lorinc
		 */
		@Override
		public void mouseClicked(MouseEvent ev) {
			// for quick access
			RendererDataStore ds=dataStore;
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
				if (ds.currentlyBuilding!=null) {
					if (ds.currUIC.firstturnactive && ds.currUIC.state==FirstTurnState.STARTED && ds.currUIC.controlledPlayer.isFirstBuildPossible(Buildable.Settlement, selectedVertex)) {
						
							ds.currUIC.state=FirstTurnState.CITYBUILT;
							ds.currUIC.controlledPlayer.firstBuild(Buildable.Settlement, selectedVertex);
							
					} else if (ds.currUIC.active) {
						switch (ds.currentlyBuilding) {
						case Settlement:
							if (ds.currUIC.controlledPlayer.isBuildPossible(Buildable.Settlement, selectedVertex))
								ds.currUIC.controlledPlayer.build(Buildable.Settlement, selectedVertex);
							break;
						case City:
							if (ds.currUIC.controlledPlayer.isBuildPossible(Buildable.City, selectedVertex))
								ds.currUIC.controlledPlayer.build(Buildable.City, selectedVertex);
							break;		
						default:
							break;
						}
						
					}
				}
			}
			Edge selectedEdge=boardPanel.edgeRenderer.getEdgeUnderCursor(ev.getX(), ev.getY());
			System.out.println(selectedEdge);
			if (selectedEdge!=null && ds.currentlyBuilding==Buildable.Road) {
					if (ds.currUIC.firstturnactive && ds.currUIC.state==FirstTurnState.CITYBUILT &&
							ds.currUIC.controlledPlayer.isFirstBuildPossible(Buildable.Road, selectedEdge)) {
						
							ds.currUIC.state=FirstTurnState.ROADBUILT;
							ds.currUIC.controlledPlayer.firstBuild(Buildable.Road, selectedEdge);
						
					}
					else if (ds.currUIC.active && ds.currUIC.controlledPlayer.isBuildPossible(Buildable.Road, selectedEdge))
						ds.currUIC.controlledPlayer.build(Buildable.Road, selectedEdge);
			}
			 
			
			// Player activity check
			if (!ds.currUIC.active && !ds.currUIC.firstturnactive) return;
			
			
			// Selecting interface elements
			Button selectedButton=hudPanel.interfaceRenderer.getButtonUnderCursor(ev.getX(), ev.getY());
			if (selectedButton!=null) {
				boardPanel.resetBoardSelection();
				hudPanel.resetInterfaceSelection();
				hudPanel.pressButton(selectedButton);
				if (selectedButton instanceof BuildButton && selectedButton.isSelected())
					ds.currentlyBuilding=((BuildButton)selectedButton).building;
			}
			
			DevCard selectedCard=hudPanel.cardRenderer.getDevCardUnderCursor(ev.getX(), ev.getY());
			if (selectedCard!=null && selectedCard!=hudPanel.cardRenderer.getSelectedDevCard()) {
				boardPanel.resetBoardSelection();
				hudPanel.resetInterfaceSelection();
				
				hudPanel.cardRenderer.selectDevCard(selectedCard);
			} else if (ds.currUIC.active && selectedCard!=null && selectedCard==hudPanel.cardRenderer.getSelectedDevCard()) { 
				ds.currUIC.controlledPlayer.playDev(selectedCard, null);
				hudPanel.cardRenderer.deselectDevCards();
			} else {hudPanel.cardRenderer.deselectDevCards();}		
			} catch (GameEndsException e) {
				displayGameEndScreen(Game.players.stream().filter(x -> (Integer)e.getPlayerID() == x.getId()).findFirst().get());
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
	
	/**
	 * Handles the rotation of the board with the E and Q keys
	 * @author Kiss Lorinc
	 *
	 */
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

	/**
	 * Handles board zoom with the use of the mouse wheel
	 * @author Kiss Lorinc
	 *
	 */
	class BoardWheelListener implements MouseWheelListener {

		@Override
		public void mouseWheelMoved(MouseWheelEvent ev) {
			//System.out.println("[BoardWheelListener] Used mousewheel for "+ev.getWheelRotation()+" clicks.");
			boardPanel.hexRenderer.zoomBoard(ev.getWheelRotation());			
		}
		
	}
}

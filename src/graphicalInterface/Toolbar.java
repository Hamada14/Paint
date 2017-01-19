package graphicalInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import ActionManagement.Client;
import ActionManagement.PaintServer;

public class Toolbar extends JMenuBar {

	private static final long serialVersionUID = 3199862667190721395L;

	private static final int FILE = 0, SERVER = 1;
	private static final int NEW = 0, SAVE = 1, LOAD = 2;
	private static final int XML = 0, JSON = 1;
	private static final int MENU_COUNT = 2, FILE_COUNT = 3, SERVER_COUNT = 2;
	private static final int HOST = 0, CONNECT = 1;

	private JMenu[] menuOptions;
	private JMenuItem[] fileItems;
	private JMenuItem[] serverItems;
	private DrawingArea drawingPad;
	private JMenuItem[] saveOptions;
	private UpperPanelBar upperPanel;
	
	private static boolean isHosting, isConnected;
	public PaintServer paintServer;
	public Client client;

	public Toolbar() {
		menuOptions = new JMenu[MENU_COUNT];
		initFileBar();
		initServerBar();
		initSaveMenu();
		addMenuOptions();
	}

	public void setArea(DrawingArea drawingPad) {
		this.drawingPad = drawingPad;
	}

	private void addMenuOptions() {
		add(menuOptions[FILE]);
		add(menuOptions[SERVER]);
	}

	public boolean isHosting() {
		return isHosting;
	}

	public void removeHost() {
		serverItems[HOST].setText("Host");
		serverItems[CONNECT].setVisible(true);
		isHosting = false;
		upperPanel.setOflineMode();
		drawingPad.newPaint();
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void removeConnection() {
		serverItems[CONNECT].setText("Connect");
		serverItems[HOST].setVisible(true);
		isConnected = false;
		upperPanel.setOflineMode();
		drawingPad.newPaint();
	}

	private void initServerBar() {
		menuOptions[SERVER] = new JMenu("Server");
		serverItems = new JMenuItem[SERVER_COUNT];
		serverItems[HOST] = new JMenuItem("Host");
		serverItems[HOST].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isHosting) {
					removeHost();
				} else {
					try {
						serverItems[HOST].setText("Cancel Host");
						serverItems[CONNECT].setVisible(false);
						isHosting = true;
						paintServer = new PaintServer(Toolbar.this, drawingPad);
						upperPanel.setOnlineMode();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Couldn't connect", "Connection",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		serverItems[CONNECT] = new JMenuItem("Connect");
		serverItems[CONNECT].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (isConnected) {
					removeConnection();
				} else {
					String ip = JOptionPane.showInputDialog(null, "Enter the Ip Address ", "Connection",
							JOptionPane.INFORMATION_MESSAGE);
					if (ip == null) {
						JOptionPane.showMessageDialog(null, "Couldn't connect", "Connection",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					try {
						serverItems[CONNECT].setText("Cancel CONNECT");
						serverItems[HOST].setVisible(false);
						isConnected = true;
						client = new Client(ip, Toolbar.this, drawingPad);
						upperPanel.setOnlineMode();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Couldn't connect" + e1.getMessage(), "Connection",
								JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
			}

		});
		menuOptions[SERVER].add(serverItems[HOST]);
		menuOptions[SERVER].add(serverItems[CONNECT]);
	}

	private void initFileBar() {
		menuOptions[FILE] = new JMenu("File");
		fileItems = new JMenuItem[FILE_COUNT];
		fileItems[NEW] = new JMenuItem("New");
		fileItems[NEW].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawingPad.newPaint();
			}
		});
		fileItems[SAVE] = new JMenu("Save");
		fileItems[LOAD] = new JMenuItem("Load");
		fileItems[LOAD].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showOpenDialog(Toolbar.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						drawingPad.load(file);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(GUIPanel.drawingPad,
								"Either the file is Corrupted or you must load custom Libraries.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		menuOptions[FILE].add(fileItems[NEW]);
		menuOptions[FILE].add(fileItems[SAVE]);
		menuOptions[FILE].add(fileItems[LOAD]);
	}

	private void initSaveMenu() {
		saveOptions = new JMenuItem[2];
		saveOptions[XML] = new JMenuItem("Xml");
		saveOptions[JSON] = new JMenuItem("JSON");
		saveOptions[XML].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showSaveDialog(Toolbar.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						drawingPad.saveXML(file);
					} catch (FileNotFoundException | UnsupportedEncodingException e1) {
						JOptionPane.showMessageDialog(GUIPanel.drawingPad,
								"Please make sure no other program is trying to access this file", "Error",
								JOptionPane.ERROR_MESSAGE);
						drawingPad.newPaint();
					}
				}
			}
		});
		saveOptions[JSON].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showSaveDialog(Toolbar.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						drawingPad.saveJSON(file);
					} catch (FileNotFoundException | UnsupportedEncodingException e1) {
						JOptionPane.showMessageDialog(GUIPanel.drawingPad,
								"Please make sure no other program is trying to access this file", "Error",
								JOptionPane.ERROR_MESSAGE);
						drawingPad.newPaint();
					}
				}
			}
		});
		fileItems[SAVE].add(saveOptions[XML]);
		fileItems[SAVE].add(saveOptions[JSON]);
	}

	public void setPanels(UpperPanelBar upperPanel) {
		this.upperPanel = upperPanel;
	}
}

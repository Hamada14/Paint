package graphicalInterface;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import colorManagement.ColorBundle;

public class GUIPanel extends JFrame {

	private static final long serialVersionUID = -8438289821471424738L;
	
	private static final int windowMaxLength = 1000, windowMaxHeight = 700;
	
	public static DrawingArea drawingPad;

	private LeftPanel leftPanel;
	private UpperPanelBar upperPanel;
	private Toolbar toolbar;
	private ColorBundle colorBundle;
	
	public GUIPanel() {
		colorBundle = new ColorBundle();
		initWindow();
		initObjects();
		add(leftPanel, BorderLayout.WEST);
		add(upperPanel, BorderLayout.NORTH);
		setJMenuBar(toolbar);
		add(drawingPad);
		drawingPad.repaint();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void initWindow(){
		setTitle("Paint Vectors");
		setSize(windowMaxLength, windowMaxHeight);
		setResizable(false);
		setBackground(Color.GRAY);
	}
	
	private void initObjects(){
		toolbar = new Toolbar();
		drawingPad = new DrawingArea(colorBundle, toolbar);
		toolbar.setArea(drawingPad);
		leftPanel = new LeftPanel(drawingPad);
		upperPanel = new UpperPanelBar(drawingPad, leftPanel);
		toolbar.setPanels(upperPanel);
	}

	public static void main(String[] args) {
		new GUIPanel();
	}
}
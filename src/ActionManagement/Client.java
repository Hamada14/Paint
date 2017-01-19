package ActionManagement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import graphicalInterface.DrawingArea;
import graphicalInterface.Toolbar;

public class Client extends Thread {

	private static final int PORT = PaintServer.PORT;

	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Socket client;
	private Toolbar toolbar;
	private ShapePackage drawPackage;
	private DrawingArea drawingPad;

	public Client(String serverIP, Toolbar toolbar, DrawingArea drawingPad) throws UnknownHostException, IOException {
		client = new Socket(serverIP, PORT);
		output = new ObjectOutputStream(client.getOutputStream());
		input = new ObjectInputStream(client.getInputStream());
		this.toolbar = toolbar;
		drawPackage = new ShapePackage();
		this.drawingPad = drawingPad;
		start();
	}

	public ShapePackage getPackage() {
		return drawPackage;
	}

	@Override
	public void run() {
		while (toolbar.isConnected()) {
			try {
				if (!client.isConnected()) {
					toolbar.removeHost();
					throw new RuntimeException();
				}
				ShapePackage sp = (ShapePackage) input.readObject();
				output.writeObject(drawingPad.getShapePackage());
				drawPackage = new ShapePackage(sp);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Connection Ended.", "Connection", JOptionPane.WARNING_MESSAGE);
				toolbar.removeConnection();
				break;
			}
		}
		try {
			client.close();
		} catch (IOException e) {
		} finally {
			toolbar.removeHost();
		}
	}
}

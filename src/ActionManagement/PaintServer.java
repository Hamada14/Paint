package ActionManagement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import graphicalInterface.DrawingArea;
import graphicalInterface.Toolbar;

public class PaintServer extends Thread {

	public static final int PORT = 3021;
	private static final int TIME_LIMIT = 30;

	private ServerSocket server;
	private Socket socket;
	private InetAddress hostAddress;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Toolbar toolbar;
	private DrawingArea drawingPad;
	private ShapePackage drawPackage;
	
	public PaintServer(Toolbar toolbar, DrawingArea drawingPad) throws IOException {
		hostAddress = InetAddress.getLocalHost();
		server = new ServerSocket(PORT, 0, hostAddress);
		server.setSoTimeout(TIME_LIMIT);
		this.toolbar = toolbar;
		this.drawingPad = drawingPad;
		this.drawPackage = new ShapePackage();
		start();
	}

	public ShapePackage getPackage() {
		return drawPackage;
	}
	
	@Override
	public void run() {
		while (toolbar.isHosting()) {
			try {
				socket = server.accept();
				input = new ObjectInputStream(socket.getInputStream());
				output = new ObjectOutputStream(socket.getOutputStream());
				JOptionPane.showMessageDialog(null, "Friend connected successfully.", "Connection",
						JOptionPane.INFORMATION_MESSAGE);
				break;
			} catch (Exception e) {
				System.out.println("No Connection");
			}
		}
		while (toolbar.isHosting()) {
			try {
				if (!socket.isConnected()) {
					toolbar.removeHost();
					throw new RuntimeException();
				}
				output.writeObject(drawingPad.getShapePackage());
				output.flush();
				ShapePackage sp = (ShapePackage) input.readObject();
				drawPackage = new ShapePackage(sp);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Connection Ended.", "Connection", JOptionPane.WARNING_MESSAGE);
				toolbar.removeHost();
				break;
			}
		}
		try {
			server.close();
		} catch (IOException e) {
		}
	}
}

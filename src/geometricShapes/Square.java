package geometricShapes;

import java.awt.Point;

import colorManagement.ColorBundle;

public class Square extends Rectangle implements java.io.Serializable {

	private static final long serialVersionUID = -284126145763814895L;

	public Square(Point cntrPnt, Point refPnt, ColorBundle colorBundle) {
		super(cntrPnt, refPnt, colorBundle);
	}
}

package ActionManagement;

import java.util.ArrayList;

import geometricShapes.CustomShape;

public class ShapePackage implements java.io.Serializable {

	private static final long serialVersionUID = 7660909671995580550L;

	ArrayList<CustomShape> shapeList;

	public ShapePackage() {
		shapeList = new ArrayList<CustomShape>();
	}

	public ShapePackage(ShapePackage sp) {
		shapeList = new ArrayList<CustomShape>();
		int vSize = sp.getSize();
		for(int i = 0; i < vSize; i++){
			shapeList.add(sp.get(i));
		}
	}

	public int getSize(){
		return shapeList.size();
	}
	
	public CustomShape get(int index){
		return shapeList.get(index);
	}
	
	public void add(CustomShape cs) {
		shapeList.add(cs);
	}

}

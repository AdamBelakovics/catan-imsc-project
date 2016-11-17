package controller.map;

public enum Orientation {
	NORTH(0), NORTHEAST(1), EAST(2), SOUTHEAST(3), SOUTH(4), SOUTHWEST(5), WEST(6), NORTHWEST(7);
	
	public final int id;
	
	Orientation(int i){
		id = i;
	}
	
	public static Orientation stringToOrientation(String direction){
		Orientation o = NORTH;
		if(Orientation.NORTH.toString().equals(direction)){
			o = Orientation.NORTH;
		}
		else if(Orientation.NORTHEAST.toString().equals(direction)){
			o = Orientation.NORTHEAST;
		}
		else if(Orientation.EAST.toString().equals(direction)){
			o = Orientation.EAST;
		}
		else if(Orientation.SOUTHEAST.toString().equals(direction)){
			o = Orientation.SOUTHEAST;
		}
		else if(Orientation.SOUTH.toString().equals(direction)){
			o = Orientation.SOUTH;
		}
		else if(Orientation.SOUTHWEST.toString().equals(direction)){
			o = Orientation.SOUTHWEST;
		}
		else if(Orientation.WEST.toString().equals(direction)){
			o = Orientation.WEST;
		}
		else if(Orientation.NORTHWEST.toString().equals(direction)){
			o = Orientation.NORTHWEST;
		}
		else{
			System.out.println("Sikertelen stringToOrientation()");
		}
		return o;
	}
}

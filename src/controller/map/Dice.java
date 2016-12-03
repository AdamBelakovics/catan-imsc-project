package controller.map;

public class Dice {

	static int currentValue;
	
	public static void setCurrentValue(int i){
		currentValue = i;
	}
	
	public static int getCurrentValue(){
		return currentValue;
	}
}

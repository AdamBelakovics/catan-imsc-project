package controller.map;

public class Dice {
	private int dice0;
	private int dice1;
	public int getDiceAt(int i) {return i==0?dice0:dice1;}
}

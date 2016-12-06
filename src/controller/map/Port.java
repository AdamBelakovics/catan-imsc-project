package controller.map;

import controller.player.Resource;

public class Port {

	Resource res;
	int changeNumber;
	
	public Port(Resource res, int changeNumber) {
		this.res = res;
		this.changeNumber = changeNumber;
	}
	public Resource getRes() {
		return res;
	}
	public void setRes(Resource res) {
		this.res = res;
	}
	public int getChangeNumber() {
		return changeNumber;
	}
	public void setChangeNumber(int changeNumber) {
		this.changeNumber = changeNumber;
	}
}

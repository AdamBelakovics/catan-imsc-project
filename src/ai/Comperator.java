package ai;

import java.util.Comparator;

public class Comperator implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return ((Double)o1).compareTo((Double)o2);
	}

}

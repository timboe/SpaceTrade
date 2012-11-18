package com.timboe.spacetrade.utility;

import java.util.ArrayList;
import java.util.Random;

public class RandName {
	protected Random rand = new Random(1);
	protected ArrayList<String> content = new ArrayList<String>();
	
	public RandName() {
	}
	
	public void add(String _s) {
		content.add(_s);
	}
	
	public String get() {
		if (content.size() == 0) return "Empty";
		return content.get( rand.nextInt( content.size() ) );
	}
	
	public int getSize() {
		return content.size();
	}
	
}

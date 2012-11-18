package com.timboe.spacetrade.utility;

public class OneOffRandName extends RandName {

	public OneOffRandName() {
	}

	@Override
	public String get() {
		if (content.size() == 0) {
			System.out.println("Empty");
			return "Empty";
		}
		return content.remove( rand.nextInt( content.size() ) );
	}


}

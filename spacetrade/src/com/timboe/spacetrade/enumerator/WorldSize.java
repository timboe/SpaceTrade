package com.timboe.spacetrade.enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.timboe.spacetrade.utility.Utility;

public enum WorldSize {
	Small(0.8f),
	Medium(1f),
	Large(1.2f);
	
	private float sizeMod;
	WorldSize(float _s) {
		sizeMod = _s;
	}
	
	public float getSizeMod() {
		return sizeMod;
	}
	
	private static final Utility util = Utility.getUtility();
	private static final List<WorldSize> content = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int size = content.size();

	public static WorldSize random()  {
		return content.get( util.getRandI(size) );
	}
}

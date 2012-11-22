package com.timboe.spacetrade.enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.timboe.spacetrade.utility.Utility;

public enum Civilisation {
	Agricultural,
	Renaissance,
	Industrial,
	Technological,
	Cybernetic,
	Transcendental;
	
	private static final List<Civilisation> content = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int size = content.size();

	public static Civilisation random()  {
		return content.get( Utility.getRandI(size) );
	}
}

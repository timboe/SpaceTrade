package com.timboe.spacetrade.enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.timboe.spacetrade.utility.Utility;

public enum Civilisation {
	Agricultural,
	Enlightenment,
	Industrial,
	Technological,
	Cybernetic,
	Transcendental;
	
	private static final Utility util = Utility.getUtility();
	private static final List<Civilisation> content = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int size = content.size();

	public static Civilisation random()  {
		return content.get( util.getRandI(size) );
	}
}

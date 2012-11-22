package com.timboe.spacetrade.enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.timboe.spacetrade.utility.Utility;

public enum Government {
	Aristocracy, //The well to do
	Geniocracy, //The wise
	Kratocracy, //The strong
	Technocracy, //The skilled
	Autocracy, //Our dear leader
	Dictatorship, //Our dear leader
	Republic, //Democratic
	Democracy, //Democratic
	Monarchy, //Feudal
	Bureaucracy, //Lots of paper
	Ergatocracy, //The proles
	Oligarchy, //The related
	Plutocracy, //The wealthy
	Theocracy; //The religious
	
	private static final List<Government> content = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int size = content.size();

	public static Government random()  {
		return content.get( Utility.getRandI(size) );
	}
}

package com.timboe.spacetrade.enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.timboe.spacetrade.utility.Rnd;

public enum Government {
	Aristocracy		( 0.06f ), //The well to do
	Geniocracy		( 0.04f ), //The wise
	Kratocracy		( 0.02f ), //The strong
	Technocracy		( 0     ), //The skilled
	Autocracy		( 0.01f ), //Our dear leader
	Dictatorship	( 0.01f ), //Our dear leader
	Republic		( 0.02f ), //Democratic
	Democracy		( 0.03f ), //Democratic
	Monarchy		( 0.05f ), //Feudal
	Bureaucracy		( 0     ), //Lots of paper
	Ergatocracy		( 0.01f ), //The proles
	Oligarchy		( 0.02f ), //The related
	Plutocracy		( 0.06f ), //The wealthy
	Theocracy		( 0.07f ); //The religious
	
	private static final List<Government> content = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int size = content.size();
	private static final Rnd rnd = new Rnd();
	
	float bribeRate;
	
	private Government(float _br) {
		bribeRate = _br;
	}
	
	public float getBribeRate() {
		return bribeRate;
	}

	public static Government random()  {
		return content.get( rnd.getRandI(size) );
	}
}

package com.timboe.spacetrade.utility;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.timboe.spacetrade.SpaceTrade;

public class RandName {
	protected Random rand = new Random(SpaceTrade.masterSeed);
	protected ArrayList<Object> content = new ArrayList<Object>();
	
	public RandName() {
	}
	
	public void addStr(String _s) {
		content.add(_s);
	}
	
	public void addCol(Color color) {
		content.add(color);
	}
	
	public String getStr() {
		if (content.size() == 0) return "Empty";
		return (String) content.get( rand.nextInt( content.size() ) );
	}
	
	public Color getCol() {
		if (content.size() == 0) return new Color();
		return (Color) content.get( rand.nextInt( content.size() ) );
	}
	
	public int getSize() {
		return content.size();
	}
	
}

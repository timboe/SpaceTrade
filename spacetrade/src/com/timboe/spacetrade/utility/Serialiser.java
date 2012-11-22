package com.timboe.spacetrade.utility;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.screen.StarmapScreen;
import com.timboe.spacetrade.world.Starmap;

public class Serialiser {
	
	public static void saveState() {
		if (true)return;
		Json json = new Json();
		
//		json.setSerializer(EnumMap.class, new Json.Serializer<EnumMap>() {
//			@Override
//			public void write (Json json, EnumMap map, Class knownType) {
//				json.writeObjectStart(EnumMap.class, knownType);
//				if (!map.isEmpty()) {
//					Class keyType = map.keySet().iterator().next().getClass();
//					json.writeValue("_keyType", json.getTag(keyType));
//					for (Map.Entry entry : (Set<Map.Entry>)map.entrySet())
//						json.writeValue(String.valueOf(entry.getKey()), entry.getValue());
//				}
//				json.writeObjectEnd();
//			}
//
//			@Override
//			public EnumMap read (Json json, Object jsonData, Class type) {
//				ObjectMap<String, Object> map = (ObjectMap<String, Object>)jsonData;
//				Class keyType = json.getClass((String)map.remove("_keyType"));
//				EnumMap enumMap = new EnumMap(keyType);
//				for (com.badlogic.gdx.utils.ObjectMap.Entry<String, Object> entry : map.entries()) {
//					Object value = json.readValue(entry.key, null, jsonData);
//					enumMap.put(Enum.valueOf(keyType, entry.key), value);
//				}
//				return enumMap;
//			}
//		});
		
	//	System.out.println(json.prettyPrint(Player.getPlayer()));

		
	//	System.out.println(json.prettyPrint(Starmap.getStarmap()));

		//alternate is json.toJson
		FileHandle saveHandle = Gdx.files.local("data/SpaceTradeSave.json");
		//saveHandle.writeString(json.toJson(Utility.getUtility()) + "\n", false);
		//saveHandle.writeString(json.toJson(Starmap.getStarmap()) + "\n", true);
		saveHandle.writeString(json.toJson(Player.getPlayer()) + "\n", true);
		Gdx.app.log("Serialiser","SavedTo:"+saveHandle.path());

	}
	
	public static void loadState() {
		Json json = new Json();
		
//		//Serialiser required to write EnumMaps
//		json.setSerializer(EnumMap.class, new Json.Serializer<EnumMap>() {
//			@Override
//			public void write (Json json, EnumMap map, Class knownType) {
//				json.writeObjectStart(EnumMap.class, knownType);
//				if (!map.isEmpty()) {
//					Class keyType = map.keySet().iterator().next().getClass();
//					json.writeValue("_keyType", json.getTag(keyType));
//					for (Map.Entry entry : (Set<Map.Entry>)map.entrySet())
//						json.writeValue(String.valueOf(entry.getKey()), entry.getValue());
//				}
//				json.writeObjectEnd();
//			}
//
//			@Override
//			public EnumMap read (Json json, Object jsonData, Class type) {
//				ObjectMap<String, Object> map = (ObjectMap<String, Object>)jsonData;
//				Class keyType = json.getClass((String)map.remove("_keyType"));
//				EnumMap enumMap = new EnumMap(keyType);
//				for (com.badlogic.gdx.utils.ObjectMap.Entry<String, Object> entry : map.entries()) {
//					Object value = json.readValue(entry.key, null, jsonData);
//					enumMap.put(Enum.valueOf(keyType, entry.key), value);
//				}
//				return enumMap;
//			}
//		});
		
		FileHandle loadHandle = Gdx.files.local("data/SpaceTradeSave.json");
		String lines[] = loadHandle.readString().split("\\r?\\n");
		
		//Utility _u = json.fromJson(Utility.class, lines[0] );
		//Utility.setUtility(_u);
		
		//Starmap _s = json.fromJson(Starmap.class, lines[1] );
		//Starmap.setStarmap(_s);
		//Starmap.getStarmap().refresh();
		
		Player _p = json.fromJson(Player.class, lines[2] );
		Player.setPlayer(_p);
		Player.getPlayer().refresh();


	}

}

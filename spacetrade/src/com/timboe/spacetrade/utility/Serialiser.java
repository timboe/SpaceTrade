package com.timboe.spacetrade.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.screen.StarmapScreen;
import com.timboe.spacetrade.world.Starmap;

public class Serialiser {
	
	public static void saveState() {
		Json json = new Json();
		
		
//		json.setSerializer(EnumMap.class, new Json.Serializer<EnumMap>() {
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
		saveHandle.writeString(json.prettyPrint(Player.getPlayer()) + "\n", false);
		//saveHandle.writeString(json.prettyPrint(AdLib.getAdLib()) + "\n", true);
		saveHandle.writeString(json.prettyPrint(Utility.getUtility()) + "\n", true);
		StarmapScreen.getStarmapScreen().unHookStars();
		saveHandle.writeString(json.prettyPrint(Starmap.getStarmap()) + "\n", true);
		Gdx.app.log("Serialiser","SavedTo:"+saveHandle.path());

	}

}

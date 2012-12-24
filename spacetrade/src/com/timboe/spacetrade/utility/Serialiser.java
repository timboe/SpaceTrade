package com.timboe.spacetrade.utility;

import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.Civilisation;
import com.timboe.spacetrade.enumerator.Equipment;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.enumerator.Government;
import com.timboe.spacetrade.enumerator.PlanetActivity;
import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.enumerator.ShipProperty;
import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.enumerator.SpecialEvents;
import com.timboe.spacetrade.enumerator.Weapons;
import com.timboe.spacetrade.enumerator.WorldSize;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.screen.PlanetScreen;
import com.timboe.spacetrade.screen.StarmapScreen;
import com.timboe.spacetrade.ship.Ship;
import com.timboe.spacetrade.world.Planet;
import com.timboe.spacetrade.world.Starmap;

public class Serialiser {
	
	private static Serialiser singleton = new Serialiser();
	public static Serialiser getSerialiser() {
		return singleton;
	}
	
	Serialiser() {
	}
	
	//view
	//TODO save current view
	private int masterSeed = SpaceTrade.masterSeed;
	
	//Player details
	private Array<Integer> stock = new Array<Integer>();
	private Array<Integer> avPrice = new Array<Integer>();
	private int credz;
	private int totalCargo;
	private int currentLocationID;
	private String name;
	private boolean isInsured;
	private float noClaimBonus;
	private int insPaidSoFar;
	private int overdraft;
	private boolean useOverdraft;
	
	//player ship details
	private ShipProperty property;
	private ShipClass shipClass;
	private Array<Weapons> weaponLoadout;
	private Array<Equipment> techLoadout;
	private Rnd shipRnd;
	private int hull;
	private int heat;
	private int shielding;
	private float age;
	private boolean hasEscapePod;
	
	//starmapScreen
	private int planetClickedID;
	private int planetHighlighID;
	//starmap
	private int starDate;
	private float starDateGalaxy;
	private float starDateShip;
	private float remainder;
	private Rnd starmapRnd;
	//planet details
	private Array<String> planetName = new Array<String>();// = AdLib.getAdLib().planets.getStr();
	private Array<Integer> nameLen = new Array<Integer>();// = name.length();
	private Array<Color> planetColour = new Array<Color>();// =  AdLib.getAdLib().starColour.getCol();
	private Array<Government> govType = new Array<Government>();// = Government.random();
	private Array<Civilisation> civType = new Array<Civilisation>();// = Civilisation.random();
	private Array<WorldSize> worldSize = new Array<WorldSize>();// = WorldSize.random();
	private Array<Integer> planetID = new Array<Integer>();
	private Array<Boolean> isVisited = new Array<Boolean>();// = false;
	private Array<SpecialEvents> specialEvent = new Array<SpecialEvents>();
	private Array<PlanetActivity> police = new Array<PlanetActivity>();// = PlanetActivity.Some;
	private Array<PlanetActivity> pirates = new Array<PlanetActivity>();// = PlanetActivity.Some;
	private Array<PlanetActivity> traders = new Array<PlanetActivity>();// = PlanetActivity.Some;
	private Array<Rnd> planetRnd = new Array<Rnd>();// = new Rnd();
//	private final EnumMap<Goods, Boolean> goodsSold = new EnumMap<Goods, Boolean>(Goods.class);
//	private final EnumMap<Goods, AtomicInteger> stock = new EnumMap<Goods, AtomicInteger>(Goods.class);
//	private final EnumMap<Goods, AtomicInteger> stockTarget = new EnumMap<Goods, AtomicInteger>(Goods.class);
//	private final EnumMap<Goods, AtomicInteger> volitility = new EnumMap<Goods, AtomicInteger>(Goods.class);
//	private final EnumMap<Goods, Array<AtomicInteger> > price = new EnumMap<Goods, Array<AtomicInteger> >(Goods.class);
//	private final EnumMap<ShipClass, ShipProperty > shipsSold = new EnumMap<ShipClass, ShipProperty >(ShipClass.class);
//	private final EnumMap<Weapons, Boolean > weaponsSold = new EnumMap<Weapons, Boolean >(Weapons.class);
//	private final EnumMap<Equipment, Boolean > equipmentSold = new EnumMap<Equipment, Boolean >(Equipment.class);
	
	private void collectPlanetDetails() {
		//starmaps screen
		planetClickedID = StarmapScreen.getPlanetClickedID();
		planetHighlighID = StarmapScreen.getPlanetHighlightID();
		//starmap
		starDate = Starmap.getStarDate();
		starDateGalaxy = Starmap.getStarDateGalaxy();
		starDateShip = Starmap.getStarDateShip();
		starDate = Starmap.getStarDate();
		starmapRnd = Starmap.getRnd();
		//planets
		planetName.clear();// = AdLib.getAdLib().planets.getStr();
		nameLen.clear();// = name.length();
		planetColour.clear();// =  AdLib.getAdLib().starColour.getCol();
		govType.clear();// = Government.random();
		civType.clear();// = Civilisation.random();
		worldSize.clear();// = WorldSize.random();
		planetID.clear();
		isVisited.clear();// = false;
		specialEvent.clear();
		police.clear();// = PlanetActivity.Some;
		pirates.clear();// = PlanetActivity.Some;
		traders.clear();// = PlanetActivity.Some;
		planetRnd.clear();// = new Rnd();
		for (Planet _p : Starmap.getPlanets()) {
			planetName.add( _p.getName() );
			nameLen.add( _p.getNameLen() );
			planetColour.add( _p.getPlanetColour() );
			govType.add( _p.getGov() );
			civType.add( _p.getCiv() );
			worldSize.add( _p.getSize() );
			planetID.add( _p.getID() );
			isVisited.add( _p.getVisited() );
			specialEvent.add( _p.getSpecial() );
			police.add( _p.getActivity(ShipTemplate.Police) );
			pirates.add( _p.getActivity(ShipTemplate.Pirate) );
			traders.add( _p.getActivity(ShipTemplate.Trader) );
			planetRnd.add( _p.getRnd() );
		}
	}
	
	private void collectPlayerDetails() {
		credz = Player.getCredz();
		totalCargo = Player.getTotalCargo();
		currentLocationID = Player.getPlanetID();
		name = Player.getPlayerName();
		isInsured = Player.getInsured();
		noClaimBonus = Player.getNoClaims();
		insPaidSoFar = Player.getInsurancePaid();
		overdraft = Player.getOD();
		useOverdraft = Player.getUseOD();
		stock.clear();
		avPrice.clear();
		for (Goods _g : Goods.values()) {
			stock.add(Player.getStock(_g));
			avPrice.add(Player.getAvPaidPrice(_g));
		}
	}
	
	private void collectShipDetails() {
		Ship ship = Player.getShip();
		property = ship.getMod();
		shipClass = ship.getShipClass();
		weaponLoadout = ship.getWeapons();
		techLoadout = ship.getEquipment();
		shipRnd = ship.getRnd();
		hull = ship.getHull();
		heat = ship.getHeat();
		shielding = ship.getShields();
		age = ship.getAge();
		hasEscapePod = ship.getEscapePod();
	}
	
	
	public void saveState(int _slot) {
		Json json = new Json();
		
		collectPlayerDetails();
		collectShipDetails();
		collectPlanetDetails();
		
		FileHandle saveHandle = Gdx.files.local("data/SpaceTradeSave_"+_slot+".json");
		saveHandle.writeString(json.toJson(this), false);
		
//		if (true)return;
//		Json json = new Json();
//		
////		json.setSerializer(EnumMap.class, new Json.Serializer<EnumMap>() {
////			@Override
////			public void write (Json json, EnumMap map, Class knownType) {
////				json.writeObjectStart(EnumMap.class, knownType);
////				if (!map.isEmpty()) {
////					Class keyType = map.keySet().iterator().next().getClass();
////					json.writeValue("_keyType", json.getTag(keyType));
////					for (Map.Entry entry : (Set<Map.Entry>)map.entrySet())
////						json.writeValue(String.valueOf(entry.getKey()), entry.getValue());
////				}
////				json.writeObjectEnd();
////			}
////
////			@Override
////			public EnumMap read (Json json, Object jsonData, Class type) {
////				ObjectMap<String, Object> map = (ObjectMap<String, Object>)jsonData;
////				Class keyType = json.getClass((String)map.remove("_keyType"));
////				EnumMap enumMap = new EnumMap(keyType);
////				for (com.badlogic.gdx.utils.ObjectMap.Entry<String, Object> entry : map.entries()) {
////					Object value = json.readValue(entry.key, null, jsonData);
////					enumMap.put(Enum.valueOf(keyType, entry.key), value);
////				}
////				return enumMap;
////			}
////		});
//		
//	//	System.out.println(json.prettyPrint(Player.getPlayer()));
//
//		
//	//	System.out.println(json.prettyPrint(Starmap.getStarmap()));
//
//		//alternate is json.toJson
//		FileHandle saveHandle = Gdx.files.local("data/SpaceTradeSave.json");
//		//saveHandle.writeString(json.toJson(Utility.getUtility()) + "\n", false);
//		//saveHandle.writeString(json.toJson(Starmap.getStarmap()) + "\n", true);
//		saveHandle.writeString(json.toJson(Player.getPlayer()) + "\n", true);
//		Gdx.app.log("Serialiser","SavedTo:"+saveHandle.path());

	}
	
	public void loadState() {
//		Json json = new Json();
//		
////		//Serialiser required to write EnumMaps
////		json.setSerializer(EnumMap.class, new Json.Serializer<EnumMap>() {
////			@Override
////			public void write (Json json, EnumMap map, Class knownType) {
////				json.writeObjectStart(EnumMap.class, knownType);
////				if (!map.isEmpty()) {
////					Class keyType = map.keySet().iterator().next().getClass();
////					json.writeValue("_keyType", json.getTag(keyType));
////					for (Map.Entry entry : (Set<Map.Entry>)map.entrySet())
////						json.writeValue(String.valueOf(entry.getKey()), entry.getValue());
////				}
////				json.writeObjectEnd();
////			}
////
////			@Override
////			public EnumMap read (Json json, Object jsonData, Class type) {
////				ObjectMap<String, Object> map = (ObjectMap<String, Object>)jsonData;
////				Class keyType = json.getClass((String)map.remove("_keyType"));
////				EnumMap enumMap = new EnumMap(keyType);
////				for (com.badlogic.gdx.utils.ObjectMap.Entry<String, Object> entry : map.entries()) {
////					Object value = json.readValue(entry.key, null, jsonData);
////					enumMap.put(Enum.valueOf(keyType, entry.key), value);
////				}
////				return enumMap;
////			}
////		});
//		
//		FileHandle loadHandle = Gdx.files.local("data/SpaceTradeSave.json");
//		String lines[] = loadHandle.readString().split("\\r?\\n");
//		
//		//Utility _u = json.fromJson(Utility.class, lines[0] );
//		//Utility.setUtility(_u);
//		
//		//Starmap _s = json.fromJson(Starmap.class, lines[1] );
//		//Starmap.setStarmap(_s);
//		//Starmap.getStarmap().refresh();
//		
//	//	Player _p = json.fromJson(Player.class, lines[2] );
//		//Player.setPlayer(_p);
//	//	Player.getPlayer().refresh();
//

	}

}

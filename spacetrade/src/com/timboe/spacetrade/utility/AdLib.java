package com.timboe.spacetrade.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class AdLib {

	public static final AdLib singleton = new AdLib();
	public static final AdLib getAdLib() {
		return singleton;
	}
	
	public OneOffRandName planets = new OneOffRandName();
	public RandName fornames = new RandName();
	public RandName starColour = new RandName();

	private AdLib() {
		planets.addStr("Magrathea");
		planets.addStr("Bernards");
		planets.addStr("Xarquest");
		planets.addStr("Spongiform");
		planets.addStr("Little Biddlington");
		planets.addStr("Eugenia");
		planets.addStr("Sponde");
		planets.addStr("Hegemone");
		planets.addStr("Aoede");
		planets.addStr("Eukelade");
		planets.addStr("Cyllene");
		planets.addStr("Hyperion");
		planets.addStr("Janus");
		planets.addStr("Epimetheus");
		planets.addStr("Pandoron");
		planets.addStr("Mundilfari");
		planets.addStr("Thrymr");
		planets.addStr("Polydeuces");
		planets.addStr("Bebhionn");
		planets.addStr("Oberon");
		planets.addStr("Bianca");
		planets.addStr("Triton");
		planets.addStr("Sao");
		planets.addStr("Xix");
		planets.addStr("Dysnomia");
		planets.addStr("Halweaver");
		planets.addStr("Hellvetica");
		planets.addStr("Eternium");
		planets.addStr("Wormulon");
		planets.addStr("Nylar");
		planets.addStr("Vinci");
		planets.addStr("Aldebaran");
		planets.addStr("Algol");
		planets.addStr("Altair");
		planets.addStr("Antares");
		planets.addStr("Betelgeuse");
		planets.addStr("Folfanga");
		planets.addStr("Frogstar");
		planets.addStr("Hastromil");
		planets.addStr("Zondostina");
		planets.addStr("Eadrax");
		planets.addStr("Squornshellous");
		planets.addStr("Ysdllodins");
		planets.addStr("Zarss");
		planets.addStr("Bartledan");
		planets.addStr("Damogran");
		planets.addStr("Gagrakacka");
		planets.addStr("Golgafrincham");
		planets.addStr("Hawalius");
		planets.addStr("Krikkiat");
		planets.addStr("Traal");
		planets.addStr("Vod");
		planets.addStr("Xaxis");
		planets.addStr("Zirzla");
		planets.addStr("Flanux");
		planets.addStr("Ariel");
		planets.addStr("Haven");
		planets.addStr("Bellerophon");
		planets.addStr("Persephone");
		planets.addStr("Regina");
		planets.addStr("Uppingham");
		planets.addStr("Rutland");
		planets.addStr("Aberdeen");
		planets.addStr("Kobol");
		planets.addStr("Arkology");
		planets.addStr("Tarazed");
		planets.addStr("Lundmark");
		planets.addStr("Empyrium");
		planets.addStr("Almagest");
		planets.addStr("Pythia");
		planets.addStr("Herwig");
		planets.addStr("Phojet");
		planets.addStr("Geant");
		planets.addStr("Athena");
		planets.addStr("Alpgen");
		planets.addStr("Epos");
		planets.addStr("Charybdis");
		planets.addStr("Baur");
		planets.addStr("Helac");
		planets.addStr("Hijing");
		planets.addStr("Photos");
		planets.addStr("Tauolapp");
		planets.addStr("Minbari");
		planets.addStr("Narn");
		planets.addStr("Vega");
		planets.addStr("Hermethica");
		planets.addStr("Scrantek");
		planets.addStr("Miros ");
		planets.addStr("Felspoon");
		planets.addStr("Chloris");
		planets.addStr("Quinnis");
		planets.addStr("Qualactin");
		planets.addStr("Ribos");
		planets.addStr("Rit");
		planets.addStr("Sarn");
		planets.addStr("Tarsius");
		planets.addStr("Tigella");
		planets.addStr("Umbeka");
		planets.addStr("Vardon");
		planets.addStr("Zil");
		planets.addStr("Vivec");
		planets.addStr("Tel Aruhn");
		planets.addStr("Khuul");
		planets.addStr("Gnsis");
		planets.addStr("Balmora");
		planets.addStr("Holamayan");
		planets.addStr("Leyawiin");
		planets.addStr("Cheydinhal");
		planets.addStr("Chorrol");
		planets.addStr("Kvatch");
		planets.addStr("Bruma");
		planets.addStr("Markarth");
		planets.addStr("Riften");
		planets.addStr("Agravo");
		planets.addStr("Atlay");
		planets.addStr("Carthenos");
		planets.addStr("Cynra");
		planets.addStr("Disentastra");
		planets.addStr("Epheron");
		planets.addStr("Gedden");
		planets.addStr("Herom");
		planets.addStr("Keldan");
		planets.addStr("Lubus");
		planets.addStr("Morphenniel");
		planets.addStr("Overon");
		planets.addStr("Prim");
		planets.addStr("Sarran");
		planets.addStr("Teal");
		planets.addStr("Zerok");
		planets.addStr("Antica");
		planets.addStr("Betazed");
		planets.addStr("Camus");
		planets.addStr("Ferenginar");
		planets.addStr("Gallitep");
		planets.addStr("Khitomer");
		planets.addStr("Nimbus");
		planets.addStr("Pollux");
		planets.addStr("Praxis");
		planets.addStr("Psi");
		planets.addStr("Sarpeidon");
		planets.addStr("Tellar");
		planets.addStr("Torad");
		planets.addStr("Rigel");
		planets.addStr("Mustafar");
		planets.addStr("Bespin");
		planets.addStr("Coruscant");
		planets.addStr("Hoth");
		planets.addStr("Despayre");
		planets.addStr("Ecaz");
		planets.addStr("Ix");
		planets.addStr("Richese");
		planets.addStr("Caladan");
		planets.addStr("Cykranosh");
		planets.addStr("Fomalhaut");
		planets.addStr("Ktynga");
		planets.addStr("Mthura");
		planets.addStr("Shaggai");
		planets.addStr("Xentilx");
		planets.addStr("Yarnak");
		planets.addStr("Solaria");
		planets.addStr("Alaspin");
		planets.addStr("Blasusarr");
		planets.addStr("Moth");
		planets.addStr("Repler");
		planets.addStr("Fiji");
		planets.addStr("Hebron");
		planets.addStr("Whirl");
		planets.addStr("Gummidgy");
		planets.addStr("Felicity");
		planets.addStr("Lalonde");
		planets.addStr("Norfolk");
		planets.addStr("Ramen");
		planets.addStr("Hijonda");
		planets.addStr("Oncier");
		planets.addStr("Alturas");
		planets.addStr("Galt");
		planets.addStr("Comptor");
		planets.addStr("Braddox");
		planets.addStr("Isperos");
		planets.addStr("Franconia");
		planets.addStr("Ni");
		planets.addStr("Meh");
		planets.addStr("Rajapar");
		planets.addStr("Dularix");
		planets.addStr("Eldora");
		planets.addStr("Canterbury");
		planets.addStr("Springvale");
		planets.addStr("Corrin");
		planets.addStr("Gamont");
		planets.addStr("Hagal");
		planets.addStr("Lampadas");
		planets.addStr("Palma");
		planets.addStr("Synchrony");
		planets.addStr("Zanovar");
		planets.addStr("Saint Genis");
		planets.addStr("Ferney");
		planets.addStr("Brimingham");
		planets.addStr("Coventry");

	
		
		fornames.addStr("Farquar");
		fornames.addStr("Cuthbert");
		fornames.addStr("Sybil");
		fornames.addStr("Cynthia");
		fornames.addStr("Horatio");
		fornames.addStr("Giles");
		fornames.addStr("Pamela");
		fornames.addStr("Reginald");
		
		//Star colours 
		starColour.addCol( new Color(1f/255f * 155f, 1f/255f * 176f, 1f/255f * 255f, 1f)); //O
		starColour.addCol( new Color(1f/255f * 170f, 1f/255f * 191f, 1f/255f * 255f, 1f)); //B
		starColour.addCol( new Color(1f/255f * 202f, 1f/255f * 215f, 1f/255f * 255f, 1f)); //A
		starColour.addCol( new Color(1f/255f * 248f, 1f/255f * 247f, 1f/255f * 255f, 1f)); //F
		starColour.addCol( new Color(1f/255f * 255f, 1f/255f * 244f, 1f/255f * 234f, 1f)); //G
		starColour.addCol( new Color(1f/255f * 255f, 1f/255f * 210f, 1f/255f * 161f, 1f)); //K
		starColour.addCol( new Color(1f/255f * 255f, 1f/255f * 204f, 1f/255f * 111f, 1f)); //M
		starColour.addCol( new Color(1f/255f * 255f, 1f/255f * 204f, 1f/255f * 111f, 1f)); //M
		starColour.addCol( new Color(1f/255f * 255f, 1f/255f * 169f, 1f/255f * 45f , 1f)); //B-V
		starColour.addCol( new Color(1f/255f * 255f, 1f/255f * 82f , 1f/255f * 0f  , 1f)); //B-V


		Gdx.app.log("AdLib","There are:"+planets.getSize()+" planets,  "+fornames.getSize()+" fornames");


	}
}

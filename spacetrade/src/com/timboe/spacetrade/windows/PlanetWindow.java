package com.timboe.spacetrade.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.utility.Help;
import com.timboe.spacetrade.utility.ScreenFade;

public class PlanetWindow {

	private static Window planetWindow;
	
	private static Label size;
	private static Label gov;
	private static Label civ;
	private static Label name;
	private static Label special;
	private static Label police;
	private static Label pirate;
	private static Label trader;
	
	private static ImageButton IButtonGov;
	private static ImageButton IButtonCiv;
	private static ImageButton IButtonSize;
	private static ImageButton IButtonSpecial;
	private static ImageButton IButtonPolice;
	private static ImageButton IButtonPirates;
	private static ImageButton IButtonTrader;

	private static TextButton bank;
	private static TextButton quest;

	private static Image divider0;
	private static Image divider1;
	private static Image divider2;

	public static boolean doFadeIn = false;

	
	public static Window getWindow() {
		populateWindow();
		return planetWindow;
	}
	
	public static void fadeIn() {
		Gdx.app.log("FadeIn", "in fade in");
		
		name.addAction( Actions.fadeOut( 0f ) );
		name.act(1);
		size.addAction( Actions.fadeOut( 0f ) );
		size.act(1);
		civ.addAction( Actions.fadeOut( 0f ) );
		civ.act(1);
		gov.addAction( Actions.fadeOut( 0f ) );
		gov.act(1);
		special.addAction( Actions.fadeOut( 0f ) );
		special.act(1);
		IButtonGov.addAction( Actions.fadeOut( 0f ) );
		IButtonGov.act(1);
		IButtonCiv.addAction( Actions.fadeOut( 0f ) );
		IButtonCiv.act(1);
		IButtonSize.addAction( Actions.fadeOut( 0f ) );
		IButtonSize.act(1);
		IButtonSpecial.addAction( Actions.fadeOut( 0f ) );
		IButtonSpecial.act(1);
		IButtonPirates.addAction( Actions.fadeOut( 0f ) );
		IButtonPirates.act(1);
		IButtonPolice.addAction( Actions.fadeOut( 0f ) );
		IButtonPolice.act(1);
		IButtonTrader.addAction( Actions.fadeOut( 0f ) );
		IButtonTrader.act(1);		
		pirate.addAction( Actions.fadeOut( 0f ) );
		pirate.act(1);	
		police.addAction( Actions.fadeOut( 0f ) );
		police.act(1);	
		trader.addAction( Actions.fadeOut( 0f ) );
		trader.act(1);
		divider0.addAction( Actions.fadeOut( 0f ) );
		divider0.act(1);
		divider1.addAction( Actions.fadeOut( 0f ) );
		divider1.act(1);	
		divider2.addAction( Actions.fadeOut( 0f ) );
		divider2.act(1);	
		bank.addAction( Actions.fadeOut( 0f ) );
		bank.act(1);	
		quest.addAction( Actions.fadeOut( 0f ) );
		quest.act(1);	
		
		name.addAction( 			Actions.delay( 0.5f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		divider0.addAction( 		Actions.delay( 1.0f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		size.addAction( 			Actions.delay( 1.5f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		IButtonSize.addAction( 		Actions.delay( 1.5f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		civ.addAction( 				Actions.delay( 2.0f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		IButtonCiv.addAction( 		Actions.delay( 2.0f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		gov.addAction( 				Actions.delay( 2.5f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		IButtonGov.addAction( 		Actions.delay( 2.5f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		special.addAction( 			Actions.delay( 3.0f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		IButtonSpecial.addAction( 	Actions.delay( 3.0f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		divider1.addAction( 		Actions.delay( 3.5f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		IButtonPolice.addAction( 	Actions.delay( 4.0f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		police.addAction( 			Actions.delay( 4.0f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		IButtonPirates.addAction( 	Actions.delay( 4.5f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		pirate.addAction( 			Actions.delay( 4.5f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		IButtonTrader.addAction( 	Actions.delay( 5.0f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		trader.addAction( 			Actions.delay( 5.0f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		divider2.addAction( 		Actions.delay( 5.5f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		bank.addAction( 			Actions.delay( 6.0f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		quest.addAction( 			Actions.delay( 6.5f*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
	}
	
	public static void populateWindow() {
		Skin _skin = Textures.getSkin();
	
		
		planetWindow = new Window("", _skin.get("transparent", WindowStyle.class));
		planetWindow.defaults().pad(10).left();
		planetWindow.setMovable(false);
		planetWindow.debug();
		
		name = new Label(Player.getPlanet().getName(), _skin.get("large", LabelStyle.class));
		planetWindow.add( name ).colspan(2);
		planetWindow.row();
		
		divider0 = new Image(_skin.getDrawable("default-splitpane-vertical"));
		planetWindow.add( divider0 ).colspan(2).fillX();
		planetWindow.row();
		
		IButtonSize = new ImageButton(_skin.get("info", ImageButtonStyle.class));
		IButtonSize.addCaptureListener( new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("InfoButton","Interact:"+event.toString());
				Help.help( Player.getPlanet().getSize() );
			}
		});
		planetWindow.add(IButtonSize);
		size = new Label("Size: "+Player.getPlanet().getSize().toString(), _skin);
		planetWindow.add( size );
		planetWindow.row();

		
		IButtonCiv = new ImageButton(_skin.get("info", ImageButtonStyle.class));
		IButtonCiv.addCaptureListener( new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("InfoButton","Interact:"+event.toString());
				Help.help( Player.getPlanet().getCiv() );
			}
		});
		planetWindow.add(IButtonCiv);
		civ = new Label("Civilisation: "+Player.getPlanet().getCiv().name(), _skin);
		planetWindow.add( civ );
		planetWindow.row();

		IButtonGov = new ImageButton(_skin.get("info", ImageButtonStyle.class));
		IButtonGov.addCaptureListener( new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("InfoButton","Interact:"+event.toString());
				Help.help( Player.getPlanet().getGov() );
			}
		});
		planetWindow.add(IButtonGov);
		gov = new Label("Government: "+Player.getPlanet().getGov().toString(), _skin);
		planetWindow.add( gov );
		planetWindow.row();

		IButtonSpecial = new ImageButton(_skin.get("info", ImageButtonStyle.class));
		IButtonSpecial.addCaptureListener( new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("InfoButton","Interact:"+event.toString());
				Help.help( Player.getPlanet().getSpecial() );
			}
		});
		planetWindow.add(IButtonSpecial);
		special = new Label("Special Events: "+Player.getPlanet().getSpecial().toString(), _skin);
		planetWindow.add( special );
		planetWindow.row();
		
		divider1 = new Image(_skin.getDrawable("default-splitpane-vertical"));
		planetWindow.add( divider1 ).colspan(2).fillX();
		planetWindow.row();
		
		IButtonPolice = new ImageButton(_skin.get("info", ImageButtonStyle.class));
		IButtonPolice.addCaptureListener( new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("InfoButton","Interact:"+event.toString());
				Help.help( Player.getPlanet().getActivity(ShipTemplate.Police), ShipTemplate.Police);
			}
		});
		planetWindow.add(IButtonPolice);
		police = new Label("Police: "+Player.getPlanet().getActivity(ShipTemplate.Police).toString(), _skin);
		planetWindow.add( police );
		planetWindow.row();
		
		IButtonPirates = new ImageButton(_skin.get("info", ImageButtonStyle.class));
		IButtonPirates.addCaptureListener( new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("InfoButton","Interact:"+event.toString());
				Help.help( Player.getPlanet().getActivity(ShipTemplate.Pirate), ShipTemplate.Pirate );
			}
		});
		planetWindow.add(IButtonPirates);
		pirate = new Label("Pirates: "+Player.getPlanet().getActivity(ShipTemplate.Pirate).toString(), _skin);
		planetWindow.add( pirate );
		planetWindow.row();
		
		IButtonTrader = new ImageButton(_skin.get("info", ImageButtonStyle.class));
		IButtonTrader.addCaptureListener( new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("InfoButton","Interact:"+event.toString());
				Help.help( Player.getPlanet().getActivity(ShipTemplate.Trader), ShipTemplate.Trader );
			}
		});
		planetWindow.add(IButtonTrader);
		trader = new Label("Traders: "+Player.getPlanet().getActivity(ShipTemplate.Trader).toString(), _skin);
		planetWindow.add( trader );
		planetWindow.row();
		
		divider2 = new Image(_skin.getDrawable("default-splitpane-vertical"));
		planetWindow.add( divider2 ).colspan(2).fillX();
		planetWindow.row();
		
		bank = new TextButton("BANK", _skin.get("large", TextButtonStyle.class));
		bank.addCaptureListener( new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//TODO
			}
		});
		planetWindow.add( bank ).colspan(2);
		planetWindow.row();
		
		quest = new TextButton("SPECIAL", _skin.get("large", TextButtonStyle.class));
		quest.addCaptureListener( new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//TODO
			}
		});
		planetWindow.add( quest ).colspan(2);
		planetWindow.row();
		
		if (doFadeIn == true) {
			doFadeIn = false;
			fadeIn();
		}
		
	}
	
	
}

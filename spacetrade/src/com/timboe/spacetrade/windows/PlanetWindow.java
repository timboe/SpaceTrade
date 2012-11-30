package com.timboe.spacetrade.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
	
	private static ImageButton IButtonGov;
	private static ImageButton IButtonCiv;
	private static ImageButton IButtonSize;
	private static ImageButton IButtonSpecial;

	
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
		
		name.addAction( 			Actions.delay( 1*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		size.addAction( 			Actions.delay( 2*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		IButtonSize.addAction( 		Actions.delay( 2*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		civ.addAction( 				Actions.delay( 3*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		IButtonCiv.addAction( 		Actions.delay( 3*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		gov.addAction( 				Actions.delay( 4*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		IButtonGov.addAction( 		Actions.delay( 4*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		special.addAction( 			Actions.delay( 5*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );
		IButtonSpecial.addAction( 	Actions.delay( 5*ScreenFade.speed,  Actions.fadeIn( 3*ScreenFade.speed ) ) );

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
		
		fadeIn();

	}
	
	
}

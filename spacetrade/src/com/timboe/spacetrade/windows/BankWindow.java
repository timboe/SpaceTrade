package com.timboe.spacetrade.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.screen.PlanetScreen;

public class BankWindow {
	
	public static final float insuranceRate = 0.001f;
	public static final int costEscapePod = 5000;
	public static final int costDiscountPod = costEscapePod/2;
	
	public static final float overdraftAmount = 0.1f;// of total worth
	public static final float authODRate = 0.05f;
	public static final float nonAuthODRate = 0.15f;

	private static final Window containerWindow = new Window("", Textures.getSkin().get("transparent", WindowStyle.class));
	private static final Window bankWindow = new Window("OmniCredit Bank", Textures.getSkin());
	private static final Window insuranceWindow = new Window("OmniCov Insurance", Textures.getSkin());
	private static final TextButton buyPod = new TextButton("", Textures.getSkin().get("large", TextButtonStyle.class));
	private static final TextButton buySellIns = new TextButton("", Textures.getSkin().get("large", TextButtonStyle.class));
	private static final TextButton changeOD = new TextButton("Change Overdraft", Textures.getSkin().get("large", TextButtonStyle.class));
	private static final Label insTxt = new Label("",Textures.getSkin());  
	private static final Label bnkTxt = new Label("",Textures.getSkin());  
	private static final Label insDiscTxt = new Label("",Textures.getSkin()); 
	private static final Label odAmount = new Label("",Textures.getSkin()); 
	private static final ImageButton closeBank = new ImageButton(Textures.getSkin().get("cancel",ImageButtonStyle.class));
	private static final ImageButton closeIns = new ImageButton(Textures.getSkin().get("cancel",ImageButtonStyle.class));
	private static final Slider odSlider = new Slider(0, 1, 1, false, Textures.getSkin() ); //set slider

	private static ChangeListener closeClick;
	
	private static boolean windowPopulated = false;
	
	public static Window getWindow() {
		if (windowPopulated == true) {
			update(true);
			return containerWindow;
		}
		windowPopulated = true;
		populateWindow();
		return containerWindow;
	}
	
	private static void doChangeOD() {
		update(true);
	}
	
	private static void doBuyPod() {
		
		if (Player.getAvailableCredzIncOD() < costDiscountPod) { //can afford neither
			notEnoughCash();
		} else if (Player.getAvailableCredzIncOD() >= costEscapePod) { //can afford both
			
			new Dialog("Buying OmniCov Escape Pod", Textures.getSkin(), "dialog") {
				protected void result (Object object) {
					if ( ((Integer)object) == 1 ) {
						givePod();
					} else if ( ((Integer)object) == 2 ) {
						startInsurance();
					}
				}
			}.text("\nAre you sure you only want the OmniCov Escape Pod\nYou can get it half price with our insurance policy.\n ")
				.button("Just Buy Escape Pod", new Integer(1), Textures.getSkin().get("large", TextButtonStyle.class))
				.button("Buy Insurance + Pod", new Integer(2), Textures.getSkin().get("large", TextButtonStyle.class))
				.button("Cancel", new Integer(3), Textures.getSkin().get("large", TextButtonStyle.class))
				.key(Keys.ENTER, true)
				.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
			
		} else { //can afford only the deal
			
			new Dialog("Buying OmniCov Escape Pod", Textures.getSkin(), "dialog") {
				protected void result (Object object) {
					if ( ((Boolean)object) == true ) {
						startInsurance();
					}
				}
			}.text("\nYou cannot afford to buy an OmniCov Escape Pod\n" +
					"\nBut you can afford to take advantage of our fantastic offer." +
					"\nYou can get your Escape Pod at half price by taking out our insurance policy.\n ")
				.button("Buy Insurance + Escape Pod", true, Textures.getSkin().get("large", TextButtonStyle.class))
				.button("No Thanks", false, Textures.getSkin().get("large", TextButtonStyle.class))
				.key(Keys.ENTER, true).key(Keys.ESCAPE, false)
				.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
			
		}
			

	}
	
	private static void doBuySellIns() {
		if (Player.getInsured() == false) {
			//BUY
			
			if (Player.getShip().getEscapePod() == false && Player.getAvailableCredzIncOD() < costDiscountPod) {
				notEnoughCash();
			} else {
				new Dialog("Insurance Bought", Textures.getSkin(), "dialog") {
					protected void result (Object object) {
						if ( ((Boolean)object) == true ) {
							startInsurance();
						}
					}
				}.text("\nAn excellent choice.\nYour coverage will start right away."
						+"\nPremiums are deducted automatically upon docking with any space port."
						+"\nTravel the galaxy safe in the knowledge that you are OmniCovered!\n ")
					.button("Great!", true, Textures.getSkin().get("large", TextButtonStyle.class))
					.button("Cancel Insurance Purchase", false, Textures.getSkin().get("large", TextButtonStyle.class))
					.key(Keys.ENTER, true).key(Keys.ESCAPE, false)
					.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
			
			}
		
		} else {
			
			//STOP INSURANCE
			if (Player.getInsurancePaid() >= (costDiscountPod+costEscapePod)) {
				//OK To stop
				new Dialog("Stop Insurance?", Textures.getSkin(), "dialog") {
					protected void result (Object object) {
						if ( ((Boolean)object) == true ) {
							stopInsurance();
						}
					}
				}.text("\nAre you sure, you will loose your no-claims bonus!!\n ")
					.button("Cancel Insurance!", true, Textures.getSkin().get("large", TextButtonStyle.class))
					.button("Keep Insurance", false, Textures.getSkin().get("large", TextButtonStyle.class))
					.key(Keys.ENTER, true).key(Keys.ESCAPE, false)
					.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
			
			} else {
				//NEED TO PAY MORE
				final int _buyout = (costDiscountPod + costEscapePod) - Player.getInsurancePaid();
				String _msg = "\nYou have not yet reached your minimum contract."
						+"\n\nYou can buy out of your contract for $" + _buyout + ".\n ";
				if (_buyout > Player.getAvailableCredzIncOD()) {
					//Cannot afford
					_msg += "\nUnfortunatly you do not have the required funds to buy yourself\nout at this moment.\n ";
					new Dialog("Buyout Insurance", Textures.getSkin(), "dialog") {
						protected void result (Object object) {
						}
					}.text(_msg)
						.button("  OK  ", true, Textures.getSkin().get("large", TextButtonStyle.class))
						.key(Keys.ENTER, true).key(Keys.ESCAPE, false)
						.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
				} else {
					new Dialog("Stop Insurance?", Textures.getSkin(), "dialog") {
						protected void result (Object object) {
							if ( ((Boolean)object) == true ) {
								Player.modCredz(_buyout * -1);
								stopInsurance();
							}
						}
					}.text(_msg)
						.button("Buy Out of Contract", true, Textures.getSkin().get("large", TextButtonStyle.class))
						.button("Keep Insurance", false, Textures.getSkin().get("large", TextButtonStyle.class))
						.key(Keys.ENTER, true).key(Keys.ESCAPE, false)
						.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
				}
			}
			
		}
	}
	
	private static void notEnoughCash() {
		new Dialog("Not Enough $", Textures.getSkin(), "dialog") {
			protected void result (Object object) {
			}
		}.text("\nYou do not have the funds\nCome back soon, with more $!\n ")
			.button("  OK  ", true, Textures.getSkin().get("large", TextButtonStyle.class))
			.key(Keys.ENTER, true).key(Keys.ESCAPE, true)
			.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
	}
	
	private static void givePod() {
		Player.modCredz(costEscapePod * -1);
		Player.getShip().setEscapePod(true);
		update(true);
	}
	
	private static void startInsurance() {
		if (Player.getShip().getEscapePod() == false) {
			Player.modCredz(costDiscountPod * -1);
			Player.getShip().setEscapePod(true);
			Player.setInsurancePaid(0);
		} else {
			Player.setInsurancePaid(costDiscountPod + costEscapePod); //no need to pay up to quit if already have pod
		}
		Player.setInsured(true);
		Player.setNoClaim(0f);
		update(true);
	}
	
	public static void stopInsurance() {
		Player.setInsured(false);
		Player.setNoClaim(0f);
		update(true);
	}
	
	private static void populateWindow() {
		bankWindow.debug();
		containerWindow.debug();
		insuranceWindow.debug();
		
		closeClick = new  ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("BankCloseButton","Interact:"+event.toString());
				PlanetScreen.showBank = false;
				PlanetScreen.triggerRefresh = true;
			}
		};
		
		buyPod.addListener( new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				doBuyPod();
			}
		});
		
		buySellIns.addListener( new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				doBuySellIns();
			}
		});
		
		changeOD.addListener( new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				doChangeOD();
			}
		});
		
		
		bankWindow.defaults().pad(5).left();
		bankWindow.add(bnkTxt).colspan(4);
		bankWindow.row();		
		bankWindow.add(odSlider).right().height(50);
		bankWindow.add(odAmount);
		bankWindow.add(changeOD);
		bankWindow.add(closeBank).right();
		closeBank.addListener(closeClick);
		
		
		insuranceWindow.defaults().pad(5).left();
		insuranceWindow.add(insTxt);
		insuranceWindow.row();
		insuranceWindow.add(buyPod);
		insuranceWindow.row();
		insuranceWindow.add(buySellIns);
		insuranceWindow.row();
		insuranceWindow.add(insDiscTxt);
		insuranceWindow.row();
		insuranceWindow.add(closeIns).right();
		closeIns.addListener(closeClick);

		containerWindow.defaults().pad(5);
		containerWindow.add(bankWindow);
		containerWindow.row();
		containerWindow.add(insuranceWindow);
		update(true);
	}
	
	public static void update(boolean _full) {
		
		if (_full = false) {
			//odAmount.setText( ((Integer) Math.round(odSlider.getValue())).toString() );
			return;
		}
		
		//Bank
		int _maxOD = Math.max(Player.getOD(), Math.round(Player.getWorth() * overdraftAmount));
		bnkTxt.setText("Based on your circumstances, OmniCredit is prepared to offer you up to $"+_maxOD
				+"\nin authorised overdraft facilities."
				+"\nAuthorised Overdraft Interest: "+Math.round(authODRate*100)+"% per Galactic Year"
				+"\nUnauthorised Overdraft Interest Rate: "+Math.round(nonAuthODRate*100)+"% per Galactic Year"
				+"\nCurrent Overdraft Facility: $"+Player.getOD()); 
		
		odSlider.setRange(0, 10);
		//odSlider.setTouchable(Touchable.enabled);
		//odSlider.setValue(Player.getOD());
		//odAmount.setText( ((Integer) Player.getOD() ).toString() );
		
		//Insurance
		float _premium = Player.getShip().getShipClass().getCost() * insuranceRate;

		if (Player.getInsured() == false) {
			//BUY
			
			String _msg = "Prepared for the worst? Do you have plans for if your ship is destroyed?"
					+"\nOmniCov insurance will re-imburse you the full retail value of your ship!"
					+"\n\nOur great rate means that for your "+Player.getShip().getShipClass().getName()+", "
					+"you will pay only $"+Math.round(_premium)+" per Galactic Year.";
			
			if (Player.getShip().getEscapePod() == false) {
				
				_msg += "\nAnd our Responsable Pilot program "
						+"allows for up to a 75% no-claims discount."
						+"\n\nSpecial offer! Buy now, and get your OmniCov Escape Pod fitted half price*!";
				
				buyPod.setVisible( true );
				buyPod.setText("Buy Escape Pod for $"+costEscapePod);
				buySellIns.setText("Buy Insurance and Escape Pod for $"+costDiscountPod);
				if (Player.getAvailableCredzIncOD() >= costEscapePod) {
					buyPod.setColor(Color.GREEN);
				} else {
					buyPod.setColor(Color.RED);
				}
				if (Player.getAvailableCredzIncOD() >= costDiscountPod) {
					buySellIns.setColor(Color.GREEN);
				} else {
					buySellIns.setColor(Color.RED);
				}
				float _nYears = (float)(costDiscountPod + costEscapePod) / _premium;
				insDiscTxt.setText("*Minumum contract of "+_nYears+" galactic years when taken with discounted Escape Pod.\nAdditional fees payable on early cancellation.");

			} else {
				buyPod.setVisible( false );
				buySellIns.setText("Buy Insurance");
				buySellIns.setColor(Color.WHITE);
				insDiscTxt.setText("");
			}
			
			insTxt.setText(_msg);
			
			
		} else {
			//SELL
			insTxt.setText("Relax! You're in the safe hands of OmniCov.\n\n" +
					"Ship Value Insured: $"+Player.getShip().getShipClass().getCost()+"\n"+
					"Premium (Per Galactic Year): $"+Math.round(_premium)+"\n"+
					"No-Claims Discount: "+(Player.getNoClaims()*100)+"%");
			buyPod.setVisible( false );
			buySellIns.setText("Stop Insurance");	
			insDiscTxt.setText("");
		}
	}
	
	
}

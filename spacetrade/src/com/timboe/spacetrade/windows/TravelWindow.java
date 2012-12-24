package com.timboe.spacetrade.windows;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.timboe.spacetrade.SpaceTrade;
import com.timboe.spacetrade.enumerator.Goods;
import com.timboe.spacetrade.enumerator.OpponentStance;
import com.timboe.spacetrade.enumerator.ShipClass;
import com.timboe.spacetrade.enumerator.ShipTemplate;
import com.timboe.spacetrade.player.Player;
import com.timboe.spacetrade.render.SpaceTradeRender;
import com.timboe.spacetrade.render.Textures;
import com.timboe.spacetrade.screen.PlanetScreen;
import com.timboe.spacetrade.ship.Ship;
import com.timboe.spacetrade.utility.Rnd;
import com.timboe.spacetrade.utility.ScreenFade;

public class TravelWindow {
	private static Window travelWindow = null;
	private static Window optionsWindow = null;
	private static Window youWindow = null;
	private static Label youHull = new Label("HULL : ", Textures.getSkin());
	private static Image youHullGreen = new Image(Textures.getSkin().getPatch("green-bar"));
	private static Image youHullRed = new Image(Textures.getSkin().getPatch("red-bar"));
	private static Label youHullPerc = new Label("  100%", Textures.getSkin().get("default-ol", LabelStyle.class));
	private static Label youShield = new Label("SHIELD : ", Textures.getSkin());
	private static Image youShieldGreen = new Image(Textures.getSkin().getPatch("green-bar"));
	private static Image youShieldRed = new Image(Textures.getSkin().getPatch("red-bar"));
	private static Label youShieldPerc = new Label("  100%", Textures.getSkin().get("default-ol", LabelStyle.class));
	private static Label youHeat = new Label("HEAT : ", Textures.getSkin());
	private static Image youHeatGreen = new Image(Textures.getSkin().getPatch("green-bar"));
	private static Image youHeatRed = new Image(Textures.getSkin().getPatch("red-bar"));
	private static Label youHeatPerc = new Label("  100%", Textures.getSkin().get("default-ol", LabelStyle.class));

	private static Window opponentWindow = null;  
	private static Label opponentHull = new Label("HULL : ", Textures.getSkin());
	private static Image opponentHullGreen = new Image(Textures.getSkin().getPatch("green-bar"));
	private static Image opponentHullRed = new Image(Textures.getSkin().getPatch("red-bar"));
	private static Label opponentHullPerc = new Label("  100%", Textures.getSkin().get("default-ol", LabelStyle.class));
	private static Label opponentShield = new Label("SHIELD : ", Textures.getSkin());
	private static Image opponentShieldGreen = new Image(Textures.getSkin().getPatch("green-bar"));
	private static Image opponentShieldRed = new Image(Textures.getSkin().getPatch("red-bar"));
	private static Label opponentShieldPerc = new Label("  100%", Textures.getSkin().get("default-ol", LabelStyle.class));
	private static Label opponentHeat = new Label("HEAT : ", Textures.getSkin());
	private static Image opponentHeatGreen = new Image(Textures.getSkin().getPatch("green-bar"));
	private static Image opponentHeatRed = new Image(Textures.getSkin().getPatch("red-bar"));
	private static Label opponentHeatPerc = new Label("  100%", Textures.getSkin().get("default-ol", LabelStyle.class));
	
	private static Label distance = new Label("DISTANCE", Textures.getSkin());
	private static Label action = new Label("ACTION", Textures.getSkin());
	private static TextButton buttonAttack = new TextButton("ATTACK", Textures.getSkin().get("large", TextButtonStyle.class));
	private static TextButton buttonFlee = new TextButton("FLEE", Textures.getSkin().get("large", TextButtonStyle.class));
	private static TextButton buttonSubmit = new TextButton("SUBMIT", Textures.getSkin().get("large", TextButtonStyle.class));
	private static TextButton buttonBribe = new TextButton("BRIBE", Textures.getSkin().get("large", TextButtonStyle.class));
	private static ChangeListener clickListener;
	
	private static Rnd rnd = new Rnd();
	
	private static boolean isGenerated = false;
		
	public static Window getWindow() {
		if (isGenerated == false) {
			populateWindow();
			isGenerated = true;
		}
		travelWindow.addAction(Actions.fadeIn(0));
		travelWindow.act(1);
		return travelWindow;
	}
	
	public static void fadeOut() {
		SequenceAction fadeSequence = new SequenceAction();
		fadeSequence.addAction( Actions.fadeOut(0.5f) );
		fadeSequence.addAction( Actions.run(new Runnable() {
	        public void run () {
				PlanetScreen.triggerRefresh = true;
				PlanetWindow.doFadeIn = true;
	        }
		}
	    ));
		travelWindow.addAction( fadeSequence );
	}
	
	public static void attack() {
		String _msg = " ";
		String _title = "";
		if (Player.getShip().getFilledWeaponSlots() == 0) {
			new Dialog("No Weapons", Textures.getSkin(), "dialog") {
				protected void result (Object object) {
				}
			}.text("\nYou do not have any weapons to attack with!\n ")
				.button("  OK  ", true, Textures.getSkin().get("large", TextButtonStyle.class))
				.key(Keys.ENTER, true).key(Keys.ESCAPE, true)
				.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
			return;
		} else if (true) {	//TODO add if clean police record
			if (PlanetScreen.getEncounter().getTemplate() == ShipTemplate.Police) {
				_msg = "\nAre you sure you want to do this?";
				if (Player.getCarryIllegal() == false) { 
					_msg += "\n\nYou are not carrying any illegal goods, and do not have to fear an inspection.";
				}
				_msg += "\n\nAttacking the police will make you a criminal.\n ";
				_title = "Messing With The Law";
			} else if (PlanetScreen.getEncounter().getTemplate() == ShipTemplate.Trader) {
				_msg = "\nAre you sure you want to do this?";
				_msg += "\n\nAttacking a trader will attract InterGal police attention.\n ";
				_title = "Avast!";
			}
		}
			
		if (_msg.hashCode() != " ".hashCode()) {
			new Dialog(_title, Textures.getSkin(), "dialog") {
				protected void result (Object object) {
					if ((Boolean)object == true) {
						++PlanetScreen.turn;
						maintenance();
						doAttack(true);
					}
				}
			}.text(_msg)
				.button(" Attack! ", true, Textures.getSkin().get("large", TextButtonStyle.class))
				.button(" Stand Down  ", false, Textures.getSkin().get("large", TextButtonStyle.class))
				.key(Keys.ENTER, true).key(Keys.ESCAPE, false)
				.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
		} else {
			++PlanetScreen.turn;
			maintenance();
			doAttack(true);
		}		
	}
	
	public static void doAttack(boolean isPlayerAttacking) {
		Ship me = Player.getShip();
		Ship them = PlanetScreen.getEncounter();
		if (isPlayerAttacking == false) {
			me = PlanetScreen.getEncounter();
			them = Player.getShip();
		}
		
		boolean isKilled = me.sendAttack( them );
		
		if (isKilled == true && isPlayerAttacking == true) {
			int _bonus = 0;
			final Goods _bonusG = Goods.random();
			if (rnd.getRandChance(0.75f) == true) { //TODO tweak
				_bonus = 2 + rnd.getRandI(4);
			}
			String _msg = "\n Your opponents ship has been destroyed!";
			if (_bonus > 0) {
				_msg += "\n\n" + _bonus + " crates of " + _bonusG.toDisplayString() + " drift within range of your grapple.";
			}
			
			if (_bonus > 0 && Player.getFreeCargo() == 0) {
				_bonus = 0;
				_msg += "\n\nYou have no free cargo bays however! So you can only watch them drift off into space.";
			} else if (_bonus > 0 && Player.getFreeCargo() >= _bonus) {
				_msg += "\n\nDo you want to bring them aboard?";
			} else if (_bonus > 0) {
				_bonus = Player.getFreeCargo();
				_msg += "\n\nYou do not have enough free cargo bays to store them all, but could collect "+_bonus+".";
				_msg += "\n\nDo you want to bring them aboard?";
			}
			
			final int _bonusFinal = _bonus;
			new Dialog("Victory", Textures.getSkin(), "dialog") {
				protected void result (Object object) {
					if ((Boolean)object == true) Player.addStock(_bonusG, _bonusFinal, 0);
					PlanetScreen.endEncounter();
				}
			}.text(_msg+"\n ")
				.button("   YES   ", true, Textures.getSkin().get("large", TextButtonStyle.class))
				.button("   NO   ", true, Textures.getSkin().get("large", TextButtonStyle.class))
				.key(Keys.ENTER, true).key(Keys.ESCAPE, false)
				.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
			
		} else if (isKilled == true && isPlayerAttacking == false) {
			
			String _msg = "\nYour ship explodes!";
			//do we have escape pod?
			if (Player.getShip().getEscapePod() == true) {
				_msg += "\n\nYou just have time to make it into your escape pod"
						+"\na few hours later you crash land into "+Player.getPlanet().getName()
						+"\n\nOver the course of two days you convert your escape pod into a "+ShipClass.Tiny.getName()+"\n ";
				if (Player.getInsured() == true) {
					_msg += "\nThe bank pays out $"+Player.getInsurancePayout()+" in insurance,\nyou should take out a new insurance policy when you buy your next ship.";
				}
			} else {
				_msg += "\n\nAs you have no escape pod, you too are vaporised into a puff of carbon."
						+"\n\nYour adventure ends here.\n ";
			}
			
			new Dialog("BOOM!", Textures.getSkin(), "dialog") {
				protected void result (Object object) {
					if (Player.getShip().getEscapePod() == false) {
						Player.setDead(true);
						ScreenFade.changeScreen( SpaceTrade.getSpaceTrade().theGameOver );
					} else if (Player.getInsured() == true) {
						PlanetScreen.setTocks(0);
						Player.modCredz( Player.getInsurancePayout() );
						Player.setNoClaim( 0f );
						Player.setInsured(false);
						Player.setShip( new Ship(ShipClass.Tiny) );
						Player.removeAllStock();
					}
				}
			}.text(_msg)
				.button("   OK   ", true, Textures.getSkin().get("large", TextButtonStyle.class))
				.key(Keys.ENTER, true).key(Keys.ESCAPE, true)
				.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
			
		} else if (isPlayerAttacking == true) {
			//Enemy's turn
			doEnemyCounter();
		}
		
	}
	
	private static void doEnemyCounter() {
		Ship enemy = PlanetScreen.getEncounter();
		if (enemy == null) return;

		if (enemy.getStance() == OpponentStance.Attack) {
			if (enemy.getMinHeatPerFire() <= enemy.getRemainingHeat()) {
				doAttack(false);
			}
		} else if (enemy.getStance() == OpponentStance.Flee) {
			//do I escape?
			//TODO tweak chance
			if (rnd.getRandChance(0.5f) == true) {
				PlanetScreen.combatLog.add(Player.getPlayerName()+"[Piloting] Your oponent has fled from battle!");
				new Dialog("Early Exit", Textures.getSkin(), "dialog") {
					protected void result (Object object) {
						PlanetScreen.endEncounter();
					}
				}.text("\nYour opponent has fled.\n ")
					.button("  OK   ", true, Textures.getSkin().get("large", TextButtonStyle.class))
					.key(Keys.ENTER, true).key(Keys.ESCAPE, true)
					.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
			} else {
				PlanetScreen.combatLog.add(Player.getPlayerName()+"[Piloting] Your oponent FAILS to flee from battle.");
			}
		}
		
		//all actions done,
		cooldown();		
	}

	public static void flee() {
		++PlanetScreen.turn;
		maintenance();
		if (rnd.getRandChance(0.5f) == true) { //flee true //TODO more sophisticated
			String _msg = "";
			if (rnd.getRandChance(0.5f) == true) { //TODO more sophisticated
				doEnemyCounter();
				if (Player.getDead() == true) return;
				PlanetScreen.combatLog.add(Player.getPlayerName()+"[Piloting] Your attempt to flee SUCEEDED but you were hit.");
				_msg = "\nYou were hit, but you managed to get away.\n ";
			} else {
				PlanetScreen.combatLog.add(Player.getPlayerName()+"[Piloting] Your attempt to flee SUCEEDED.");
				_msg = "\nYou manage to escape.\n ";
			}
			new Dialog("Early Exit", Textures.getSkin(), "dialog") {
				protected void result (Object object) {
					PlanetScreen.endEncounter();
				}
			}.text(_msg)
				.button("  OK   ", true, Textures.getSkin().get("large", TextButtonStyle.class))
				.key(Keys.ENTER, true).key(Keys.ESCAPE, true)
				.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
		} else { //flee fail 
			PlanetScreen.combatLog.add(Player.getPlayerName()+"[Piloting] Your attempt to flee FAILED.");
			doEnemyCounter();
		}		
	}
	
	public static void submit() {
		//TODO check for criminal -> fine and imprison
		if (Player.getCarryIllegal() == true) {
			final int _fine = Math.round( Player.getAvailableCredzIncOD() * 0.05f ); //TODO change this to worth
			final int _nCrack = Player.getStock(Goods.SpaceCrack);
			final int _nAI = Player.getStock(Goods.AI);
			String _msg  = "\nUpon inspecting your cargo hold the authoraties discover illegal materials."
					+ "\nThis has been noted on your criminal record"
					+ "\n\nThey confiscate:";
			if (_nCrack > 0) {
				_msg += "\n" + _nCrack + " " + Goods.SpaceCrack.toDisplayString();
			}
			if (_nAI > 0) {
				_msg += "\n" + _nAI + " " + Goods.AI.toDisplayString();
			}
			_msg += "\n\nAnd fine you $"+_fine+"\n ";
			new Dialog("Illegal Goods", Textures.getSkin(), "dialog") {
				protected void result (Object object) {
					Player.modCredz( -_fine );
					Player.removeStock(Goods.SpaceCrack, _nCrack);
					Player.removeStock(Goods.AI, _nAI);
					PlanetScreen.endEncounter();
				}
			}.text(_msg)
				.button("  OK  ", true, Textures.getSkin().get("large", TextButtonStyle.class))
				.key(Keys.ENTER, true).key(Keys.ESCAPE, true)
				.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
			
		} else {
			String _msg  = "\nThe officers find nothing untoward in your cargo bays.\n\nThey appologise for your inconvienience and let you continue your journey.\n ";
			new Dialog("Law and Order", Textures.getSkin(), "dialog") {
				protected void result (Object object) {
					PlanetScreen.endEncounter();
				}
			}.text(_msg)
				.button("  OK   ", true, Textures.getSkin().get("large", TextButtonStyle.class))
				.key(Keys.ENTER, true).key(Keys.ESCAPE, true)
				.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
			}
	}
	
	public static void trade() {
		final Goods _g = Goods.random();
		int _amount = rnd.getRandI(9) + 2;
		int _price = Math.round( Player.getPlanet().getPrice(_g) * ( rnd.getRandF() + 0.25f ) );
		String _msg = "\nThe trader offers to sell you "+_amount+" "+_g.toDisplayString()+"\nfor "+_price+" each.";
		
		if (_amount > Player.getFreeCargo()) {
			_amount = Player.getFreeCargo();
			if (_amount == 0) {
				_msg += "\n\nYou cannot buy any as you have no cargo bays to store them!";
			} else {
				_msg += "\n\nYou do not have the storage space for all these\n, you have room for "+_amount+".";
			}

		}
		
		if (_price * _amount > Player.getAvailableCredz()) {
			_amount =  Player.getAvailableCredz() / _price ;
			if (_amount == 0) {
				_msg += "\n\nYou cannot buy any as you can't afford them!";
			} else {
				_msg += "\n\nThough you cannot afford to buy this many, you can afford "+_amount+".";
			}
		}
		_msg += "\n ";
		
		final int _fAmount = _amount;
		final int _fPrice = _price;
		
		if (_fAmount > 0) {
			new Dialog("Buisness Dealings", Textures.getSkin(), "dialog") {
				protected void result (Object object) {
					if ((Boolean)object == true) {
						Player.modCredz( -(_fPrice * _fAmount) );
						Player.addStock(_g, _fAmount, _fPrice);
					}
					PlanetScreen.endEncounter();
				}
			}.text(_msg)
				.button(" Buy "+_fAmount+" ", true, Textures.getSkin().get("large", TextButtonStyle.class))
				.button("   Decline   ", false, Textures.getSkin().get("large", TextButtonStyle.class))
				.key(Keys.ENTER, true).key(Keys.ESCAPE, false)
				.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
		} else {
			new Dialog("Buisness Dealings", Textures.getSkin(), "dialog") {
				protected void result (Object object) {
					PlanetScreen.endEncounter();
				}
			}.text(_msg)
				.button("  OK  ", true, Textures.getSkin().get("large", TextButtonStyle.class))
				.key(Keys.ENTER, true).key(Keys.ESCAPE, false)
				.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);	
		}
	}
	
	public static void bribe() {
		if (Player.getPlanet().getGov().getBribeRate() == 0) {
			String _msg = "\nThe police here take a very negative view to corruption.\n\nThey insist that they be let aboard to search the ship.\n ";
			new Dialog("Bribe Failed", Textures.getSkin(), "dialog") {
				//Nothing happens
			}.text(_msg)
				.button("   OK   ", true, Textures.getSkin().get("large", TextButtonStyle.class))
				.key(Keys.ENTER, true).key(Keys.ESCAPE, true)
				.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
		} else {
			final int _bribe = Math.round( Player.getWorth() * Player.getPlanet().getGov().getBribeRate() );
			String _msg = "\nThe police could be persuaded to look the other way just this once, for $"+_bribe+".\n ";
			if (Player.getAvailableCredzIncOD() >= _bribe) {
				new Dialog("Bribery and Corruption", Textures.getSkin(), "dialog") {
					protected void result (Object object) {
						if ((Boolean)object == true) {
							Player.modCredz( -_bribe );
							PlanetScreen.endEncounter();
						}
						//Else nothing happens
					}
				}.text(_msg)
					.button(" Pay The Bribe ", true, Textures.getSkin().get("large", TextButtonStyle.class))
					.button("   Don't Pay   ", false, Textures.getSkin().get("large", TextButtonStyle.class))
					.key(Keys.ENTER, true).key(Keys.ESCAPE, false)
					.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
			} else {
				_msg += "\nYou do not however have access to this many funds.\n ";
				new Dialog("Bribery and Corruption", Textures.getSkin(), "dialog") {
					protected void result (Object object) {
					}
				}.text(_msg)
					.button("  OK  ", true, Textures.getSkin().get("large", TextButtonStyle.class))
					.key(Keys.ENTER, true).key(Keys.ESCAPE, true)
					.show(((SpaceTradeRender)SpaceTrade.getSpaceTrade().getScreen()).getStage()).getContentTable().defaults().pad(10);
			}
		}
	}
	
	public static void surrender() { //TODO
		
	}
	
	public static void plunder() { //TODO
		
	}

	public static void updateList() {
		distance.setText("At " + PlanetScreen.getTocksStr() 
				+ " from " + Player.getPlanet().getName() 
				+ ", you encounter a " + PlanetScreen.getEncounter().getMod().toString()
				+ " " + PlanetScreen.getEncounter().getTemplate().toString() 
				+ " " + PlanetScreen.getEncounter().getShipClass().getName() );	
		OpponentStance _stance = PlanetScreen.getEncounter().getStance();
		switch (_stance) {
		case Attack:
			action.setText("Your opponent attacks.");
			buttonAttack.setText("ATTACK");
			buttonFlee.setText("FLEE");
			buttonSubmit.setText("SURRENDER");
			
			buttonAttack.setVisible(true);
			buttonFlee.setVisible(true);
			buttonSubmit.setVisible(true);
			buttonBribe.setVisible(false);
			break;
		case Flee:
			action.setText("Your opponent is fleeing from you.");
			buttonAttack.setText("ATTACK");
			buttonFlee.setText("IGNORE");
			
			buttonAttack.setVisible(true);
			buttonFlee.setVisible(true);
			buttonSubmit.setVisible(false);
			buttonBribe.setVisible(false);
			break;
		case Ignore:
			action.setText("Your opponent ignores you.");
			buttonAttack.setText("ATTACK");
			buttonFlee.setText("IGNORE");
			
			buttonAttack.setVisible(true);
			buttonFlee.setVisible(true);
			buttonSubmit.setVisible(false);
			buttonBribe.setVisible(false);
			break;
		case OfferTrade:
			action.setText("The trader offers to do buisness with you.");
			buttonAttack.setText("ATTACK");
			buttonFlee.setText("IGNORE");
			buttonSubmit.setText("TRADE");
			
			buttonAttack.setVisible(true);
			buttonFlee.setVisible(true);
			buttonSubmit.setVisible(true);
			buttonBribe.setVisible(false);
			break;
		case RequestInspect:
			action.setText("The police request that you submit your cargo holds for inspection.");
			buttonAttack.setText("ATTACK");
			buttonFlee.setText("FLEE");
			buttonSubmit.setText("SUBMIT");
			buttonBribe.setText("BRIBE");
			
			buttonAttack.setVisible(true);
			buttonFlee.setVisible(true);
			buttonSubmit.setVisible(true);
			buttonBribe.setVisible(true);
			break;
		case Surrender:
			action.setText("Your opponent surrenderes to you!");
			buttonAttack.setText("ATTACK");
			buttonFlee.setText("IGNORE");
			buttonSubmit.setText("PLUNDER");
			
			buttonAttack.setVisible(true);
			buttonFlee.setVisible(true);
			buttonSubmit.setVisible(true);
			buttonBribe.setVisible(false);
			break;
		case Dead:
			action.setText("Your opponent is dead!");

			buttonAttack.setVisible(false);
			buttonFlee.setVisible(false);
			buttonSubmit.setVisible(false);
			buttonBribe.setVisible(false);	
		}
		
		if (Player.getShip().getRemainingHeat() < Player.getShip().getMinHeatPerFire()) {
			buttonAttack.setText("COOLDOWN");
		}
		
		youHullPerc.setText(toPerc(Player.getShip().getHull(), Player.getShip().getMaxHull()));
		youHullGreen.setScale(0.1f + (0.9f*(float)Player.getShip().getHull()/ (float)Player.getShip().getMaxHull()), 1f);
		youHeatPerc.setText(toPerc(Player.getShip().getHeat(), Player.getShip().getMaxHeat()));
		youHeatRed.setScale(0.1f + (0.9f*(float)Player.getShip().getHeat()/ (float)Player.getShip().getMaxHeat()), 1f);
		if (Player.getShip().getMaxShields() > 0) {
			youShieldPerc.setVisible(true);
			youShieldGreen.setVisible(true);
			youShieldRed.setVisible(true);
			youShieldPerc.setText(toPerc(Player.getShip().getShields(), Player.getShip().getMaxShields()));
			youShieldGreen.setScale(0.1f + (0.9f* (float)Player.getShip().getShields()/ (float)Player.getShip().getMaxShields()), 1f);
		} else {
			youShieldPerc.setVisible(false);
			youShieldGreen.setVisible(false);
			youShieldRed.setVisible(false);
		}
		
		opponentHullPerc.setText(toPerc(PlanetScreen.getEncounter().getHull(), PlanetScreen.getEncounter().getMaxHull()));
		opponentHullGreen.setScale(0.1f + (0.9f*(float)PlanetScreen.getEncounter().getHull()/ (float)PlanetScreen.getEncounter().getMaxHull()), 1f);
		opponentHeatPerc.setText(toPerc(PlanetScreen.getEncounter().getHeat(), PlanetScreen.getEncounter().getMaxHeat()));
		opponentHeatRed.setScale(0.1f + (0.9f*(float)PlanetScreen.getEncounter().getHeat()/ (float)PlanetScreen.getEncounter().getMaxHeat()), 1f);
		if (PlanetScreen.getEncounter().getMaxShields() > 0) {
			opponentShieldPerc.setVisible(true);
			opponentShieldGreen.setVisible(true);
			opponentShieldRed.setVisible(true);
			opponentShieldPerc.setText(toPerc(PlanetScreen.getEncounter().getShields(), PlanetScreen.getEncounter().getMaxShields()));
			opponentShieldGreen.setScale(0.1f + (0.9f* (float)PlanetScreen.getEncounter().getShields()/ (float)PlanetScreen.getEncounter().getMaxShields()), 1f);
		} else {
			opponentShieldPerc.setVisible(false);
			opponentShieldGreen.setVisible(false);
			opponentShieldRed.setVisible(false);
		}

	}
	
	private static String toPerc(float _n, float _d) {
		return String.format("%2.0f", 100*(_n/_d)) + "%";
	}
	
	private static void cooldown() {
		Player.getShip().doCooldown(false);
		if (PlanetScreen.getEncounter() != null) {
			PlanetScreen.getEncounter().doCooldown(false);				
		}
	}	
	 
	private static void maintenance() {
		Player.getShip().doMaintenance();
		if (PlanetScreen.getEncounter() != null) {
			PlanetScreen.getEncounter().doMaintenance();				
		}
	}	
	
	private static void populateWindow() {
		final Skin _skin = Textures.getSkin();

		clickListener = new  ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (PlanetScreen.getEncounter() == null) return;
				int playerAction = ((TextButton)actor).getText().toString().hashCode();

				//Cover actions which end encounter first
				if (playerAction == "IGNORE".hashCode()) {
					PlanetScreen.endEncounter();
				} else if (playerAction == "SUBMIT".hashCode()) {
					submit();
				} else if (playerAction == "BRIBE".hashCode()) {
					bribe();
				} else if (playerAction == "TRADE".hashCode()) {
					trade();
				} else if (playerAction == "SURRENDER".hashCode()) {
					surrender();
				} else if (playerAction == "SURRENDER".hashCode()) {
					plunder();
				} else if (playerAction == "FLEE".hashCode()) {
					flee();
				} else if (playerAction == "ATTACK".hashCode()) {
					attack();	
				} else if (playerAction == "COOLDOWN".hashCode()) {
					++PlanetScreen.turn; //no player action
					
					doEnemyCounter(); //followed by cooldown
				}
			}
		};
		

	
		travelWindow = new Window("", _skin.get("transparent", WindowStyle.class));
		travelWindow.defaults().pad(5);
		travelWindow.setMovable(false);
		travelWindow.debug();
		
		youWindow = new Window("You", _skin);
		youWindow.defaults().pad(5);
		youWindow.setMovable(false);
		youWindow.debug();
		Stack youHullStack = new Stack();
		youHullStack.add(youHullRed);
		youHullStack.add(youHullGreen);
		youHullStack.add(youHullPerc);
		Stack youShieldStack = new Stack();
		youShieldStack.add(youShieldRed);
		youShieldStack.add(youShieldGreen);
		youShieldStack.add(youShieldPerc);
		Stack youHeatStack = new Stack();
		youHeatStack.add(youHeatGreen);
		youHeatStack.add(youHeatRed);
		youHeatStack.add(youHeatPerc);
		//
		youWindow.add(youHull).right();
		youWindow.add(youHullStack).left().width(150);
		//youWindow.row();
		youWindow.add(youHeat).right();
		youWindow.add(youHeatStack).left().width(150);
		youWindow.row();
		youWindow.add(youShield).colspan(2).right();
		youWindow.add(youShieldStack).colspan(2).left().width(150);

		opponentWindow = new Window("Opponent", _skin);
		opponentWindow.defaults().pad(5);
		opponentWindow.setMovable(false);
		opponentWindow.debug();
		Stack opponentHullStack = new Stack();
		opponentHullStack.add(opponentHullRed);
		opponentHullStack.add(opponentHullGreen);
		opponentHullStack.add(opponentHullPerc);
		Stack opponentShieldStack = new Stack();
		opponentShieldStack.add(opponentShieldRed);
		opponentShieldStack.add(opponentShieldGreen);
		opponentShieldStack.add(opponentShieldPerc);
		Stack opponentHeatStack = new Stack();
		opponentHeatStack.add(opponentHeatGreen);
		opponentHeatStack.add(opponentHeatRed);
		opponentHeatStack.add(opponentHeatPerc);
		//
		opponentWindow.add(opponentHull).right();
		opponentWindow.add(opponentHullStack).left().width(150);
		//opponentWindow.row();
		opponentWindow.add(opponentHeat).right();
		opponentWindow.add(opponentHeatStack).left().width(150);
		opponentWindow.row();
		opponentWindow.add(opponentShield).colspan(2).right();
		opponentWindow.add(opponentShieldStack).colspan(2).left().width(150);
		
		optionsWindow = new Window("ENCOUNTER", _skin);
		optionsWindow.defaults().pad(5);
		optionsWindow.setMovable(false);
		optionsWindow.debug();
		optionsWindow.add(distance).colspan(4);
		optionsWindow.row();
		optionsWindow.add(action).colspan(4);
		optionsWindow.row();

		optionsWindow.add(buttonAttack).width(240).height(50);
		optionsWindow.add(buttonFlee).width(240).height(50);
		optionsWindow.add(buttonSubmit).width(240).height(50);
		optionsWindow.add(buttonBribe).width(240).height(50);
		
		buttonAttack.addListener(clickListener);
		buttonFlee.addListener(clickListener);
		buttonSubmit.addListener(clickListener);
		buttonBribe.addListener(clickListener);
		
	
		travelWindow.add(optionsWindow).colspan(2).width(PlanetScreen.width);
		travelWindow.row();
		travelWindow.add(youWindow);
		travelWindow.add(opponentWindow);

	}




}

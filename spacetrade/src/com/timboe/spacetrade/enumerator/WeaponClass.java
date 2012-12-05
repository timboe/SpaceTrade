package com.timboe.spacetrade.enumerator;

public enum WeaponClass {
	Laser,
	Missile,
	MassDriver;

	public boolean getStrongAgainst(ShipProperty _sp) {
		if (this == WeaponClass.Laser		&& _sp == ShipProperty.Dark) return true;
		if (this == WeaponClass.MassDriver 	&& _sp == ShipProperty.Shiny) return true;
		if (this == WeaponClass.Missile 	&& _sp == ShipProperty.Rigid) return true;
		return false;
	}
	
	public boolean getWeakAgainst(ShipProperty _sp) {
		if (this == Laser 		&& _sp == ShipProperty.Shiny) return true;
		if (this == MassDriver 	&& _sp == ShipProperty.Rigid) return true;
		if (this == Missile 	&& _sp == ShipProperty.Dark) return true;
		return false;
	}
}

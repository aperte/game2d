package game2d.charmechanics;

public class Buffs{

	/**Called every frame*/
	public void tick(){
		immunity--;
	}

	private int immunity;
	//TODO: add poisons, power-ups, health extensions, speed etc..

	/**Amount of frames where you're immune.*/
	public Buffs(){
		immunity = 0;
	}
	/**Reset*/
	public void defaultImmunityOnHit(){
		immunity = 30;
	}

	public boolean isImmune(){
		return immunity > 0;
	}
}

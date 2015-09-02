package game2d.charmechanics;

import game2d.SoundController;

import java.util.ArrayList;

public class CharStats {
	private ArrayList<Weapon> weapons;
	private Weapon current_weapon;
	private int weapon_index;//weapon slot
	private int weapon_limit = 4;
	private Buffs buffs;

	private int max_health;
	private int health;

	private int max_mana;
	private int mana;

	private int armor;
	private int attack_damage;//mainly for mobs
	private String name;
	private boolean alive = true;

	private boolean player;

	private double roll;
	private Equipment[] available_equipment;
	private Weapon[] available_weapons;

	private void isAliveCheck(){
		if(health <= 0) alive = false;
	}

	public boolean isImmune(){ return buffs.isImmune(); }
	public boolean isPlayer(){ return player; }
	public boolean isAlive(){ return alive; }
	public int getHealth(){ return health; }
	public int getMana(){ return mana; }
	public Weapon getWeapon(){ return current_weapon; }

	public CharStats(String name, int max_health, int max_mana, int armor, 
			int base_damage) {
		this.player = false;
		this.name = name;
		this.health = this.max_health = max_health;
		this.mana = this.max_mana = max_mana;
		this.armor = armor;
		this.attack_damage = base_damage;
		this.weapons = new ArrayList<Weapon>();
		this.buffs = new Buffs();
	}

	public void setToPlayer(){
		player = true;
	}

	public void instantiateLoot(double drop_chance_multiplier, 
			Equipment[] available_equipment, Weapon[] available_weapons){
		this.roll = Math.random() * drop_chance_multiplier;
		this.available_equipment = available_equipment;
		this.available_weapons = available_weapons;
	}

	public void rollForDrops(NPC npc){
		if(available_equipment != null && available_weapons != null){
			double item_choice = Math.random();
			if(item_choice > 0.5){//go with equipment

			} else{//weapons

			}
		} else if(available_weapons != null){
			npc.sharedDataLists.addWeaponDrop(npc, available_weapons[0]);
		} else{

		}
	}

	private void setWeaponDrop(Weapon weapon){

	}

	private void setEquipmentDrop(Equipment equipment){

	}

	/**Percentage based hit, not effected by armor.*/
	public void fallingDamage(int stacked_velocity){
		if(stacked_velocity > 0){
			health -= ((max_health * stacked_velocity) / 100);
			System.out.println("health: "+health+", max health: "+max_health+ ", stacked v: " + stacked_velocity);
			if(Math.random() > 0.5) SoundController.playSound("AAA2");
			else SoundController.playSound("AAA3");
			System.out.println("HP: "+health);
		}
		isAliveCheck();
	}

	public void takingDamage(CharStats CS){
		if(player){
			if(!buffs.isImmune()){
				takingDamage(CS.attack_damage);
				buffs.defaultImmunityOnHit();
				System.out.println("HP: "+health);
			}
		}else{
			takingDamage(CS.attack_damage);
			System.out.println("Mob's HP: "+health);
		}	
	}

	/**Take physical damage(effected by armor).*/
	private void takingDamage(int raw_hit){
		if(armor <= raw_hit){
			SoundController.playSound("AAA");
			health = health - (raw_hit - armor);
		}
		isAliveCheck();
	}

	/**<b>This CS</b> will deal its damage to the given <b>CS</b>.*/
	public void dealDamage(CharStats CS){
		//System.out.println(CS.attack_damage);
		CS.takingDamage(this);
	}

	public boolean isWeaponSlotAvailable(){
		return weapons.size() < weapon_limit;
	}
	
	/**Fill an empty weapon slot.*/
	public void obtainWeapon(Weapon W){
		//System.out.println("CHECK");
		if(!weapons.contains(W)){//amount of weapons limit
			weapons.add(W);
		}
	}

	public void replaceCurrentWeapon(){
		//TODO
	}
	
	public void dropCurrentWeapon(){
		//TODO
	}

	/**Throw current weapon and replace with one on the ground.*/
	public void switchWeapon(Weapon W){
		attack_damage -= current_weapon.getDamage();

		current_weapon = W; //current weapon switched
		attack_damage += current_weapon.getDamage();
		weapons.set(weapon_index, current_weapon);

	}

	/**Select from obtained weapons. Currently limited to 4 slots*/
	public void equipWeapon(char key){//'1','2','3','4'
		Weapon W = null;
		switch(key){
		case '1':
			if(weapons.size() > 0){
				W = weapons.get(0);
				weapon_index = 0;
			}
			break;
		case '2':
			if(weapons.size() > 1){
				W = weapons.get(1);
				weapon_index = 1;
			}
			break;
		case '3':
			if(weapons.size() > 2){
				W = weapons.get(2);
				weapon_index = 2;
			}
			break;
		case '4':
			if(weapons.size() > 3){
				W = weapons.get(3);
				weapon_index = 3;
			}
			break;
		}

		if(current_weapon != null){
			attack_damage -= current_weapon.getDamage();
		}
		current_weapon = W;
		attack_damage += current_weapon.getDamage();

		System.out.println("You're now equipping " + current_weapon.getName());
	}

	/**Called every frame*/
	public void buffsTick() {
		buffs.tick();
	}
}







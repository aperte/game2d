package game2d.charmechanics;

import game2d.shapes.Point;

import java.util.ArrayList;

/**creates all the items in the game*/
public class GameItems{
	//weapons
	private ArrayList<Weapon> all_weapons = new ArrayList<Weapon>();
	//equipment
	@SuppressWarnings("unchecked")
	private ArrayList<Equipment>[] all_equipment = new ArrayList[6];
	//drops
	private ArrayList<Weapon> weapon_drops = new ArrayList<Weapon>();
	private ArrayList<Equipment> equipment_drops = new ArrayList<Equipment>();
	
	public Weapon getWeaponIndex(int weapon_index){
		return all_weapons.get(weapon_index);
	}

	/**
	 *     0: head
	 * <br>1: arms
	 * <br>2: chest
	 * <br>3: legs
	 * <br>4: neck
	 * <br>5: ring*/
	public Equipment getEquipmentIndex(int equipment_index, int slot){
		//equipment index == index in the list of items of the specific type
		//given by the 'slot' variable
		return all_equipment[slot].get(equipment_index);
	}

	public GameItems(){
		for (int i = 0; i < all_equipment.length; i++) {
			all_equipment[i] = new ArrayList<Equipment>();
		}
		//=================================================================
		//add weapons:
		all_weapons.add(new Weapon("Slayer of Nothing", 200, 50, 10, 0f, 0f,
				new Point[]{new Point(5,1), new Point(5,1), new Point(6,1), 
				new Point(6,1), new Point(7,1), new Point(7, 1)}, 0.9));
		all_weapons.add(new Weapon("Illuminati Slasher", 200, 50, 10, 0.5f, -0.5f,
				new Point[]{new Point(0,0), new Point(0,0), new Point(1,0), 
				new Point(1,0), new Point(0,1), new Point(0,1), new Point(1,1), 
				new Point(1,1)}, 0.9));
		//System.out.println("w1: "+all_weapons.get(0));
		//=================================================================
		//add equipment:
	}
}

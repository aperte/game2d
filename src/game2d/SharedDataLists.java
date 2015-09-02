package game2d;

import game2d.charmechanics.CharStats;
import game2d.charmechanics.DroppedWeaponPackage;
import game2d.charmechanics.Equipment;
import game2d.charmechanics.GameItems;
import game2d.charmechanics.Monster;
import game2d.charmechanics.NPC;
import game2d.charmechanics.Player;
import game2d.charmechanics.Weapon;
import game2d.shapes.Point;
import game2d.shapes.Rectangle;

import java.util.ArrayList;

public class SharedDataLists {
	public Player Actor;
	public void setActorOnce(Player Actor){
		this.Actor = Actor;
	}

	public GameItems gameItems;

	/**Each column represents the things to draw on the upcoming frame.
	 * That column will then be removed and if one of the arrays get
	 * empty, it's then removed.*/
	public ArrayList<ArrayList<Point>> image_sequences = new ArrayList<ArrayList<Point>>();
	public void add_sequence(Point[] seq){
		image_sequences.add(new ArrayList<Point>());
		for (int i = 0; i < seq.length; i++) {
			image_sequences.get(image_sequences.size() - 1).add(seq[i]);
		}
	}

	public Map map_list[];
	public int map_index;//Should be interacting later on with saves/loads

	public SharedDataLists(Settings config, GameItems GI) {
		this.gameItems = GI;

		map_index = 0;

		String mapstr = config.get("mapseq", "M2.bmp");
		String maps_folder = config.get("mapfolder", "maps");
		String[] maps = mapstr.split(",");

		map_list = new Map[maps.length/2];//There're two ','s for every map.
		int fetch = 0;
		for (int i = 0; i < map_list.length; i++) {
			map_list[i] = new Map(String.format("%s/%s", maps_folder, maps[fetch++]), Integer.parseInt(maps[fetch++]));
		}
	}

	public void new_level(){
		if(map_list[map_index].bonus) map_index++;//THIS MIGHT NEED AN UPDATE WHEN WORKING ON BONUS LEVELS. ALSO CAPS LOCK OP.
		else map_index = map_list[map_index].next_map_index;//case where next map is -1 not used yet

		Point p = map_list[map_index].player_starting_coords;
		Actor.shape = new Rectangle(p.x, p.y, Actor.width, Actor.height);
		Actor.setCoords(p.x - 300, p.y - 300);

		dropped_weapons.clear();
		//dropped_equipment.clear(); TODO
		
		initialize_monsters();
	}

	/** monster objects to paint and act with */
	public void initialize_monsters(){
		Map map = map_list[map_index];

		int mobs_amount = map.monster_coords.size();
		map.mobs_in_map = new ArrayList<Monster>();

		CharStats charStats;
		for (int i = 0; i < mobs_amount; i++) {
			//each i stands for a different mob in the current map.
			charStats = new CharStats("monsta", 100, 30, 0, 1);
			charStats.instantiateLoot(1, null, new Weapon[]{gameItems.getWeaponIndex(0)});
			map.mobs_in_map.add(new Monster(this,
					charStats, i));
		}
	}

	private ArrayList<DroppedWeaponPackage> dropped_weapons = 
			new ArrayList<DroppedWeaponPackage>();
	//	private ArrayList<DroppedEquipmentPackage> dropped_equipment = 
	//new ArrayList<DroppedEquipmentPackage>(); //TODO

	public void addWeaponDrop(NPC npc, Weapon weapon){
		DroppedWeaponPackage DWP = new DroppedWeaponPackage(weapon, 
				npc.shape.x, npc.shape.y, weapon.getSequence()[0]);
		dropped_weapons.add(DWP);
		//TODO sort
	}

	public void addEquipmentDrop(NPC npc, Equipment equipment){
		//TODO
	}
	
	public ArrayList<DroppedWeaponPackage> getDroppedWeaponsList(){
		return dropped_weapons;
	}
	
	/*public ArrayList<DroppedEquipmentPackage> getDroppedEquipmentList(){
		return dropped_equipment; TODO
	}*/
	
	//can be used as a save point later on
	/*public ... load_map(...){...}*/
	/*public ... save_map(...){...}*/
}



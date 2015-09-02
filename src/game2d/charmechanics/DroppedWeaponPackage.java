package game2d.charmechanics;

import game2d.shapes.Point;

public class DroppedWeaponPackage{//TODO make abstract class for items

	private Weapon weapon;
	private int x_coord;
	private int y_coord;
	private Point image;

	//to make get the weapon to float... feel free to be more creative!!!
	private int[] y_pos = {0,2,4,6,4,2,0,-2,-4,-6,-4,-2};
	private int i = 0;

	public DroppedWeaponPackage(Weapon weapon, int x_coord, int y_coord,
			Point image){
		this.weapon = weapon;
		this.x_coord = x_coord;
		this.y_coord = y_coord;
		this.image = image;
	}

	public int getX(){ return x_coord; }
	public int getY(){ return y_coord; }
	public Weapon getWeapon(){ return weapon; }
	public Point getImage(){ return image; }
	
	public int getYPosition(){ 
		i = ++i % y_pos.length;
		return y_pos[i]; 
	}
}






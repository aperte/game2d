package game2d.charmechanics;

import game2d.shapes.Point;
import game2d.shapes.Triangle;

public class Weapon{

	private String name;
	private int dmg_bonus;
	private int range;
	private int cd;//cooldown
	private float degree_a, degree_b;//from - to: f.e 1 to 90 would be a slash from
	//above the player into a straight line where the player is facing.
	private Point[] sequence;
	private Triangle slashing_triangle;
	private double drop_chance;

	public Weapon(String name, int dmg_bonus, int range, int cd,
			float degree_a, float degree_b, Point[] atk_seq,
			double drop_chance){
		this.name = name;
		this.dmg_bonus = dmg_bonus;
		this.range = range;
		this.cd = cd;
		this.degree_a = degree_a;
		this.degree_b = degree_b;
		this.sequence = atk_seq;
		slashing_triangle = new Triangle();
		this.drop_chance = drop_chance;
	}
	
	public double getDropChance(){
		return drop_chance;
	}

	public Triangle setTriangle(int x1, int y1, int x2, int y2,
			int x3, int y3){
		slashing_triangle.a.x = x1;
		slashing_triangle.a.y = y1;

		slashing_triangle.b.x = x2;
		slashing_triangle.b.y = y2;

		slashing_triangle.c.x = x3;
		slashing_triangle.c.y = y3;

		return slashing_triangle;
	}

	public int getDamage(){ return dmg_bonus; }
	public int getRange(){ return range; }
	public int getCD(){ return cd; }
	public float getDegreeA(){ return degree_a; }
	public float getDegreeB(){ return degree_b; }
	public Point[] getSequence(){ return sequence; }
	public String getName(){ return name; }
}








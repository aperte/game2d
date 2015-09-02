package game2d.charmechanics;

import game2d.MapNode;
import game2d.SharedDataLists;
import game2d.shapes.Point;
import game2d.shapes.Rectangle;
import javafx.scene.input.KeyCode;

public class Monster extends NPC {

	private KeyCode movementDirection = null;

	private Point[][] images_to_grab = {
			//standing (0)
			{new Point(0,2)}
	};
	
	/**
	 * @param sharedDataLists - connection with the map
	 * @param charStats - stats
	 * @param index - retrieves index from <b>monster_coords</b> ArrayList
	 */
	public Monster(SharedDataLists sharedDataLists, CharStats charStats, 
			int index){
		init();
		this.charStats = charStats; //create a new one with relevance to the level
		this.sharedDataLists = sharedDataLists;
		//mob coordinates
		Point mc = this.sharedDataLists.map_list[this.sharedDataLists.map_index].monster_coords.get(index);
		shape = new Rectangle(mc.x, mc.y, width, height);
	}

	public void AI_movement(){
		Rectangle c = sharedDataLists.Actor.shape;
		boolean move = true;
		if(c.x + c.width < shape.x){
			if(!isFlying()){
				if(shape.x - (c.x + c.width) > 350) move = false;
				else movementDirection = KeyCode.A;//'a'
			}
		}
		else if(c.x > shape.x + shape.width){
			if(!isFlying()){
				if(c.x - (shape.x + shape.width) > 350) move = false;
				else movementDirection = KeyCode.D;//'d'
			}
		}
		else if((c.y > shape.y && c.y < shape.y + shape.height)
				|| (c.y < shape.y && c.y + c.height > shape.y)){
			//inside the x range of the player's rectangle
			sharedDataLists.Actor.charStats.takingDamage(this.charStats);
		}

		if(c.y + c.height < shape.y){//player above monster
			if(shape.y - (c.y + c.height) > 300) move = false;
		}
		else{//player below monster
			if(c.y - (shape.y + shape.height) > 300) move = false;
		}

		//====================================================
		//move = false;
		if(move) movement(movementDirection);//try to reach the player
	}

	public void AI_gravity(){
		double j = Math.random();
		KeyCode key = null;

		if(j > 0.90)
			key = KeyCode.SPACE;//10% per frame to jump

		gravity(key);//gravity mechanics + option to jump
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void movement(KeyCode key) {
		if (key == null) return;

		final int SPEED = speed;
		int hill_tolerance;
		MapNode[][] MN = sharedDataLists.map_list[sharedDataLists.map_index].map;

		switch(key){
		case A:
			hill_tolerance = climbLeft(MN);
			boolean LB = leftBump(hill_tolerance);
			if(LB){
				shape.y -= hill_tolerance;
				shape.x -= speed;

				previousStepsStack();
			}
			else if(!LB && !rightBump(hill_tolerance) && !isFlying()){
				stepReturn();
			}
			break;

		case D:
			hill_tolerance = climbRight(MN);
			boolean RB = rightBump(hill_tolerance);
			if(RB){
				shape.y -= hill_tolerance;
				shape.x += speed;

				previousStepsStack();
			}
			else if(!RB && !leftBump(hill_tolerance) && !isFlying()){
				stepReturn();
			}
			break;
		}
		speed = SPEED;
	}

	@Override
	public void gravity(KeyCode key) {
		if(gravityMethod(key)){

			shape.y += velocity;
			if(velocity < 14) velocity += 2;

		}else{
			velocity = 6;
		}
		//System.out.println("health: " + CS.getHealth()); //SHOW HEALTH
		//System.out.println("y:"+(shape.y + height) + ", type:" + Map.map[shape.y + height][shape.x].type);
	}


	@Override
	public void init() {
		speed = (int)(Math.random() * 4 + 1);
		height = 18;//sides hitbox split into 5
		width = 12;//buttom/top hitbox split into 4
		velocity = 0;
		previous_step = new PreviousStepNode();
		
	}

	@Override
	public Point getCurrentImagePt() {
		return images_to_grab[0][0];
	}

}

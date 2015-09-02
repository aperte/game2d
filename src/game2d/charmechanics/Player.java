package game2d.charmechanics;

import game2d.MapNode;
import game2d.SharedDataLists;
import game2d.SoundController;
import game2d.shapes.Point;
import game2d.shapes.Rectangle;
import game2d.shapes.Triangle;
import javafx.scene.input.KeyCode;

public class Player extends NPC{

	private int y_coord;//camera related
	private int x_coord;//camera related

	private int attack_delay = 0;
	/**related to attack moves*/
	private char facing;

	public char getFacing(){ return facing; }

	/**Camera related*/
	public int yCoord(){ return y_coord; }
	/**Camera related*/
	public int xCoord(){ return x_coord; }
	/**Camera related*/
	public void setCoords(int x, int y){
		x_coord = x;
		y_coord = y;
	}

	private Point[][] images_to_grab = {
			//standing (0)
			{new Point(2,0), new Point(2,0), new Point(2,0), new Point(2,0), new Point(2,0), new Point(2,0)},
			//walking right (1)
			{new Point(4,0), new Point(4,0), new Point(5,0), new Point(5,0), new Point(6,0), new Point(6,0)},
			//jumping (2)
			{new Point(7,0), new Point(7,0), new Point(7,0), new Point(7,0), new Point(7,0), new Point(7,0)},
			//walking left (3)
			{new Point(2,1), new Point(2,1), new Point(3,1), new Point(3,1), new Point(4,1), new Point(4,1)},
			//taking damage - immunity (4)
			{new Point(3,0), new Point(3,0), new Point(3,0), new Point(3,0), new Point(3,0), new Point(3,0)},
			//attack right (5)
			{new Point(2,2)},
			//attack left (6)
			{new Point(3,2)}
	};

	public Point getCurrentImagePt(){
		sequence = ++sequence % 6;

		switch(current_action){
		case STANDING: 
			return images_to_grab[0][sequence];
		case WALKING:
			if(getFacing() == 'd') return images_to_grab[1][sequence];//right
			else return images_to_grab[3][sequence];//left
		case JUMPING:
			return images_to_grab[2][sequence];
		case ATTACKING:
			if(getFacing() == 'd') return images_to_grab[5][0];//right
			else return images_to_grab[6][0];//left
		default: return null; //shouldn't happen!
		}
	}

	public Point getGoreImage(){
		return new Point(3, 0);
	}

	public void init(){
		speed = 4;
		height = 18;//sides hitbox split into 5
		width = 12;//buttom/top hitbox split into 4
		velocity = 0;
		previous_step = new PreviousStepNode();
		stacked_velocity = -44;
	}

	public Player(SharedDataLists SDL, CharStats CS) {
		init();
		charStats = CS;
		charStats.setToPlayer();
		sharedDataLists = SDL;
		
		
		charStats.obtainWeapon(sharedDataLists.gameItems.getWeaponIndex(0));
		charStats.obtainWeapon(sharedDataLists.gameItems.getWeaponIndex(1));
		charStats.equipWeapon('1');

		Point p = sharedDataLists.map_list[sharedDataLists.map_index].player_starting_coords;
		shape = new Rectangle(p.x, p.y, width, height);
		//camera:
		x_coord = p.x - 300;
		y_coord = p.y - 300;
	}

	private boolean xCameraPos(){
		//System.out.println((x_coord - shape.x) + ", " + (y_coord - shape.y));
		return x_coord - shape.x > -300 || x_coord - shape.x < -500;
	}

	private boolean yCameraPos(){
		//System.out.println((y_coord - shape.y));// + ", " + (y_coord - shape.y));
		return y_coord - shape.y > -200 || y_coord - shape.y < -400;
	}

	private void stabRight(Monster M){
		if(M.shape.x + M.shape.width > this.shape.x + this.shape.width //checking weapon base
				&& M.shape.x <= //checking weapon edge
				this.shape.x + this.shape.width + this.charStats.getWeapon().getRange()){
			this.charStats.dealDamage(M.charStats);
			//System.out.println("HIT (right)");
		}
	}

	private void stabLeft(Monster M){
		if(M.shape.x < this.shape.x //checking weapon base
				&& M.shape.x + M.shape.width >= //checking weapon edge
				this.shape.x - this.charStats.getWeapon().getRange()){
			this.charStats.dealDamage(M.charStats);
			//System.out.println("HIT (left)");
		}
	}

	private void attack(){
		float degree_a = charStats.getWeapon().getDegreeA();
		float degree_b = charStats.getWeapon().getDegreeB();

		setCurrentActionImage(ActionImage.ATTACKING);

		if(degree_a - degree_b == 0){//stab
			SoundController.playSound("stab");
			for (Monster M : sharedDataLists.map_list[sharedDataLists.map_index].mobs_in_map) {
				int weapon_y_axis = this.shape.y + this.height/2;
				boolean within_y = (M.shape.y <= weapon_y_axis)
						&& (M.shape.y + M.shape.height >= weapon_y_axis);
				if(within_y){
					if(facing == 'd')
						stabRight(M);
					else
						stabLeft(M);
				}
			}
		}else{//slash
			SoundController.playSound("slash");
			Triangle triangular_area;
			Weapon W = charStats.getWeapon();
			int actor_x, actor_y = shape.y + shape.height/2;

			for (Monster M : sharedDataLists.map_list[sharedDataLists.map_index].mobs_in_map) {
				if(facing == 'd'){//right
					actor_x = shape.x + shape.width;

					triangular_area = W.setTriangle(actor_x, actor_y,
							actor_x + W.getRange(), actor_y - (int)(W.getRange() * W.getDegreeA()),
							actor_x + W.getRange(), actor_y - (int)(W.getRange() * W.getDegreeB()));
				}else{//left
					actor_x = shape.x;
					triangular_area = W.setTriangle(actor_x, actor_y,
							actor_x - W.getRange(), actor_y - (int)(W.getRange() * W.getDegreeA()),
							actor_x - W.getRange(), actor_y - (int)(W.getRange() * W.getDegreeB()));
				}
				//===================================================================================
				if(M.shape.isIntersectingTriangle(triangular_area)){
					charStats.dealDamage(M.charStats);

					/*System.out.println("actor_y "+actor_y+
							", top_reach "+ (-(int)(W.getRange() * W.getDegreeA()))+
							", bottom_reach "+ (-(int)(W.getRange() * W.getDegreeB())));
					System.out.println("mob_y top "+M.shape.y+"mob_y bottom "+(M.shape.y + M.shape.height));*/
					//System.out.println("HIT");
				}
			}
		}
	}

	public void actions(KeyCode key){
		if(attack_delay > 0) attack_delay--;
		else if(key == KeyCode.CONTROL){ //ctrl (both sides)
			sharedDataLists.add_sequence(charStats.getWeapon().getSequence());
			attack_delay = charStats.getWeapon().getCD();
			attack();
		}

		if(attack_delay == charStats.getWeapon().getCD() -3)
			attack(); //consistency with animation
	}


	@SuppressWarnings("incomplete-switch")
	@Override
	public void movement(KeyCode key) {
		if (key == null){
			if(attack_delay == 0)
				setCurrentActionImage(ActionImage.STANDING);	
			return;
		}

		final int SPEED = speed;
		int hill_tolerance;
		MapNode[][] MN = sharedDataLists.map_list[sharedDataLists.map_index].map;
		if(attack_delay == 0)
		setCurrentActionImage(ActionImage.WALKING);

		switch(key){
		case A:
			facing = 'a';
			hill_tolerance = climbLeft(MN);
			boolean LB = leftBump(hill_tolerance);
			if(LB){
				shape.y -= hill_tolerance;
				shape.x -= speed;

				//player stuff
				if(xCameraPos()) x_coord -= speed;
				if(yCameraPos()) y_coord -= hill_tolerance;

				previousStepsStack();
			}
			else if(!LB && !rightBump(hill_tolerance) && !isFlying()){
				stepReturn();
			}
			break;

		case D:
			facing = 'd';
			hill_tolerance = climbRight(MN);
			boolean RB = rightBump(hill_tolerance);
			if(RB){
				shape.y -= hill_tolerance;
				shape.x += speed;

				//player stuff
				if(xCameraPos()) x_coord += speed;
				if(yCameraPos()) y_coord -= hill_tolerance;

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
			if(yCameraPos())
				y_coord += velocity;
			if(velocity < 14)
				velocity += 2;
			if(velocity > 0 && !ground_below)
				stacked_velocity += 2;
		}else{
			velocity = 6;//constant falling speed
			charStats.fallingDamage(stacked_velocity);
			stacked_velocity = -44;
		}
		checkForPortals();//much wow very next level
		//System.out.println("health: " + CharStats.getHealth()); //SHOW HEALTH
		//System.out.println("y:"+(shape.y + height) + ", type:" + Map.map[shape.y + height][shape.x].type);
	}

	public void buffDurations() {
		charStats.buffsTick();
	}

	private void checkForPortals(){
		MapNode[][] MN = sharedDataLists.map_list[sharedDataLists.map_index].map;//fucking code size
		if(MN[shape.y - 3][shape.x - 3].type == 'P'
				|| MN[shape.y - 3][shape.x + width + 3].type == 'P'
				|| MN[shape.y + height + 3][shape.x - 3].type == 'P'
				|| MN[shape.y + height + 3][shape.x + width + 3].type == 'P'
				|| MN[shape.y + (height / 2)][shape.x + width + 5].type == 'P'
				|| MN[shape.y + (height / 2)][shape.x - 5].type == 'P')
			sharedDataLists.new_level();
	}
	
	public void checkForItems(KeyCode key){
		if(key == KeyCode.E || charStats.isWeaponSlotAvailable()){
			searchForItemsAroundActor();
		}
	}
	
	private void searchForItemsAroundActor(){
		sharedDataLists.getDroppedWeaponsList();
	}


}

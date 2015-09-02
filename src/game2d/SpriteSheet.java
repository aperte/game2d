package game2d;

import game2d.shapes.Point;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpriteSheet {

	private Image sheet;
	private Image background;
	
	public SpriteSheet(){
		sheet = new Image("file:gfx/spriteSheets/sprite_sheet.png");
		background = new Image("file:gfx/spriteSheets/grass_all.png");
		
	}

	public Image getBackground(){
		return background;
	}
	
	/**f.e: (0, 0) would be the first image from the top left of the loaded sheet*/
	public ImageView grabImage(int col, int row){
		ImageView iv = new ImageView();
		iv.setImage(sheet);
		iv.setViewport(new Rectangle2D(col*32, row*32, 32, 32));
		return iv;
	}
	
	public ImageView grabImage(Point p){
		ImageView iv = new ImageView();
		iv.setImage(sheet);
		iv.setViewport(new Rectangle2D(p.x*32, p.y*32, 32, 32));
		return iv;
	}
}
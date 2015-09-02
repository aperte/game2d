package game2d;

import game2d.shapes.Point;

public class ObjectAnimations {

	private int sequence = 0;
	private Point[] images_to_grab;
	
	public ObjectAnimations(Point[] images_to_grab){
		this.images_to_grab = images_to_grab;
	}
	
	public ObjectAnimations(Point image_to_grab){
		this.images_to_grab = new Point[]{image_to_grab};
	}
	
	public Point getImagePt(){
	sequence = ++sequence % images_to_grab.length;
		return images_to_grab[sequence];
	}
	
}

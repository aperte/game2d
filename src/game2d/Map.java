package game2d;

import game2d.charmechanics.Monster;
import game2d.shapes.Point;

import java.awt.Rectangle;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Map {
	public MapNode[][] map;
	ArrayList<PaintRectNode> toPaint;
	private int MAP_WIDTH;
	private int MAP_HEIGHT;

	public Point player_starting_coords;
	
	public ArrayList<Point> monster_coords;
	public ArrayList<Monster> mobs_in_map;
	
	public boolean bonus = false;//YOU'LL NEED TO EARN IT, BIATCH.

	/** represents the index of the next map, if for any reason
	 * that index is none, the value should be <b>-1</b>*/
	public int next_map_index;
	
	public int getWidth(){
		return MAP_WIDTH;
	}
	
	public int getHeight(){
		return MAP_HEIGHT;
	}

	private void build_toPaint(){//Might be messy, but it's only done once every map we load :P
		toPaint = new ArrayList<PaintRectNode>();
		for (int i = 0; i < MAP_HEIGHT; i++) {
			for (int j = 0, x = 0, y = 0; j < MAP_WIDTH; j++) {
				if(!map[i][j].checked){
					int height = 1, width = 1;
					boolean firstRowChecked = false, keepCheckingRows = true;
					for (y = i; y < MAP_HEIGHT && keepCheckingRows; y++) {
						if(map[y][j].checked || map[y][j].type != map[i][j].type){
							break;
						}
						for (x = j; x < MAP_WIDTH && (x < j + width || !firstRowChecked) ; x++) {
							if(firstRowChecked && (map[y][x].checked || map[y][x].type != map[i][j].type)){
								while(x >= j){
									map[y][x--].checked = false;
								}//unchecked the new line with less than width 'fitting' pixels
								keepCheckingRows = false;
								break;
							}
							//this condition is entered during first row check to limit width
							else if(map[y][x].checked || map[y][x].type != map[i][j].type){
								break;
							}//width collected
							if(!firstRowChecked && x - j +1 > width)//Building width only within the first row
								width = x - j +1;
							map[y][x].checked = true;
						}
						firstRowChecked = true;
						if(keepCheckingRows && y - i +1 > height)
							height = y - i +1;
					}//height collected
					//System.out.println("type:"+map[i][j].type+", y:"+i+", x:"+j+", width:"+width+", height:"+height);
					toPaint.add(new PaintRectNode(map[i][j].type, new Rectangle(j, i, width, height)));	
				}
			}
		}
		System.out.println("size of toPaint: " + toPaint.size());
	}
	
	/**bitmap*/
	public Map(String filename, int next_map_index) {
		monster_coords = new ArrayList<Point>();
		player_starting_coords = new Point();
		
		fromBitmap(filename);
		this.next_map_index = next_map_index;
		build_toPaint();
	}

	void fromBitmap(String filename) {
		// Reads entire bitmap into a byte array..
		//
		// watch out for big bitmaps.
		byte[] bytes = null;
		try {
			Path path = Paths.get(filename);
			bytes = Files.readAllBytes(path);
		} catch (IOException ioe) {
			System.out.println("Error reading bitmap. Please kill me, I'm bugged!");
			System.exit(1);
		}

		// Read in some information
		MAP_WIDTH = bytesToInt(18, 4, bytes);
		MAP_HEIGHT = bytesToInt(22, 4, bytes);

		// Define types of MapNode's, used to convert color combination
		// (RED, BLUE, GREEN) into a certain type
		//To add more colors, just change the charmap array accordingly
		//and then follow the pattern of making a new MapNode below
		MapNode[] charmap = new MapNode[5];
		MapNode ground = new MapNode('G');
		ground.r = 0;
		ground.g = 0;
		ground.b = 0;
		charmap[0] = ground;

		MapNode air = new MapNode('A');
		air.r = 255;
		air.g = 255;
		air.b = 255;
		charmap[1] = air;

		MapNode chguy = new MapNode('C');
		chguy.r = 255;
		chguy.g = 0;
		chguy.b = 0;
		charmap[2] = chguy;

		MapNode portal = new MapNode('P');
		portal.r = 225;
		portal.g = 225;
		portal.b = 50;
		charmap[3] = portal;

		MapNode monster = new MapNode('M');
		monster.r = 0;
		monster.g = 0;
		monster.b = 255;
		charmap[4] = monster;

		MapNode.mappings = charmap;

		// Populate map with info from bitmap
		map = new MapNode[MAP_HEIGHT][MAP_WIDTH];
		int cursor = 0;
		int row = MAP_HEIGHT-1;
		MapNode tmp = null;
		// 54: magic number, indicates start of pixel array in byte-count
		for (int i = 54; i < bytes.length; i+=3) { // +3 because it's 3 bytes per pixel, one per color
			if (cursor == MAP_WIDTH) {
				cursor = 0;
				row = (row > 1) ? row - 1 : 0;
			}
			tmp = new MapNode('x');
			tmp.b = bytes[i] & 0xff;
			tmp.g = bytes[i+1] & 0xff;
			tmp.r = bytes[i+2] & 0xff;
			tmp.identify();
			map[row][cursor] = tmp;

			switch(tmp.type){
			case 'C':
				player_starting_coords.x = cursor;
				player_starting_coords.y = row;
				map[row][cursor] = air;
				break;
			case 'M':
				monster_coords.add(new Point(cursor, row));
				map[row][cursor] = air;
			}

			cursor++;
		}
	}

	// Used to convert bitmap bytes into integers
	int bytesToInt(int offset, int byteCount, byte[] bytes) {
		ByteBuffer bb = ByteBuffer.allocate(byteCount);
		bb.order(ByteOrder.LITTLE_ENDIAN); // bitmap uses little endian
		for (int i = 0; i < byteCount && i < bytes.length; i++) {
			bb.put(bytes[i+offset]);
		}
		bb.flip();
		return bb.getInt();
	}

}

class PaintRectNode{
	char type;
	Rectangle rect;
	/**
	 * Builds a new rectangle node to add to the map painting method with the given
	 * <b>new rect</b> and tile type for every node.
	 */
	public PaintRectNode(char type, Rectangle rect) {
		this.type = type;
		this.rect = rect;
	}
}


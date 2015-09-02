package game2d;

public class MapNode{
	//'G'Ground, 'A'Air, 'C'Char, 'P'Portal.
	int r, g, b;
	static MapNode[] mappings;
	public char type;
	boolean checked = false;//For the RectNodes to pick every pixel properly! And yes, some of those comments are not important, they're just here for YOU to understand this code better, PROBLIM?!

	MapNode(char type){
		this.type = type;
	}

	// compared self to all nodes in `mappings` and copies type from first match
	void identify() {
		for (MapNode p : mappings) {
			if (p.r == r && p.g == g && p.b == b){
				type = p.type;
				return;
				//System.out.printf("r:%d, g:%d, b:%d\n", r, g, b);
			}
		}
		type = '.'; // default type if no defined type is found
	}
}

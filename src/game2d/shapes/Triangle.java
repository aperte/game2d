package game2d.shapes;

public class Triangle{
	public Point a, b, c;

	public Triangle(){
		a = new Point();
		b = new Point();
		c = new Point();
	}

	public Triangle(Point a, Point b, Point c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
}

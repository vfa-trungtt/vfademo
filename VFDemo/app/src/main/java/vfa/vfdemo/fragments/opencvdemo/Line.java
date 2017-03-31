package vfa.vfdemo.fragments.opencvdemo;

import org.opencv.core.Point;

public class Line {
	public Point arrPoint[] = new Point[100];
	public int numberOfPoint;
	public Point point1;
	public Point point2;
	public int originalIndex;
	public double botRight;
	public Line(){
		
	}
	public Line(Point p1, Point p2){
		point1  = p1;
		point2 = p2;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "";
		int i=0;
		for (Point p : arrPoint) {
			if (p !=null)
				s+= String.format("point %d (%f,%f) \n",i++,p.x,p.y);
		}
		return s;
	}
}

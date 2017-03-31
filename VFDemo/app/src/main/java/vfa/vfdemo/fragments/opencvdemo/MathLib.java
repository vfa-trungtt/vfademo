package vfa.vfdemo.fragments.opencvdemo;

import android.graphics.PointF;
import android.graphics.RectF;


import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;


/**
 * Math library
 * 
 * @author Kinnd
 * @since 2015.5.12
 * 
 */
public class MathLib {

	/**
	 * get distance from point to line
	 * 
	 * @param point
	 * @param lineMod
	 * @return
	 */
	public static double getDistance(PointF point, ALineMod lineMod) {
		// Line line = getLineFromMod(lineMod);
		// Vector2D p = new Vector2D(point.x, point.y);
		// double a1= line.getAngle();
		// double a2 = Math.PI + a1;
		// Line line2 = new Line(p, a2);
		// Vector2D itersect = line.intersection(line2);
		// if (itersect !=null)
		// return Vector2D.distance(itersect, p);
		// return Double.MAX_VALUE;
		double distance = 0;
		PointF line1Point1 = lineMod.mStart;
		PointF line1Point2 = lineMod.mEnd;
		float a1 = 0;
		float b1 = 0;
		boolean flag = false;

		if ((line1Point1.x == line1Point2.x && line1Point1.y == line1Point2.y)) {
			return distance;
		}

		if (line1Point1.x == line1Point2.x) {
			a1 = line1Point1.x;
			flag = true;
		} else {
			a1 = (float) (line1Point1.y - line1Point2.y)
					/ (line1Point1.x - line1Point2.x);
			b1 = line1Point1.y - line1Point1.x * a1;

		}

		if (flag == true) {
			distance = Math.abs(line1Point1.x - point.x);
		} else {
			distance = Math.abs(a1 * point.x - point.y + b1)
					/ Math.sqrt(a1 * a1 + 1);
		}
		return distance;
	}

	/**
	 * get distance from point to line
	 * 
	 * @param point
	 * @param lineMod
	 * @return
	 */
	public static double getDistance(Point point, ALineMod lineMod) {
		// Line line = getLineFromMod(lineMod);
		// Vector2D p = new Vector2D(point.x, point.y);
		// double a1= line.getAngle();
		// double a2 = Math.PI + a1;
		// Line line2 = new Line(p, a2);
		// Vector2D itersect = line.intersection(line2);
		// if (itersect !=null)
		// return Vector2D.distance(itersect, p);
		// return Double.MAX_VALUE;
		double distance = 0;
		PointF line1Point1 = lineMod.mStart;
		PointF line1Point2 = lineMod.mEnd;
		float a1 = 0;
		float b1 = 0;
		boolean flag = false;

		if ((line1Point1.x == line1Point2.x && line1Point1.y == line1Point2.y)) {
			return distance;
		}

		if (line1Point1.x == line1Point2.x) {
			a1 = line1Point1.x;
			flag = true;
		} else {
			a1 = (float) (line1Point1.y - line1Point2.y)
					/ (line1Point1.x - line1Point2.x);
			b1 = line1Point1.y - line1Point1.x * a1;

		}

		if (flag == true) {
			distance = Math.abs(line1Point1.x - point.x);
		} else {
			distance = Math.abs(a1 * point.x - point.y + b1)
					/ Math.sqrt(a1 * a1 + 1);
		}
		return distance;

	}

	/**
	 * get distance from line to line
	 * 
	 * @param line1
	 * @param line2
	 * @return
	 */
	public static double getDistance(ALineMod line1, ALineMod line2) {
		double d1 = getDistance(line1.mStart, line2);
		double d2 = getDistance(line1.mEnd, line2);
		return (d1 + d2) / 2;
	}

	/**
	 * get angle between 2 lines
	 * 
	 * @param line1
	 * @param line2
	 * @return
	 */
	public static double getAngle(ALineMod line1, ALineMod line2) {
		// Line l1 = getLineFromMod(line1);
		// Line l2 = getLineFromMod(line2);
		// double a1 = l1.getAngle();
		// double a2 = l2.getAngle();
		// double a = a2-a1;
		// if (a<0) a = 2*Math.PI + a;
		// return Math.toDegrees(a);
		PointF line1Point1 = line1.mStart;
		PointF line1Point2 = line1.mEnd;
		PointF line2Point1 = line2.mStart;
		PointF line2Point2 = line2.mEnd;

		double a = line1Point2.x - line1Point1.x;
		double b = line1Point2.y - line1Point1.y;
		double c = line2Point2.x - line2Point1.x;
		double d = line2Point2.y - line2Point1.y;

		double atanA = Math.atan2(a, b);
		double atanB = Math.atan2(c, d);

		double degree = (atanA - atanB) * 180 / Math.PI;

		return degree;

	}
	
	public static double getAngle(Line line1, Line line2) {
		// Line l1 = getLineFromMod(line1);
		// Line l2 = getLineFromMod(line2);
		// double a1 = l1.getAngle();
		// double a2 = l2.getAngle();
		// double a = a2-a1;
		// if (a<0) a = 2*Math.PI + a;
		// return Math.toDegrees(a);
		Point line1Point1 = line1.point1;
		Point line1Point2 = line1.point2;
		Point line2Point1 = line2.point1;
		Point line2Point2 = line2.point2;

		double a = line1Point2.x - line1Point1.x;
		double b = line1Point2.y - line1Point1.y;
		double c = line2Point2.x - line2Point1.x;
		double d = line2Point2.y - line2Point1.y;

		double atanA = Math.atan2(a, b);
		double atanB = Math.atan2(c, d);

		double degree = (atanA - atanB) * 180 / Math.PI;

		return degree;

	}

	/**
	 * Get intersect between 2 Line
	 * 
	 * @param line1
	 * @param line2
	 * @return
	 */
	public static PointF getIntersect(ALineMod line1, ALineMod line2) {
		
		PointF intersect = line1.intersection(line2);
		if (intersect != null)
			return intersect;
		return new PointF(Float.MIN_VALUE, Float.MIN_VALUE);
		
//		Line l1 = getLineFromMod(line1);
//		Line l2 = getLineFromMod(line2);
//		Vector2D intersect = l1.intersection(l2);
//		if (intersect != null)
//			return new PointF((float) intersect.getX(),
//					(float) intersect.getY());
//		return new PointF(Float.MIN_VALUE, Float.MIN_VALUE);
		/*
		 * if(line1.mStart.x == line1.mEnd.x) { line1.mStart.x++; }
		 * if(line1.mStart.y == line1.mEnd.y) { line1.mStart.y++; }
		 * 
		 * if(line2.mStart.x == line2.mEnd.x) { line2.mStart.x++; }
		 * if(line2.mStart.y == line2.mEnd.y) { line2.mStart.y++; } PointF
		 * point1 = line1.mStart; PointF point2 = line1.mEnd; PointF p1 =
		 * findCoefficientA_B(point1, point2); PointF point3 = line2.mStart;
		 * PointF point4 = line2.mEnd; PointF p2 = findCoefficientA_B(point3,
		 * point4); return findIntersectWithPoint(p1, p2);
		 */
	
	}

	private static PointF findIntersectWithPoint(PointF p1, PointF p2) {
		float a1 = p1.x;
		float a2 = p2.x;
		float b1 = p1.y;
		float b2 = p2.y;
		if (a1 == a2) {
			a1++;
		}
		PointF result = new PointF();
		result.x = (b2 - b1) / (a1 - a2);
		result.y = a1 * (b2 - b1) / (a1 - a2) + b1;
		return result;

	}

	/**
	 * check point in rect or not
	 * 
	 * @param point
	 * @param rect
	 * @return
	 */
	public static boolean isPointInRect(PointF point, RectF rect) {
		if (point.x > rect.left && point.x < rect.right && point.y > rect.top
				&& point.y < rect.bottom)
			return true;
		return false;
	}

	/**
	 * get center of line
	 * 
	 * @param line
	 * @return
	 */
	public static PointF getCenterOfLine(ALineMod line) {
		PointF point = new PointF();
		point.x = (line.mStart.x + line.mEnd.x) / 2;
		point.y = (line.mStart.y + line.mEnd.y) / 2;
		return point;
	}

	public static float[] getArrFloat(List<PointF> points) {
		float[] result = new float[points.size() * 2];
		int i = 0;
		for (PointF p : points) {
			result[i++] = p.x;
			result[i++] = p.y;
		}
		return result;
	}

	public static ArrayList<PointF> getArrPointF(float[] fs) {
		ArrayList<PointF> result = new ArrayList<PointF>();
		result.add(new PointF(fs[0], fs[1]));
		result.add(new PointF(fs[2], fs[3]));
		result.add(new PointF(fs[4], fs[5]));
		result.add(new PointF(fs[6], fs[7]));
		return result;
	}

	/**
	 * get line from model
	 * 
	 * @param lineMod
	 * @return
	 */
//	private static Line getLineFromMod(ALineMod lineMod) {
//		Vector2D line_p1 = new Vector2D(lineMod.mStart.x, lineMod.mStart.y);
//		Vector2D line_p2 = new Vector2D(lineMod.mEnd.x, lineMod.mEnd.y);
//		return new Line(line_p1, line_p2);
//	}

	/**
	 * find coeff of A,B
	 * 
	 * @param pointA
	 * @param pointB
	 * @return
	 */
	public static PointF findCoefficientA_B(PointF pointA, PointF pointB) {
		PointF result = new PointF();
		if (pointA.x == pointB.x) {
			pointA.x++;
		}

		result.x = Math.abs((pointA.y - pointB.y) / (pointA.x - pointB.x));
		result.y = Math.abs((pointA.y - pointA.x * result.x));
		return result;
	}

	public static Point findCoefficientA_B(Point pointA, Point pointB) {
		Point result = new Point();
		if (pointA.x == pointB.x) {
			pointA.x++;
		}

		result.x = ((pointA.y - pointB.y) / (pointA.x - pointB.x));
		result.y = ((pointA.y - pointA.x * result.x));
		return result;
	}

	/**
	 * is point in right side of line
	 * 
	 * @param point
	 * @param line
	 * @return
	 */
	public static boolean isPointInRightSide(Point point, ALineMod line) {
		float minX = Math.min(line.mStart.x, line.mEnd.x);
		float maxX = Math.max(line.mStart.x, line.mEnd.x);
		PointF start = null;
		PointF end = null;
		if (minX == line.mStart.x) {
			start = line.mStart;
			end = line.mEnd;
		} else {
			end = line.mStart;
			start = line.mEnd;
		}
		// if (point.x < minX || point.x > maxX) return false;
		// Line l = getLineFromMod(line);
		// Vector2D p = new Vector2D(point.x, point.y);
		// if (l.contains(p)) return true;
		//
		// Vector2D p1=null,p2=null;
		// if (minX == line.mStart.x) {
		// p1= new Vector2D(line.mStart.x, line.mStart.y);
		// p2= new Vector2D(line.mEnd.x, line.mEnd.y);
		// }
		// else{
		// p2= new Vector2D(line.mStart.x, line.mStart.y);
		// p1= new Vector2D(line.mEnd.x, line.mEnd.y);
		// }
		// Line l1 = new Line(p1, p);
		// Line l2 = new Line(p2, p);
		// double a = l.getAngle();
		// double a1 = l1.getAngle();
		// double angle = a-a1;
		// System.out.println("NamLH angle "+angle);
		// if (angle<0) return true;
		// return false;
		//
		return ((end.x - start.x) * (point.y - start.y) - (end.y - start.y)
				* (point.x - end.x)) > 0;
	}

	public static boolean checkPointIn2Line(Point point, ALineMod top,
                                            ALineMod bot) {
		boolean check1 = pntInTriangle(point.x, point.y, top.mStart.x,
				top.mStart.y, top.mEnd.x, top.mEnd.y, bot.mStart.x,
				bot.mStart.y);
		boolean check2 = pntInTriangle(point.x, point.y, bot.mStart.x,
				bot.mStart.y, bot.mEnd.x, bot.mEnd.y, top.mEnd.x, top.mEnd.y);
		return (check1 || check2);
	}

	public static boolean pntInTriangle(double px, double py, double x1,
			double y1, double x2, double y2, double x3, double y3) {

		double o1 = getOrientationResult(x1, y1, x2, y2, px, py);
		double o2 = getOrientationResult(x2, y2, x3, y3, px, py);
		double o3 = getOrientationResult(x3, y3, x1, y1, px, py);

		return (o1 == o2) && (o2 == o3);
	}

	private static int getOrientationResult(double x1, double y1, double x2,
			double y2, double px, double py) {
		double orientation = ((x2 - x1) * (py - y1)) - ((px - x1) * (y2 - y1));
		if (orientation > 0) {
			return 1;
		} else if (orientation < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	public static double findAverageY(Point[] points) {
		double sumY = 0;
		for (int i = 0; i < points.length; ++i) {
			sumY += points[i].y;
		}
		return sumY / points.length;
//		double minY = Integer.MAX_VALUE;
//		double maxY = Integer.MIN_VALUE;
//		for (Point point : points) {
//			if (minY > point.y) minY = point.y;
//			if (maxY < point.y) maxY = point.y;
//		}
//		return (minY + maxY)/2;
	}

}

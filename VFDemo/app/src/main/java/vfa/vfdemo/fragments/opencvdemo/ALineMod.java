package vfa.vfdemo.fragments.opencvdemo;

import android.graphics.PointF;

/**
 * Line model for draw on view
 * @author NamLH
 * @since 2012.09.10
 */
public class ALineMod {
	
	  /** Angle with respect to the abscissa axis. */
    private double angle;

    /** Cosine of the line angle. */
    private double cos;

    /** Sine of the line angle. */
    private double sin;

    /** Offset of the frame origin. */
    private double originOffset;


	public PointF mStart;
	public PointF mEnd;
	
	public ALineMod(){
		mStart = new PointF();
		mEnd = new PointF();
	}
	public ALineMod(PointF start, PointF end){
		mStart = start;
		mEnd = end;
		reset(start,end);
	}
	
	public void reset(final PointF p1, final PointF p2) {
        final double dx = p2.x - p1.x;
        final double dy = p2.y - p1.y;
        final double d = Math.hypot(dx, dy);
        if (d == 0.0) {
            angle        = 0.0;
            cos          = 1.0;
            sin          = 0.0;
            originOffset = p1.y;
        } else {
            angle        = Math.PI + Math.atan2(-dy, -dx);
            cos          = Math.cos(angle);
            sin          = Math.sin(angle);
            originOffset = (p2.x * p1.y - p1.x * p2.y) / d;
        }
    }
	
	/** Get the intersection point of the instance and another line.
     * @param other other line
     * @return intersection point of the instance and the other line
     * or null if there are no intersection points
     */
    public PointF intersection(final ALineMod other) {
    	this.reset(mStart, mEnd);
    	other.reset(other.mStart, other.mEnd);
        final double d = sin * other.cos - other.sin * cos;
        if (Math.abs(d) < 1.0e-10) {
            return null;
        }
        return new PointF((float)((cos * other.originOffset - other.cos * originOffset) / d),
                            (float)((sin * other.originOffset - other.sin * originOffset) / d));
    }
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("Start(%f,%f) End(%f,%f)",mStart.x,mStart.y,mEnd.x, mEnd.y);
	}
}

package vfa.vfdemo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Vitalify on 3/1/17.
 */

public class BubbleView extends FrameLayout {

    private int _borderColor = Color.RED;
    private int _solidColor  = Color.WHITE;
    private int _borderWidth = 1;


    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;

    // Private Instance Variables
    ////////////////////////////////////////////////////////////

    private Paint mPaint;
    private int mColor;

    private RectF mBoxRect;
    private int mBoxWidth;
    private int mBoxHeight;
    private float mCornerRad;
    private Rect mBoxPadding = new Rect();

    private Path mPointer;
    private int mPointerWidth;
    private int mPointerHeight;
    private int mPointerAlignment;

    private Paint paint;

    public BubbleView(Context context) {
        super(context);
        initBubble();
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBubble();
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBubble();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initBubble();
    }

    private void initBubbleView(){
        paint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBoxRect = new RectF(0.0f, 0.0f, mBoxWidth, mBoxHeight);
        canvas.drawRoundRect(mBoxRect, mCornerRad, mCornerRad, mPaint);
        updatePointerPath();
        canvas.drawPath(mPointer, mPaint);
    }

    public void setPadding(int left, int top, int right, int bottom) {
        mBoxPadding.left = left;
        mBoxPadding.top = top;
        mBoxPadding.right = right;
        mBoxPadding.bottom = bottom;
    }

    public void setCornerRadius(float cornerRad) {
        mCornerRad = cornerRad;
    }

    public void setPointerAlignment(int pointerAlignment) {
        if (pointerAlignment < 0 || pointerAlignment > 3) {
//            Log.e("BubbleDrawable", "Invalid pointerAlignment argument");
        } else {
            mPointerAlignment = pointerAlignment;
        }
    }

    public void setPointerWidth(int pointerWidth) {
        mPointerWidth = pointerWidth;
    }

    public void setPointerHeight(int pointerHeight) {
        mPointerHeight = pointerHeight;
    }

    // Private Methods
    ////////////////////////////////////////////////////////////

    private void initBubble() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mColor = Color.RED;
        mPaint.setColor(mColor);
        mCornerRad = 0;
        setPointerWidth(40);
        setPointerHeight(40);
    }

    private void updatePointerPath() {
        mPointer = new Path();
        mPointer.setFillType(Path.FillType.EVEN_ODD);

        // Set the starting point
        mPointer.moveTo(pointerHorizontalStart(), mBoxHeight);

        // Define the lines
        mPointer.rLineTo(mPointerWidth, 0);
        mPointer.rLineTo(-(mPointerWidth / 2), mPointerHeight);
        mPointer.rLineTo(-(mPointerWidth / 2), -mPointerHeight);
        mPointer.close();
    }

    private float pointerHorizontalStart() {
        float x = 0;
        switch (mPointerAlignment) {
            case LEFT:
                x = mCornerRad;
                break;
            case CENTER:
                x = (mBoxWidth / 2) - (mPointerWidth / 2);
                break;
            case RIGHT:
                x = mBoxWidth - mCornerRad - mPointerWidth;
        }
        return x;
    }
}

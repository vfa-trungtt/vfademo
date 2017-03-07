package vfa.vfdemo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Vitalify on 2/28/17.
 */

public class VFCalendarView extends FrameLayout {

    private Paint paintGrid;

    LinearLayout _viewContent;


    public VFCalendarView(Context context) {
        super(context);
    }

    public VFCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VFCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VFCalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(){

        paintGrid = new Paint();
        paintGrid.setStyle(Paint.Style.STROKE);
        paintGrid.setColor(Color.GRAY);

        setBackgroundColor(Color.WHITE);

        _viewContent = new LinearLayout(getContext());
        _viewContent.setOrientation(LinearLayout.VERTICAL);

        addView(_viewContent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}

package vfa.vfdemo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;


public class DayCell extends TextView {

    public DayCell(Context context) {
        super(context);
    }

    public DayCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DayCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DayCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(){
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawRect(2, 2, getWidth() -2, getHeight() - 2, paint);
        canvas.drawRect(0, 0, getWidth() , getHeight() , getPaint());
    }
}

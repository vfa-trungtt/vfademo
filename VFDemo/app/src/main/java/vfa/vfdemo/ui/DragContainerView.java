package vfa.vfdemo.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import vfa.vfdemo.R;
import vn.hdisoft.hdilib.utils.LogUtils;


public class DragContainerView extends FrameLayout{

    private ImageView ivThumb;

    private boolean isShowThumb = false;
    private float thumbX,thumbY;
    private float thumbWidth,thumbHeight;

    private RectF rectThumb = new RectF();

    private Paint _paint = new Paint();
    private Bitmap bmp;

    private ViewGroup _container;
    private boolean isDragging = false;

//    FragCalendar _fragCalendar = null;
//    com.nifty.otayori.fragment.home.FragCalendar _fragCalendar1 = null;

    public DragContainerView(Context context) {
        super(context);
        init();
    }

//    public void setFragCalendar(FragCalendar fragment){
//        _fragCalendar = fragment;
//    }
//
//    public void setFragCalendar1(com.nifty.otayori.fragment.home.FragCalendar fragment){
//        _fragCalendar1 = fragment;
//    }

    public DragContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DragContainerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    int _containerHeight = 0;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(_container != null){
            _containerHeight = _container.getHeight();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        if(isShowThumb){
            if(bmp != null){
//                canvas.drawBitmap(bmp,rectBitmap,rectThumb,_paint);
                canvas.drawBitmap(bmp,rectThumb.left,rectThumb.top,_paint);
            }

        }

    }

    Rect rectBitmap;
    public void init(){
        _paint = new Paint();
        _paint.setColor(Color.RED);
        _paint.setStyle(Paint.Style.FILL);

        thumbWidth = 200.0f;
        thumbHeight = 60.0f;

        rectThumb = new RectF();
        rectBitmap = new Rect();

        rectThumb.set(thumbX,thumbY,thumbX + thumbWidth,thumbY + thumbHeight);
        setBackgroundColor(Color.TRANSPARENT);

        initView(R.layout.float_view);
    }

    public Bitmap getThumb(){
        return bmp;
    }

    public void setThumb(Bitmap thumb){
        if(thumb != null){
//            ivThumb.setImageBitmap(thumb);

//            Point size = _fragCalendar.getThumbSize();
//            thumbWidth  = (int)size.x;
//
//            LogUtils.info("set thumb...");
            bmp = Bitmap.createBitmap(thumb);
//            bmp = getResizedBitmapByHeight(bmp,100);
            //bmp = getResizedBitmapByHeight(bmp,20);//ScreenUtils.convertDpToPixel(50, getContext()));
            bmp = getResizedBitmapByWidth(bmp,(int)thumbWidth);//ScreenUtils.convertDpToPixel(50, getContext()));
            thumbHeight = bmp.getHeight();
            rectThumb.set(thumbX,thumbY,thumbX + thumbWidth,thumbY + thumbHeight);

            rectBitmap.set(0,0,bmp.getWidth(),bmp.getHeight());
            postInvalidate();
        }

    }

    public void setThumbMask(Bitmap bmp){
        if(bmp == null) {
            ivThumb.setVisibility(INVISIBLE);
            ivThumb.setImageDrawable(null);
            return;
        }

        ivThumb.setImageBitmap(Bitmap.createBitmap(bmp));
        ivThumb.setVisibility(VISIBLE);

    }

    public void drawThumb(float x,float y){

        _container.setVisibility(View.GONE);
        thumbX = x;
        thumbY = y;

        int[] pos = new int[2];
        getLocationOnScreen(pos);

        float x1 = getLeft();
        float y1 = pos[1];

        float offsetx = x - x1;
        float offsety = y - y1;

        //ThanhNH
//        Point size = _fragCalendar.getThumbSize();
//        float distanceX = -(int)(size.x/2);
//        float distanceY = -(int)size.y*1.5f;
//        thumbX = distanceX + offsetx;
//        thumbY = distanceY + offsety;// - thumbHeight;// - 3*thumbHeight;
//        rectThumb.set(thumbX,thumbY,thumbX + thumbWidth,thumbY + thumbHeight);
//        isShowThumb = true;
//
//        stopAnim();
//        postInvalidate();


    }


    public void clear(){

        isShowThumb = false;
        if (bmp != null) {
            bmp.recycle();
            bmp = null;
        }


        _container.setVisibility(View.INVISIBLE);
        isDragging = false;
        postInvalidate();


    }

    public Bitmap getResizedBitmapByHeight(Bitmap bm, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int newWidth = bm.getWidth()*newHeight/bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        bm = null;
        return resizedBitmap;
    }
    public Bitmap getResizedBitmapByWidth(Bitmap bm, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int newHeight = (int)(height*((float)newWidth/width));

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        bm = null;
        return resizedBitmap;
    }

    public void updatePositionY(int dy){
        if(_container == null) {
            LogUtils.error("NULLL container.");
            return;
        }
        int[] pos = new int[2];
        getLocationOnScreen(pos);
        int y = dy -  pos[1] - _containerHeight/2 ;
        _container.setY(y);

    }
    public void showAnimation(int dy){
        if(_container == null) {
            LogUtils.error("NULLL container.");
            return;
        }

        int[] pos = new int[2];
        getLocationOnScreen(pos);

        if(_containerHeight == 0){
            measureFloatView();
        }
        int y = dy -  pos[1] - _containerHeight/2 ;

//        LogUtils.info("layout Y:"+y + ",height:"+_containerHeight);
//        _container.layout(0,y,_container.getRight(),y + _containerHeight);
        ivThumb.setVisibility(VISIBLE);
        _container.setTranslationY(y);
//        _container.setVisibility(VISIBLE);
        isDragging = true;
        startAnim();

    }

    public void showAnimStart(float y){
        showAnimation((int)y);
        ivThumb.setVisibility(INVISIBLE);
    }

    private int mWidthMeasureSpec = 0;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Log.d("mobeta", "onMeasure called");
        if (_container != null) {
            if (_container.isLayoutRequested()) {
                measureFloatView();
            }

        }
        mWidthMeasureSpec = widthMeasureSpec;
    }

    private void measureFloatView() {
        if (_container != null) {
            measureItem(_container);
            _containerHeight = _container.getMeasuredHeight();
        }
    }

    private void measureItem(View item) {
        ViewGroup.LayoutParams lp = item.getLayoutParams();
        if (lp == null) {
            lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            item.setLayoutParams(lp);
        }
        int wspec = ViewGroup.getChildMeasureSpec(mWidthMeasureSpec, getPaddingLeft() + getPaddingRight(), lp.width);
        int hspec;
        if (lp.height > 0) {
            hspec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
        } else {
            hspec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        item.measure(wspec, hspec);
    }


    View top;
    View bottom;
    TranslateAnimation transTop;
    TranslateAnimation transBottom;
    Animation blinkle;

    private void initView(int layoutId){

        LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _container = (ViewGroup) inflator.inflate(layoutId, null);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        _container.setLayoutParams(params);
        addView(_container);

        ivThumb = (ImageView) _container.findViewById(R.id.ivThumb);


        top = _container.findViewById(R.id.ivTop);
        bottom = _container.findViewById(R.id.ivBottom);


        int dy = 40;
        int duration = 600;
        transTop = new TranslateAnimation(0, 0, 0, dy);
        transTop.setInterpolator(new AccelerateInterpolator());
//        transTop.setStartOffset(100);
        transTop.setDuration(duration);
//        transTop.setFillAfter(false);
        transTop.setRepeatMode(Animation.RESTART);
        transTop.setRepeatCount(Animation.INFINITE);

//        top.startAnimation(transTop);

        transBottom = new TranslateAnimation(0, 0, 0, (-1)*dy);
        transBottom.setInterpolator(new AccelerateInterpolator());
//        transBottom.setStartOffset(100);
        transBottom.setDuration(duration);
//        transBottom.setFillAfter(false);
        transBottom.setRepeatMode(Animation.RESTART);
        transBottom.setRepeatCount(Animation.INFINITE);


        blinkle = new AlphaAnimation(1, 0.0f);  // the 1, 0 here notifies that we want the opacity to go from opaque (1) to transparent (0)
//        blinkle = new AlphaAnimation(0.0f,1);
        blinkle.setInterpolator(new AccelerateInterpolator());
//        blinkle.setStartOffset(100); // Start fading out after 500 milli seconds
        blinkle.setDuration(duration);
        blinkle.setRepeatMode(Animation.RESTART);
        blinkle.setRepeatCount(Animation.INFINITE);

        _container.setVisibility(View.GONE);

        _container.setClipChildren(false);
        setClipChildren(false);

        setTop = new AnimationSet(true);
        setTop.addAnimation(transBottom);
        setTop.addAnimation(blinkle);

        setBottom = new AnimationSet(true);
        setBottom.addAnimation(transTop);
        setBottom.addAnimation(blinkle);
    }

    boolean has_anim = false;
    AnimationSet setTop;
    AnimationSet setBottom;
    public void startAnim(){
        _container.setVisibility(View.VISIBLE);
        _container.setClipChildren(false);
        setClipChildren(false);


        if(has_anim) return;

        has_anim = true;

        top.clearAnimation();
        bottom.clearAnimation();

//        top.setAnimation(setTop);
//        setTop.start();
        top.startAnimation(setTop);
        bottom.startAnimation(setBottom);

//        bottom.startAnimation(transTop);
//        this.startAnimation(blinkle);
//        _container.startAnimation(blinkle);
    }

    public void stopAnim(){
//        LogUtils.info("stop anim");
        _container.setVisibility(View.INVISIBLE);


        if(!has_anim) return;
        has_anim = false;

        top.clearAnimation();
        bottom.clearAnimation();
//        ivThumb.setImageDrawable(null);
//        _container.clearAnimation();
//        this.clearAnimation();

    }
}

package vfa.vfdemo.videoeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.VideoView;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.animation.SimpleValueAnimator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import vn.hdisoft.hdilib.utils.LogUtils;


public class CropVideoView extends VideoView {

    private static final int HANDLE_SIZE_IN_DP = 14;
    private static final int MIN_FRAME_SIZE_IN_DP = 50;
    private static final int FRAME_STROKE_WEIGHT_IN_DP = 1;
    private static final int GUIDE_STROKE_WEIGHT_IN_DP = 1;
    private static final float DEFAULT_INITIAL_FRAME_SCALE = 1f;
    private static final int DEFAULT_ANIMATION_DURATION_MILLIS = 100;
    private static final int DEBUG_TEXT_SIZE_IN_DP = 15;

    private static final int TRANSPARENT = 0x00000000;
    private static final int TRANSLUCENT_WHITE = 0xBBFFFFFF;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int TRANSLUCENT_BLACK = 0xBB000000;

    // Member variables ////////////////////////////////////////////////////////////////////////////

    private int mViewWidth = 0;
    private int mViewHeight = 0;
    private float mScale = 1.0f;
    private float mAngle = 0.0f;
    private float mImgWidth = 0.0f;
    private float mImgHeight = 0.0f;

    private boolean mIsInitialized = false;
    private Matrix mMatrix = null;
    private Paint mPaintTranslucent;
    private Paint mPaintFrame;
    private Paint mPaintBitmap;
    private Paint mPaintDebug;
    private RectF mFrameRect;
    private RectF mInitialFrameRect;
    private RectF mImageRect;
    private PointF mCenter = new PointF();
    private float mLastX, mLastY;
    private boolean mIsRotating = false;
    private boolean mIsAnimating = false;
    private SimpleValueAnimator mAnimator = null;
    private final Interpolator DEFAULT_INTERPOLATOR = new DecelerateInterpolator();
    private Interpolator mInterpolator = DEFAULT_INTERPOLATOR;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Uri mSourceUri = null;
    private Uri mSaveUri = null;
    private int mExifRotation = 0;
    private int mOutputMaxWidth;
    private int mOutputMaxHeight;
    private int mOutputWidth = 0;
    private int mOutputHeight = 0;
    private boolean mIsDebug = false;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.PNG;
    private int mCompressQuality = 100;
    private int mInputImageWidth = 0;
    private int mInputImageHeight = 0;
    private int mOutputImageWidth = 0;
    private int mOutputImageHeight = 0;
    private AtomicBoolean mIsLoading = new AtomicBoolean(false);
    private AtomicBoolean mIsCropping = new AtomicBoolean(false);
    private AtomicBoolean mIsSaving = new AtomicBoolean(false);
    private ExecutorService mExecutor;
    // Instance variables for customizable attributes //////////////////////////////////////////////

    private CropVideoView.TouchArea mTouchArea = CropVideoView.TouchArea.OUT_OF_BOUNDS;

    private CropImageView.CropMode mCropMode = CropImageView.CropMode.SQUARE;
    private CropImageView.ShowMode mGuideShowMode = CropImageView.ShowMode.SHOW_ALWAYS;
    private CropImageView.ShowMode mHandleShowMode = CropImageView.ShowMode.SHOW_ALWAYS;
    private float mMinFrameSize;
    private int mHandleSize;
    private int mTouchPadding = 0;
    private boolean mShowGuide = true;
    private boolean mShowHandle = true;
    private boolean mIsCropEnabled = true;
    private boolean mIsEnabled = true;
    private PointF mCustomRatio = new PointF(1.0f, 1.0f);
    private float mFrameStrokeWeight = 2.0f;
    private float mGuideStrokeWeight = 2.0f;
    private int mBackgroundColor;
    private int mOverlayColor;
    private int mFrameColor;
    private int mHandleColor;
    private int mGuideColor;
    private float mInitialFrameScale; // 0.01 ~ 1.0, 0.75 is default value
    private boolean mIsAnimationEnabled = true;
    private int mAnimationDurationMillis = DEFAULT_ANIMATION_DURATION_MILLIS;
    private boolean mIsHandleShadowEnabled = true;

    private enum TouchArea {
        OUT_OF_BOUNDS, CENTER, LEFT_TOP, RIGHT_TOP, LEFT_BOTTOM, RIGHT_BOTTOM
    }

    public CropVideoView(Context context) {
        super(context);
        init();
    }

    public CropVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CropVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CropVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(viewWidth, viewHeight);

        mViewWidth = viewWidth - getPaddingLeft() - getPaddingRight();
        mViewHeight = viewHeight - getPaddingTop() - getPaddingBottom();
    }

    @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getDrawable() != null) setupLayout(mViewWidth, mViewHeight);
    }

    Paint paint;
    private void init(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);

        setBackgroundColor(Color.TRANSPARENT);

        mExecutor = Executors.newSingleThreadExecutor();

        float density = getDensity();
        mHandleSize = (int) (density * HANDLE_SIZE_IN_DP);
        mMinFrameSize = density * MIN_FRAME_SIZE_IN_DP;
        mFrameStrokeWeight = density * FRAME_STROKE_WEIGHT_IN_DP;
        mGuideStrokeWeight = density * GUIDE_STROKE_WEIGHT_IN_DP;

        mPaintFrame = new Paint();
        mPaintTranslucent = new Paint();
        mPaintBitmap = new Paint();
        mPaintBitmap.setFilterBitmap(true);
        mPaintDebug = new Paint();
        mPaintDebug.setAntiAlias(true);
        mPaintDebug.setStyle(Paint.Style.STROKE);
        mPaintDebug.setColor(WHITE);
        mPaintDebug.setTextSize((float) DEBUG_TEXT_SIZE_IN_DP * density);

        mMatrix = new Matrix();
        mScale = 1.0f;
        mBackgroundColor = TRANSPARENT;
        mFrameColor = WHITE;
        mOverlayColor = TRANSLUCENT_BLACK;
        mHandleColor = WHITE;
        mGuideColor = TRANSLUCENT_WHITE;

//        setupLayout();
    }

    private void setupLayout(int viewW, int viewH) {
        if (viewW == 0 || viewH == 0) return;
        setCenter(new PointF(getPaddingLeft() + viewW * 0.5f, getPaddingTop() + viewH * 0.5f));
        setScale(calcScale(viewW, viewH, mAngle));
        setMatrix();
        mImageRect = calcImageRect(new RectF(0f, 0f, mImgWidth, mImgHeight), mMatrix);

        if (mInitialFrameRect != null) {
            mFrameRect = applyInitialFrameRect(mInitialFrameRect);
        } else {
            mFrameRect = calcFrameRect(mImageRect);
        }

        mIsInitialized = true;
        invalidate();
    }
    private void setScale(float mScale) {
        this.mScale = mScale;
    }
    private RectF applyInitialFrameRect(RectF initialFrameRect) {
        RectF frameRect = new RectF();
        frameRect.set(initialFrameRect.left * mScale, initialFrameRect.top * mScale,
                initialFrameRect.right * mScale, initialFrameRect.bottom * mScale);
        frameRect.offset(mImageRect.left, mImageRect.top);
        float l = Math.max(mImageRect.left, frameRect.left);
        float t = Math.max(mImageRect.top, frameRect.top);
        float r = Math.min(mImageRect.right, frameRect.right);
        float b = Math.min(mImageRect.bottom, frameRect.bottom);
        frameRect.set(l, t, r, b);
        return frameRect;
    }

    private float calcScale(int viewW, int viewH, float angle) {
        mImgWidth = getDrawable().getIntrinsicWidth();
        mImgHeight = getDrawable().getIntrinsicHeight();
        if (mImgWidth <= 0) mImgWidth = viewW;
        if (mImgHeight <= 0) mImgHeight = viewH;
        float viewRatio = (float) viewW / (float) viewH;
        float imgRatio = getRotatedWidth(angle) / getRotatedHeight(angle);
        float scale = 1.0f;
        if (imgRatio >= viewRatio) {
            scale = viewW / getRotatedWidth(angle);
        } else if (imgRatio < viewRatio) {
            scale = viewH / getRotatedHeight(angle);
        }
        return scale;
    }
    public Drawable getDrawable() {
        Bitmap bm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
//        Canvas cnv = new Canvas(bm);
//        int red = 0xffff0000;
//        cnv.drawColor(red);
        Drawable drawable = new BitmapDrawable(bm);
        return drawable;
    }

    private float getRotatedWidth(float angle) {
        return getRotatedWidth(angle, mImgWidth, mImgHeight);
    }

    private float getRotatedWidth(float angle, float width, float height) {
        return angle % 180 == 0 ? width : height;
    }

    private float getRotatedHeight(float angle) {
        return getRotatedHeight(angle, mImgWidth, mImgHeight);
    }

    private float getRotatedHeight(float angle, float width, float height) {
        return angle % 180 == 0 ? height : width;
    }

    private Bitmap getRotatedBitmap(Bitmap bitmap) {
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.setRotate(mAngle, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotateMatrix,
                true);
    }

    private void setCenter(PointF mCenter) {
        this.mCenter = mCenter;
    }

    private RectF calcImageRect(RectF rect, Matrix matrix) {
        RectF applied = new RectF();
        matrix.mapRect(applied, rect);
        return applied;
    }

    private RectF calcFrameRect(RectF imageRect) {
        float frameW = getRatioX(imageRect.width());
        float frameH = getRatioY(imageRect.height());
        float imgRatio = imageRect.width() / imageRect.height();
        float frameRatio = frameW / frameH;
        float l = imageRect.left, t = imageRect.top, r = imageRect.right, b = imageRect.bottom;
        if (frameRatio >= imgRatio) {
            l = imageRect.left;
            r = imageRect.right;
            float hy = (imageRect.top + imageRect.bottom) * 0.5f;
            float hh = (imageRect.width() / frameRatio) * 0.5f;
            t = hy - hh;
            b = hy + hh;
        } else if (frameRatio < imgRatio) {
            t = imageRect.top;
            b = imageRect.bottom;
            float hx = (imageRect.left + imageRect.right) * 0.5f;
            float hw = imageRect.height() * frameRatio * 0.5f;
            l = hx - hw;
            r = hx + hw;
        }
        float w = r - l;
        float h = b - t;
        float cx = l + w / 2;
        float cy = t + h / 2;
        float sw = w * mInitialFrameScale;
        float sh = h * mInitialFrameScale;
        return new RectF(cx - sw / 2, cy - sh / 2, cx + sw / 2, cy + sh / 2);
    }
    private void setMatrix() {
        mMatrix.reset();
        mMatrix.setTranslate(mCenter.x - mImgWidth * 0.5f, mCenter.y - mImgHeight * 0.5f);
        mMatrix.postScale(mScale, mScale, mCenter.x, mCenter.y);
        mMatrix.postRotate(mAngle, mCenter.x, mCenter.y);
    }

    private float getRatioX(float w) {
        switch (mCropMode) {
            case FIT_IMAGE:
                return mImageRect.width();
            case FREE:
                return w;
            case RATIO_4_3:
                return 4;
            case RATIO_3_4:
                return 3;
            case RATIO_16_9:
                return 16;
            case RATIO_9_16:
                return 9;
            case SQUARE:
            case CIRCLE:
            case CIRCLE_SQUARE:
                return 1;
            case CUSTOM:
                return mCustomRatio.x;
            default:
                return w;
        }
    }

    private float getRatioY(float h) {
        switch (mCropMode) {
            case FIT_IMAGE:
                return mImageRect.height();
            case FREE:
                return h;
            case RATIO_4_3:
                return 3;
            case RATIO_3_4:
                return 4;
            case RATIO_16_9:
                return 9;
            case RATIO_9_16:
                return 16;
            case SQUARE:
            case CIRCLE:
            case CIRCLE_SQUARE:
                return 1;
            case CUSTOM:
                return mCustomRatio.y;
            default:
                return h;
        }
    }

    private float getRatioX() {
        switch (mCropMode) {
            case FIT_IMAGE:
                return mImageRect.width();
            case RATIO_4_3:
                return 4;
            case RATIO_3_4:
                return 3;
            case RATIO_16_9:
                return 16;
            case RATIO_9_16:
                return 9;
            case SQUARE:
            case CIRCLE:
            case CIRCLE_SQUARE:
                return 1;
            case CUSTOM:
                return mCustomRatio.x;
            default:
                return 1;
        }
    }

    private float getRatioY() {
        switch (mCropMode) {
            case FIT_IMAGE:
                return mImageRect.height();
            case RATIO_4_3:
                return 3;
            case RATIO_3_4:
                return 4;
            case RATIO_16_9:
                return 9;
            case RATIO_9_16:
                return 16;
            case SQUARE:
            case CIRCLE:
            case CIRCLE_SQUARE:
                return 1;
            case CUSTOM:
                return mCustomRatio.y;
            default:
                return 1;
        }
    }
//    setupLayout(mViewWidth, mViewHeight);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isInEditMode()){
            drawCropFrame(canvas);
        }
    }
    private float getDensity() {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(displayMetrics);
            return displayMetrics.density;
    }

    private void drawCropFrame(Canvas canvas) {
        if (!mIsCropEnabled) return;
        if (mIsRotating) return;
        drawOverlay(canvas);
        drawFrame(canvas);
        if (mShowGuide) drawGuidelines(canvas);
        if (mShowHandle) drawHandles(canvas);
    }

    private void drawOverlay(Canvas canvas) {
        mPaintTranslucent.setAntiAlias(true);
        mPaintTranslucent.setFilterBitmap(true);
        mPaintTranslucent.setColor(mOverlayColor);
        mPaintTranslucent.setStyle(Paint.Style.FILL);
        Path path = new Path();
        RectF overlayRect =
                new RectF((float) Math.floor(mImageRect.left), (float) Math.floor(mImageRect.top),
                        (float) Math.ceil(mImageRect.right), (float) Math.ceil(mImageRect.bottom));
        if (!mIsAnimating && (mCropMode == CropImageView.CropMode.CIRCLE || mCropMode == CropImageView.CropMode.CIRCLE_SQUARE)) {
            path.addRect(overlayRect, Path.Direction.CW);
            PointF circleCenter = new PointF((mFrameRect.left + mFrameRect.right) / 2,
                    (mFrameRect.top + mFrameRect.bottom) / 2);
            float circleRadius = (mFrameRect.right - mFrameRect.left) / 2;
            path.addCircle(circleCenter.x, circleCenter.y, circleRadius, Path.Direction.CCW);
            canvas.drawPath(path, mPaintTranslucent);
        } else {
            path.addRect(overlayRect, Path.Direction.CW);
            path.addRect(mFrameRect, Path.Direction.CCW);
            canvas.drawPath(path, mPaintTranslucent);
        }
    }

    private void drawFrame(Canvas canvas) {
        mPaintFrame.setAntiAlias(true);
        mPaintFrame.setFilterBitmap(true);
        mPaintFrame.setStyle(Paint.Style.STROKE);
        mPaintFrame.setColor(mFrameColor);
        mPaintFrame.setStrokeWidth(mFrameStrokeWeight);
        canvas.drawRect(mFrameRect, mPaintFrame);
    }

    private void drawGuidelines(Canvas canvas) {
        mPaintFrame.setColor(mGuideColor);
        mPaintFrame.setStrokeWidth(mGuideStrokeWeight);
        float h1 = mFrameRect.left + (mFrameRect.right - mFrameRect.left) / 3.0f;
        float h2 = mFrameRect.right - (mFrameRect.right - mFrameRect.left) / 3.0f;
        float v1 = mFrameRect.top + (mFrameRect.bottom - mFrameRect.top) / 3.0f;
        float v2 = mFrameRect.bottom - (mFrameRect.bottom - mFrameRect.top) / 3.0f;
        canvas.drawLine(h1, mFrameRect.top, h1, mFrameRect.bottom, mPaintFrame);
        canvas.drawLine(h2, mFrameRect.top, h2, mFrameRect.bottom, mPaintFrame);
        canvas.drawLine(mFrameRect.left, v1, mFrameRect.right, v1, mPaintFrame);
        canvas.drawLine(mFrameRect.left, v2, mFrameRect.right, v2, mPaintFrame);
    }

    private void drawHandles(Canvas canvas) {
        if (mIsHandleShadowEnabled) drawHandleShadows(canvas);
        mPaintFrame.setStyle(Paint.Style.FILL);
        mPaintFrame.setColor(mHandleColor);
        canvas.drawCircle(mFrameRect.left, mFrameRect.top, mHandleSize, mPaintFrame);
        canvas.drawCircle(mFrameRect.right, mFrameRect.top, mHandleSize, mPaintFrame);
        canvas.drawCircle(mFrameRect.left, mFrameRect.bottom, mHandleSize, mPaintFrame);
        canvas.drawCircle(mFrameRect.right, mFrameRect.bottom, mHandleSize, mPaintFrame);
    }

    private void drawHandleShadows(Canvas canvas) {
        mPaintFrame.setStyle(Paint.Style.FILL);
        mPaintFrame.setColor(TRANSLUCENT_BLACK);
        RectF rect = new RectF(mFrameRect);
        rect.offset(0, 1);
        canvas.drawCircle(rect.left, rect.top, mHandleSize, mPaintFrame);
        canvas.drawCircle(rect.right, rect.top, mHandleSize, mPaintFrame);
        canvas.drawCircle(rect.left, rect.bottom, mHandleSize, mPaintFrame);
        canvas.drawCircle(rect.right, rect.bottom, mHandleSize, mPaintFrame);
    }

}

package vfa.vfdemo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import vn.hdisoft.hdilib.utils.LogUtils;


public class VFCameraView extends SurfaceView implements SurfaceHolder.Callback, PreviewCallback {

    public interface VFCameraListener{
        public void onCaptureDone(Bitmap bmp);
    }

    private VFCameraListener _litener;
    public void setCameraListener(VFCameraListener listener){
        _litener = listener;
    }

    //==========================
	SurfaceHolder mHolder;
    private int formatSurface, widthSurface, heightsurface;

	private Camera _camera;

	int mCamWidth   = 320;
	int mCamHeight  = 240;

	private Parameters mParameters;
	ShutterCallback myShutterCallback;
	PictureCallback myPictureCallback_RAW;
	PictureCallback myPictureCallback_JPG;

	public VFCameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public VFCameraView(Context context) {
		super(context);
		initView(context);
	}

	public void initView(Context context) {
        if(isInEditMode()) return;

		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		mCamHeight = displaymetrics.heightPixels;
		mCamWidth = displaymetrics.widthPixels;

		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		closeDriver();
		destroyDrawingCache();
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		mHolder = holder;

		formatSurface  = format;
		widthSurface   = w;
		heightsurface  = h;

		buildCamera(holder, format, w, h);

		if (_camera != null){
            _camera.setPreviewCallback(VFCameraView.this);
        }
	}

	private void openCamera() {

		try {
			if (_camera == null) {
				_camera = Camera.open();
			}
		} catch (Exception e) {
			closeDriver();
			return;
		}

		if (_camera == null)
			return;

		_camera.setErrorCallback(new ErrorCallback() {

			@Override
			public void onError(int error, Camera camera) {
				closeDriver();
			}
		});

	}

	public void buildCamera(SurfaceHolder holder, int format, int w, int h) {
		int left = getLeft();
		if (left == 0 && holder != null) {

			if (_camera == null) {
				openCamera();
			}
			if (_camera != null) {

				setParamCamera(holder, format, w, h);

			}
		}
	}

	@SuppressLint("NewApi")
	private void setParamCamera(SurfaceHolder holder, int format, int w, int h) {
		if (_camera != null) {

			mParameters = _camera.getParameters();
			mParameters.setPictureFormat(ImageFormat.JPEG);
			mParameters.setJpegQuality(100);
			mParameters.setWhiteBalance(Parameters.WHITE_BALANCE_AUTO);

			List<Size> supportPicSize = mParameters.getSupportedPictureSizes();
			int indexMaxPicture = getMaxSizePic(supportPicSize);
			mParameters.setPictureSize(supportPicSize.get(indexMaxPicture).width,
					supportPicSize.get(indexMaxPicture).height);

			List<Size> supportReviewSize = mParameters.getSupportedPreviewSizes();
			int indextMaxPreview = getMaxSizeReview(supportReviewSize, supportPicSize);

            if (indextMaxPreview > -1) {
				mParameters.setPreviewSize(supportReviewSize.get(indextMaxPreview).width,
						supportReviewSize.get(indextMaxPreview).height);

			} else {
				mParameters.setPreviewSize(640, 480);
			}
            if (Build.VERSION.SDK_INT >= 8) {
                if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    setDisplayOrientation(_camera, 90);
                }
            }

			try {
				_camera.setPreviewDisplay(holder);
                _camera.setPreviewCallback(this);
                _camera.setParameters(mParameters);
                _camera.startPreview();

			} catch (IOException e) {
				e.printStackTrace();
			}

//			if (mParameters.getSupportedFocusModes().contains(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//				mParameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//			} else if (mParameters.getSupportedFocusModes().contains(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
//				mParameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
//			} else if (mParameters.getSupportedFocusModes().contains(Parameters.FOCUS_MODE_MACRO)) {
//				mParameters.setFocusMode(Parameters.FOCUS_MODE_MACRO);
//			} else if (mParameters.getSupportedFocusModes().contains(Parameters.FOCUS_MODE_AUTO)) {
//				mParameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
//			}


		}
	}

	private int getMaxSizePic(List<Size> size) {

		int sizePic = 0;
		if (size.get(0).width > size.get(size.size() - 1).width) {
			for (int i = 0; i < size.size(); i++) {
				if (size.get(i).height * (float) 4 / 3 == size.get(i).width) {
					sizePic = i;
					break;
				}
			}
		} else {
			for (int i = size.size() - 1; i >= 0; i--) {
				if (size.get(i).height * (float) 4 / 3 == size.get(i).width) {
					sizePic = i;
					break;
				}
			}
		}

		return sizePic;

	}

	private int getMaxSizeReview(List<Size> sizeList, List<Size> sizePicList) {
		int sizeIndex = -1;
		if (sizeList.get(0).width > sizeList.get(sizeList.size() - 1).width) {
			for (int i = 0; i < sizeList.size(); i++) {
				if ((sizeList.get(i).height * (float) 4 / 3 == sizeList.get(i).width)) {
					if (findSizeInlist(sizePicList, sizeList.get(i))) {
						sizeIndex = i;
						break;
					}
				}
			}
		} else {
			for (int i = sizeList.size() - 1; i >= 0; i--) {
				if ((sizeList.get(i).height * (float) 4 / 3 == sizeList.get(i).width)) {
					if (findSizeInlist(sizePicList, sizeList.get(i))) {
						sizeIndex = i;
						break;
					}
				}
			}
		}
		return sizeIndex;

	}

	private boolean findSizeInlist(List<Size> sizeList, Size size) {
		for (int i = 0; i < sizeList.size(); i++) {
			if ((sizeList.get(i).width == size.width) && (sizeList.get(i).height == size.height)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onPreviewFrame(final byte[] data, Camera camera) {
		try {
            String model = Build.MODEL;
            final String[] SUPPORTED_DEVICES_LIST_S2 = new String[] { /* SII */
                    "GT-I9100", "GT-I9100G", "GT-I9210", "GT-I9210T", "SHV-E110S", "E120S", "SGH-i727R", "GT-I9100M",
                    "SGH-I757M", "SGH-T989D", "GT-I9108", "GT-I9100P", "SGH-T989", "SGH-I777", "SGH-I927", "SCH-R760",
                    "SC-02C" };

            if (Arrays.asList(SUPPORTED_DEVICES_LIST_S2).contains(model)) {
				if (Build.VERSION.SDK_INT >= 14) {
					System.gc();
				}
			}

			synchronized (this) {
//				if (mHandler != null) {
//					if (mHandler.hasMessages(ConfigLib.HANDLER_UPDATE))
//						return;
//					if (currentTask != null && currentTask.getStatus() == AsyncTask.Status.RUNNING) {
//						return;
//					}
//
//					currentTask = new AsyncTask<Void, Void, Void>() {
//						@Override
//						protected Void doInBackground(Void... params) {
//							try {
//
//								long start = System.currentTimeMillis();
//								Vector[] points = new Vector[1];
//								points[0] = mDetector.detectOnLastFrame(data);
//								mHandler.sendMessage(mHandler.obtainMessage(ConfigLib.HANDLER_UPDATE, points[0]));
//
//							} catch (Exception e) {
//
//								e.printStackTrace();
//							}
//
//							return null;
//						}
//					}.execute();
//
//				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeDriver() {
		if (_camera != null) {
			_camera.cancelAutoFocus();
			_camera.stopPreview();
			_camera.setPreviewCallback(null);
			_camera.release();
			_camera = null;
		}
	}

	public void resumDriver() {
		buildCamera(mHolder, this.formatSurface, this.widthSurface, this.heightsurface);
		if (_camera != null)
			_camera.setPreviewCallback(VFCameraView.this);

	}

	protected void setDisplayOrientation(Camera camera, int angle) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null)
				downPolymorphic.invoke(camera, new Object[] { angle });
		} catch (Exception e1) {
		}
	}

	public Bitmap decodeByteArray(byte[] source, int angle) {
		try {

			System.gc();

			List<Size> supportReviewSize = mParameters.getSupportedPreviewSizes();
			int sizePicReview = getMaxSizePic(supportReviewSize);
			int widthPreview = supportReviewSize.get(sizePicReview).width;
			BitmapFactory.Options o = new BitmapFactory.Options();

			o.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(source, 0, source.length, o);

			int scale = o.outWidth / widthPreview;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bitmap = BitmapFactory.decodeByteArray(source, 0, source.length, o2);

			if (bitmap.getWidth() < 960) {

				BitmapFactory.Options o3 = new BitmapFactory.Options();
				if(scale > 1){
					o3.inSampleSize = scale - 1 ;
					if(o3.inSampleSize == 1 || o3.inSampleSize == 2){
						o3.inSampleSize = 3;
					}
				}
//				LogUtils.memoryUsedInfo("before");
				bitmap = BitmapFactory.decodeByteArray(source, 0, source.length, o3);
//				LogUtils.memoryUsedInfo("after");
				System.gc();
			}
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			System.gc();

//			LogUtils.memoryUsedInfo("after decode:");
			return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

		} catch (Exception e) {
//			LogUtils.memoryUsedInfo("crash:");
			e.printStackTrace();
		}
		return null;
	}
    /*=================*/
    //TODO:NEW METHODS
    public void turnFlashLight(boolean isOn) {
        if (this._camera == null)
            return;
        try {
            Parameters params = this._camera.getParameters();
            if (isOn) {
                params.setFlashMode(Parameters.FLASH_MODE_TORCH);
                this._camera.setParameters(params);
                this._camera.startPreview();
                // params.setFlashMode(Parameters.FLASH_MODE_ON);
            } else {
                params.setFlashMode(Parameters.FLASH_MODE_OFF);
                this._camera.setParameters(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.error("error:" + e.toString());
        }

    }

    public void capture(){
        myShutterCallback = new ShutterCallback() {
            @Override
            public void onShutter() {
                LogUtils.error("onShutter!");
            }
        };
        myPictureCallback_JPG = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] arg0, Camera arg1) {
                LogUtils.error("onPictureTaken!");
                Bitmap bmpBitmap = decodeByteArray(arg0, 90);
                if(_litener != null) _litener.onCaptureDone(bmpBitmap);
            }
        };

        try {
            _camera.setPreviewCallback(null);
            _camera.takePicture(myShutterCallback, null, myPictureCallback_JPG);

        } catch (Exception e) {
            e.printStackTrace();
            closeDriver();
        }

    }
}

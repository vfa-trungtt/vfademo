package vfa.vfdemo.fragments.opencvdemo;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;


public class ImageDetectorLib {

	private static final int AUTO_IMG_HEIGHT = 240;
	private static final int MANUAL_IMG_HEIGHT = 480;
	private static final int AUTO_MIN_AREA = 11000;
	private static final int MANUAL_MIN_AREA = 9000;
	private static int CURVE_TOP_DISTANCE_UP = 60;
	private static int CURVE_TOP_DISTANCE_DOWN = 25;

	private int mSoureWidth = -1;
	private int mSourceHeight = -1;

	private int mDesWidth = -1;
	private int mDesHeight = AUTO_IMG_HEIGHT;
	private double mRatio = 1;

	private int mLayoutWidth = -1;
	private int mLayoutHeight = -1;

	private boolean mIsAuto = true;



	private byte[] mData = null;
	private Mat mDebugMat = null;

	public ImageDetectorLib() {

	}


	public ImageDetectorLib(int srcWidth, int srcHeight) {
		mSoureWidth = srcWidth;
		mSourceHeight = srcHeight;
		computeScale();
	}

	public void updateSourceSize(int srcWidth, int srcHeight) {
		mSoureWidth = srcWidth;
		mSourceHeight = srcHeight;
		computeScale();
	}

	public void setDesHeight(int height) {
		mIsAuto = false;
		mDesHeight = MANUAL_IMG_HEIGHT;
		computeScale();
	}

	public void updateLayoutSize(int layoutWidth, int layoutHeight) {
		mLayoutWidth = layoutWidth;
		mLayoutHeight = layoutHeight;
	}

	public List<ALineMod> detectImage(byte[] data) {
		List<ALineMod> result = new ArrayList<ALineMod>();
		Mat cannyMat = getCannyMatFromData(data);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(cannyMat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

		Mat lines = new Mat();
		int threshold = 80;
		int minLineSize = 30;
		int lineGap = 3;

		Imgproc.HoughLinesP(cannyMat, lines, 1, Math.PI / 180, threshold, minLineSize, lineGap);

		List<ALineMod> lstLines = DetectorUtilsLib.getListLineFromMat(lines);
		lstLines = DetectorUtilsLib.mergeLines(lstLines);
		return result;
	}

	/**
	 * Detect on last frame
	 * 
	 * @param data
	 * @return
	 */
	public Vector<PointF> detectOnLastFrame(byte[] data) {
		mData = data;
		Mat cannyImage = getCannyMatFromData(data);
		Vector<PointF> result = contourDetect(cannyImage, true);
		cannyImage.release();
		return result;
	}

	public Vector<PointF> detectOnLastFrame(Bitmap bm) {
		Mat cannyImage = getCannyMatFromBm(bm, true);
		Vector<PointF> result = contourDetect(cannyImage, true);
		cannyImage.release();
		return result;
	}

	public Vector<PointF> detectOnLastFrameWithNoScale(byte[] data) {
		Mat cannyImage = getCannyMatFromData(data);
		Vector<PointF> result = contourDetect(cannyImage, false);
		cannyImage.release();
		return result;
	}


	private Vector<PointF> contourDetect(Mat src, boolean isScale) {

		Vector<PointF> result = new Vector<PointF>();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(src, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		MatOfPoint maxContours = null;
		double maxArea = -1;
		// temp

		int minArea = 0;
		if (mIsAuto)
			minArea = AUTO_MIN_AREA;
		else
			minArea = MANUAL_MIN_AREA;

		Size size = src.size();

		Point center = new Point(size.width / 2, size.height / 2);
		List<MatOfPoint> hullPoints = new ArrayList<MatOfPoint>();
		List<MatOfPoint> lstCanidate = new ArrayList<MatOfPoint>();

		for (MatOfPoint matOfPoint : contours) {
			double contourArea = Imgproc.contourArea(matOfPoint);

			/*
			 * new code not use convex hull
			 */

			if (contourArea < minArea) {
				continue;
			}

			// Declarations
			MatOfInt hull = new MatOfInt();
			Imgproc.convexHull(matOfPoint, hull);

			MatOfPoint2f contour2f = new MatOfPoint2f();
			MatOfPoint2f mat_hull2f = new MatOfPoint2f();
			MatOfPoint mat_hull_approximated = new MatOfPoint();

			MatOfPoint mat_hull = new MatOfPoint();

			Point[] mplist = matOfPoint.toArray();
			// Hull points' indices
			int[] intlist = hull.toArray();
			hull.release();
			List<Point> plist = new ArrayList<Point>();

			for (int idx = 0; idx < intlist.length; idx++) {
				plist.add(mplist[intlist[idx]]);
			}

			mat_hull.fromList(plist);

			mat_hull.convertTo(contour2f, CvType.CV_32FC2);

			Imgproc.approxPolyDP(contour2f, mat_hull2f, Imgproc.arcLength(contour2f, true) * 0.01, true);

			mat_hull2f.convertTo(mat_hull_approximated, CvType.CV_32S);
			hullPoints.add(mat_hull_approximated);

			int w, h;
			w = (int) size.width;
			h = (int) size.height;

			if (mat_hull_approximated.total() >= 4 && mat_hull_approximated.total() <= 6) {
				Point[] ps = mat_hull_approximated.toArray();
				Point topleft = DetectorUtilsLib.findBestPoint(ps, new Point(0, 0));
				Point topright = DetectorUtilsLib.findBestPoint(ps, new Point(w, 0));
				Point botleft = DetectorUtilsLib.findBestPoint(ps, new Point(0, h));
				Point botright = DetectorUtilsLib.findBestPoint(ps, new Point(w, h));
				if (!DetectorUtilsLib.pointsIsValid(topleft, topright, botleft, botright, w, h)) {
					continue;
				}
				if (maxArea == -1) {


					// CongVC Fix bug opencv
					if (null != mData) {
						boolean res = isHolderSelected(topleft, topright, botleft, botright);
						if (res)
							continue;
					}
					maxContours = mat_hull_approximated;
					maxArea = contourArea;

					lstCanidate.add(mat_hull_approximated);

				} 
				else if (maxArea * 0.9 < contourArea && maxContours != null) {
					Point centerMax = DetectorUtilsLib.getCenterPoint(maxContours);
					Point centerTemp = DetectorUtilsLib.getCenterPoint(mat_hull2f);
					double distanceMax = DetectorUtilsLib.getDistance(center, centerMax);
					double distanceTemp = DetectorUtilsLib.getDistance(center, centerTemp);
					double widthMax = DetectorUtilsLib.getWidthOfListPoint(maxContours);
					double widthTemp = DetectorUtilsLib.getWidthOfListPoint(mat_hull2f);
					// CongVC Fix bug opencv
					if (null != mData) {
						boolean res = isHolderSelected(topleft, topright, botleft, botright);
//						Log.w("Check Holder Corner", "isHolderSelected " + res);
						if (res)
							continue;
					}

					if (distanceMax > distanceTemp || widthMax < widthTemp) {
						maxContours = mat_hull_approximated;
						maxArea = contourArea;

						lstCanidate.add(mat_hull_approximated);
					}
				}
			}
		}


		if (lstCanidate.size() > 1) {
//			Log.e("Detect Blank", "Step 5");
			int w, h;
			w = (int) size.width;
			h = (int) size.height;
			double contourMax = Imgproc.contourArea(lstCanidate.get(lstCanidate.size() - 1));

			Point[] ps = lstCanidate.get(lstCanidate.size() - 1).toArray();
			Point topleft = DetectorUtilsLib.findBestPoint(ps, new Point(0, 0));
			Point topright = DetectorUtilsLib.findBestPoint(ps, new Point(w, 0));
			Point botleft = DetectorUtilsLib.findBestPoint(ps, new Point(0, h));
			Point botright = DetectorUtilsLib.findBestPoint(ps, new Point(w, h));

			ALineMod line1 = new ALineMod(new PointF((float) topleft.x, (float) topleft.y), new PointF((float) botright.x, (float) botright.y));
			ALineMod line2 = new ALineMod(new PointF((float) topright.x, (float) topright.y), new PointF((float) botleft.x, (float) botleft.y));

			PointF centerMax = MathLib.getIntersect(line1, line2);

			for (MatOfPoint matofP : lstCanidate) {
				double contourArea = Imgproc.contourArea(matofP);
				if ((contourArea / contourMax) > 0.8 && (contourArea / contourMax) < 0.9) {

					Point[] points = matofP.toArray();
					Point tleft = DetectorUtilsLib.findBestPoint(points, new Point(0, 0));
					Point tright = DetectorUtilsLib.findBestPoint(points, new Point(w, 0));
					Point bleft = DetectorUtilsLib.findBestPoint(points, new Point(0, h));
					Point bright = DetectorUtilsLib.findBestPoint(points, new Point(w, h));

					ALineMod ln1 = new ALineMod(new PointF((float) tleft.x, (float) tleft.y), new PointF((float) bright.x, (float) bright.y));
					ALineMod ln2 = new ALineMod(new PointF((float) tright.x, (float) tright.y), new PointF((float) bleft.x, (float) bleft.y));

					PointF centerTemp = MathLib.getIntersect(ln1, ln2);
					double res = DetectorUtilsLib.getDistance(centerMax, centerTemp);
					if (res < 10) {
						maxContours = matofP;
						break;
					}

				}
			}
		}

		if (maxContours != null) {
			Point[] ps = maxContours.toArray();
			int w, h;
			int mind;
			w = (int) size.width;
			h = (int) size.height;
			mind = (int) (w / 10);

			Point topleft = DetectorUtilsLib.findBestPoint(ps, new Point(0, 0));
			Point topright = DetectorUtilsLib.findBestPoint(ps, new Point(w, 0));
			Point botleft = DetectorUtilsLib.findBestPoint(ps, new Point(0, h));
			Point botright = DetectorUtilsLib.findBestPoint(ps, new Point(w, h));

			if (DetectorUtilsLib.checkCornerDuplicateWithMinDistance(topleft, topright, botleft, botright, mind)) {
				return result;
			}
			if (!DetectorUtilsLib.checkContourIsNameCardOrClearFile(topleft, topright, botleft, botright)) {
				return result;
			}
						 
			result.add(new PointF((float) topleft.x, (float) topleft.y));
			result.add(new PointF((float) topright.x, (float) topright.y));
			result.add(new PointF((float) botright.x, (float) botright.y));
			result.add(new PointF((float) botleft.x, (float) botleft.y));
			for (PointF pointF : result) {
				fixPointWithRatio(pointF);
			}
			return result;
		} 
		return result;
	}

	private Vector<PointF> contourDetectByHoughLine(Mat src, boolean isScale) {
		Vector<PointF> result = new Vector<PointF>();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(src, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		MatOfPoint maxContours = null;
		double maxArea = -1;

		int minArea = 0;
		if (mIsAuto)
			minArea = AUTO_MIN_AREA;
		else
			minArea = MANUAL_MIN_AREA;

		Size size = src.size();
		Point center = new Point(size.width / 2, size.height / 2);
		List<MatOfPoint> hullPoints = new ArrayList<MatOfPoint>();

		for (MatOfPoint matOfPoint : contours) {

			//
			double contourArea = Imgproc.contourArea(matOfPoint);

			/*
			 * new code not use convex hull
			 */

			if (contourArea < minArea) {
				continue;
			}

			// Declarations
			MatOfInt hull = new MatOfInt();
			Imgproc.convexHull(matOfPoint, hull);

			MatOfPoint2f contour2f = new MatOfPoint2f();
			MatOfPoint2f mat_hull2f = new MatOfPoint2f();
			MatOfPoint mat_hull_approximated = new MatOfPoint();

			MatOfPoint mat_hull = new MatOfPoint();

			Point[] mplist = matOfPoint.toArray();
			// Hull points' indices
			int[] intlist = hull.toArray();

			List<Point> plist = new ArrayList<Point>();

			for (int idx = 0; idx < intlist.length; idx++) {
				plist.add(mplist[intlist[idx]]);
			}

			mat_hull.fromList(plist);

			mat_hull.convertTo(contour2f, CvType.CV_32FC2);

			Imgproc.approxPolyDP(contour2f, mat_hull2f, Imgproc.arcLength(contour2f, true) * 0.01, true);

			mat_hull2f.convertTo(mat_hull_approximated, CvType.CV_32S);
			hullPoints.add(mat_hull_approximated);

			int w, h;
			w = (int) size.width;
			h = (int) size.height;

			if (mat_hull_approximated.total() >= 4 && mat_hull_approximated.total() <= 6) {
				Point[] ps = mat_hull_approximated.toArray();
				Point topleft = DetectorUtilsLib.findBestPoint(ps, new Point(0, 0));
				Point topright = DetectorUtilsLib.findBestPoint(ps, new Point(w, 0));
				Point botleft = DetectorUtilsLib.findBestPoint(ps, new Point(0, h));
				Point botright = DetectorUtilsLib.findBestPoint(ps, new Point(w, h));

				if (!DetectorUtilsLib.pointsIsValid(topleft, topright, botleft, botright, w, h)) {
					continue;
				}
				if (maxArea == -1) {

					double horizontalLine = DetectorUtilsLib.getDistance(topleft, topright);
					double verticalLine = DetectorUtilsLib.getDistance(topleft, botleft);
					double ratio = (horizontalLine > verticalLine ? horizontalLine / verticalLine : verticalLine / horizontalLine);

					if (ratio > 2) {
						continue;
					}

					maxContours = mat_hull_approximated;
					maxArea = contourArea;
				} else if (maxArea * 0.9 < contourArea && maxContours != null) {
					Point centerMax = DetectorUtilsLib.getCenterPoint(maxContours);
					Point centerTemp = DetectorUtilsLib.getCenterPoint(mat_hull2f);
					double distanceMax = DetectorUtilsLib.getDistance(center, centerMax);
					double distanceTemp = DetectorUtilsLib.getDistance(center, centerTemp);
					double widthMax = DetectorUtilsLib.getWidthOfListPoint(maxContours);
					double widthTemp = DetectorUtilsLib.getWidthOfListPoint(mat_hull2f);
					double horizontalLine = (DetectorUtilsLib.getDistance(topleft, topright) + DetectorUtilsLib.getDistance(botleft, botright)) / 2;
					double verticalLine = (DetectorUtilsLib.getDistance(topleft, botleft) + DetectorUtilsLib.getDistance(topright, botright)) / 2;
					double ratio = (horizontalLine > verticalLine ? horizontalLine / verticalLine : verticalLine / horizontalLine);

					if (ratio > 2) {
						continue;
					}

					if (distanceMax > distanceTemp || widthMax < widthTemp) {
						maxContours = mat_hull_approximated;
						maxArea = contourArea;
					}
				}
			}
		}

		if (maxContours != null) {
			Point[] ps = maxContours.toArray();
			int w, h;
			int mind;

			w = (int) size.width;
			h = (int) size.height;
			mind = (int) (w / 10);
			Point topleft = DetectorUtilsLib.findBestPoint(ps, new Point(0, 0));
			Point topright = DetectorUtilsLib.findBestPoint(ps, new Point(w, 0));
			Point botleft = DetectorUtilsLib.findBestPoint(ps, new Point(0, h));
			Point botright = DetectorUtilsLib.findBestPoint(ps, new Point(w, h));
			if (DetectorUtilsLib.checkCornerDuplicateWithMinDistance(topleft, topright, botleft, botright, mind)) {
				return result;
			}
			if (!DetectorUtilsLib.checkContourIsNameCardOrClearFile(topleft, topright, botleft, botright)) {
				return result;
			}

			result.add(new PointF((float) topleft.x, (float) topleft.y));
			result.add(new PointF((float) topright.x, (float) topright.y));
			result.add(new PointF((float) botright.x, (float) botright.y));
			result.add(new PointF((float) botleft.x, (float) botleft.y));
			for (PointF pointF : result) {
				fixPointWithRatio(pointF);
			}
			return result;
		}
		// saveGrayImage(src, "contour.png");
		return result;
	}

	private boolean isHolderSelected(Point topleft, Point topright, Point botleft, Point botright) {

		Mat mYuv = new Mat(mSourceHeight + mSourceHeight / 2, mSoureWidth, CvType.CV_8UC1);
		mYuv.put(0, 0, mData);
		Imgproc.cvtColor(mYuv, mYuv, Imgproc.COLOR_YUV2RGB_NV21);
		mYuv = rotateMatrix(mYuv);

		// check top left
		Point pointTL = topleft.clone();
		pointTL.x /= mRatio;
		pointTL.y /= mRatio;

		int TL_X = (int) (pointTL.x);
		int TL_Y = (int) (pointTL.y);

		Rect rectTL = new Rect(TL_X + 6, TL_Y + 6, 8, 8);
		Mat checkMatTL = mYuv.submat(rectTL);

		int R = (int) (Core.mean(checkMatTL).val[0]);
		int G = (int) (Core.mean(checkMatTL).val[1]);
		int B = (int) (Core.mean(checkMatTL).val[2]);
		boolean checkColorTL = checkRGB(R, G, B, 45);

		if (checkColorTL) {
			return true;
		}

		// check top right
		Point pointTR = topright.clone();
		pointTR.x /= mRatio;
		pointTR.y /= mRatio;

		int TR_X = (int) (pointTR.x);
		int TR_Y = (int) (pointTR.y);

		Rect rectTR = new Rect(TR_X - 12, TR_Y + 6, 8, 8);
		Mat checkMatTR = mYuv.submat(rectTR);
		// saveGrayImage1(checkMatTR, "rectTopRight.png");

		R = 0;
		G = 0;
		B = 0;

		R = (int) (Core.mean(checkMatTR).val[0]);
		G = (int) (Core.mean(checkMatTR).val[1]);
		B = (int) (Core.mean(checkMatTR).val[2]);
		boolean checkColorTR = checkRGB(R, G, B, 45);

		if (checkColorTR) {
			return true;
		}

		// check bottom right
		Point pointBR = botright.clone();
		pointBR.x /= mRatio;
		pointBR.y /= mRatio;

		int BR_X = (int) (pointBR.x);
		int BR_Y = (int) (pointBR.y);

		Rect rectBR = new Rect(BR_X - 12, BR_Y - 10, 8, 8);
		Mat checkMatBR = mYuv.submat(rectBR);
		// saveGrayImage1(checkMatBR, "rectBottomRight.png");

		R = 0;
		G = 0;
		B = 0;

		R = (int) (Core.mean(checkMatBR).val[0]);
		G = (int) (Core.mean(checkMatBR).val[1]);
		B = (int) (Core.mean(checkMatBR).val[2]);

		boolean checkColorBR = checkRGB(R, G, B, 45);

		if (checkColorBR) {
			return true;
		}

		return false;
	}

	

	private boolean checkRGB(int r, int g, int b, int subCon) {

		if ((b > r) && (b > g)) {
			if (((b - r) > subCon) && ((b - g) > 35))
				return true;
		}

		if (((r - g) > subCon) && ((r - b) > subCon))
			return true;

		return false;
	}

	public Vector<Vector<PointF>> detectByHoughLine(Bitmap bm) {
		Mat cannyImage = getCannyMatFromBm(bm, true);

		/** CongVC 2013-01-28 **/

		Vector<PointF> contourDetect = contourDetectByHoughLine(cannyImage, true);
		/** CongVC 2013-01-28 **/
		if (contourDetect.size() > 0) {
			Vector<Vector<PointF>> result = new Vector<Vector<PointF>>();
			result.add(contourDetect);
			return result;
		}
		// end
		float width = cannyImage.width();
		float height = cannyImage.height();
		double rho = 1.0;
		double theta = Math.PI / 180;
		int threshold = 80;// 60
		int lineLength = 30;
		int lineGap = 3;
		Mat lines = new Mat();
		Imgproc.HoughLinesP(cannyImage, lines, rho, theta, threshold, lineLength, lineGap);
		List<ALineMod> lstLines = DetectorUtilsLib.getListLineFromMat(lines);
		Vector<ALineMod> arrLines = new Vector<ALineMod>();
		for (ALineMod aLineMod : lstLines) {
			arrLines.add(aLineMod);
		}
		Vector<ALineMod> arrLineAfterMerge = DetectorUtilsLib.mergeLineInList(arrLines);
		Vector<ALineMod> arrLineAfterFilter = DetectorUtilsLib.filterLines(arrLineAfterMerge, width, height);
		// Sort Lines to Lelf, Top, Right, Bottom
		Vector<ALineMod> arrLinesLeft, arrLinesTop, arrLinesRight, arrLinesBottom;
		ALineMod LeftCenterLine = new ALineMod();
		LeftCenterLine.mStart = new PointF(0, height / 2);// Left Center Point
		LeftCenterLine.mEnd = new PointF(width * 1 / 3, height / 2);
		arrLinesLeft = DetectorUtilsLib.getArrayAfterSort(arrLineAfterFilter, width, height, LeftCenterLine.mStart, LeftCenterLine, 1);

		ALineMod RightCenterLine = new ALineMod();
		RightCenterLine.mStart = new PointF(width * 2 / 3, height / 2);
		RightCenterLine.mEnd = new PointF(width, height / 2);// Right Center
																// Point
		arrLinesRight = DetectorUtilsLib.getArrayAfterSort(arrLineAfterFilter, width, height, RightCenterLine.mStart, RightCenterLine, 1);

		ALineMod TopCenterLine = new ALineMod();
		TopCenterLine.mStart = new PointF(width / 2, 0);// Top Center Point
		TopCenterLine.mEnd = new PointF(width / 2, height * 2 / 5);

		arrLinesTop = DetectorUtilsLib.getArrayAfterSort(arrLineAfterFilter, width, height, TopCenterLine.mStart, TopCenterLine, 2);

		ALineMod BottomCenterLine = new ALineMod();
		BottomCenterLine.mStart = new PointF(width / 2, height * 3 / 5);
		BottomCenterLine.mEnd = new PointF(width / 2, height);// Bottom Center
																// Point

		arrLinesBottom = DetectorUtilsLib.getArrayAfterSort(arrLineAfterFilter, width, height, BottomCenterLine.mStart, BottomCenterLine, 2);

		// Delete Lines Noise
		double widthDelta = 10;
		double angleDelta = 4;
		arrLinesLeft = DetectorUtilsLib.deleteLinesInvalid(arrLinesLeft, widthDelta, angleDelta, width, height);
		arrLinesRight = DetectorUtilsLib.deleteLinesInvalid(arrLinesRight, widthDelta, angleDelta, width, height);
		arrLinesTop = DetectorUtilsLib.deleteLinesInvalid(arrLinesTop, widthDelta, angleDelta, width, height);
		arrLinesBottom = DetectorUtilsLib.deleteLinesInvalid(arrLinesBottom, widthDelta, angleDelta, width, height);

		// Check Lines and Processing
		arrLinesLeft = DetectorUtilsLib.getArrAfterSS(arrLinesLeft, arrLinesRight, 7, 2, width, height);
		arrLinesLeft = DetectorUtilsLib.sortLine(arrLinesLeft, LeftCenterLine.mStart);

		arrLinesRight = DetectorUtilsLib.getArrAfterSS(arrLinesRight, arrLinesLeft, 7, 2, width, height);
		arrLinesRight = DetectorUtilsLib.sortLine(arrLinesRight, RightCenterLine.mEnd);

		arrLinesTop = DetectorUtilsLib.getArrAfterSS(arrLinesTop, arrLinesBottom, 5, 1, width, height);
		arrLinesTop = DetectorUtilsLib.sortLine(arrLinesTop, TopCenterLine.mStart);

		arrLinesBottom = DetectorUtilsLib.getArrAfterSS(arrLinesBottom, arrLinesTop, 5, 1, width, height);
		arrLinesBottom = DetectorUtilsLib.sortLine(arrLinesBottom, BottomCenterLine.mEnd);

		Vector<ALineMod> arrLinesLeftTemp = new Vector<ALineMod>();

		for (ALineMod aLineMod : arrLinesLeft) {

			String positionStartX = String.format("%.4f", aLineMod.mStart.x);
			String positionEndX = String.format("%.4f", aLineMod.mEnd.x);

			if (("0.0000".equals(positionStartX)) || ("0.0000".equals(positionEndX))) {
			} else {
				arrLinesLeftTemp.add(aLineMod);
			}

		}
		arrLinesLeft.clear();
		arrLinesLeft.addAll(arrLinesLeftTemp);

		// After check and get result
		Vector<Vector<PointF>> arrResult = new Vector<Vector<PointF>>();
		Vector<PointF> result = new Vector<PointF>();
		int[] indexs = new int[4];
		boolean rs = DetectorUtilsLib.checkLineLeft(arrLinesLeft, arrLinesTop, arrLinesRight, arrLinesBottom, result, indexs, width, height, 7);
		if (rs) {
			arrResult.add(result);
			int indexTemp = indexs[0];
			boolean isUpTop = false;
			indexs[0] = Math.min(indexs[0] + 1, arrLinesLeft.size() - 1);
			if (indexTemp == indexs[0] && !isUpTop) {
				indexs[1] = Math.min(indexs[1] + 1, arrLinesTop.size() - 1);
				isUpTop = true;
			} else {
				indexs[1] = Math.min(indexs[1], arrLinesTop.size() - 1);
			}
			indexTemp = indexs[2];
			indexs[2] = Math.min(indexs[2] + 1, arrLinesRight.size() - 1);
			if (indexTemp == indexs[2] && !isUpTop) {
				indexs[1] = Math.min(indexs[1] + 1, arrLinesTop.size() - 1);
				isUpTop = true;
			}

			indexs[3] = Math.min(indexs[3], arrLinesBottom.size() - 1);
			result = new Vector<PointF>();
			rs = DetectorUtilsLib.checkLineLeft(arrLinesLeft, arrLinesTop, arrLinesRight, arrLinesBottom, result, indexs, width, height, 7);
			if (rs)
				arrResult.add(result);

			indexs[0] = Math.min(indexs[0], arrLinesLeft.size() - 1);
			indexs[1] = Math.min(indexs[1] + 1, arrLinesTop.size() - 1);
			indexs[2] = Math.min(indexs[2], arrLinesRight.size() - 1);
			indexs[3] = Math.min(indexs[3] + 1, arrLinesBottom.size() - 1);
			result = new Vector<PointF>();

			rs = DetectorUtilsLib.checkLineLeft(arrLinesLeft, arrLinesTop, arrLinesRight, arrLinesBottom, result, indexs, width, height, 8);
			if (rs)
				arrResult.add(result);
		} else {
		}
		// fix point with ratio
		int trueRect = 0;
		Vector<Vector<PointF>> backupResult = new Vector<Vector<PointF>>();
		for (Vector<PointF> vectorPoint : arrResult) {
			if (vectorPoint.size() == 0)
				continue;
			for (PointF pointF : vectorPoint) {
				fixPointWithRatio(pointF);
			}
			PointF topleft = DetectorUtilsLib.findBestPoint(vectorPoint, new PointF(0, 0));
			PointF topright = DetectorUtilsLib.findBestPoint(vectorPoint, new PointF(mSoureWidth, 0));
			PointF botleft = DetectorUtilsLib.findBestPoint(vectorPoint, new PointF(0, mSourceHeight));
			PointF botright = DetectorUtilsLib.findBestPoint(vectorPoint, new PointF(mSoureWidth, mSourceHeight));
			if (!DetectorUtilsLib.checkCornerDuplicate(topleft, topright, botleft, botright) && DetectorUtilsLib.checkContourIsNameCardOrClearFile(topleft, topright, botleft, botright)) {
				trueRect++;
				backupResult.add(vectorPoint);
			}
		}
		//
		if (trueRect == 0)
			return new Vector<Vector<PointF>>();
		else if (trueRect == 1) {
			return backupResult;
		} else if (trueRect == 2) {
			Vector<PointF> p1 = backupResult.get(0);
			Vector<PointF> p2 = backupResult.get(1);
			if (DetectorUtilsLib.checkPointDuplicateWithMinDistance(p1.get(0), p2, 5) && DetectorUtilsLib.checkPointDuplicateWithMinDistance(p1.get(1), p2, 5)
					&& DetectorUtilsLib.checkPointDuplicateWithMinDistance(p1.get(2), p2, 5) && DetectorUtilsLib.checkPointDuplicateWithMinDistance(p1.get(3), p2, 5)) {
				backupResult.remove(1);
				return backupResult;
			} else {
				Vector<PointF> tmp = new Vector<PointF>(backupResult.get(0));
				backupResult.add(tmp);
				return backupResult;
			}

		} else {
			return arrResult;
		}

	}

	public Vector<PointF> detectPointsInTopline(Bitmap bm, PointF topLeft, PointF topRight) {
		Mat cannyEdge = getCannyMatFromBm(bm, false);
		Vector<PointF> result = detectPointsInTopline(cannyEdge, topLeft, topRight);

		cannyEdge.release();
		return result;
	}

	private Vector<PointF> detectPointsInTopline(Mat cannyEdge, PointF topLeft, PointF topRight) {
		// compute mindistance
		double width = cannyEdge.size().width;
		double height = cannyEdge.size().height;
		int minDistance = 20;
		if (width > height)
			minDistance = (int) (width / 20);
		else
			minDistance = (int) (height / 20);
		CURVE_TOP_DISTANCE_UP = minDistance * 2;
		CURVE_TOP_DISTANCE_DOWN = minDistance;
//
//		if (CameraViewActivity.cameraMode == ConfigLib.CAMERA_MODE_SHOCNOTE) {
//			CURVE_TOP_DISTANCE_UP /= 3;
//			CURVE_TOP_DISTANCE_DOWN /= 5;
//		}

		Vector<Point> resultArr = new Vector<Point>();

		int line2Distance = CURVE_TOP_DISTANCE_DOWN;
		int line2DistanceTop = CURVE_TOP_DISTANCE_UP;

		ALineMod twoCornerLine2 = new ALineMod();
		twoCornerLine2.mStart = new PointF(topLeft.x, topLeft.y + line2Distance);
		twoCornerLine2.mEnd = new PointF(topRight.x, topRight.y + line2Distance);

		ALineMod twoCornerLine3 = new ALineMod();
		twoCornerLine3.mStart = new PointF(topLeft.x, topLeft.y - line2DistanceTop);
		twoCornerLine3.mEnd = new PointF(topRight.x, topRight.y - line2DistanceTop);

		// Get the Canny edge image
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(cannyEdge, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		MatOfPoint2f maxPerimeterContours = null;
		double maxPerimeter = -1;

		MatOfPoint2f maxPerimeterContoursFail = null;
		double maxPerimeterFail = -1;
		Vector<Point> arrPointOnTop = new Vector<Point>();
		// find a rectangle that has max are
		for (MatOfPoint matOfPoint : contours) {
			MatOfPoint2f curve = new MatOfPoint2f();
			matOfPoint.convertTo(curve, CvType.CV_32FC2);
			MatOfPoint2f approxCurve = new MatOfPoint2f();
			Imgproc.approxPolyDP(curve, approxCurve, matOfPoint.total() * 0.002, false);
			boolean tempFlag = false;
			Point[] approxPoints = approxCurve.toArray();

			for (Point ap : approxPoints) {
				if (!MathLib.isPointInRightSide(ap, twoCornerLine2) || MathLib.isPointInRightSide(ap, twoCornerLine3)) {// Note:		
					tempFlag = true;
					break;
				}
			}
			double perimeter = Imgproc.arcLength(curve, false);

			if (!tempFlag) {
				if (maxPerimeter == -1 || maxPerimeter < perimeter) {
					maxPerimeter = perimeter;
					maxPerimeterContours = approxCurve;
				}
			}
			if (maxPerimeterFail == -1 || maxPerimeterFail < perimeter) {
				maxPerimeterFail = perimeter;
				maxPerimeterContoursFail = approxCurve;
			}
		}

		if (maxPerimeterContours == null) {
			maxPerimeterContours = maxPerimeterContoursFail;
		}
		if (maxPerimeterContours != null) {
			Point[] points = maxPerimeterContours.toArray();
			for (Point pt : points) {
				arrPointOnTop.add(pt);
			}
		}
		if (arrPointOnTop.size() == 0) {
			return new Vector<PointF>();
		}
		// check and remove some point
		Vector<Point> tmpListPoint = new Vector<Point>();
		for (Point point : arrPointOnTop) {
			boolean check = MathLib.checkPointIn2Line(point, twoCornerLine3, twoCornerLine2);
			if (check) {
				tmpListPoint.add(point);
			}

		}
		arrPointOnTop = tmpListPoint;
		// end
		Collections.sort(arrPointOnTop, new Comparator<Point>() {
			@Override
			public int compare(Point lhs, Point rhs) {
				if (lhs.x > rhs.x)
					return 1;
				else if (lhs.x < rhs.x)
					return -1;
				return 0;
			}
		});

		if (arrPointOnTop.size() == 0) {
			return new Vector<PointF>();
		}
		Point topPoint1 = arrPointOnTop.firstElement();
		Point topPoint2 = arrPointOnTop.lastElement();
		Point pointMaxDistance1 = findMaxCurvedPointBetweenPoint(arrPointOnTop, topPoint1, topPoint2);
		Point pointMaxDistance2 = findMaxCurvedPointBetweenPoint(arrPointOnTop, topPoint1, pointMaxDistance1);
		Point pointMaxDistance3 = findMaxCurvedPointBetweenPoint(arrPointOnTop, pointMaxDistance1, topPoint2);

		resultArr.clear();
		resultArr.add(pointMaxDistance1);
		resultArr.add(pointMaxDistance2);
		resultArr.add(pointMaxDistance3);

		Point pointMaxDistance4a = findMaxCurvedPointBetweenPoint(arrPointOnTop, pointMaxDistance2, topPoint1);
		Point pointMaxDistance5a = findMaxCurvedPointBetweenPoint(arrPointOnTop, pointMaxDistance1, pointMaxDistance2);

		Point pointMaxDistance6a = findMaxCurvedPointBetweenPoint(arrPointOnTop, pointMaxDistance3, topPoint2);
		Point pointMaxDistance7a = findMaxCurvedPointBetweenPoint(arrPointOnTop, pointMaxDistance1, pointMaxDistance3);

		resultArr.add(pointMaxDistance4a);
		resultArr.add(pointMaxDistance5a);
		resultArr.add(pointMaxDistance6a);
		resultArr.add(pointMaxDistance7a);

		Vector<PointF> result = new Vector<PointF>();
		for (Point point : resultArr) {
			PointF pF = new PointF((float) point.x, (float) point.y);
			PointF pFTemp = new PointF((float) point.x, (float) point.y);

			if (DetectorUtilsLib.checkPointDuplicateWithMinDistance(pFTemp, result, minDistance))
				continue;
			if (DetectorUtilsLib.getDistance(topLeft, pF) < minDistance)
				continue;
			if (DetectorUtilsLib.getDistance(topRight, pF) < minDistance)
				continue;
			result.add(pF);
		}

		Collections.sort(result, new Comparator<PointF>() {
			@Override
			public int compare(PointF lhs, PointF rhs) {
				if (lhs.x > rhs.x)
					return 1;
				else if (lhs.x < rhs.x)
					return -1;
				return 0;
			}
		});
		// NamLH return all point detected
		if (result.size() > 0 && result.size() < 7)
			return result;
		else
			return new Vector<PointF>();
	}

	/**
	 * Compute scale
	 */
	private void computeScale() {
		mRatio = ((double) mDesHeight) / mSourceHeight;
		mDesWidth = (int) (mRatio * mSoureWidth);
	}

	

	/**
	 * fix point with radio
	 * 
	 * @param des
	 */
	private void fixPointWithRatio(PointF des) {
		des.x /= mRatio;
		des.y /= mRatio;
		double rx = 1;
		double ry = 1;
		if (mLayoutHeight > 0 && mLayoutWidth > 0) {
			if (mLayoutHeight != mSourceHeight) {
				rx = ((double) mLayoutHeight) / mSourceHeight;
			}
			if (mLayoutWidth != mSoureWidth) {
				ry = ((double) mLayoutWidth) / mSoureWidth;
			}
			des.x *= rx;
			des.y *= ry;
		}
	}



	/**
	 * get canny image
	 * 
	 * @param data
	 * @return mat
	 */
	private Mat getCannyMatFromData(byte[] data) {
		mIsAuto = true;
		Mat mYuv = new Mat(mSourceHeight + mSourceHeight / 2, mSoureWidth, CvType.CV_8UC1);
		mYuv.put(0, 0, data);
		// Mat mRgba = new Mat();

		Mat mGray = new Mat();
		Imgproc.cvtColor(mYuv, mGray, Imgproc.COLOR_YUV2GRAY_NV21, 1);
		Imgproc.resize(mGray, mGray, new Size(mDesWidth, mDesHeight));
		mGray = rotateMatrix(mGray);// Rotate image


		Imgproc.GaussianBlur(mGray, mGray, new Size(5, 5), 0, 0);

		Mat kernel_erode = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3), new Point(-1, -1));
		Imgproc.erode(mGray, mGray, kernel_erode);

		Core.bitwise_not(mGray, mGray);

		Mat kernelEx = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3), new Point(-1, -1));
		Imgproc.morphologyEx(mGray, mGray, Imgproc.MORPH_OPEN, kernelEx);

		Imgproc.medianBlur(mGray, mGray, 3);
		
		Mat cannyImage = new Mat();
		Imgproc.Canny(mGray, cannyImage, 30, 70, 3, true);

		mYuv.release();
		mGray.release();
		kernel_erode.release();
		kernelEx.release();
		return cannyImage;

	}

	/**
	 * get canny image
	 * 
	 * @param bm
	 * @return
	 */
	private Mat getCannyMatFromBm(Bitmap bm, boolean isNeedResize) {
		mIsAuto = false;
		Mat srcMat = new Mat();
		Utils.bitmapToMat(bm, srcMat);
		Mat mGray = new Mat();
		Imgproc.cvtColor(srcMat, mGray, Imgproc.COLOR_RGB2GRAY, 1);
		if (isNeedResize) {
			Imgproc.resize(mGray, mGray, new Size(mDesWidth, mDesHeight));
			Imgproc.GaussianBlur(mGray, mGray, new Size(3, 3), 1);
			mGray.convertTo(mGray, -1, 1.2, 0.5);
			Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5), new Point(1, 1));
			Imgproc.morphologyEx(mGray, mGray, Imgproc.MORPH_CLOSE, kernel);
		} else {
			Imgproc.blur(mGray, mGray, new Size(3, 3));
			Imgproc.GaussianBlur(mGray, mGray, new Size(3, 3), 1);
		}

		int minThreshold = 35;
		int cannyThreshold = minThreshold + 80;
		Mat cannyImage = new Mat();

		Imgproc.Canny(mGray, cannyImage, minThreshold, cannyThreshold, 3, true);

		return cannyImage;
	}

	/**
	 * rotate bitmap
	 * 
	 * @param src
	 * @return
	 */
	private static Mat rotateMatrix(Mat src) {
		Mat des = new Mat();
		Core.transpose(src, des);
		Core.flip(des, des, 1);
		return des;
	}

	/**
	 * find max curve point
	 * 
	 * @param points
	 * @param point1
	 * @param point2
	 * @return
	 */
	private Point findMaxCurvedPointBetweenPoint(Vector<Point> points, Point point1, Point point2) {
		double xMax = Math.max(point1.x, point2.x);
		double xMin = Math.min(point1.x, point2.x);

		int numberOfCornerOnTop = points.size();
		Point result = new Point();

		double maxDistance1 = -1;
		Point point = MathLib.findCoefficientA_B(point1.clone(), point2.clone());

		for (int i = 0; i < numberOfCornerOnTop; ++i) {
			Point pi = points.get(i);
			if (pi.x < xMin || pi.x > xMax)
				continue;
			double distance = DetectorUtilsLib.getDistance(point.x, point.y, pi);

			if (maxDistance1 < distance || (point1.x == result.x && point1.y == result.y)) {
				maxDistance1 = distance;
				result = points.get(i).clone();
			}
		}
		return result;

	}

	

	public Bitmap getDebugBitmap(byte[] data) {
		mDebugMat = getCannyMatFromData(data);
		if (mDebugMat == null)
			return null;
		Mat dst = new Mat();
		Imgproc.cvtColor(mDebugMat, dst, Imgproc.COLOR_GRAY2RGBA);
		Bitmap bm = Bitmap.createBitmap(dst.width(), dst.height(), Config.ARGB_8888);
		Utils.matToBitmap(dst, bm);
		return bm;
	}


	public Bitmap processData (byte[] data, int rotate, int width, int height) {

		Bitmap bmp = BitmapFactory.decodeByteArray(data , 0, data.length);
		rotateBitmap(bmp, rotate);
		Mat orig = new Mat(bmp.getHeight(),bmp.getWidth(),CvType.CV_8UC3);
		Bitmap myBitmap32 = bmp.copy(Config.ARGB_8888, true);
		Utils.bitmapToMat(myBitmap32, orig);
		Imgproc.cvtColor(orig, orig, Imgproc.COLOR_BGR2RGB,4);


		 Bitmap bmpOut = Bitmap.createBitmap(bmp.getHeight(),bmp.getWidth(),
                 Config.ARGB_8888);

		 	Utils.matToBitmap(orig,bmpOut);
	    return bmpOut;
	}
	
	/**
	 * Rotate bitmap get from camera
	 * 
	 * @param src
	 * @return
	 */
	public Bitmap rotateBitmap(Bitmap src, int rotate) {

		Mat srcMat = new Mat();
		Utils.bitmapToMat(src, srcMat);
		Mat desMat = null;
		switch (rotate) {
		case 90:
			desMat = rotateMatrix(srcMat);
			break;
		case 180:
		case -180:
			desMat = rotateMatrix(srcMat);
			desMat = rotateMatrix(desMat);
			break;
		case -90:
		case 270:
			desMat = rotateMatrix(srcMat);
			desMat = rotateMatrix(desMat);
			desMat = rotateMatrix(desMat);
			break;
		default:
			desMat = srcMat;
			break;
		}

		Bitmap dst = Bitmap.createBitmap(desMat.width(), desMat.height(), Config.ARGB_8888);
		Utils.matToBitmap(desMat, dst);
		desMat.release();
		srcMat.release();
		return dst;
	}

}

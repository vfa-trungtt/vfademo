package vfa.vfdemo.fragments.opencvdemo;

import android.graphics.PointF;
import android.graphics.RectF;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DetectorUtilsLib {

	public static Point getCenterPoint(MatOfPoint src) {
		Rect rect = Imgproc.boundingRect(src);
		return new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
	}

	public static Point getCenterPoint(MatOfPoint2f src) {
		MatOfPoint tmp = new MatOfPoint(src.toArray());
		Rect rect = Imgproc.boundingRect(tmp);
		return new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
	}
	
	public static int getWidthOfListPoint(MatOfPoint src) {
		MatOfPoint tmp = new MatOfPoint(src.toArray());
		Rect rect = Imgproc.boundingRect(tmp);
		return rect.width;
	}
	
	public static int getWidthOfListPoint(MatOfPoint2f src) {
		MatOfPoint tmp = new MatOfPoint(src.toArray());
		Rect rect = Imgproc.boundingRect(tmp);
		return rect.width;
	}

	public static double getDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}

	public static double getDistanceFromA(double a1, double b1, Point point) {
		double distanceTemp = Math.abs(a1 * point.x - point.y + b1)
				/ Math.sqrt(a1 * a1 + 1);
		return distanceTemp;
	}

	public static double getDistance(double a, double b, Point p2) {
		return Math.abs(a * p2.x - p2.y + b) / Math.sqrt(a * a + 1);

	}

	public static double getDistance(PointF p1, PointF p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}

	public static List<ALineMod> getListLineFromMat(Mat lines) {
		List<ALineMod> result = new ArrayList<ALineMod>();
		for (int x = 0; x < lines.cols(); x++) {
			double[] vec = lines.get(0, x);
			double x1 = vec[0], y1 = vec[1], x2 = vec[2], y2 = vec[3];
			PointF start = new PointF((float) x1, (float) y1);
			PointF end = new PointF((float) x2, (float) y2);
			result.add(new ALineMod(start, end));
		}
		return result;
	}

	public static PointF getMidPoint(PointF[] points) {
		PointF result = new PointF();
		float sx = 0;
		float sy = 0;
		for (PointF pointF : points) {
			sx += pointF.x;
			sy += pointF.y;
		}
		result.x = sx / points.length;
		result.y = sy / points.length;
		return result;
	}

	public static PointF getMidPoint(List<PointF> points) {
		PointF result = new PointF();
		float sx = 0;
		float sy = 0;
		for (PointF pointF : points) {
			sx += pointF.x;
			sy += pointF.y;
		}
		result.x = sx / points.size();
		result.y = sy / points.size();
		return result;
	}

	public static List<ALineMod> mergeLines(List<ALineMod> source) {
		// Find the lines nearly merge to one line
		// Horizal
		int N = source.size();
		List<ALineMod> groupLines = source.subList(0, N);

		for (int i = 0; i < N; i++) {
			ALineMod tmp = source.get(i);
			if (!groupLines.contains(tmp))
				continue;
			for (int j = i + 1; j < N; j++) {
				ALineMod line = source.get(j);
				double angle = MathLib.getAngle(tmp, line);
				if (angle <= Math.PI / 45
						|| (angle >= Math.PI + Math.PI * 44 / 45 && angle <= Math.PI * 2)) {
					double d1 = MathLib.getDistance(line.mStart, tmp);
					double d2 = MathLib.getDistance(line.mEnd, tmp);
					if (Math.max(d1, d2) < 5) {
						groupLines.remove(line);
					}
				}
			}
		}
		return groupLines;
	}

	public static void filterLineWithBorder(List<ALineMod> source, float width,
			float height) {
		ALineMod top = new ALineMod(new PointF(0, 0), new PointF(width, 0));
		ALineMod bot = new ALineMod(new PointF(0, height), new PointF(width,
				height));
		ALineMod left = new ALineMod(new PointF(0, 0), new PointF(0, height));
		ALineMod right = new ALineMod(new PointF(width, 0), new PointF(width,
				height));
		RectF screen = new RectF(0, 0, width, height);

		for (ALineMod line : source) {
			PointF iTop, iLeft, iRight, iBot;
			int t = 0;
			iTop = MathLib.getIntersect(line, top);
			if (iTop.x < 0 || iTop.x > width)
				iTop = null;
			else
				line.mStart = iTop;
			t++;
			iLeft = MathLib.getIntersect(line, left);
			if (iLeft.y < 0 || iLeft.y > height)
				iLeft = null;
			else {
				if (t == 2)
					break;
				if (t == 0)
					line.mStart = iLeft;
				else
					line.mEnd = iLeft;
				t++;
			}
			iRight = MathLib.getIntersect(line, right);
			if (iRight.y < 0 || iRight.y > height)
				iRight = null;
			else {
				if (t == 2)
					break;
				if (t == 0)
					line.mStart = iRight;
				else
					line.mEnd = iRight;
				t++;
			}
			iBot = MathLib.getIntersect(line, bot);
			if (iBot.x < 0 || iBot.x > width)
				iBot = null;
			else {
				if (t == 2)
					break;
				if (t == 0)
					line.mStart = iBot;
				else
					line.mEnd = iBot;
				t++;
			}

		}
	}

	public static Vector<ALineMod> mergeLineInList(Vector<ALineMod> list) {
		Vector<ALineMod> resultList = new Vector<ALineMod>();
		Vector<Integer> checkedList = new Vector<Integer>();
		Vector<Vector<ALineMod>> groupedList = new Vector<Vector<ALineMod>>();

		for (int i = 0; i < list.size(); i++) {
			if (checkedList.indexOf(i) == -1) {
				checkedList.add(i);
				Vector<ALineMod> subList = new Vector<ALineMod>();
				subList.add(list.get(i));

				for (int j = i + 1; j < list.size(); j++) {
					if (checkedList.indexOf(j) == -1) {
						ALineMod line = list.get(i);
						ALineMod lineTemp = list.get(j);

						double angle = MathLib.getAngle(line, lineTemp);

						if (Math.abs(angle) <= 2
								|| (Math.abs(angle) <= 180 && Math.abs(angle) >= 178)) {

							PointF pt1 = line.mStart;
							PointF pt2 = line.mEnd;
							double distance1 = MathLib.getDistance(pt1,
									lineTemp);
							double distance2 = MathLib.getDistance(pt2,
									lineTemp);

							if (Math.max(distance1, distance2) < 5) {
								checkedList.add(j);
								subList.add(list.get(j));
							}
						}
					}
				}

				groupedList.add(subList);
			}
		}

		for (int i = 0; i < groupedList.size(); i++) {
			Vector<ALineMod> subList = groupedList.get(i);

			int maxY = 0;
			int minY = -1;
			PointF maxYPoint = new PointF();
			PointF minYPoint = new PointF();

			int maxX = 0;
			int minX = -1;
			PointF maxXPoint = new PointF();
			PointF minXPoint = new PointF();
			for (int j = 0; j < subList.size(); j++) {
				ALineMod line = subList.get(j);

				PointF pt1 = line.mStart;
				PointF pt2 = line.mEnd;

				double theta = Math.atan((double) (pt2.y - pt1.y)
						/ (pt2.x - pt1.x));
				float degree = (float) Math.toDegrees(theta);

				if ((degree >= -90 && degree < -45)
						|| (degree > 45 && degree <= 90)) {
					if (maxY < pt1.y) {
						maxY = (int) pt1.y;
						maxYPoint = pt1;
					}
					if (maxY < pt2.y) {
						maxY = (int) pt2.y;
						maxYPoint = pt2;
					}

					if (minY == -1) {
						minY = (int) pt1.y;
						minYPoint = pt1;

						if (minY > pt2.y) {
							minY = (int) pt2.y;
							minYPoint = pt2;
						}
					} else {
						if (minY > pt1.y) {
							minY = (int) pt1.y;
							minYPoint = pt1;
						}
						if (minY > pt2.y) {
							minY = (int) pt2.y;
							minYPoint = pt2;
						}
					}
				}
				if ((degree >= -45 && degree <= 0)
						|| (degree >= 0 && degree <= 45)) {
					if (maxX < pt1.x) {
						maxX = (int) pt1.x;
						maxXPoint = pt1;
					}
					if (maxX < pt2.x) {
						maxX = (int) pt2.x;
						maxXPoint = pt2;
					}
					if (minX == -1) {
						minX = (int) pt1.x;
						minXPoint = pt1;

						if (minX > pt2.x) {
							minX = (int) pt2.x;
							minXPoint = pt2;
						}
					} else {
						if (minX > pt1.x) {
							minX = (int) pt1.x;
							minXPoint = pt1;
						}
						if (minX > pt2.x) {
							minX = (int) pt2.x;
							minXPoint = pt2;
						}
					}

				}
			}
			if (maxX != 0) {
				ALineMod line = new ALineMod(minXPoint, maxXPoint);
				resultList.add(line);

			}
			if (maxY != 0) {
				ALineMod line = new ALineMod(minYPoint, maxYPoint);
				resultList.add(line);
			}
		}

		return resultList;

	}

	/**
	 * filter list lines
	 * 
	 * @param result
	 * @param width
	 * @param height
	 * @return
	 */
	public static Vector<ALineMod> filterLines(Vector<ALineMod> result,
			float width, float height) {
		Vector<ALineMod> arrRS = new Vector<ALineMod>();
		double minLength = 50;
		for (int i = 0; i < result.size(); i++) {
			ALineMod line = result.get(i);
			PointF point1 = line.mStart;
			PointF point2 = line.mEnd;

			double length = DetectorUtilsLib.getDistance(point1, point2);
			if (length > minLength) {
				ALineMod line1 = new ALineMod(new PointF(0, 0), new PointF(0,
						height));
				ALineMod line2 = new ALineMod(new PointF(0, 0), new PointF(
						width, 0));
				ALineMod line3 = new ALineMod(new PointF(width, 0), new PointF(
						width, height));
				ALineMod line4 = new ALineMod(new PointF(0, height),
						new PointF(width, height));

				ALineMod linetest = new ALineMod(point1, point2);
//				System.out.println("NamLH filterline linetest "
//						+ linetest.toString());
				PointF p1 = MathLib.getIntersect(linetest, line1);
				PointF p2 = MathLib.getIntersect(linetest, line2);
				PointF p3 = MathLib.getIntersect(linetest, line3);
				PointF p4 = MathLib.getIntersect(linetest, line4);

				PointF rs1 = new PointF();
				PointF rs2 = new PointF();
				boolean haveRS1 = false;
				if (p1.x >= 0 && p1.x <= width && p1.y >= 0 && p1.y <= height) {
					if (!haveRS1) {
						rs1 = p1;
						haveRS1 = true;
					} else {
						rs2 = p1;
					}
				}
				if (p2.x >= 0 && p2.x <= width && p2.y >= 0 && p2.y <= height) {
					if (!haveRS1) {
						rs1 = p2;
						haveRS1 = true;
					} else {
						rs2 = p2;
					}
				}
				if (p3.x >= 0 && p3.x <= width && p3.y >= 0 && p3.y <= height) {
					if (!haveRS1) {
						rs1 = p3;
						haveRS1 = true;
					} else {
						rs2 = p3;
					}
				}
				if (p4.x >= 0 && p4.x <= width && p4.y >= 0 && p4.y <= height) {
					if (!haveRS1) {
						rs1 = p4;
						haveRS1 = true;
					} else {
						rs2 = p4;
					}
				}
				point1 = rs1;
				point2 = rs2;
				ALineMod l = new ALineMod(point1, point2);
				arrRS.add(l);
			}
		}
		return arrRS;
	}

	/**
	 * get array of line after sort
	 * 
	 * @param arr
	 * @param width
	 * @param height
	 * @param center
	 * @param centerLine
	 * @param type
	 * @return
	 */
	public static Vector<ALineMod> getArrayAfterSort(Vector<ALineMod> arr,
			float width, float height, PointF center, ALineMod centerLine,
			int type) {
		Vector<ALineMod> arrLeft = new Vector<ALineMod>();
		for (int i = 0; i < arr.size(); i++) {
			PointF p = new PointF();
			boolean rs = checkLineWhenSort(arr.get(i), centerLine, p, type);
			double angle = MathLib.getAngle(arr.get(i), centerLine);
			boolean checkAngle = checkAngle(angle, 2);// [ImageUtilities
														// checkAngcle:angle
														// withDelta:55];
			if (rs && !checkAngle) {
				arrLeft.add(arr.get(i));
			}
		}
		arrLeft = sortLine(arrLeft, center);
		return arrLeft;

	}

	/**
	 * get arr after sort
	 * 
	 * @param arr
	 * @param arr2
	 * @param delta
	 * @param type
	 * @param width
	 * @param height
	 * @return
	 */
	public static Vector<ALineMod> getArrAfterSS(Vector<ALineMod> arr,
			Vector<ALineMod> arr2, double delta, int type, float width,
			float height) {
		Vector<ALineMod> testLines = null;
		if (type == 1) {
			testLines = getArrAfterSSWithType1(arr, arr2, delta, width, height);
		} else {
			testLines = getArrAfterSSWithType2(arr, arr2, delta, width, height);
		}
		if (testLines.size() > 1) {
			return testLines;
		} else {
			return arr;
		}
	}

	public static Vector<ALineMod> deleteLinesInvalid(
			Vector<ALineMod> arrLines, double delta, double angleDelta,
			float width, float height) {
		Vector<ALineMod> resultLines = arrLines;
		if (resultLines.size() > 2) {
			for (int i = 0; i < resultLines.size() - 1; i++) {
				for (int j = i + 1; j < resultLines.size(); j++) {
					double distance = MathLib.getDistance(
							MathLib.getCenterOfLine(resultLines.get(i)),
							resultLines.get(j));
					double angle = MathLib.getAngle(resultLines.get(i),
							resultLines.get(j));
					PointF point = new PointF();
					if (distance < delta && checkAngle(angle, angleDelta)
							&& j >= i + 1) {
						for (int t = i + 1; t < j; t++)
							resultLines.remove(t);
						j = i + 1;
					} else if (distance < delta
							&& checkLine(resultLines.get(i),
									resultLines.get(j), point, width, height)) {
						for (int t = i; t < resultLines.size(); t++) {
							resultLines.remove(t);
						}
						i--;
						break;
					}

				}
			}
		}

		return resultLines;
	}

	public static boolean checkLineLeft(List<ALineMod> arrLeft,
			List<ALineMod> arrTop, List<ALineMod> arrRight,
			List<ALineMod> arrBottom, Vector<PointF> resultPoints,
			int[] indexs, float width, float height, double delta) {
		int md = 5;
		PointF pointTL = new PointF();
		PointF pointTR = new PointF();
		PointF pointBL = new PointF();
		PointF pointBR = new PointF();
		for (int i = indexs[3]; i < arrBottom.size(); i++) {
			for (int j = indexs[0]; j < arrLeft.size(); j++) {
				// Check Bottom and Left
				if (checkLine(arrBottom.get(i), arrLeft.get(j), pointBL, width,
						height)) {
					for (int k = indexs[1]; k < arrTop.size(); k++) {
						// Check Left and Top
						if (checkLine(arrLeft.get(j), arrTop.get(k), pointTL,
								width, height)) {
							for (int l = indexs[2]; l < arrRight.size(); l++) {
								// Check Top and Right
								if (checkLine(arrTop.get(k), arrRight.get(l),
										pointTR, width, height)) {
									// Check Right and Bottom
									if (checkLine(arrRight.get(l),
											arrBottom.get(i), pointBR, width,
											height)) {
										if (((Math.abs(pointTL.x - pointTR.x) <= md) && (Math
												.abs(pointTL.y - pointTR.y) <= md))
												|| ((Math.abs(pointTL.x
														- pointBL.x) <= md) && (Math
														.abs(pointTL.y
																- pointBL.y) <= md))
												|| ((Math.abs(pointTL.x
														- pointBR.x) <= md) && (Math
														.abs(pointTL.y
																- pointBR.y) <= md))
												|| ((Math.abs(pointTR.x
														- pointBL.x) <= md) && (Math
														.abs(pointTR.y
																- pointBL.y) <= md))
												|| ((Math.abs(pointTR.x
														- pointBR.x) <= md) && (Math
														.abs(pointTR.y
																- pointBR.y) <= md))
												|| ((Math.abs(pointBL.x
														- pointBR.x) <= md) && (Math
														.abs(pointBL.y
																- pointBR.y) <= md))) {
											return false;
										} else {
											indexs[3] = i;
											indexs[0] = j;
											indexs[1] = k;
											indexs[2] = l;

											if (delta > 0) {

												ALineMod lineL, lineT, lineR, lineB;
												lineL = arrLeft.get(indexs[0]);
												lineT = arrTop.get(indexs[1]);
												lineR = arrRight.get(indexs[2]);
												lineB = arrBottom
														.get(indexs[3]);

												boolean check2Line = false;
												if (arrBottom.size() > (indexs[3] + 1)) {
													check2Line = checkLine(
															lineB,
															arrBottom
																	.get(indexs[3] + 1),
															2, delta, lineB);
													if (check2Line) {
														indexs[3]++;
														check2Line = false;
													}
												}
												if (arrLeft.size() > (indexs[0] + 1)) {
													check2Line = checkLine(
															lineL,
															arrLeft.get(indexs[0] + 1),
															2, delta, lineL);
													if (check2Line) {
														indexs[0]++;
														check2Line = false;
													}
												}
												if (arrTop.size() > (indexs[1] + 1)) {
													check2Line = checkLine(
															lineT,
															arrTop.get(indexs[1] + 1),
															2, delta, lineT);
													if (check2Line) {
														indexs[1]++;
														check2Line = false;
													}
												}
												if (arrRight.size() > (indexs[2] + 1)) {
													check2Line = checkLine(
															lineR,
															arrRight.get(indexs[2] + 1),
															2, delta, lineR);
													if (check2Line) {
														indexs[2]++;
														check2Line = false;
													}
												}
												pointTL = MathLib.getIntersect(
														lineL, lineT);
												pointTR = MathLib.getIntersect(
														lineT, lineR);
												pointBR = MathLib.getIntersect(
														lineR, lineB);
												pointBL = MathLib.getIntersect(
														lineB, lineL);
											}

											resultPoints.add(pointBR);
											resultPoints.add(pointTR);
											resultPoints.add(pointTL);
											resultPoints.add(pointBL);
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;

	}

	/**
	 * sort line
	 * 
	 * @param lines
	 * @param center
	 * @return
	 */
	public static Vector<ALineMod> sortLine(Vector<ALineMod> lines,
			PointF center) {
		Vector<ALineMod> arrLeft = lines;
		if (arrLeft.size() == 0) {
			return arrLeft;
		}
		for (int i = 0; i < arrLeft.size() - 1; i++) {
			for (int j = i + 1; j < arrLeft.size(); j++) {
				double disi = MathLib.getDistance(center, arrLeft.get(i));
				double disj = MathLib.getDistance(center, arrLeft.get(j));
				if (disi > disj) {
					swapLine(arrLeft.get(i), arrLeft.get(j));
				}

			}
		}
		return arrLeft;
	}

	/**
	 * check point dupliate
	 * 
	 * @param pa
	 * @param points
	 * @return
	 */
	public static boolean checkPointDuplicate(Point pa, Vector<PointF> points) {
		for (PointF p : points) {
			if (getDistance(pa, new Point(p.x, p.y)) < 10)
				return true;
		}
		return false;
	}
	/**
	 * check point duplicate
	 * @param pa
	 * @param points
	 * @return
	 */
	public static boolean checkPointDuplicate(PointF pa, Vector<PointF> points) {
		
		
		for (PointF p : points) {
			
			// if (pa.x == p.x && pa.y == p.y)
			if(pa.x == p.x && pa.y == p.y) return true;
			if (getDistance(p, pa) < 40)
				return true;
		}
		return false;
	}
	/**
	 * check point duplicate with min distance
	 * @param pa
	 * @param points
	 * @param minDistance
	 * @return
	 */
	public static boolean checkPointDuplicateWithMinDistance(PointF pa, Vector<PointF> points,int minDistance) {
		
		
		for (PointF p : points) {
			
			// if (pa.x == p.x && pa.y == p.y)
			if(pa.x == p.x && pa.y == p.y) return true;
			if (getDistance(p, pa) < minDistance)
				return true;
		}
		return false;
	}
	/**
	 * 
	 * @param pa
	 * @param points
	 * @return
	 */
	public static boolean checkPointDuplicatePoint(Point pa, Vector<Point> points) {
		
		for (Point p : points) {
			// if (pa.x == p.x && pa.y == p.y)
			
			if (getDistance(pa, p ) < 20)
				return true;
		}
		return false;
	}

	/**
	 * check point of corner is duplicated
	 * 
	 * @param topleft
	 * @param topright
	 * @param botleft
	 * @param botright
	 * @return
	 */
	public static boolean checkCornerDuplicate(Point topleft, Point topright,
                                               Point botleft, Point botright) {
		Point[] listpoint = new Point[] { topleft, topright, botright, botleft };
		for (int i = 0; i < 4; i++) {
			for (int j = i + 1; j < 4; j++) {
				Point p1 = listpoint[i];
				Point p2 = listpoint[j];
				if (getDistance(p1, p2) < 20)
					return true;
			}
		}
		return false;
	}
	/**
	 * check point of corner is duplicated
	 * 
	 * @param topleft
	 * @param topright
	 * @param botleft
	 * @param botright
	 * @return
	 */
	public static boolean checkCornerDuplicateWithMinDistance(Point topleft, Point topright,
                                                              Point botleft, Point botright, int minDistance) {
		Point[] listpoint = new Point[] { topleft, topright, botright, botleft };
		for (int i = 0; i < 4; i++) {
			for (int j = i + 1; j < 4; j++) {
				Point p1 = listpoint[i];
				Point p2 = listpoint[j];
				if (getDistance(p1, p2) < minDistance)
					return true;
			}
		}
		return false;
	}
	/**
	 * Check point duplicate
	 * @param topleft
	 * @param topright
	 * @param botleft
	 * @param botright
	 * @return
	 */
	public static boolean checkCornerDuplicate(PointF topleft, PointF topright,
			PointF botleft, PointF botright) {
		PointF[] listpoint = new PointF[] { topleft, topright, botright, botleft };
		for (int i = 0; i < 4; i++) {
			for (int j = i + 1; j < 4; j++) {
				PointF p1 = listpoint[i];
				PointF p2 = listpoint[j];
				if (getDistance(p1, p2) < 20)
					return true;
			}
		}
		return false;
	}

	/**
	 * check line
	 * 
	 * @param line1
	 * @param line2
	 * @param angleDelta
	 * @param delta
	 * @param result
	 * @return
	 */
	private static boolean checkLine(ALineMod line1, ALineMod line2,
			double angleDelta, double delta, ALineMod result) {
		double angle = MathLib.getAngle(line1, line2);

		double distance = MathLib.getDistance(line1, line2);
		if (checkAngle(angle, angleDelta) && distance < delta) {
			result.mStart = line2.mStart;
			result.mEnd = line2.mEnd;
			return true;
		}
		result.mStart = line1.mStart;
		result.mEnd = line1.mEnd;
		return false;
	}

	/**
	 * check line
	 * 
	 * @param line1
	 * @param line2
	 * @param point
	 * @param width
	 * @param height
	 * @return
	 */
	private static boolean checkLine(ALineMod line1, ALineMod line2,
			PointF point, float width, float height) {
		PointF tmp_point = MathLib.getIntersect(line1, line2);
		if (tmp_point == null)
			return false;
		point.x = tmp_point.x;
		point.y = tmp_point.y;
		if (point.x >= 0 && point.x <= width && point.y >= 0
				&& point.y <= height) {
			return true;
		}
		return false;
	}

	/**
	 * swap line
	 * 
	 * @param l1
	 * @param l2
	 */
	private static void swapLine(ALineMod l1, ALineMod l2) {
		ALineMod tmp = new ALineMod(l1.mStart, l1.mEnd);
		l1.mStart = l2.mStart;
		l1.mEnd = l2.mEnd;
		l2.mStart = tmp.mStart;
		l2.mEnd = tmp.mEnd;
	}

	/**
	 * check angle
	 * 
	 * @param angle
	 * @param delta
	 * @return
	 */
	private static boolean checkAngle(double angle, double delta) {
//		System.out.println(String.format("check angle %f %f", angle, delta));
		if ((Math.abs(angle) <= delta || (Math.abs(angle) <= (180 + delta) && Math
				.abs(angle) >= (180 - delta)))) {
			return true;
		}
		return false;

	}

	/**
	 * check line when sort
	 * 
	 * @param line1
	 * @param line2
	 * @param point
	 * @param type
	 * @return
	 */
	private static boolean checkLineWhenSort(ALineMod line1, ALineMod line2,
			PointF point, int type) {
		PointF tmpPoint = MathLib.getIntersect(line1, line2);
		float minX = 0, maxX = 0, minY = 0, maxY = 0;
		switch (type) {
		case 1:
			minX = line2.mStart.x;
			maxX = line2.mEnd.x;
			minY = maxY = line2.mStart.y;
			break;
		case 2:
			minY = line2.mStart.y;
			maxY = line2.mEnd.y;
			minX = maxX = line2.mStart.x;
			break;

		default:
			break;
		}
		point.x = tmpPoint.x;
		point.y = tmpPoint.y;
		if (point.x >= minX && point.x <= maxX && point.y >= minY
				&& point.y <= maxY) {
			return true;
		}
		return false;

	}

	/**
	 * get list line after sort with type1
	 * 
	 * @param arr
	 * @param arr2
	 * @param delta
	 * @param width
	 * @param height
	 * @return
	 */
	private static Vector<ALineMod> getArrAfterSSWithType1(
			Vector<ALineMod> arr, Vector<ALineMod> arr2, double delta,
			float width, float height) {
		Vector<ALineMod> testLines = new Vector<ALineMod>();

		for (int i = 0; i < arr.size(); i++) {
			for (int j = 0; j < arr2.size(); j++) {
				double angle = MathLib.getAngle(arr.get(i), arr2.get(j));
				boolean checkangle = checkAngle(angle, delta);
				if (checkangle) {
					testLines.add(arr.get(i));
					break;
				}
			}
		}
		return testLines;

	}

	/**
	 * make mapping with lines
	 * 
	 * @param arr
	 * @param width
	 * @param height
	 * @return
	 */
	private static Vector<ALineMod> makeMappingWithLines(Vector<ALineMod> arr,
			float width, float height) {
		Vector<ALineMod> arrResult = new Vector<ALineMod>();
		for (int i = 0; i < arr.size(); i++) {
			ALineMod centerLine = new ALineMod(new PointF(arr.get(i).mStart.x,
					0), new PointF(arr.get(i).mStart.x, height));
			ALineMod newLine;
			double deltaX2 = MathLib.getDistance(arr.get(i).mEnd, centerLine);
			PointF point2 = null;
			if (arr.get(i).mStart.x < arr.get(i).mEnd.x) {

				point2 = new PointF(
						arr.get(i).mEnd.x - 2 * Math.round(deltaX2),
						arr.get(i).mEnd.y);
			} else {
				point2 = new PointF(
						arr.get(i).mEnd.x + 2 * Math.round(deltaX2),
						arr.get(i).mEnd.y);
			}
			arrResult.add(new ALineMod(arr.get(i).mStart, point2));
		}
		return arrResult;
	}

	/**
	 * get line after sort type 2
	 * 
	 * @param arr
	 * @param arr2
	 * @param delta
	 * @param width
	 * @param height
	 * @return
	 */
	private static Vector<ALineMod> getArrAfterSSWithType2(
			Vector<ALineMod> arr, Vector<ALineMod> arr2, double delta,
			float width, float height) {
		Vector<ALineMod> testLines = new Vector<ALineMod>();
		Vector<ALineMod> arr3 = new Vector<ALineMod>();
		arr3 = makeMappingWithLines(arr2, width, height);

		for (int i = 0; i < arr.size(); i++) {
			for (int j = 0; j < arr3.size(); j++) {
				double angle = MathLib.getAngle(arr.get(i), arr3.get(j));
				boolean checkangle = checkAngle(angle, delta + 5);
				if (checkangle) {
					testLines.add(arr.get(i));
					break;
				}
			}
		}
		if (testLines.size() > 1) {
			return testLines;
		} else {
			Vector<ALineMod> linesSS = getArrAfterSSWithType1(arr, arr2, delta,
					width, height);
			for (int i = 0; i < linesSS.size(); i++) {
				testLines.add(linesSS.get(i));
			}
			return testLines;
		}

	}

	public static PointF findBestPoint(Vector<PointF> points, PointF achor) {
		PointF result = points.get(0);
		float min = Float.MAX_VALUE;
		for (PointF p : points) {
			float d = (float) getDistance(p, achor);
			if (min > d) {
				min = d;
				result = p;
			}
		}
		return result;
	}

	public static Point findBestPoint(Point[] points, Point achor) {
		Point result = points[0];
		float min = Float.MAX_VALUE;
		for (Point p : points) {
			float d = (float) getDistance(p, achor);
			if (min > d) {
				min = d;
				result = p;
			}
		}
		return result;
	}
	
	public static PointF findBestPoint(PointF[] points, PointF achor) {
		PointF result = points[0];
		float min = Float.MAX_VALUE;
		for (PointF p : points) {
			float d = (float) getDistance(p, achor);
			if (min > d) {
				min = d;
				result = p;
			}
		}
		return result;
	}

	public static double findMostLeftOfListPoint(Point[] points) {
		double result = -1;
		for (int i = 0; i < points.length; i++) {
			Point pt = points[i];
			if (result == -1 || result > pt.x) {
				result = pt.x;
			}
		}
		return result;
	}

	public static double findMostTopOfListPoint(Point[] points) {
		double result = -1;
		for (int i = 0; i < points.length; i++) {
			Point pt = points[i];
			if (result == -1 || result > pt.y) {
				result = pt.y;
			}
		}
		return result;
	}

	public static double findMostRightOfListPoint(Point[] points) {
		double result = -1;
		for (int i = 0; i < points.length; i++) {
			Point pt = points[i];
			if (result == -1 || result < pt.x) {
				result = pt.x;
			}
		}
		return result;
	}

	public static double findMostBottomOfListPoint(Point[] points) {
		double result = -1;
		for (int i = 0; i < points.length; i++) {
			Point pt = points[i];
			if (result == -1 || result < pt.y) {
				result = pt.y;
			}
		}
		return result;
	}
	
	private double getAngleNew() {
//		double l1x = points_new[1].x - points_new[0].x;
//		double l1y = points_new[1].y - points_new[0].y;
//		double l2x = points_new[3].x - points_new[0].x;
//		double l2y = points_new[3].y - points_new[0].y;
//		double tan1 = (double)l1y/(double)l1x;
//		double tan2 = (double)l2y/(double)l2x;
//		double ang1 = Math.atan2(l1y, l1x);
//		double ang2 = Math.atan2(l2y, l2x);
//		double angle = Math.abs(Math.toDegrees(ang2-ang1));
//		Log.e("CongVCCC", "=====angle:"+ angle);
		return 0;
	}

	public static Rect findBoudingRectFromListPoint(Point[] points) {
		double left = findMostLeftOfListPoint(points);
		double right = findMostRightOfListPoint(points);
		double top = findMostTopOfListPoint(points);
		double bottom = findMostBottomOfListPoint(points);
		double width = right - left;
		double height = bottom - top;
		
		
		double widthMin = points[1].x - points[0].x;
		double widthMax = points[3].x - points[2].x;
		double heightMin = points[2].y - points[0].y;
		double heightMax = points[3].y - points[1].y;
		
		if(widthMax < widthMin)
		{
			widthMax = points[1].x - points[0].x;
			widthMin = points[3].x - points[2].x;
		}
		
		if(heightMax < heightMin)
		{
			heightMin = points[3].y - points[1].y;
			heightMax = points[2].y - points[0].y;
		}
		
		if(widthMax - widthMin > 16 || heightMax - heightMin > 16)
		{
//			width = (widthMax+widthMin)/2;
//			width = width + (widthMax - width)/2;
			
			width = widthMin;
			
			height = heightMax;
		}
		
//		if(CameraViewActivity.cameraMode == ConfigLib.CAMERA_MODE_SHOCNOTE){
//			return new Rect((int) left, (int) top, (int) width, (int) height);
//		}
		
		
		
		double widthDefault = right - left;
		double heightDefault = bottom - top;
				
		Point[] points_new = new Point[4];
		points_new[0] = points[0];
		points_new[1] = points[1];
		points_new[2] = points[3];
		points_new[3] = points[2];
			
		
		Line leftLineT2B = new Line(); // top to bottom
		leftLineT2B.point1 = points_new[0];
		leftLineT2B.point2 = points_new[3];

		Line topLineL2R = new Line(); // left to right
		topLineL2R.point1 = points_new[0];
		topLineL2R.point2 = points_new[1];
		
		double angleTopLeft = MathLib.getAngle(leftLineT2B, topLineL2R);

		// ===================
		// bottom left angel └
		// ===================
		Line leftLineB2T = new Line(); // bottom to top
		leftLineB2T.point1 = points_new[3];
		leftLineB2T.point2 = points_new[0];

		Line bottomLineL2R = new Line(); // left to right
		bottomLineL2R.point1 = points_new[3];
		bottomLineL2R.point2 = points_new[2];

		double angleBottomLeft = MathLib.getAngle(leftLineB2T, bottomLineL2R);

		// top right angel ┐
		Line rightLineT2B = new Line(); // top to bottom
		rightLineT2B.point1 = points_new[1];
		rightLineT2B.point2 = points_new[2];

		Line topLineR2L = new Line(); // left to right
		topLineR2L.point1 = points_new[1];
		topLineR2L.point2 = points_new[0];

		double angleTopRight = MathLib.getAngle(rightLineT2B, topLineR2L);

		// bottom right angel ┘
		Line rightLineB2T = new Line(); // top to bottom
		rightLineB2T.point1 = points_new[2];
		rightLineB2T.point2 = points_new[1];

		Line bottomLineR2L = new Line(); // left to right
		bottomLineR2L.point1 = points_new[2];
		bottomLineR2L.point2 = points_new[3];

		double angleBottomRight = MathLib.getAngle(rightLineB2T, bottomLineR2L);
		
		
		
		
		double AB = Math.sqrt((Math.pow(points_new[0].x - points_new[1].x, 2)) + (Math.pow(points_new[0].y - points_new[1].y, 2)));
		double BC = Math.sqrt((Math.pow(points_new[1].x - points_new[2].x, 2)) + (Math.pow(points_new[1].y - points_new[2].y, 2)));
		double CD = Math.sqrt((Math.pow(points_new[2].x - points_new[3].x, 2)) + (Math.pow(points_new[2].y - points_new[3].y, 2)));
		double DA = Math.sqrt((Math.pow(points_new[3].x - points_new[0].x, 2)) + (Math.pow(points_new[3].y - points_new[0].y, 2)));
		double AC = Math.sqrt((Math.pow(points_new[0].x - points_new[2].x, 2)) + (Math.pow(points_new[0].y - points_new[2].y, 2)));
		double BD = Math.sqrt((Math.pow(points_new[1].x - points_new[3].x, 2)) + (Math.pow(points_new[1].y - points_new[3].y, 2)));
		
		double maxCanh = Math.max( Math.max(AB, BC),  Math.max(CD, DA));
		
		double sABD = 0.5*AB*DA*Math.abs(Math.sin(angleTopLeft));
		double sCBD = 0.5*BC*CD*Math.abs(Math.sin(angleBottomRight));
		
		double sABCD = sABD + sCBD;
		
//		Log.e("CongVCCCC", "===AB:" + AB + "===BC:"+ BC + "===CD:"+ CD + "===DA:"+DA);
//		
//		Log.e("CongVCCCC", "===(BC + DA) / (AB + CD):" + (BC + DA) / (AB + CD) );
		
//		Log.e("COngVCCCC", "====sABCD:"+sABCD);
		
//		double canhDai = 0;
//		double canhNgan = 0;
//		if(maxCanh == AB || maxCanh == CD){
//			canhNgan = Math.sqrt((91f/55f)*sABCD);
//			canhDai = Math.sqrt((55f/91f)*sABCD);
//		}
//		else {
//			canhDai = Math.sqrt((29.7f/21f)*sABCD);
//			canhNgan = Math.sqrt((21f/29.7f)*sABCD);
//		}
//		
//		Log.e("COngVCCCC", "===canhDai:" + canhDai + "===canhNgan:" + canhNgan);
//		
//		height = canhDai;
//		width = canhNgan;
		
		
		
		
		
		
		
		
				
		double avageAngleTop = (angleTopLeft+ angleTopRight)/2;
		double avageAngleBottom = (angleBottomLeft+ angleBottomRight)/2;
		int distanceAngleTop = 0;

//		Log.e("CongVCCC", "=====angleTopLeft:"+ angleTopLeft + "=====angleBottomLeft:"+ angleBottomLeft+"=======angleTopRight:"+angleTopRight+"========angleBottomRight:"+ angleBottomRight);

		if(angleTopLeft >= 90 && angleTopRight >= 90){
//			Log.e("CongVCCC", "=====Dzo 2 goc tren > 90 do");
			return adjustEdgeByAllTopLarger90((int)left, (int)top,
								width, height, 
								angleTopLeft, angleTopRight, 
								angleBottomLeft, angleBottomRight,
								avageAngleTop, 
								AB, BC, CD, DA);
			
		}
		else if(angleTopLeft < 90 && angleTopRight < 90){
			
			if((angleBottomLeft >= 90 && angleBottomRight >= 90) ){
//				Log.e("CongVCCC", "=====Dzo 2 goc tren < 90 do, Dzo 2 goc duoi > 90 do");
				return adjustEdgeByAllTopSmaller90((int)left, (int)top,
						width, height,
						angleTopLeft, angleTopRight,  
						angleBottomLeft, angleBottomRight,
						avageAngleBottom, 
						AB, BC, CD, DA);
			}
			else {
				double maxAngleTop = angleBottomLeft >= 90 ? angleBottomLeft : angleBottomRight;
//				Log.e("CongVCCC", "=====Dzo 2 goc tren < 90 do, Dzo 2 goc duoi <> 90 do");
				return adjustEdgeByTopLS90_BottomS90((int)left, (int)top, 
						width, height, 
						angleBottomLeft, angleBottomRight,
						angleTopLeft, angleTopRight,  
						maxAngleTop,
						AB, BC, CD, DA);
			}
		}
		else if((angleTopLeft >= 90 && angleTopRight < 90) || (angleTopLeft < 90 && angleTopRight >= 90)){
			
			width = widthMax;
			
			double maxAngleTop = angleTopLeft >= 90 ? angleTopLeft : angleTopRight;
			if(Math.abs(angleBottomLeft - angleBottomRight) >=10 &&
				Math.abs(angleTopLeft - angleTopRight) >= 10){
				
				if(maxAngleTop < angleBottomLeft){
					maxAngleTop = angleBottomLeft;
				}
				if(maxAngleTop < angleBottomRight){
					maxAngleTop = angleBottomRight;
				}
			}

			if((angleBottomLeft >= 90 && angleBottomRight < 90) || (angleBottomLeft < 90 && angleBottomRight >= 90)){
				
//				Log.e("CongVCCC", "=====Dzo 2 goc tren <> 90 do, 2 goc duoi <> 90 do");
				
				return adjustEdgeByTopLS90_BottomLS90((int)left, (int)top, 
						width, height, 
						angleTopLeft, angleTopRight, 
						angleBottomLeft, angleBottomRight, 
						AB, BC, CD, DA);
				
			}
			else {
//				Log.e("CongVCCC", "=====Dzo 2 goc tren <> 90 do, 2 goc duoi < 90 do");			
				return adjustEdgeByTopLS90_BottomS90((int)left, (int)top, 
						width, height, 
						angleTopLeft, angleTopRight, 
						angleBottomLeft, angleBottomRight, 
						maxAngleTop,
						AB, BC, CD, DA);
			}
		}
		
		return new Rect((int) left, (int) top, (int) width, (int) height);
	}
	
	/**
	 * All angles top is larger 90 degree
	 * 
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 * @param angleTopLeft
	 * @param angleTopRight
	 * @param avageAngleTop
	 * @param EdgeAB
	 * @param EdgeBC
	 * @param EdgeCD
	 * @param EdgeDA
	 * @return
	 */
	private static Rect adjustEdgeByAllTopLarger90(int left, int top,
                                                   double width, double height,
                                                   double angleTopLeft, double angleTopRight,
                                                   double angleBottomLeft, double angleBottomRight,
                                                   double avageAngleTop,
                                                   double EdgeAB, double EdgeBC, double EdgeCD, double EdgeDA) {
		
		int distanceAngleTop = 0;
		double pABCD = 0;
		
		if(Math.abs(angleTopLeft - angleTopRight) >= 20){
			distanceAngleTop = 20;
		}
		else if(Math.abs(angleTopLeft - angleTopRight) >= 10){
			distanceAngleTop = 10;
		}
		
		double rateEdge = (EdgeBC > EdgeDA) ? EdgeBC/EdgeDA : EdgeDA/EdgeBC;
		
		if(rateEdge < 1.08 && Math.abs(angleBottomLeft - angleBottomRight) >= 4){
			avageAngleTop = angleTopLeft > angleTopRight ? angleTopLeft : angleTopRight;
		}
		
		if(avageAngleTop >= 99){
			width = ((EdgeAB < EdgeCD) ? EdgeAB : EdgeCD);// + ((AB < CD) ? AB : CD)/10;
			height = (EdgeBC + EdgeDA)/2;//BC > DA ? BC : DA;
			pABCD = (EdgeAB + EdgeBC + EdgeCD + EdgeDA) / 4;//(BC > DA) ? BC : DA;
		}
		
		boolean isLandscape = false;
		double rateEdgeP_L = (EdgeBC + EdgeDA) / (EdgeAB + EdgeCD);
		if(rateEdgeP_L < 0.7 && avageAngleTop >= 99){
			width = (EdgeAB + EdgeCD) / 2;//((EdgeAB > EdgeCD) ? EdgeAB : EdgeCD);
			isLandscape = true;
		}
		
		
		if(avageAngleTop >= 118){
			if(distanceAngleTop == 20){
				if(isLandscape)
					height = height + pABCD/5;
				else 
					height = height + pABCD/3;
			}
			else if(distanceAngleTop == 10){
				if(isLandscape)
					height = height + pABCD/4.5;
				else
					height = height + pABCD/2.75;
			}
			else{
				if(isLandscape)
					height = height + pABCD/4;
				else
					height = height + pABCD/2.5;
			}
		}
		else if(avageAngleTop >= 116){
			if(distanceAngleTop == 20){
				if(isLandscape)
					height = height + pABCD/5.5;
				else
				height = height + pABCD/3.5;
			}
			else if(distanceAngleTop == 10){
				if(isLandscape)
					height = height + pABCD/5;
				else
				height = height + pABCD/3;
			}
			else{
				if(isLandscape)
					height = height + pABCD/4.5;
				else
				height = height + pABCD/2.75;
			}
		}
		else if(avageAngleTop >= 114){
			if(distanceAngleTop == 20){
				if(isLandscape)
					height = height + pABCD/6.5;
				else
				height = height + pABCD/4;
			}
			else if(distanceAngleTop == 10){
				if(isLandscape)
					height = height + pABCD/6;
				else
				height = height + pABCD/3.5;
			}
			else{
				if(isLandscape)
					height = height + pABCD/5.5;
				else
				height = height + pABCD/3;
			}
		}
		else if(avageAngleTop >= 111){
			if(distanceAngleTop == 20){
				if(isLandscape)
					height = height + pABCD/7;
				else
				height = height + pABCD/4.5;
			}
			else if(distanceAngleTop == 10){
				if(isLandscape)
					height = height + pABCD/6.5;
				else
				height = height + pABCD/4;
			}
			else{
				if(isLandscape)
					height = height + pABCD/6;
				else
				height = height + pABCD/3.5;
			}
		}
		else if(avageAngleTop >= 108){
			if(distanceAngleTop == 20){
				if(isLandscape)
					height = height + pABCD/7.5;
				else
				height = height + pABCD/5;
			}
			else if(distanceAngleTop == 10){
				if(isLandscape)
					height = height + pABCD/7;
				else
				height = height + pABCD/4.5;
			}
			else{
				if(isLandscape)
					height = height + pABCD/6.5;
				else
				height = height + pABCD/4;
			}
		}
		else if(avageAngleTop >= 107){
			if(distanceAngleTop == 20){
				if(isLandscape)
					height = height + pABCD/8;
				else
				height = height + pABCD/5.5;
			}
			else if(distanceAngleTop == 10){
				if(isLandscape)
					height = height + pABCD/7.5;
				else
				height = height + pABCD/5;
			}
			else{
				if(isLandscape)
					height = height + pABCD/7;
				else
				height = height + pABCD/4.5;
			}
		}
		else if(avageAngleTop >= 106){
			if(distanceAngleTop == 20){
				if(isLandscape)
					height = height + pABCD/8.5;
				else
				height = height + pABCD/6;
			}
			else if(distanceAngleTop == 10){
				if(isLandscape)
					height = height + pABCD/8;
				else
				height = height + pABCD/5.5;
			}
			else{
				if(isLandscape)
					height = height + pABCD/7.5;
				else
				height = height + pABCD/5;
			}
		}
		else if(avageAngleTop >= 105){
			if(distanceAngleTop == 20){
				if(isLandscape)
					height = height + pABCD/9;
				else
				height = height + pABCD/6.5;
			}
			else if(distanceAngleTop == 10){
				if(isLandscape)
					height = height + pABCD/8.5;
				else
				height = height + pABCD/6;
			}
			else{
				if(isLandscape)
					height = height + pABCD/8;
				else
				height = height + pABCD/5.5;
			}
		}
		else if(avageAngleTop >= 104){
			if(distanceAngleTop == 20){
				if(isLandscape)
					height = height + pABCD/10;
				else
				height = height + pABCD/8;
			}
			else if(distanceAngleTop == 10){
				if(isLandscape)
					height = height + pABCD/9.5;
				else
				height = height + pABCD/7.5;
			}
			else{
				if(isLandscape)
					height = height + pABCD/9;
				else
				height = height + pABCD/7;
			}
		}
		else if(avageAngleTop >= 103){
			if(distanceAngleTop == 20){
				if(isLandscape)
					height = height + pABCD/12;
				else
				height = height + pABCD/11;
			}
			else if(distanceAngleTop == 10){
				if(isLandscape)
					height = height + pABCD/11.5;
				else
				height = height + pABCD/10.5;
			}
			else{
				if(isLandscape)
					height = height + pABCD/11;
				else
				height = height + pABCD/10;
			}
		}
		else if(avageAngleTop >= 102){
			if(distanceAngleTop == 20){
				if(isLandscape)
					height = height + pABCD/14;
				else
				height = height + pABCD/13;
			}
			else if(distanceAngleTop == 10){
				if(isLandscape)
					height = height + pABCD/13.5;
				else
				height = height + pABCD/12.5;
			}
			else{
				if(isLandscape)
					height = height + pABCD/13;
				else
				height = height + pABCD/12;
			}
		}
		else if(avageAngleTop >= 101){
			if(distanceAngleTop == 20){
				height = height + pABCD/14;
			}
			else if(distanceAngleTop == 10){
				height = height + pABCD/13.5;
			}
			else{
				height = height + pABCD/13;
			}
		}
		else if(avageAngleTop >= 100){
			if(distanceAngleTop == 20){
				height = height + pABCD/17;
			}
			else if(distanceAngleTop == 10){
				height = height + pABCD/16.5;
			}
			else{
				height = height + pABCD/16;
			}
		}
		else if(avageAngleTop >= 99){
			if(distanceAngleTop == 20){
				height = height + pABCD/19;
			}
			else if(distanceAngleTop == 10){
				height = height + pABCD/18.5;
			}
			else{
				height = height + pABCD/18;
			}
		}
		
		return new Rect((int) left, (int) top, (int) width, (int) height);
	}
	
	
	/**
	 * All angles top is smaller 90 degrees and
	 * 	all angles bottom is larger 90 degrees
	 * 
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 * @param angleTopLeft
	 * @param angleTopRight
	 * @param avageAngleBottom
	 * @param EdgeAB
	 * @param EdgeBC
	 * @param EdgeCD
	 * @param EdgeDA
	 * @return
	 */
	private static Rect adjustEdgeByAllTopSmaller90(int left, int top,
                                                    double width, double height,
                                                    double angleTopLeft, double angleTopRight,
                                                    double angleBottomLeft, double angleBottomRight,
                                                    double avageAngleBottom,
                                                    double EdgeAB, double EdgeBC, double EdgeCD, double EdgeDA) {
		
		int distanceAngleBottom = 0;
		double pABCD = 0;
		
		if(Math.abs(angleBottomLeft - angleBottomRight) >= 20){
			distanceAngleBottom = 20;
		}
		else if(Math.abs(angleBottomLeft - angleBottomRight) >= 10){
			distanceAngleBottom = 10;
		}
		
		double rateEdge = (EdgeBC > EdgeDA) ? EdgeBC/EdgeDA : EdgeDA/EdgeBC;
		
//		if(rateEdge < 1.08 && Math.abs(angleBottomLeft - angleBottomRight) >= 4){
//			avageAngleBottom = angleTopLeft > angleTopRight ? angleTopLeft : angleTopRight;
//		}
		
		if(avageAngleBottom >= 99){
			width = ((EdgeAB < EdgeCD) ? EdgeAB : EdgeCD);// + ((AB < CD) ? AB : CD)/10;
			height = (EdgeBC + EdgeDA)/2;//BC > DA ? BC : DA;
			pABCD = (EdgeAB + EdgeBC + EdgeCD + EdgeDA) / 4;//(BC > DA) ? BC : DA;
		}
		
		boolean isLandscape = false;
		double rateEdgeP_L = (EdgeBC + EdgeDA) / (EdgeAB + EdgeCD);
		if(rateEdgeP_L < 0.7 && avageAngleBottom >= 99){
			width = (EdgeAB + EdgeCD) / 2;//((EdgeAB > EdgeCD) ? EdgeAB : EdgeCD);
			isLandscape = true;
		}
		
//		Log.e("CongVCCC", "=====avageAngleBottom:"+ avageAngleBottom +  "===rateEdge:" + rateEdge);
		if(avageAngleBottom >= 103 && Math.abs((int)angleBottomLeft - (int)angleBottomRight) <= 3 &&
				Math.abs((int)angleTopLeft - (int)angleTopRight) <= 3){
//			Log.e("CongVCCC", "=====cac goc gan bang nhau========");
			if(! isLandscape)
				height = height + pABCD/8;
		}
		
		if(avageAngleBottom >= 118){
			if(isLandscape)
				height = height + pABCD/3.25;
			else
			height = height + pABCD/2.25;
		}
		else if(avageAngleBottom >= 116){
			if(isLandscape)
				height = height + pABCD/3.5;
			else
			height = height + pABCD/2.5;
		}
		else if(avageAngleBottom >= 114){
			if(isLandscape)
				height = height + pABCD/3.75;
			else
			height = height + pABCD/2.75;
		}
		else if(avageAngleBottom >= 111){
			if(isLandscape)
				height = height + pABCD/4;
			else
			height = height + pABCD/3;
		}
		else if(avageAngleBottom >= 109){
			if(isLandscape)
				height = height + pABCD/4.5;
			else
			height = height + pABCD/3.25;
		}
		else if(avageAngleBottom >= 108){
			if(distanceAngleBottom == 20){
				if(isLandscape)
					height = height + pABCD/6;
				else
				height = height + pABCD/4.5;
			}
			else if(distanceAngleBottom == 10){
				if(isLandscape)
					height = height + pABCD/5.5;
				else
				height = height + pABCD/4;
			}
			else{
				if(isLandscape)
					height = height + pABCD/5;
				else
				height = height + pABCD/3.25;
			}
		}
		else if(avageAngleBottom >= 107){
			if(distanceAngleBottom == 20){
				if(isLandscape)
					height = height + pABCD/7;
				else
				height = height + pABCD/5;
			}
			else if(distanceAngleBottom == 10){
				if(isLandscape)
					height = height + pABCD/6.5;
				else
				height = height + pABCD/4.5;
			}
			else{
				if(isLandscape)
					height = height + pABCD/6;
				else
				height = height + pABCD/3.75;
			}
		}
		else if(avageAngleBottom >= 106){
			if(distanceAngleBottom == 20){
				if(isLandscape)
					height = height + pABCD/7;
				else
				height = height + pABCD/6;
			}
			else if(distanceAngleBottom == 10){
				if(isLandscape)
					height = height + pABCD/6.5;
				else
				height = height + pABCD/5;
			}
			else{
				if(isLandscape)
					height = height + pABCD/7;
				else
				height = height + pABCD/4.25;
			}
		}
		else if(avageAngleBottom >= 105){
			if(distanceAngleBottom == 20){
				if(isLandscape)
					height = height + pABCD/7;
				else
				height = height + pABCD/6.5;
			}
			else if(distanceAngleBottom == 10){
				if(isLandscape)
					height = height + pABCD/7.5;
				else
				height = height + pABCD/6;
			}
			else{
				if(isLandscape)
					height = height + pABCD/8;
				else
				height = height + pABCD/5;
			}
		}
		else if(avageAngleBottom >= 104){
			if(distanceAngleBottom == 20){
				if(isLandscape)
					height = height + pABCD/8;
				else
				height = height + pABCD/7.25;
			}
			else if(distanceAngleBottom == 10){
				if(isLandscape)
					height = height + pABCD/8.5;
				else
				height = height + pABCD/6.75;
			}
			else{
				if(isLandscape)
					height = height + pABCD/9;
				else
				height = height + pABCD/5.75;
			}
		}
		else if(avageAngleBottom >= 103){
			if(distanceAngleBottom == 20){
				if(isLandscape)
					height = height + pABCD/11;
				else
				height = height + pABCD/10;
			}
			else if(distanceAngleBottom == 10){
				if(isLandscape)
					height = height + pABCD/10.5;
				else
				height = height + pABCD/9.5;
			}
			else{
				if(isLandscape)
					height = height + pABCD/10;
				else
				height = height + pABCD/9;
			}
		}
		else if(avageAngleBottom >= 102){
			if(distanceAngleBottom == 20){
				height = height + pABCD/13;
			}
			else if(distanceAngleBottom == 10){
				height = height + pABCD/12.5;
			}
			else{
				height = height + pABCD/12;
			}
		}
		else if(avageAngleBottom >= 101){
			if(distanceAngleBottom == 20){
				height = height + pABCD/14;
			}
			else if(distanceAngleBottom == 10){
				height = height + pABCD/13.5;
			}
			else{
				height = height + pABCD/13;
			}
		}
		else if(avageAngleBottom >= 100){
			if(distanceAngleBottom == 20){
				height = height + pABCD/17;
			}
			else if(distanceAngleBottom == 10){
				height = height + pABCD/16.5;
			}
			else{
				height = height + pABCD/16;
			}
		}
		else if(avageAngleBottom >= 99){
			if(distanceAngleBottom == 20){
				height = height + pABCD/19;
			}
			else if(distanceAngleBottom == 10){
				height = height + pABCD/18.5;
			}
			else{
				height = height + pABCD/18;
			}
		}
		
		return new Rect((int) left, (int) top, (int) width, (int) height);
	}
	
	/**
	 * Angles top are larger 90 degrees and smaller 90 degrees
	 * Angles bottom are larger 90 degrees and smaller 90 degrees
	 * 
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 * @param angleTopLeft
	 * @param angleTopRight
	 * @param angleBottomLeft
	 * @param angleBottomRight
	 * @param AB
	 * @param BC
	 * @param CD
	 * @param DA
	 * @return
	 */
	private static Rect adjustEdgeByTopLS90_BottomLS90(int left, int top,
                                                       double width, double height,
                                                       double angleTopLeft, double angleTopRight,
                                                       double angleBottomLeft, double angleBottomRight,
                                                       double AB, double BC, double CD, double DA) {
		
		int angleDistance = 0;
		double maxAngleTop = angleTopLeft > angleTopRight ? angleTopLeft : angleTopRight;
		double maxAngleBottom = angleBottomLeft > angleBottomRight ? angleBottomLeft : angleBottomRight;
		
		if(Math.abs((int)maxAngleTop - (int)maxAngleBottom) >= 10){
			angleDistance = 10;
		}
		else if(Math.abs((int)maxAngleTop - (int)maxAngleBottom) >= 5){
			angleDistance = 5;
		}
		double avageAngleL90 = (maxAngleTop + maxAngleBottom) / 2;
								
		double pABCD = (AB + BC + CD + DA) / 4;
		
		if(avageAngleL90 >= 98){
			width = (AB + CD) / 2;
			height = (BC + DA)/2;
		}
		
		double rateEdgeP_L = (BC + DA) / (AB + CD);
		
		if(((angleTopLeft >= 90 && angleBottomRight >= 90) || (angleTopRight >= 90 && angleBottomLeft >= 90)) && rateEdgeP_L >= 1.4){
			
			double rateEdge = (BC > DA) ? BC/DA : DA/BC;
			if(rateEdge >= 1.1){
				if(rateEdge >= 1.20 && rateEdge <= 1.25){
					height = height + pABCD/8;
				}
				else if(rateEdge >= 1.18){
					height = height + pABCD/7;
				}
				else if(rateEdge >= 1.17){
					height = height + pABCD/6.5;
				}
				else if(rateEdge >= 1.16){
					height = height + pABCD/6;
				}
				else if(rateEdge >= 1.15){
					height = height + pABCD/5.5;
				}
				else if(rateEdge >= 1.14){
					height = height + pABCD/5.25;
				}
				else if(rateEdge >= 1.13){
					height = height + pABCD/5;
				}
				else if(rateEdge >= 1.12){
					height = height + pABCD/4.75;
				}
				else if(rateEdge >= 1.11){
					height = height + pABCD/4.5;
				}
				else if(rateEdge >= 1.10){
					height = height + pABCD/4;
				}
				else if(rateEdge >= 1.08){
					height = height + pABCD/3.75;
				}
				else if(rateEdge >= 1.06){
					height = height + pABCD/3.5;
				}
			}
			avageAngleL90 = 0;

//			Log.e("CongVCCC", "=====rateEdge:" + rateEdge);
		}
		
		
		
		if(avageAngleL90 >= 115){
			if(angleDistance == 5){
				width = width + pABCD/3.7;
			}
			else if(angleDistance == 10){
				width = width + pABCD/4;
			}
			else {
				width = width + pABCD/3.5;
			}
		}
		else if(avageAngleL90 >= 113){
			if(angleDistance == 5){
				width = width + pABCD/4.2;
			}
			else if(angleDistance == 10){
				width = width + pABCD/4.5;
			}
			else {
				width = width + pABCD/4;
			}
		}
		else if(avageAngleL90 >= 111){
			if(angleDistance == 5){
				width = width + pABCD/4.7;
			}
			else if(angleDistance == 10){
				width = width + pABCD/5;
			}
			else {
				width = width + pABCD/4.5;
			}
		}
		else if(avageAngleL90 >= 110){
			if(angleDistance == 5){
				width = width + pABCD/5.2;
			}
			else if(angleDistance == 10){
				width = width + pABCD/5.5;
			}
			else {
				width = width + pABCD/5;
			}
		}
		else if(avageAngleL90 >= 109){
			if(angleDistance == 5){
				width = width + pABCD/5.7;
			}
			else if(angleDistance == 10){
				width = width + pABCD/6;
			}
			else {
				width = width + pABCD/5.5;
			}
		}
		else if(avageAngleL90 >= 108){
			if(angleDistance == 5){
				width = width + pABCD/6.2;
			}
			else if(angleDistance == 10){
				width = width + pABCD/6.5;
			}
			else {
				width = width + pABCD/6;
			}
		}
		else if(avageAngleL90 >= 107){
			if(angleDistance == 5){
				width = width + pABCD/6.7;
			}
			else if(angleDistance == 10){
				width = width + pABCD/7;
			}
			else {
				width = width + pABCD/6.5;
			}
		}
		else if(avageAngleL90 >= 106){
			if(angleDistance == 5){
				width = width + pABCD/7.2;
			}
			else if(angleDistance == 10){
				width = width + pABCD/7.5;
			}
			else {
				width = width + pABCD/7;
			}
		}
		else if(avageAngleL90 >= 105){
			if(angleDistance == 5){
				width = width + pABCD/7.7;
			}
			else if(angleDistance == 10){
				width = width + pABCD/8;
			}
			else {
				width = width + pABCD/7.5;
			}
		}
		else if(avageAngleL90 >= 104){
			if(angleDistance == 5){
				width = width + pABCD/8.2;
			}
			else if(angleDistance == 10){
				width = width + pABCD/8.5;
			}
			else {
				width = width + pABCD/8;
			}
		}
		else if(avageAngleL90 >= 103){
			if(angleDistance == 5){
				width = width + pABCD/8.7;
			}
			else if(angleDistance == 10){
				width = width + pABCD/9;
			}
			else {
				width = width + pABCD/8.5;
			}
		}
		else if(avageAngleL90 >= 102){
			if(angleDistance == 5){
				width = width + pABCD/9.2;
			}
			else if(angleDistance == 10){
				width = width + pABCD/9.5;
			}
			else{
				width = width + pABCD/9;
			}
		}
		else if(avageAngleL90 >= 101){
			if(angleDistance == 5){
				width = width + pABCD/9.7;
			}
			else if(angleDistance == 10){
				width = width + pABCD/10;
			}
			else {
				width = width + pABCD/9.5;
			}
		}
		else if(avageAngleL90 >= 100){
			if(angleDistance == 5){
				width = width + pABCD/10.2;
			}
			else if(angleDistance == 10){
				width = width + pABCD/10.5;
			}
			else {
				width = width + pABCD/10;
			}
		}
		else if(avageAngleL90 >= 98){
			if(angleDistance == 5){
				width = width + pABCD/22.2;
			}
			else if(angleDistance == 10){
				width = width + pABCD/22.5;
			}
			else {
				width = width + pABCD/22;
			}
		}
		
		
		avageAngleL90 = (maxAngleTop + maxAngleBottom) / 2;
		
		if(((rateEdgeP_L <= 1.3 || (rateEdgeP_L > 1.3 && rateEdgeP_L <= 1.45 && avageAngleL90 >= 105))
				&& ((angleTopLeft >= 90 && angleBottomLeft >= 90) 
						|| (angleTopRight >= 90 && angleBottomRight >= 90))) 
			|| rateEdgeP_L <= 1.15){
			double ratePotrait = (BC > DA) ? BC/DA : DA/BC;
			height = (BC < DA ? BC : DA) ;
			if(ratePotrait >= 1.25){
				height = height + pABCD/18;
			}
			if(rateEdgeP_L > 1.3){
				width = width + pABCD/15;
			}
		}
		
		
		return new Rect((int) left, (int) top, (int) width, (int) height);
	}
	
	
	/**
	 * Angles top are larger 90 degrees and smaller 90 degrees
	 * All angles bottom is smaller 90 degrees
	 * 
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 * @param angleTopLeft
	 * @param angleTopRight
	 * @param angleBottomLeft
	 * @param angleBottomRight
	 * @param maxAngleTop
	 * @param AB
	 * @param BC
	 * @param CD
	 * @param DA
	 * @return
	 */
	private static Rect adjustEdgeByTopLS90_BottomS90(int left, int top,
                                                      double width, double height,
                                                      double angleTopLeft, double angleTopRight,
                                                      double angleBottomLeft, double angleBottomRight,
                                                      double maxAngleTop,
                                                      double AB, double BC, double CD, double DA) {
		
		double maxAngleTopBK = maxAngleTop;
				
		double pABCD = (AB + BC + CD + DA) / 4;
		width = AB < CD ? AB : CD;//(AB + CD) / 2;
		height = (BC + DA)/2;
		
		double rateEdge = (BC > DA) ? BC/DA : DA/BC;
		
		double rateEdgeP_L = (BC + DA) / (AB + CD);
		
		boolean isLandscape = false;
		if(rateEdgeP_L <= 1.3 || (rateEdgeP_L > 1.3 && rateEdgeP_L <= 1.45 && maxAngleTopBK >= 105)){
			isLandscape = true;
		}
		
		if(rateEdge >= 1.1){
			width = (AB + CD)/ 2;
			if(rateEdge <= 1.23){
				if(rateEdge >= 1.20){
					height = height + pABCD/8;
				}
				else if(rateEdge >= 1.18){
					height = height + pABCD/7;
				}
				else if(rateEdge >= 1.17){
					height = height + pABCD/6.5;
				}
				else if(rateEdge >= 1.16){
					height = height + pABCD/6;
				}
				else if(rateEdge >= 1.15){
					height = height + pABCD/5.5;
				}
				else if(rateEdge >= 1.14){
					height = height + pABCD/5.25;
				}
				else if(rateEdge >= 1.13){
					height = height + pABCD/5;
				}
				else if(rateEdge >= 1.12){
					height = height + pABCD/4.75;
				}
				else if(rateEdge >= 1.11){
					height = height + pABCD/4.5;
				}
				else if(rateEdge >= 1.10){
					height = height + pABCD/4;
				}
			}
			else if(((AB > CD) ? AB/CD : CD/AB) < 1.2) {
				rateEdge = (AB > CD) ? AB/CD : CD/AB;
				if(rateEdge >= 1.17){
					if(isLandscape)
						width = width + pABCD/15;
					else 
						width = width + pABCD/17;
				}
				else if(rateEdge >= 1.15){
					if(isLandscape)
						width = width + pABCD/14;
					else 
					width = width + pABCD/16;
				}
				else if(rateEdge >= 1.13){
					if(isLandscape)
						width = width + pABCD/13;
					else 
					width = width + pABCD/15;
				}
				else if(rateEdge >= 1.10){
					if(isLandscape)
						width = width + pABCD/12;
					else 
					width = width + pABCD/14;
				}
				else {
					if(isLandscape)
						width = width + pABCD/11;
					else 
					width = width + pABCD/13;
				}
//				Log.e("CongVCCC", "======rateEdgeNgang:"+ rateEdge);
			}
			
			maxAngleTop = 0;
		}
		
//		Log.e("CongVCCC", "=====maxAngleTop:"+ maxAngleTop + "======rateEdge:"+ rateEdge);
		
		if(maxAngleTop >= 120){
			height = height + pABCD/8.5;
		}
		else if(maxAngleTop >= 119){
			height = height + pABCD/9;
		}
		else if(maxAngleTop >= 118){
			height = height + pABCD/9.5;
		}
		else if(maxAngleTop >= 117){
			height = height + pABCD/10;
		}
		else if(maxAngleTop >= 116){
			height = height + pABCD/10.5;
		}
		else if(maxAngleTop >= 115){
			height = height + pABCD/11;
		}
		else if(maxAngleTop >= 114){
			height = height + pABCD/11.5;
		}
		else if(maxAngleTop >= 113){
			height = height + pABCD/12;
		}
		else if(maxAngleTop >= 112){
			height = height + pABCD/12.5;
		}
		else if(maxAngleTop >= 111){
			height = height + pABCD/13;
		}
		else if(maxAngleTop >= 110){
			height = height + pABCD/13.5;
		}
		else if(maxAngleTop >= 109){
			height = height + pABCD/14;
		}
		else if(maxAngleTop >= 108){
			height = height + pABCD/14.5;
		}
		else if(maxAngleTop >= 107){
			height = height + pABCD/15;
		}
		else if(maxAngleTop >= 106){
			height = height + pABCD/15.25;
		}
		else if(maxAngleTop >= 105){
			height = height + pABCD/15.5;
		}
		else if(maxAngleTop >= 104){
			height = height + pABCD/15.75;
		}
		else if(maxAngleTop >= 103){
			height = height + pABCD/16;
		}
		else if(maxAngleTop >= 102){
			height = height + pABCD/16.25;
		}
		else if(maxAngleTop >= 101){
			height = height + pABCD/16.5;
		}
		else if(maxAngleTop >= 100){
			height = height + pABCD/17;
		}
		else if(maxAngleTop >= 97){
			height = height + pABCD/18;
		}
				
		if(rateEdgeP_L <= 1.3 || (rateEdgeP_L > 1.3 && rateEdgeP_L <= 1.45 && maxAngleTopBK >= 105)){
			double ratePotrait = (BC > DA) ? BC/DA : DA/BC;
			
			height = (BC < DA ? BC : DA) ;
			if(ratePotrait >= 1.3){
				height = height + pABCD/18;
			}
		}
		
		return new Rect((int) left, (int) top, (int) width, (int) height);
	}
	
	public static Line createTopLine(Point[] arrPoints,
			double averageY) {
		Line line = new Line();
		int numOfPointTopLine = 0;
		for (int i = 0; i < arrPoints.length-2; ++i) {
			Point point = arrPoints[i];
//			if (point.y <= averageY) {
				line.arrPoint[numOfPointTopLine] = point;
				numOfPointTopLine++;
//			}
		}

		line.numberOfPoint = numOfPointTopLine;
		return line;
	}

	public static Line createBotLine(Point[] arrPoints,
			double averageY) {
		Line line = new Line();
		int numOfPointTopLine = 0;
//		for (int i = 0; i < arrPoints.length; ++i) {
//			Point point = arrPoints[i];
//			if (point.y >= averageY) {
//				line.arrPoint[numOfPointTopLine] = point;
//				numOfPointTopLine++;
//			}
//		}
		int n = arrPoints.length;
		line.arrPoint[0] = arrPoints[n-2];
		line.arrPoint[1] = arrPoints[n-1];

		line.numberOfPoint = 2;
		return line;
	}

	/**
	 * co ngoc nho hon 80 va lon hon 91 thi tra ve FALSE
	 * 
	 * @param topleft
	 * @param topright
	 * @param botleft
	 * @param botright
	 * @return
	 */
	public static boolean checkAngleInPolygon(Point topleft, Point topright,
                                              Point botleft, Point botright) {
		double MIN_ANGLE_TOP = 60.0;
		// double MIN_ANGLE_BOT =
		double[] listAngle = new double[4];

		double[] listAngleTopNew = new double[2];

		// Goc Top Left
		listAngle[0] = findAngle2Vector(topright, topleft, topright, botright);
		listAngle[1] = findAngle2Vector(botright, topright, botright, botleft);
		listAngle[2] = findAngle2Vector(botleft, botright, botleft, topleft);
		listAngle[3] = findAngle2Vector(topleft, botleft, topleft, topright);

		listAngleTopNew[0] = findAngle2Vector(topright, botright, botright,
				botleft);
		listAngleTopNew[1] = findAngle2Vector(topleft, botleft, botleft,
				botright);

//		System.out.println(String.format("NamLH angle topright = %f botright = %f botleft = %f topleft = %f", listAngle[0],listAngle[1],listAngle[2],listAngle[3]));
		if (listAngle[0] < MIN_ANGLE_TOP || listAngle[1] < MIN_ANGLE_TOP
				|| listAngle[2] < MIN_ANGLE_TOP || listAngle[3] < MIN_ANGLE_TOP
				|| listAngleTopNew[0] < MIN_ANGLE_TOP || listAngleTopNew[1] < MIN_ANGLE_TOP)
			return false;

		// if ((listAngle[0] < MIN_ANGLE_TOP && listAngleTopNew[0] <
		// MIN_ANGLE_TOP) || (listAngle[3] < MIN_ANGLE_TOP && listAngleTopNew[1]
		// < MIN_ANGLE_TOP)) {
		// return false;
		// }
		
//		double angle1 = findAngle2Vector(topleft, topright, botleft, botright);
//		double angle2 = findAngle2Vector(topleft, botleft, topright, botright);
//		System.out.println(String.format("NamLH angle a1 = %f a2 = %f", angle1,angle2));
//		if (angle1 > 10 || angle2 > 10) return false;

		return true;

	}

	private static double findAngle2Vector(Point p1, Point p2, Point p3,
                                           Point p4) {
		// vector 1 : p1 -> p2
		// vector 2 : p3 -> p4
//		double vector1x = p2.x - p1.x;
//		double vector1y = p2.y - p1.y;
//		double vector2x = p4.x - p3.y;
//		double vector2y = p4.y - p3.y;
//		double A = vector1x * vector2x + vector1y * vector2y;
//		double B1 = Math.sqrt(vector1x * vector1x + vector1y * vector1y);
//		double B2 = Math.sqrt(vector2x * vector2x + vector2y * vector2y);
//		double B = B1 * B2;
//
//		double angle = A / B;
//
//		return Math.acos(angle) * 180 / Math.PI;
		Line line1 = new Line();
		line1.point1 = p1;
		line1.point2 = p2;
		Line line2 = new Line();
		line2.point1 = p3;
		line2.point2 = p4;
		return Math.abs(MathLib.getAngle(line1, line2));
	}

	/**
	 * check contour is valid or not
	 * @param topleft
	 * @param topright
	 * @param botleft
	 * @param botright
	 * @return
	 */
	public static boolean checkContourIsNameCardOrClearFile(PointF topleft, PointF topright, PointF botleft, PointF botright){
		Point tl = new Point(topleft.x, topleft.y);
		Point tr = new Point(topright.x, topright.y);
		Point bl = new Point(botleft.x, botleft.y);
		Point br = new Point(botright.x, botright.y);
		return checkContourIsNameCardOrClearFile(tl, tr, bl, br);
	}

	public static boolean checkContourIsNameCardOrClearFile(Point topleft, Point topright, Point botleft, Point botright) {
		double MIN_RATIO = 0.60;
		if (!checkAngleInPolygon(topleft, topright, botleft, botright)) {
			return false;
		}


		double line1 = getDistance(topleft, topright);
		double line3 = getDistance(botright, botleft);
		double ratioTwoLine = ratioOfTowLine(line1, line3);

		if (ratioTwoLine < MIN_RATIO) {
			return false;
		}

		double line2 = getDistance(topright, botright);
		double line4 = getDistance(botleft, topleft);

		ratioTwoLine = ratioOfTowLine(line2, line4);
		if (ratioTwoLine < MIN_RATIO) {// 2 canh chenh lech nhau hon 2/3
			return false;
		}
		return true;

	}

	private static boolean is4CornersValidSpaceNew(Point[] arrCorner, Size cSize) {

		int x0 = 0;
		int xWidth = (int) cSize.width;
		int y0 = 0;
		int yHeight = (int) cSize.height;
		int scale = 5;
		if (arrCorner == null) {
			return false;
		}

		for (int i = 0; i < 4; ++i) {
			Point tmp = arrCorner[i];
			if ((tmp.x >= x0 && tmp.x < x0 + scale)
					|| (tmp.x > xWidth - scale && tmp.x <= xWidth)
					|| (tmp.y >= y0 && tmp.y < y0 + scale)
					|| (tmp.y > yHeight - scale && tmp.y <= yHeight)) {
				return false;
			}
		}

		return true;
	}

	private static double ratioOfTowLine(double disLine1, double disLine2) {
		double ratio = 1.0;
		if (disLine1 > disLine2) {
			ratio = disLine2 / disLine1;
		} else {
			ratio = disLine1 / disLine2;
		}
		return ratio;
	}
	
	private static int DELTA_BORDER = 5;
	
	public static boolean pointsIsValid(Point topleft, Point topright, Point botleft, Point botright, int w, int h) {
		
			if(  (topleft.x < DELTA_BORDER || (topleft.x > (w - DELTA_BORDER) && topleft.x < (w - 1))  || topleft.y < DELTA_BORDER || (topleft.y > (h - DELTA_BORDER) && topleft.y < (h - 1)) ) ||
					 (topright.x < DELTA_BORDER || ( topright.x > (w - DELTA_BORDER) &&  topright.x < (w - 1)) || topright.y < DELTA_BORDER || (topright.y > (h - DELTA_BORDER) && topright.y < (h - 1)))  ||
					 (botleft.x < DELTA_BORDER || ( botleft.x > (w - DELTA_BORDER) &&  botleft.x < (w - 1)) || botleft.y < DELTA_BORDER || (botleft.y > (h - DELTA_BORDER) && botleft.y < (h - 1))) ||
					 (botright.x < DELTA_BORDER || ( botright.x > (w - DELTA_BORDER) &&  botright.x < (w - 1)) || botright.y < DELTA_BORDER || (botright.y > (h - DELTA_BORDER) && botright.y < (h - 1)))){
				return false;
			}
			
			// ===================
			// top left angel 
			// ===================

			Line leftLineT2B = new Line(); // top to bottom
			leftLineT2B.point1 = topleft;
			leftLineT2B.point2 = botleft;

			Line topLineL2R = new Line(); // left to right
			topLineL2R.point1 = topleft;
			topLineL2R.point2 = topright;

			double angleTopLeft = MathLib.getAngle(leftLineT2B, topLineL2R);

			// ===================
			// bottom left angel 
			// ===================
			Line leftLineB2T = new Line(); // bottom to top
			leftLineB2T.point1 = botleft;
			leftLineB2T.point2 = topleft;

			Line bottomLineL2R = new Line(); // left to right
			bottomLineL2R.point1 = botleft;
			bottomLineL2R.point2 = botright;

			double angleBottomLeft = MathLib.getAngle(leftLineB2T, bottomLineL2R);

			// ===================
			// top right angel 
			// ===================
			Line rightLineT2B = new Line(); // top to bottom
			rightLineT2B.point1 = topright;
			rightLineT2B.point2 = botright;

			Line topLineR2L = new Line(); // left to right
			topLineR2L.point1 = topright;
			topLineR2L.point2 = topleft;

			double angleTopRight = MathLib.getAngle(rightLineT2B, topLineR2L);

			// ===================
			// bottom right angel
			// ===================
			
			Line rightLineB2T = new Line(); // top to bottom
			rightLineB2T.point1 = botright;
			rightLineB2T.point2 = topright;

			Line bottomLineR2L = new Line(); // left to right
			bottomLineR2L.point1 = botright;
			bottomLineR2L.point2 = botleft;
			
			double angleBottomRight = MathLib.getAngle(rightLineB2T, bottomLineR2L);

			
			double subAngleBot = Math.abs(angleBottomLeft - angleBottomRight);
			double subAngleTop = Math.abs(angleTopLeft - angleTopRight);

			if( (subAngleTop < 5) && (subAngleBot > 27) ||(subAngleBot < 5) && (subAngleTop > 27) ){
				return false;
			}

        return true;
	}

	/******* End *********************************/
}

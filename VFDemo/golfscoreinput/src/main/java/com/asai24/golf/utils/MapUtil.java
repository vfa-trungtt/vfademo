package com.asai24.golf.utils;

import android.location.Location;

import com.asai24.golf.Constant;
import com.google.android.maps.GeoPoint;

public class MapUtil {

	private static final double d2r = Math.PI / 180; // degrees to radians
	private static final double r2d = 180 / Math.PI; // radians to degrees

	/**
	 * curPosからdist(m)離れた0度方向への位置を計算します
	 * 
	 * @param curPos
	 * @param dist
	 * @return
	 */
	public static GeoPoint getPointByDistance(GeoPoint curPos, double dist) {
		double lat = curPos.getLatitudeE6() / 1E6; // 緯度
		double lon = curPos.getLongitudeE6() / 1E6;
		; // 経度
		double dr = dist / Constant.RADIUS_OF_EARTH_WGS84_METER; // 指定距離/地球半径（m）

		// 緯度計算（本来の計算式：double targetLat = lat + (dr * r2d * Math.sin(0));）
		double targetLat = lat;
		// 経度計算（本来の計算式：double targetLon = lon + ((dr * r2d) / Math.cos(lat*d2r)
		// * Math.cos(0));）
		double targetLon = lon + (dr * r2d) / Math.cos(lat * d2r);

		return new GeoPoint((int) (targetLat * 1E6), (int) (targetLon * 1E6));
	}

	// GeoPointで与えられた２地点間の距離を求める
	public static double getDistanceFromToByGeo(GeoPoint from, GeoPoint to) {
		return getDistanceFromTo((double) from.getLatitudeE6() / 1E6,
				(double) from.getLongitudeE6() / 1E6, (double) to
						.getLatitudeE6() / 1E6,
				(double) to.getLongitudeE6() / 1E6);
	}

	// 2地点の緯度、経度から距離を求める
	public static double getDistanceFromTo(double fLat, double fLon,
			double tLat, double tLon) {
		float[] results = new float[2];
		Location.distanceBetween(fLat, fLon, tLat, tLon, results);
		return results[0];
	}
}

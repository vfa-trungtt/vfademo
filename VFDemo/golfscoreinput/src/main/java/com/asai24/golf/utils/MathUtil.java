package com.asai24.golf.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MathUtil {

	/*
	 * 四捨五入関数（整数表示用）
	 */
	public static long RoundNumber(double value) {
		BigDecimal L01 = new BigDecimal(String.valueOf(value));

		return L01.setScale(0, RoundingMode.HALF_UP).longValue();
	}

	/*
	 * 四捨五入関数（小数点表示用）
	 */
	public static double RoundNumber(double value, int digit) {
		BigDecimal L01 = new BigDecimal(String.valueOf(value));

		return L01.setScale(digit, RoundingMode.HALF_UP).doubleValue();
	}
	public static void makeSubsetCombine(int[] a, List<Integer> outstr, List<Integer> outInd, int index, int expectedSum, List<List<Integer>> list)
    {
        for (int i = index; i < a.length; i++)
        {

            int count = 0;
            for (Integer item : outstr)
                count += item;

            outstr.add(a[i]);
            outInd.add(i);
            
            if ((a[i] + count) == expectedSum)
            {
                List<Integer> resultItem = new ArrayList<Integer>();
            	for(Integer item : outInd){
                	resultItem.add(item);
                }
            	list.add(resultItem);
            }

            makeSubsetCombine(a, outstr, outInd, i + 1, expectedSum, list);
            outstr.remove((Integer) a[i]);
            outInd.remove((Integer)i);
        }
    }
	
	public static void makeSubsetCombine(int[] a, List<Integer> result){ // int[] a = {4, 5, 3, 4, 5, 3, 2, 4, 5}
		List<Integer> par3List = new ArrayList<Integer>();
		List<Integer> par4List = new ArrayList<Integer>();
		List<Integer> par5List = new ArrayList<Integer>();
		
		for(int index = 0; index < a.length; index ++){
			if(a[index] == 3){
				par3List.add(index);
			}else if(a[index] == 4){
				par4List.add(index);
			}else{
				par5List.add(index);
			}
		}
		// Not as expected
		if(par4List.size() < 4 || par3List.size() < 1 || par5List.size() < 1){
			return;
		}
		
		// Shuffle 3 list
		Collections.shuffle(par3List);
		Collections.shuffle(par4List);
		Collections.shuffle(par5List);
		
		// Add the first item of par 3 list
		result.add(par3List.get(0));
		
		// Add the 4 first items of par 4 list
		result.add(par4List.get(0));
		result.add(par4List.get(1));
		result.add(par4List.get(2));
		result.add(par4List.get(3));
		
		// Add the first item of par 5 list
		result.add(par5List.get(0));
	}
	/**
	 * Round to certain number of decimals
	 *
	 * @param d
	 * @param decimalPlace
	 * @return
	 */

	public static BigDecimal round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd;
	}
}

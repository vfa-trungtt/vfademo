package com.asai24.golf.domain;

import com.google.gson.annotations.SerializedName;

/**
 * @author Akira Sosa
 *
 */
public class ScoreCardSummary {

	private String key;
	@SerializedName("total_round")
	private int totalRound;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getTotalRound() {
		return totalRound;
	}

	public void setTotalRound(int totalRound) {
		this.totalRound = totalRound;
	}

}

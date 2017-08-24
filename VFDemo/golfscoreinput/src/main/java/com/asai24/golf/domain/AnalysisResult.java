package com.asai24.golf.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Akira Sosa
 *
 */
public class AnalysisResult {

	private String title;
	private List<AnalysisDetail> details = new ArrayList<AnalysisDetail>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void addDetail(AnalysisDetail detail) {
		details.add(detail);
	}
	
	public List<AnalysisDetail> getDetails() {
		return details;
	}
	

}

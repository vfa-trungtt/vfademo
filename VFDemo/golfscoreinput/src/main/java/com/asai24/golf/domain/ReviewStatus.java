package com.asai24.golf.domain;

/**
 * @author Akira Sosa
 *
 */
public class ReviewStatus {

	public enum Status {
		REVIEWED("reviewed"),
		CANCELED("canceled");

		private String name;

		private Status (String name) {
			this.name = name;
		}
		public String toString() {
			return name;
		}
	}
	
	private Boolean shouldRemind;
	private Status status;
	
	public Boolean getShouldRemind() {
		return this.shouldRemind;
	}

	public void setShouldRemind(Boolean shouldRemind) {
		this.shouldRemind = shouldRemind;
	}

	public Status getStatus() {
		return status;
	}

	public void setAsReviewd() {
		this.status = Status.REVIEWED;
	}

	public void setAsCanceled() {
		this.status = Status.CANCELED;
	}



}

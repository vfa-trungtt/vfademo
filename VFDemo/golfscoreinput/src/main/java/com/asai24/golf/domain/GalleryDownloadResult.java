package com.asai24.golf.domain;

import java.util.List;

/**
 * @author Akira Sosa
 */
public class GalleryDownloadResult {
	
	private List<String> photoUrls;
	/*ThuNA 2013/06/17 ADD-S*/
	// Add more photo url detail
	private List<String> photoUrlDetails;
	/*ThuNA 2013/06/17 ADD-E*/
	private String lastModified;
	
	public List<String> getPhotoUrls() {
		return photoUrls;
	}

	public void setPhotoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
	}

	public List<String> getPhotoUrlDetails() {
		return photoUrlDetails;
	}

	public void setPhotoUrlDetails(List<String> photoUrlDetails) {
		this.photoUrlDetails = photoUrlDetails;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

}

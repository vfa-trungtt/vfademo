package com.asai24.golf.object;

public class SendSNSObj {
	
	private String mShareText;
	private String mShareImg;
    private String mImageName;
	
	public String getShareText() {
		return mShareText;
	}

	public void setShareText(String shareText) {
		mShareText = shareText;
	}

	public String getShareImg() {
		return mShareImg;
	}

	public void setShareImg(String shareImg) {
		mShareImg = shareImg;
	}

    public void setImageName(String imageName) {
        this.mImageName = imageName;
    }

    public String getImageName() {
        return mImageName;
    }
}

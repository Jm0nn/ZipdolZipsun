package com.ssafy.zip.util.geohash;

public enum GcsType {

	LATITUDE(-90, 90), LONGITUDE(-180, 180);

	private int left;
	private int right;

	GcsType(int left, int right) {
		this.left = left;
		this.right = right;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

}
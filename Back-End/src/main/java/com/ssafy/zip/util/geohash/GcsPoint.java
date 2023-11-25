package com.ssafy.zip.util.geohash;

public class GcsPoint {

	private final double latitude;
	private final double longitude;

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public GcsPoint(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		if (Math.abs(latitude) > 90 || Math.abs(longitude) > 180) {
			throw new IllegalArgumentException("out of range");
		}
	}

	public double getValue(GcsType geoHashType) {
		switch (geoHashType) {
		case LONGITUDE:
			return this.longitude;
		case LATITUDE:
			return this.latitude;
		}
		return 0d;
	}

	public GcsPoint addLatitude(double latitude) {
		return new GcsPoint(this.latitude + latitude, this.longitude);
	}

	public GcsPoint addLongitude(double longitude) {
		return new GcsPoint(this.latitude, this.longitude + longitude);
	}
}

package com.ssafy.zip.region.model.service;

import java.util.List;

import com.ssafy.zip.region.model.RegionDto;

public interface RegionService {

	List<RegionDto> regionList(String name);

	String getGeoHash(float lat, float lng, int hashLevel);

}

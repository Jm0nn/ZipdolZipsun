package com.ssafy.zip.region.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.zip.region.model.RegionDto;
import com.ssafy.zip.region.model.mapper.RegionMapper;
import com.ssafy.zip.util.geohash.GcsPoint;
import com.ssafy.zip.util.geohash.GeoHashLevel;
import com.ssafy.zip.util.geohash.GeoHashModule;

@Service
public class RegionServiceImpl implements RegionService {

	private RegionMapper regionMapper;

	public RegionServiceImpl(RegionMapper regionMapper) {
		this.regionMapper = regionMapper;
	}

	@Override
	public List<RegionDto> regionList(String name) {
		return regionMapper.regionList(name);
	}

	@Override
	public String getGeoHash(float lat, float lng, int hashLevel) {
		String geoHash = "";

		if (hashLevel == 4)
			geoHash = GeoHashModule.getGeoHashValueFromCoordinate(new GcsPoint(lat, lng), GeoHashLevel.FOUR);
		else if (hashLevel == 5)
			geoHash = GeoHashModule.getGeoHashValueFromCoordinate(new GcsPoint(lat, lng), GeoHashLevel.FIVE);

		return geoHash;
	}

}

package com.ssafy.zip.region.model.mapper;

import java.util.List;

import com.ssafy.zip.region.model.RegionDto;

public interface RegionMapper {

	List<RegionDto> regionList(String name);

}

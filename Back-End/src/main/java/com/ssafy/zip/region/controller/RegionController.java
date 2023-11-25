package com.ssafy.zip.region.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.zip.region.model.RegionDto;
import com.ssafy.zip.region.model.service.RegionService;

@RestController
@RequestMapping("/region")
@CrossOrigin("*")
public class RegionController {

	private RegionService regionService;

	public RegionController(RegionService regionService) {
		this.regionService = regionService;
	}

	@GetMapping("/list/{name}")
	public ResponseEntity<?> regionList(@PathVariable("name") String name) {
		List<RegionDto> list = regionService.regionList(name);

		return ResponseEntity.ok(list);
	}

	@GetMapping("/geohash/{lat}/{lng}/{hashLevel}")
	public ResponseEntity<?> getGeoHash(@PathVariable("lat") float lat, @PathVariable("lng") float lng,
			@PathVariable("hashLevel") int hashLevel) {
		String geoHash = regionService.getGeoHash(lat, lng, hashLevel);

		return ResponseEntity.ok(geoHash);
	}

}

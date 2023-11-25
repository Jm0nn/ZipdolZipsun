package com.ssafy.zip.region.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegionDto {

	private String regionCode;
	private String addressName;
	private String localName;
	private float lat;
	private float lng;
	
}

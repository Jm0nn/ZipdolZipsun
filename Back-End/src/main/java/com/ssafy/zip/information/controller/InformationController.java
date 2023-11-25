package com.ssafy.zip.information.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
@CrossOrigin("*")
public class InformationController {

	private final String XNaverClientId = "jerKw6lqMbBMgNf5LjVe";
	private final String XNaverClientSecret = "P78oSJ56sa";
	private final String nkisServiceKey = "38F517D48816362FD260";

	@GetMapping("/news/{date}")
	public ResponseEntity<?> getNewsList(@PathVariable("date") String date) throws Exception {
		URL url = new URL("https://land.naver.com/news/airsList.naver?size=12&baseDate=" + date);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");

		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}

		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}

		rd.close();
		conn.disconnect();

		return ResponseEntity.ok(sb.toString());
	}

	@GetMapping("/blog/{pgno}")
	public ResponseEntity<?> getBlog(@PathVariable("pgno") int pgno) throws Exception {
		String encUrl = URLEncoder.encode("부동산", "UTF-8");
		int page = (pgno - 1) * 20 + 1;

		StringBuilder urlSb = new StringBuilder();
		urlSb.append("https://openapi.naver.com/v1/search/blog.json?query=");
		urlSb.append(encUrl);
		urlSb.append("&start=");
		urlSb.append(page);
		urlSb.append("&display=20&sort=sim");

		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", XNaverClientId);
		requestHeaders.put("X-Naver-Client-Secret", XNaverClientSecret);
		
		URL url = new URL(urlSb.toString());
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("GET");
		
		for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
			conn.setRequestProperty(header.getKey(), header.getValue());
		}
		
		int responseCode = conn.getResponseCode();
		
		InputStreamReader streamReader = null;
		
		if (responseCode == HttpURLConnection.HTTP_OK) {
			streamReader = new InputStreamReader(conn.getInputStream());
		} else {
			streamReader = new InputStreamReader(conn.getErrorStream());
		}
		
		BufferedReader br = new BufferedReader(streamReader);
		
		StringBuilder responseBody = new StringBuilder();
		
		String line;
		while ((line = br.readLine()) != null) {
			responseBody.append(line.replace("<b>", "").replace("<\\/b>", "")).append('\n');
		}
		
		return ResponseEntity.ok(responseBody.toString());
	}

	@GetMapping("/research/{pgno}")
	public ResponseEntity<?> getResearch(@PathVariable("pgno") int pgno) throws Exception {
		String encUrl = URLEncoder.encode("국토개발", "UTF-8");

		StringBuilder urlSb = new StringBuilder();
		urlSb.append("https://nkis.re.kr/nkisApi/search/ResearchList.do?serviceKey=");
		urlSb.append(nkisServiceKey);
		urlSb.append("&pageNo=");
		urlSb.append(pgno);
		urlSb.append("&lclaScsNm=");
		urlSb.append(encUrl);
		urlSb.append("&rowCnt=24");

		URL url = new URL(urlSb.toString());

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");

		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader((InputStream) conn.getContent(), "EUC-KR"));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}

		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line.replace("\t", ""));
		}

		rd.close();
		conn.disconnect();

		return ResponseEntity.ok(sb.toString());
	}

	@GetMapping("/policy/{pgno}")
	public ResponseEntity<?> getPolicy(@PathVariable("pgno") int pgno) throws Exception {
		String encUrl = URLEncoder.encode("국토개발", "UTF-8");

		StringBuilder urlSb = new StringBuilder();
		urlSb.append("https://nkis.re.kr/nkisApi/search/ReportList.do?serviceKey=");
		urlSb.append(nkisServiceKey);
		urlSb.append("&pageNo=");
		urlSb.append(pgno);
		urlSb.append("&lclaScsNm=");
		urlSb.append(encUrl);

		URL url = new URL(urlSb.toString());

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");

		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader((InputStream) conn.getContent(), "EUC-KR"));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}

		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}

		rd.close();
		conn.disconnect();

		return ResponseEntity.ok(sb.toString());
	}

}

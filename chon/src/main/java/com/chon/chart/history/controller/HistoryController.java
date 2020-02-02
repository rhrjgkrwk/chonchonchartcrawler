package com.chon.chart.history.controller;

import java.io.IOException;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chon.chart.history.service.HistoryService;
import com.chon.chart.history.vo.History;
import com.chon.chart.history.vo.HistoryAnalytics;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/history")
public class HistoryController {
	@Autowired
	HistoryService historyService;

	@GetMapping("/getAllHistoryList")
	public List<History> getAllHistoryList() {
		return historyService.findAll(Sort.by(Sort.Direction.DESC, "historyId"));
	}

	@GetMapping("/getAllHistoryListByArtistName")
	public List<History> getAllHistoryListByArtistName(String artistName) {
		if (artistName == null || artistName.equals("")) {
			return historyService.findAll(Sort.by(Sort.Direction.DESC, "historyId"));
		}
		return historyService.findAllByArtistName(artistName, Sort.by(Sort.Direction.DESC, "historyId"));
	}

	@GetMapping("/getAllHistoryListByWatchId")
	public List<History> getAllHistoryListByWatchId(String watchId) {
		return historyService.findAllByWatchId(Integer.parseInt(watchId), Sort.by(Sort.Direction.DESC, "historyId"));
	}

	@GetMapping("/getSongHistoryAnalytics")
	public List<HistoryAnalytics> getSongHistoryAnalytics(String artistName, int rank, boolean isIng)
			throws IOException {
		return historyService.findSongHistoryAnalyticsByArtistNameAndRank(artistName, rank, isIng);
	}

	@GetMapping("/getAlbumHistoryAnalytics")
	public List<HistoryAnalytics> getAlbumHistoryAnalytics(String artistName, int rank, boolean isIng)
			throws IOException {
		return historyService.findAlbumHistoryAnalyticsByArtistNameAndRank(artistName, rank, isIng);
	}

	@GetMapping("isValidWatchArtistName")
	public boolean isValidWatchArtistName(String watchArtistName) throws IOException {
		watchArtistName = watchArtistName.toLowerCase().trim();
		System.out.println(watchArtistName);

		try {
			Jsoup.connect("https://kworb.net/itunes/artist/" + watchArtistName + ".html").get();
		} catch (HttpStatusException e) {
			// TODO: handle exception
			log.info("가수명이 잘못되었습니다.");
			return false;
		}
		return true;
	}

}
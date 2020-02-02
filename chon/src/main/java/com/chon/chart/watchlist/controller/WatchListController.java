package com.chon.chart.watchlist.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chon.chart.history.dao.HistoryDAO;
import com.chon.chart.history.vo.History;
import com.chon.chart.watchlist.service.WatchListService;
import com.chon.chart.watchlist.vo.WatchList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/watchList")
public class WatchListController {
	@Autowired
	WatchListService watchListService;
	
	@Autowired
	HistoryDAO historyDAO;

	@GetMapping("/getWatchListByWatchId")
	public WatchList getWatchListByWatchId(String watchId) {
		return watchListService.findAllByWatchId(Integer.parseInt(watchId));
	}

	@GetMapping("/getAllWatchList")
	public List<WatchList> getAllWatchList() {
		return watchListService.findAll(Sort.by(Sort.Direction.DESC, "watchId"));
	}

	@GetMapping("/isValidWatchArtistName")
	public boolean isValidWatchArtistName(String watchArtistName) throws IOException {
		watchArtistName = watchArtistName.toLowerCase().replaceAll(" ", "");
		System.out.println(watchArtistName);
		try {
			Jsoup.connect("https://kworb.net/itunes/artist/" + watchArtistName + ".html").get();
		} catch (HttpStatusException e) {
			log.info("가수명이 잘못되었습니다.");
			return false;
		}
		return true;
	}

	@PutMapping("/addArtist")
	public boolean addArtist(HttpServletResponse res, String watchArtistName, String watchRankFrom, String watchRankTo)
			throws IOException {
		watchArtistName = watchArtistName.toLowerCase().replaceAll(" ", "");
		WatchList watch = new WatchList(watchArtistName, watchRankFrom, watchRankTo);
		watchListService.addArtist(watch);
		return true;
	}

	@DeleteMapping("/deleteArtist")
	public boolean deleteArtist(HttpServletResponse res, int watchId) throws IOException {
		historyDAO.removeByWatchId(watchId);
		watchListService.deleteByWatchId(watchId);
		return true;
	}

}
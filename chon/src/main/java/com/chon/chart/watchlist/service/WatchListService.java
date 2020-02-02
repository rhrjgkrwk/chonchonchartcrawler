package com.chon.chart.watchlist.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.chon.chart.watchlist.vo.WatchList;

public interface WatchListService {
	public WatchList findAllByWatchId(int watchId);
	public List<WatchList> findAll(Sort sort);
	public WatchList addArtist(WatchList watch);
	public void deleteByWatchId(int watchId);
}

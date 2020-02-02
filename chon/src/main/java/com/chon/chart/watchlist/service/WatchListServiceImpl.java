package com.chon.chart.watchlist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.chon.chart.watchlist.dao.WatchListDAO;
import com.chon.chart.watchlist.vo.WatchList;

@Service
public class WatchListServiceImpl implements WatchListService {
	@Autowired
	WatchListDAO watchListDAO;

	@Override
	public List<WatchList> findAll(Sort sort) {
		return watchListDAO.findAll(sort);
	}

	@Override
	public WatchList addArtist(WatchList watch) {
		watchListDAO.saveAndFlush(watch);
		return watch;
	}

	@Override
	public void deleteByWatchId(int watchId) {
		WatchList watchList = new WatchList();
		watchList.setWatchId(watchId);
		watchListDAO.delete(watchList);
	}

	@Override
	public WatchList findAllByWatchId(int watchId) {
		return watchListDAO.findAllByWatchId(watchId).get(0);
	}

}

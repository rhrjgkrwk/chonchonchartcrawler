package com.chon.chart.watchlist.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.chon.chart.watchlist.vo.WatchList;

public interface WatchListDAO extends JpaRepository<WatchList, Integer> {
	List<WatchList> findAllByWatchId(int watchId);

	List<WatchList> findAll(Sort sort);

	public void deleteByWatchId(int watchId);
}

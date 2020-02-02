package com.chon.chart.history.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.chon.chart.history.vo.History;
import com.chon.chart.history.vo.HistoryAnalytics;

public interface HistoryService {
	public List<History> findAll(Sort sort);

	public List<History> findAllByArtistName(String artistName, Sort sort);
	public List<History> findAllByWatchId(int watchId, Sort sort);

	public List<History> findAllByWatchIdAndToDate(int watchId, String toDate);

	public History addOrReplaceHistory(History history);

	History findTopByWatchIdAndChartNameAndCountryNameOrderByWatchIdDesc(int watchId, String chartName,
			String countryName);

	History findTopByWatchIdAndChartNameAndCountryNameAndSongNameOrderByHistoryIdDesc(int watchId, String chartName,
			String countryName, String songName);

	History findTopByWatchIdAndChartNameAndCountryNameAndAlbumNameOrderByHistoryIdDesc(int watchId, String chartName,
			String countryName, String albumName);

	List<HistoryAnalytics> findSongHistoryAnalyticsByArtistNameAndRank(String artistName, int rank, boolean isIng);

	List<HistoryAnalytics> findAlbumHistoryAnalyticsByArtistNameAndRank(String artistName, int rank, boolean isIng);

}

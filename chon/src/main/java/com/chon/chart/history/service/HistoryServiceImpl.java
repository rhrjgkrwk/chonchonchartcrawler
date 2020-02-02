package com.chon.chart.history.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.chon.chart.history.dao.HistoryDAO;
import com.chon.chart.history.vo.History;
import com.chon.chart.history.vo.HistoryAnalytics;

@Service
public class HistoryServiceImpl implements HistoryService {
	@Autowired
	HistoryDAO historyDAO;

	@Override
	public List<History> findAll(Sort sort) {
		return historyDAO.findAll(sort);
	}

	@Override
	public List<History> findAllByArtistName(String artistName, Sort sort) {
		return historyDAO.findAllByArtistName(artistName, sort);
	}

	@Override
	public List<History> findAllByWatchId(int watchId, Sort sort) {
		return historyDAO.findAllByWatchId(watchId, sort);
	}

	@Override
	public List<History> findAllByWatchIdAndToDate(int watchId, String toDate) {
		return historyDAO.findAllByWatchIdAndToDate(watchId, toDate);
	}

	@Override
	public History addOrReplaceHistory(History history) {
		historyDAO.saveAndFlush(history);
		return history;
	}

	@Override
	public History findTopByWatchIdAndChartNameAndCountryNameOrderByWatchIdDesc(int watchId, String chartName,
			String countryName) {
		return historyDAO.findTopByWatchIdAndChartNameAndCountryNameOrderByWatchIdDesc(watchId, chartName, countryName);
	}

	@Override
	public History findTopByWatchIdAndChartNameAndCountryNameAndSongNameOrderByHistoryIdDesc(int watchId,
			String chartName, String countryName, String songName) {
		return historyDAO.findTopByWatchIdAndChartNameAndCountryNameAndSongNameOrderByHistoryIdDesc(watchId, chartName,
				countryName, songName);
	}

	@Override
	public History findTopByWatchIdAndChartNameAndCountryNameAndAlbumNameOrderByHistoryIdDesc(int watchId,
			String chartName, String countryName, String albumName) {
		return historyDAO.findTopByWatchIdAndChartNameAndCountryNameAndAlbumNameOrderByHistoryIdDesc(watchId, chartName,
				countryName, albumName);
	}

	@Override
	public List<HistoryAnalytics> findSongHistoryAnalyticsByArtistNameAndRank(String artistName, int rank,
			boolean isIng) {
		List<HistoryAnalytics> result = new ArrayList<>();
		List<String> distinctSongNames = historyDAO.findDistinctSongNameByArtistName(artistName);
		for (int i = 0; i < distinctSongNames.size(); i++) {
			String songName = distinctSongNames.get(i);

			List<History> historyList = null;

			if (isIng) {
				historyList = historyDAO.findAllByArtistNameAndSongNameAndRankAndToDateNullOrderByHistoryIdDesc(
						artistName, songName, rank);
			} else {
				historyList = historyDAO.findAllByArtistNameAndSongNameAndRankAndToDateNotNullOrderByHistoryIdDesc(
						artistName, songName, rank);
			}

			Set<String> itunesCountrySet = new HashSet<>();
			Set<String> appleCountrySet = new HashSet<>();

			HistoryAnalytics itunesHistoryAnalytics = new HistoryAnalytics(artistName, songName, null, "Itunes", rank,
					0, new HashMap<>());
			HistoryAnalytics appleHistoryAnalytics = new HistoryAnalytics(artistName, songName, null, "Apple", rank, 0,
					new HashMap<>());

			for (int j = 0; j < historyList.size(); j++) {
				int historyId = historyList.get(j).getHistoryId();
				String chartName = historyList.get(j).getChartName();
				String countryName = historyList.get(j).getCountryName();
				if (chartName.equals("Itunes")) {
					if (itunesCountrySet.contains(countryName)) {
						continue;
					}
					itunesCountrySet.add(countryName);
					itunesHistoryAnalytics.getCountries().put(countryName, historyId);
				}
				if (chartName.equals("Apple")) {
					if (appleCountrySet.contains(countryName)) {
						continue;
					}
					appleCountrySet.add(countryName);
					appleHistoryAnalytics.getCountries().put(countryName, historyId);
				}
			}
			if (itunesHistoryAnalytics.getCountries().size() > 0) {
				itunesHistoryAnalytics.setCount(itunesHistoryAnalytics.getCountries().size());
				result.add(itunesHistoryAnalytics);
			}
			if (appleHistoryAnalytics.getCountries().size() > 0) {
				appleHistoryAnalytics.setCount(appleHistoryAnalytics.getCountries().size());
				result.add(appleHistoryAnalytics);
			}
		}
		return result;
	}

	@Override
	public List<HistoryAnalytics> findAlbumHistoryAnalyticsByArtistNameAndRank(String artistName, int rank,
			boolean isIng) {
		List<HistoryAnalytics> result = new ArrayList<>();
		List<String> distinctAlbumNames = historyDAO.findDistinctAlbumNameByArtistName(artistName);
		for (int i = 0; i < distinctAlbumNames.size(); i++) {
			String albumName = distinctAlbumNames.get(i);
			List<History> historyList = null;

			if (isIng) {
				historyList = historyDAO.findAllByArtistNameAndAlbumNameAndRankAndToDateNullOrderByHistoryIdDesc(
						artistName, albumName, rank);
			} else {
				historyList = historyDAO.findAllByArtistNameAndAlbumNameAndRankAndToDateNotNullOrderByHistoryIdDesc(
						artistName, albumName, rank);
			}

			Set<String> itunesCountrySet = new HashSet<>();
			Set<String> appleCountrySet = new HashSet<>();

			HistoryAnalytics itunesHistoryAnalytics = new HistoryAnalytics(artistName, null, albumName, "Itunes", rank,
					0, new HashMap<>());
			HistoryAnalytics appleHistoryAnalytics = new HistoryAnalytics(artistName, null, albumName, "Apple", rank, 0,
					new HashMap<>());

			for (int j = 0; j < historyList.size(); j++) {
				int historyId = historyList.get(j).getHistoryId();
				String chartName = historyList.get(j).getChartName();
				String countryName = historyList.get(j).getCountryName();

				System.out.println(chartName);
				System.out.println(countryName);

				if (chartName.equals("Itunes")) {
					if (itunesCountrySet.contains(countryName)) {
						continue;
					}
					itunesCountrySet.add(countryName);
					itunesHistoryAnalytics.getCountries().put(countryName, historyId);
				}
				if (chartName.equals("Apple")) {
					if (appleCountrySet.contains(countryName)) {
						continue;
					}
					appleCountrySet.add(countryName);
					appleHistoryAnalytics.getCountries().put(countryName, historyId);
				}
			}
			
			
			if (itunesHistoryAnalytics.getCountries().size() > 0) {
				itunesHistoryAnalytics.setCount(itunesHistoryAnalytics.getCountries().size());
				result.add(itunesHistoryAnalytics);
			}
			if (appleHistoryAnalytics.getCountries().size() > 0) {
				appleHistoryAnalytics.setCount(appleHistoryAnalytics.getCountries().size());
				result.add(appleHistoryAnalytics);
			}
		}
		return result;
	}

}

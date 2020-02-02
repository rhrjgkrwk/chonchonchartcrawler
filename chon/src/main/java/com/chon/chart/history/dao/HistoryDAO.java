package com.chon.chart.history.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.chon.chart.history.vo.History;

public interface HistoryDAO extends JpaRepository<History, Integer> {
	List<History> findAll(Sort sort);

	List<History> findAllByArtistName(String artistName, Sort sort);

	List<History> findAllByWatchId(int watchId, Sort sort);

	List<History> findAllByWatchIdAndToDate(int watchId, String toDate);

	@Query(value = "select * from history where artist_name = ? and song_name = ? and rank = ? and to_date is null", nativeQuery = true)
	List<History> findAllByArtistNameAndSongNameAndRankAndToDateNullOrderByHistoryIdDesc(String artistName,
			String songName, int rank);

	@Query(value = "select * from history where artist_name = ? and song_name = ? and rank = ? and to_date is not null", nativeQuery = true)
	List<History> findAllByArtistNameAndSongNameAndRankAndToDateNotNullOrderByHistoryIdDesc(String artistName,
			String songName, int rank);

	@Query(value = "select * from history where artist_name = ? and album_name = ? and rank = ? and to_date is null", nativeQuery = true)
	List<History> findAllByArtistNameAndAlbumNameAndRankAndToDateNullOrderByHistoryIdDesc(String artistName,
			String albumName, int rank);

	@Query(value = "select * from history where artist_name = ? and album_name = ? and rank = ? and to_date is not null", nativeQuery = true)
	List<History> findAllByArtistNameAndAlbumNameAndRankAndToDateNotNullOrderByHistoryIdDesc(String artistName,
			String albumName, int rank);

	@Query(value = "select distinct album_name from history where artist_name = ?", nativeQuery = true)
	List<String> findDistinctAlbumNameByArtistName(String artistName);

	@Query(value = "select distinct song_name from history where artist_name = ?", nativeQuery = true)
	List<String> findDistinctSongNameByArtistName(String artistName);

	History findTopByWatchIdAndChartNameAndCountryNameOrderByWatchIdDesc(int watchId, String chartName,
			String countryName);

	History findTopByWatchIdAndChartNameAndCountryNameAndSongNameOrderByHistoryIdDesc(int watchId, String chartName,
			String countryName, String songName);

	History findTopByWatchIdAndChartNameAndCountryNameAndAlbumNameOrderByHistoryIdDesc(int watchId, String chartName,
			String countryName, String albumName);
	
	@Transactional
	List<History> removeByWatchId(int watchId);  
}
 
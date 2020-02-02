package com.chon.chart.history.vo;

import java.util.HashMap;

import lombok.Data;

@Data
public class HistoryAnalytics {
	private String artistName;
	private String songName;
	private String albumName;
	private String chartName;
	private int rank;
	private int count;
	private HashMap<String, Integer> countries;

	public HistoryAnalytics() {

	}

	public HistoryAnalytics(String artistName, String songName, String albumName, String chartName, int rank, int count,
			HashMap<String, Integer> countries) {
		super();
		this.artistName = artistName;
		this.songName = songName;
		this.albumName = albumName;
		this.chartName = chartName;
		this.rank = rank;
		this.count = count;
		this.countries = countries;
	}
}

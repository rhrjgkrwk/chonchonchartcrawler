package com.chon.chart.history.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class History {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int historyId;
	private int watchId;
	private int rank;
	private String artistName;
	private String countryName;
	private String albumName;
	private String chartName;
	private String songName;
	private String fromDate;
	private String toDate;
	private String alarmYn;

	public History() {

	}
	
	public History(int watchId, int rank, String artistName, String countryName, String albumName, String songName, String fromDate,
			String toDate, String alarmYn, String chartName) {
		super();
		this.watchId = watchId;
		this.rank = rank;
		this.artistName = artistName;
		this.countryName = countryName;
		this.albumName = albumName;
		this.songName = songName;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.alarmYn = alarmYn;
		this.chartName = chartName;
	}

	public History(int historyId, int watchId, int rank, String artistName, String countryName, String albumName, String songName, String fromDate,
			String toDate, String alarmYn, String chartName) {
		super();
		this.historyId = historyId;
		this.watchId = watchId;
		this.rank = rank;
		this.artistName = artistName;
		this.countryName = countryName;
		this.albumName = albumName;
		this.songName = songName;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.alarmYn = alarmYn;
		this.chartName = chartName;
	}

}

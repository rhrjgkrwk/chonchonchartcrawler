package com.chon.chart.watchlist.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class WatchList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int watchId;
	private String watchArtistName;
	private String watchRankFrom;
	private String watchRankTo;

	public WatchList() {

	}

	public WatchList(String watchArtistName, String watchRankFrom, String watchRankTo) {
		super();
		this.watchArtistName = watchArtistName;
		this.watchRankFrom = watchRankFrom;
		this.watchRankTo = watchRankTo;
	}

	public WatchList(int watchId, String watchArtistName, String watchRankFrom, String watchRankTo) {
		super();
		this.watchId = watchId;
		this.watchArtistName = watchArtistName;
		this.watchRankFrom = watchRankFrom;
		this.watchRankTo = watchRankTo;
	}

}

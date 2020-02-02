package com.chon.chart.crawler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.chon.chart.history.dao.HistoryDAO;
import com.chon.chart.history.service.HistoryService;
import com.chon.chart.history.vo.History;
import com.chon.chart.watchlist.service.WatchListService;
import com.chon.chart.watchlist.vo.WatchList;

@Configuration
@EnableScheduling
public class BatchCrawler {
	@Autowired
	WatchListService watchListService;
	@Autowired
	HistoryService historyService;
	@Autowired
	HistoryDAO his;

	@Scheduled(fixedDelay = 50000)
	public void storeHistory() throws IOException {
		
		
		List<WatchList> watchList = watchListService.findAll(Sort.by(Sort.Direction.DESC, "watchId")); // 모든 watchlist를
																										// 가져온다.
		for (WatchList watch : watchList) {
			int watchId = watch.getWatchId();
			String artistName = watch.getWatchArtistName();
			int watchRankFrom = Integer.parseInt(watch.getWatchRankFrom());
			int watchRankTo = Integer.parseInt(watch.getWatchRankTo());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			HashMap<Integer, History> outOfChart = new HashMap<>();
			List<History> tempList = historyService.findAllByWatchIdAndToDate(watchId, null); // 해당 watch id로부터 크롤링되었던
																								// history 내역 중완료일자가 없는
																								// 것을 전부 가져온다.
			for (History temp : tempList) {
				outOfChart.put(temp.getHistoryId(), temp);
			}

			if (watch.getWatchArtistName() == null) {
				continue;
			}

			String watchArtistName = watch.getWatchArtistName().toLowerCase().trim();

			Document artistDocument = null;
			try {
				artistDocument = Jsoup.connect("https://kworb.net/itunes/artist/" + watchArtistName + ".html").get();
			} catch (HttpStatusException e) {
				continue;
			}

			// prefix : Album:
			Elements albums = artistDocument.select("#albums").select("td[valign=top]");

			for (int i = 0; i < albums.size(); i++) {
				String albumName = albums.get(i).select(".wrap").text().substring(7);
				Elements itunesRankAndCountry = albums.get(i).select(".itu").select("a[href^=/charts/]");
				Elements appleRankAndCountry = albums.get(i).select(".app").select("a[href^=/charts/]");

				if (!itunesRankAndCountry.isEmpty()) {
					for (int j = 0; j < itunesRankAndCountry.size(); j++) {
						if (itunesRankAndCountry.get(j).classNames().size() > 1) {
							continue;
						}

						StringTokenizer st = new StringTokenizer(itunesRankAndCountry.get(j).text(), "# ");
						int rank = Integer.parseInt(st.nextToken());
						String countryName = "";
						while (st.hasMoreTokens()) {
							countryName += st.nextToken();
						}

						// getLatest watch history and compare with new history
						// watchId, 차트이름, 나라이름같은거중에 가장 최신 history 가져와서 비교

						/////////////////////////
						// 비교 순서 변경해야된다.. 랭크부터 확인하면 endDate가 입력이 안되는 경우 발생함..
						//////////////////////

						History latestHistory = historyService
								.findTopByWatchIdAndChartNameAndCountryNameAndAlbumNameOrderByHistoryIdDesc(watchId,
										"Itunes", countryName, albumName);

						History newHistory = new History(watchId, rank, artistName, countryName, albumName, null, null,
								null, null, "Itunes");
						if (latestHistory == null) {
							// 새로 저장
							if (watchRankFrom <= rank && watchRankTo >= rank) {
								newHistory.setFromDate(sdf.format(new Date()));
								historyService.addOrReplaceHistory(newHistory);
							}
						} else {

							if (latestHistory.getToDate() != null) {
								// 새로 저장
								if (watchRankFrom <= rank && watchRankTo >= rank) {
									newHistory.setFromDate(sdf.format(new Date()));
									historyService.addOrReplaceHistory(newHistory);
								}
							} else if (latestHistory.getRank() != rank) {
								// To Date Update.
								latestHistory.setToDate(sdf.format(new Date()));
								historyService.addOrReplaceHistory(latestHistory);
								// 새로 저장
								if (watchRankFrom <= rank && watchRankTo >= rank) {
									newHistory.setFromDate(sdf.format(new Date()));
									historyService.addOrReplaceHistory(newHistory);
								}

							} else {
								outOfChart.remove(latestHistory.getHistoryId());
							}
						}
					}
				}

				if (!appleRankAndCountry.isEmpty()) {
					for (int j = 0; j < appleRankAndCountry.size(); j++) {
						if (appleRankAndCountry.get(j).classNames().size() > 1) {
							continue;
						}

						StringTokenizer st = new StringTokenizer(appleRankAndCountry.get(j).text(), "# ");
						int rank = Integer.parseInt(st.nextToken());
						String countryName = "";
						while (st.hasMoreTokens()) {
							countryName += st.nextToken();
						}

						History latestHistory = historyService
								.findTopByWatchIdAndChartNameAndCountryNameAndAlbumNameOrderByHistoryIdDesc(watchId,
										"Apple", countryName, albumName);

						History newHistory = new History(watchId, rank, artistName, countryName, albumName, null, null,
								null, null, "Apple");
						if (latestHistory == null) {
							// 새로 저장
							if (watchRankFrom <= rank && watchRankTo >= rank) {
								newHistory.setFromDate(sdf.format(new Date()));
								historyService.addOrReplaceHistory(newHistory);
							}
						} else {

							if (latestHistory.getToDate() != null) {
								// 새로 저장
								if (watchRankFrom <= rank && watchRankTo >= rank) {
									newHistory.setFromDate(sdf.format(new Date()));
									historyService.addOrReplaceHistory(newHistory);
								}
							} else if (latestHistory.getRank() != rank) {
								// To Date Update.

								latestHistory.setToDate(sdf.format(new Date()));
								historyService.addOrReplaceHistory(latestHistory);
								// 새로 저장
								if (watchRankFrom <= rank && watchRankTo >= rank) {
									newHistory.setFromDate(sdf.format(new Date()));
									historyService.addOrReplaceHistory(newHistory);
								}

							} else {
								outOfChart.remove(latestHistory.getHistoryId());
							}
						}
					}

				}
			}

			// prefix : Album:
			Elements songs = artistDocument.select("#songs").select("td[valign=top]");

			for (int i = 0; i < songs.size(); i++) {
				String songName = songs.get(i).select(".wrap").text();
				Elements itunesRankAndCountry = songs.get(i).select(".itu").select("a[href^=/charts/]");
				Elements appleRankAndCountry = songs.get(i).select(".app").select("a[href^=/charts/]");

				if (!itunesRankAndCountry.isEmpty()) {
					for (int j = 0; j < itunesRankAndCountry.size(); j++) {
						if (itunesRankAndCountry.get(j).classNames().size() > 1) {
							continue;
						}

						StringTokenizer st = new StringTokenizer(itunesRankAndCountry.get(j).text(), "# ");
						int rank = Integer.parseInt(st.nextToken());
						String countryName = "";
						while (st.hasMoreTokens()) {
							countryName += st.nextToken();
						}

						History latestHistory = historyService
								.findTopByWatchIdAndChartNameAndCountryNameAndSongNameOrderByHistoryIdDesc(watchId,
										"Itunes", countryName, songName);

						History newHistory = new History(watchId, rank, artistName, countryName, null, songName, null,
								null, null, "Itunes");
						if (latestHistory == null) {
							// 새로 저장
							if (watchRankFrom <= rank && watchRankTo >= rank) {
								newHistory.setFromDate(sdf.format(new Date()));
								historyService.addOrReplaceHistory(newHistory);
							}
						} else {
							if (latestHistory.getToDate() != null) {
								// 새로 저장
								if (watchRankFrom <= rank && watchRankTo >= rank) {
									newHistory.setFromDate(sdf.format(new Date()));
									historyService.addOrReplaceHistory(newHistory);
								}
							} else if (latestHistory.getRank() != rank) {
								// To Date Update 하고랭크안에 들었으면. 새로 저장.
								latestHistory.setToDate(sdf.format(new Date()));
								historyService.addOrReplaceHistory(latestHistory);

								// 새로 저장
								if (watchRankFrom <= rank && watchRankTo >= rank) {
									newHistory.setFromDate(sdf.format(new Date()));
									historyService.addOrReplaceHistory(newHistory);
								}

							} else {
								outOfChart.remove(latestHistory.getHistoryId());
							}
						}
					}
				}

				if (!appleRankAndCountry.isEmpty()) {
					for (int j = 0; j < appleRankAndCountry.size(); j++) {
						if (appleRankAndCountry.get(j).classNames().size() > 1) {
							continue;
						}

						StringTokenizer st = new StringTokenizer(appleRankAndCountry.get(j).text(), "# ");
						int rank = Integer.parseInt(st.nextToken());
						String countryName = "";
						while (st.hasMoreTokens()) {
							countryName += st.nextToken();
						}

						History latestHistory = historyService
								.findTopByWatchIdAndChartNameAndCountryNameAndSongNameOrderByHistoryIdDesc(watchId,
										"Apple", countryName, songName);

						History newHistory = new History(watchId, rank, artistName, countryName, null, songName, null,
								null, null, "Apple");
						if (latestHistory == null) {
							// 새로 저장
							if (watchRankFrom <= rank && watchRankTo >= rank) {
								newHistory.setFromDate(sdf.format(new Date()));
								historyService.addOrReplaceHistory(newHistory);
							}
						} else {
							if (latestHistory.getToDate() != null) {
								// 새로 저장
								if (watchRankFrom <= rank && watchRankTo >= rank) {
									newHistory.setFromDate(sdf.format(new Date()));
									historyService.addOrReplaceHistory(newHistory);
								}
							} else if (latestHistory.getRank() != rank) {
								// To Date Update.

								latestHistory.setToDate(sdf.format(new Date()));
								historyService.addOrReplaceHistory(latestHistory);
								// 새로 저장
								if (watchRankFrom <= rank && watchRankTo >= rank) {
									newHistory.setFromDate(sdf.format(new Date()));
									historyService.addOrReplaceHistory(newHistory);
								}
							} else {
								outOfChart.remove(latestHistory.getHistoryId());
							}
						}
					}
				}
			}

			for (History history : outOfChart.values()) {
				history.setToDate(sdf.format(new Date()));
				historyService.addOrReplaceHistory(history);
			}
		}
		
		
		
		List<String> albumNames = his.findDistinctSongNameByArtistName("taeyeon");
		for (String a : albumNames) {
		}
		
		
		
	}
}

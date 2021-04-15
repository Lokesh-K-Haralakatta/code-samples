package com.lunatech.imdb.dto;

import lombok.Data;

@Data
public class TitleAndRatingsDetails {
	private String primaryTitle;
	private String originalTitle;
	private String genres;
	//private Double avgRating;
	//private Integer receivedVotes;
	
	public TitleAndRatingsDetails() { }

	public TitleAndRatingsDetails(String primaryTitle, String originalTitle, String genres) {
		this.primaryTitle = primaryTitle;
		this.originalTitle = originalTitle;
		this.genres = genres;
		//this.avgRating = avgRating;
		//this.receivedVotes = receivedVotes;
	}
	
	
}

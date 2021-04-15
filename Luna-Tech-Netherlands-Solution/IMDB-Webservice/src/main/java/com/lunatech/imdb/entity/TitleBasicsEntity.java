package com.lunatech.imdb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TITLE_BASICS")
public class TitleBasicsEntity {
	@Id
	@Column(name = "tconst")
	private String tconst;
	
	@Column(name = "titletype")
	private String titleType;
	
	@Column(name = "primarytitle")
	private String primaryTitle;
	
	@Column(name = "originaltitle")
	private String originalTitle;
	
	@Column(name = "isadult")
	private Boolean isAdult;
	
	@Column(name = "startyear")
	private Integer startYear;
	
	@Column(name = "endyear")
	private Integer endYear;
	
	@Column(name = "runtimeminutes")
	private Integer runtimeMinutes;
	
	@Column(name = "genres")
	private String genres;

	public TitleBasicsEntity() {}

	public TitleBasicsEntity(String tconst, String titleType, String primaryTitle, String originalTitle,
			Boolean isAdult, Integer startYear, Integer endYear, Integer runtimeMinutes, String genres) {
		super();
		this.tconst = tconst;
		this.titleType = titleType;
		this.primaryTitle = primaryTitle;
		this.originalTitle = originalTitle;
		this.isAdult = isAdult;
		this.startYear = startYear;
		this.endYear = endYear;
		this.runtimeMinutes = runtimeMinutes;
		this.genres = genres;
	}

	
}

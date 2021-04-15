package com.lunatech.imdb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "NAME_BASICS")
public class NameBasicsEntity {
	@Id
	@Column(name = "nconst")
	private String nconst;
	
	@Column(name = "primaryname")
	private String primaryName;
	
	@Column(name = "birthyear")
	private Integer birthYear;
	
	@Column(name = "deathyear")
	private Integer deathYear;
	
	@Column(name = "primaryprofession")
	private String primaryProfession;
	
	@Column(name = "knownfortitles")
	private String knownForTitles;
	
	public NameBasicsEntity() {}

	public NameBasicsEntity(String nconst, String primaryName, Integer birthYear,
			Integer deathYear, String primaryProfession,String knownForTitles) {
		super();
		this.nconst = nconst;
		this.primaryName = primaryName;
		this.birthYear = birthYear;
		this.deathYear = deathYear;
		this.primaryProfession = primaryProfession;
		this.knownForTitles = knownForTitles;
	}
	
	
}

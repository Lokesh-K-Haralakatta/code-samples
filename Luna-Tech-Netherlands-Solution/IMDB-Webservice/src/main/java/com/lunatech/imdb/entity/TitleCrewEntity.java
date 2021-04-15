package com.lunatech.imdb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TITLE_CREW")
public class TitleCrewEntity {
	@Id
	@Column(name = "tconst")
	private String tconst;
	
	@Column(name = "directors")
	private String directors;
	
	@Column(name = "writers")
	private String writers;
}

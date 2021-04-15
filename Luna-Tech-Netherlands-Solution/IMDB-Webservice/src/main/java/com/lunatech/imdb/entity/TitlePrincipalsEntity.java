package com.lunatech.imdb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TITLE_PRINCIPALS")
public class TitlePrincipalsEntity {
	@Id
	@Column(name = "tconst")
	private String tconst;
	
	@Column(name = "ordering")
	private Integer ordering;
	
	@Column(name = "nconst")
	private String nconst;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "job")
	private String job;
	
	@Column(name = "characters")
	private String characters;
	
	public TitlePrincipalsEntity() { }

	public TitlePrincipalsEntity(String tconst, Integer ordering, String nconst, String category, String job,
			String characters) {
		super();
		this.tconst = tconst;
		this.ordering = ordering;
		this.nconst = nconst;
		this.category = category;
		this.job = job;
		this.characters = characters;
	}
	
	
}

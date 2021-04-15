package com.lunatech.imdb.dto;

import java.util.List;

import com.lunatech.imdb.pojo.CrewPerson;
import com.lunatech.imdb.pojo.TitleDetails;

import lombok.Data;

@Data
public class CastAndCrewDetails {
	private TitleDetails title;
	private List<CrewPerson> castAndCrew;	
}

package com.lunatech.imdb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lunatech.imdb.dto.CastAndCrewDetails;
import com.lunatech.imdb.dto.TitleAndRatingsDetails;
import com.lunatech.imdb.service.MoviesService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/imdb/movies")
public class MoviesController {
	@Autowired
	private MoviesService service;
	
	@GetMapping("/title/{movieTitle}")
	public List<CastAndCrewDetails> getCastAndCrewDetailsForMovies(@PathVariable String movieTitle){
		log.info("Received request for /title with movieTitle: " +movieTitle);
		List<CastAndCrewDetails> castAndCrewData = service.retrieveCastAndCrewForMoviesWithTitle(movieTitle);
		if(castAndCrewData == null)
			log.error("Error occurred while serving request to /title end point, refer to logs for details");
		else
			log.info("Total number of CastAndCrewDetails Objects in response: " +castAndCrewData.size());
		
		return castAndCrewData;
	}
	
	@GetMapping("/genre/{genreType}")
	public List<TitleAndRatingsDetails> getTitlesAndRatingsForGenre(@PathVariable String genreType){
		log.info("Received request for /genre with genreType: " +genreType);
		List<TitleAndRatingsDetails> titlesAndRatings = service.retrieveTitlesAndRatingsBasedOnGenre(genreType);
		if(titlesAndRatings == null)
			log.error("Error occurred while serving request to /genre end point, refer to logs for details");
		else
			log.info("Total number of TitleAndRatingsDetails Objects in response: " + titlesAndRatings.size());
		
		return titlesAndRatings;
	}
}

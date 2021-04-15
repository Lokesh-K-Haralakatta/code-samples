package com.lunatech.imdb.service;

import java.util.List;

import com.lunatech.imdb.dto.CastAndCrewDetails;
import com.lunatech.imdb.dto.TitleAndRatingsDetails;

public interface MoviesInterface {
	List<CastAndCrewDetails> retrieveCastAndCrewForMoviesWithTitle(String title);
	List<TitleAndRatingsDetails> retrieveTitlesAndRatingsBasedOnGenre(String genreType);
}

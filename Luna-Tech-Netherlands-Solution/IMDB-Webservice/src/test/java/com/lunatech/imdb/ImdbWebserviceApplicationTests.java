package com.lunatech.imdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import com.lunatech.imdb.dto.CastAndCrewDetails;
import com.lunatech.imdb.dto.TitleAndRatingsDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ImdbWebserviceApplicationTests {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	void testCastAndCrewForMoviesWithTitle() {
		String URL = "http://localhost:" + port + "/imdb/movies/title/";
		String movieTitle = "GeGe";
		
		//Retrieve movies associated with title GeGe
		ResponseEntity<CastAndCrewDetails[]> response = this.restTemplate.getForEntity
				                                          (URL+movieTitle, CastAndCrewDetails[].class);
		
		assertEquals(response.getBody().length,1,"Expected movies count not equal to 1");
		List<CastAndCrewDetails> castAndCrewList = new ArrayList<CastAndCrewDetails>();
		Collections.addAll(castAndCrewList, response.getBody());
		assertEquals(castAndCrewList.get(0).getTitle().getType(), 
				                           "movie", "Type is not as expected");
		
		assertEquals(castAndCrewList.get(0).getTitle().getPrimaryTitle(),
				                      "Brother","PrimaryTitle not as expected");
		
		assertEquals(castAndCrewList.get(0).getTitle().getOriginalTitle(),
				                         "GeGe","OriginalTitle not as expected");
	}

	@Test
	void testTitlesAndRatingsBasedOnGenre() {
		String URL = "http://localhost:" + port + "/imdb/movies/genre/";
		String genreType = "Horror";
		
		//Retrieve titles associated with genre type Horror
		ResponseEntity<TitleAndRatingsDetails[]> response = this.restTemplate.getForEntity
				                                     (URL+genreType, TitleAndRatingsDetails[].class);
		List<TitleAndRatingsDetails> titleAndRatingsList = new ArrayList<TitleAndRatingsDetails>();
		Collections.addAll(titleAndRatingsList, response.getBody());
		titleAndRatingsList.forEach(titleAndRating -> {
			assertTrue(titleAndRating.getGenres().contains(genreType));
		});
		
	}
}

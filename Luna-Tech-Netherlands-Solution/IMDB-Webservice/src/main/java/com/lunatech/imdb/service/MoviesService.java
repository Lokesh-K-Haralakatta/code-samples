package com.lunatech.imdb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lunatech.imdb.dto.CastAndCrewDetails;
import com.lunatech.imdb.dto.TitleAndRatingsDetails;
import com.lunatech.imdb.entity.NameBasicsEntity;
import com.lunatech.imdb.entity.TitleBasicsEntity;
import com.lunatech.imdb.entity.TitlePrincipalsEntity;
import com.lunatech.imdb.pojo.CrewPerson;
import com.lunatech.imdb.pojo.TitleDetails;
import com.lunatech.imdb.repository.NameBasicsRepository;
import com.lunatech.imdb.repository.TitleBasicsRepository;
import com.lunatech.imdb.repository.TitlePrincipalsRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MoviesService implements MoviesInterface{
	@Autowired
	private TitleBasicsRepository titleBasicsRepo;
	
	@Autowired
	private TitlePrincipalsRepository titlePrincipalsRepo;
	
	@Autowired
	private NameBasicsRepository nameBasicsRepo;

	@Override
	public List<CastAndCrewDetails> retrieveCastAndCrewForMoviesWithTitle(String title) {
		List<CastAndCrewDetails> castAndCrewList = new ArrayList<>();
		try {
			List<TitleBasicsEntity> titleBasicsList = titleBasicsRepo.findByTitle(title);
			titleBasicsList.forEach(tb ->{
				//Create instances needed to frame response
				List<CrewPerson> cPersonList = new ArrayList<>();
				TitleDetails tDetails = new TitleDetails();
				CastAndCrewDetails cncDetails = new CastAndCrewDetails();
				
				//Fill in title details
				tDetails.setType(tb.getTitleType());
				tDetails.setPrimaryTitle(tb.getPrimaryTitle());
				tDetails.setOriginalTitle(tb.getOriginalTitle());
				tDetails.setGenres(tb.getGenres());
				
				String titleId = tb.getTconst();
				log.debug("Retrieved Title Id: " +titleId);
				List<TitlePrincipalsEntity> castCrewList = titlePrincipalsRepo.findPrincipalsByTitleId(titleId);
				log.debug("Number of title principals retrieved: " +castCrewList.size());
				castCrewList.forEach(castAndCrew -> {
					String personId = castAndCrew.getNconst();
					log.debug("Name/Person Id: " +personId);
					Optional<NameBasicsEntity> personEntity = nameBasicsRepo.findById(personId);
					if(personEntity.isPresent()) {
						log.debug("Name: " + personEntity.get().getPrimaryName());
						//Fill in Cast and Crew Details
						CrewPerson cPerson = new CrewPerson();
						cPerson.setName(personEntity.get().getPrimaryName());
						cPerson.setRole(castAndCrew.getCategory());
						//Add crew person details to list
						cPersonList.add(cPerson);
					}
				});
				
				//Fill in CastAndCrew Details
				cncDetails.setTitle(tDetails);
				cncDetails.setCastAndCrew(cPersonList);
				
				//Add cncDetails to List
				castAndCrewList.add(cncDetails);
			}); 
			return castAndCrewList;
		} catch(Exception e){
			log.error(ExceptionUtils.getStackTrace(e));
			return null;
		}

	}

	@Override
	public List<TitleAndRatingsDetails> retrieveTitlesAndRatingsBasedOnGenre(String genreType) {
		try {
			//List<TitleAndRatingsDetails> titlesDetails = titleBasicsRepo.findTitlesByGenreAndRating(genreType);
			List<TitleBasicsEntity> titlesDetails = titleBasicsRepo.findTitlesByGenres(genreType);
			log.debug("Number of titles retrieved based on given genre type: " +titlesDetails.size());
			List<TitleAndRatingsDetails> titlesDetailsList = new ArrayList<>();
			titlesDetails.forEach(title ->{
				log.debug("Primary Title: " +title.getPrimaryTitle());
				log.debug("Original Title: " +title.getOriginalTitle());
				log.debug("Genres: " + title.getGenres());
				TitleAndRatingsDetails tnrDetails = new TitleAndRatingsDetails(title.getPrimaryTitle(),
						title.getOriginalTitle(), title.getGenres());
				titlesDetailsList.add(tnrDetails);
			});
			return titlesDetailsList;
		}catch(Exception e){
			log.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}
}

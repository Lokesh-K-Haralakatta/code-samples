package com.lunatech.imdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.lunatech.imdb.entity.TitleCrewEntity;

@EnableJpaRepositories
public interface TitleCrewRepository extends JpaRepository<TitleCrewEntity, String> {
	//Customer Query to retrieve directors and writers for given unique identifier of the title
	@Query(value = "select c.tconst, c.directors, c.writers from title_basics t join "
			       + "title_crew c on (t.tconst=c.tconst) where t.tconst = :titleId", 
			  nativeQuery = true)
	TitleCrewEntity findByTitleId(@Param("titleId") String titleId);
}

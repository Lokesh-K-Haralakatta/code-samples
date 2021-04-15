package com.lunatech.imdb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.lunatech.imdb.dto.TitleAndRatingsDetails;
import com.lunatech.imdb.entity.TitleBasicsEntity;

@EnableJpaRepositories
public interface TitleBasicsRepository extends JpaRepository<TitleBasicsEntity, String> {
	//Custom Query to retrieve movies based on provided primaryTitle or originalTitle
	//@Query("Select t from title_basics t where t.primarytitle = ':title' OR t.originaltitle = ':title' ")
	@Query(value = "SELECT * FROM title_basics t WHERE t.primarytitle = :title OR "
			       + "t.originaltitle = :title ", 
			  nativeQuery = true)
	List<TitleBasicsEntity> findByTitle(@Param("title") String title);
	
	/*@Query("SELECT new com.lunatech.imdb.dto.TitleAndRatingsDetails( "
			+ " b.primarytitle, b.originaltitle, b.genres, r.averagerating, r.numvotes ) "
			+ " FROM title_basics b join title_ratings r on (b.tconst = r.tconst) "
			+ " WHERE position(lower(':genreType') in lower(b.genres)) > 0 "
			+ " ORDER BY r.averagerating DESC")
	List<TitleAndRatingsDetails> findTitlesByGenreAndRating(@Param("genreType") String genreType);*/
	
	@Query(value = "SELECT * FROM title_basics b join title_ratings r on (b.tconst = r.tconst) WHERE "
			+ " position(lower(:genreType) in lower(b.genres)) > 0 AND r.averagerating > 9 "
			+ " ORDER BY r.averagerating DESC", 
		  nativeQuery = true)
	List<TitleBasicsEntity> findTitlesByGenres(String genreType);
}

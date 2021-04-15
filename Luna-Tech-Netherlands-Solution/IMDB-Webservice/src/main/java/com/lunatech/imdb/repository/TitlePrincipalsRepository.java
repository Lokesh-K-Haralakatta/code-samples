package com.lunatech.imdb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.lunatech.imdb.entity.TitlePrincipalsEntity;

@EnableJpaRepositories
public interface TitlePrincipalsRepository extends JpaRepository<TitlePrincipalsEntity, String> {
	//Custom query to retrieve details for given title id
	@Query(value = "SELECT * FROM title_principals p join title_basics b on (p.tconst=b.tconst) "
				   + "WHERE p.tconst = :titleId", 
		  nativeQuery = true)
	List<TitlePrincipalsEntity> findPrincipalsByTitleId(@Param("titleId") String titleId);
}

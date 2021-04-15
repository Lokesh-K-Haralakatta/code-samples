package com.lunatech.imdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.lunatech.imdb.entity.NameBasicsEntity;

@EnableJpaRepositories
public interface NameBasicsRepository extends JpaRepository<NameBasicsEntity, String> {
}

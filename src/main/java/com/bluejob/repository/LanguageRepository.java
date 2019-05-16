package com.dufther.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dufther.domain.Language;

public interface LanguageRepository extends JpaRepository<Language,Long>{

}

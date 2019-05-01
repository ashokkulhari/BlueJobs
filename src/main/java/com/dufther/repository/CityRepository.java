package com.dufther.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dufther.domain.City;

public interface CityRepository extends JpaRepository<City,Long>{

}

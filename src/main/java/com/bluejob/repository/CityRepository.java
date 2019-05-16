package com.bluejob.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluejob.domain.City;

public interface CityRepository extends JpaRepository<City,Long>{

}

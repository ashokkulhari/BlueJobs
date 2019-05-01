package com.dufther.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dufther.domain.ProfileStatus;

public interface ProfileStatusRepository extends JpaRepository<ProfileStatus,String>  {

}

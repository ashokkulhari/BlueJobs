package com.bluejob.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluejob.domain.ProfileStatus;

public interface ProfileStatusRepository extends JpaRepository<ProfileStatus,String>  {

}

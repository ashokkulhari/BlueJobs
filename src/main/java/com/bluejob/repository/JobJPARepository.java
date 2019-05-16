package com.dufther.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dufther.domain.Job;

public interface JobJPARepository extends JpaRepository<Job,Long>{

	Optional<Job> findOneById(Long jobId);

}

package com.dufther.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dufther.domain.Candidate;

public interface CandidateJPARepository extends JpaRepository<Candidate,Long>{

	
	Optional<Candidate> findOneByUserUserId(String userId);
	
	Page<Candidate> findAll(Pageable pageable);
}

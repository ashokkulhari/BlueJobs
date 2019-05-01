package com.dufther.searchrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.dufther.domain.Candidate;

public interface CandidateSearchRepository extends ElasticsearchRepository<Candidate, Long>{

}

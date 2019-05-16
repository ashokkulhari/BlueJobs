package com.bluejob.searchrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bluejob.domain.Candidate;

public interface CandidateSearchRepository extends ElasticsearchRepository<Candidate, Long>{

}

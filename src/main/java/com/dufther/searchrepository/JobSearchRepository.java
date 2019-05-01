package com.dufther.searchrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.dufther.domain.Job;


public interface JobSearchRepository extends ElasticsearchRepository<Job, Long> {

}

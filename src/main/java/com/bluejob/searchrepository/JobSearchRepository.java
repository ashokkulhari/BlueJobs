package com.bluejob.searchrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bluejob.domain.Job;


public interface JobSearchRepository extends ElasticsearchRepository<Job, Long> {

}

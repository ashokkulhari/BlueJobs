package com.bluejob.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluejob.domain.Authority;
import com.bluejob.repository.AuthorityRepository;

@Service
public class AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;
	public Authority findRoleById(Long id) {
		return authorityRepository.findById(id).get();
	}
	
	public List<Authority> findAllRoleById(Set<Long> ids) {
		return authorityRepository.findAllById(ids);
	}
}

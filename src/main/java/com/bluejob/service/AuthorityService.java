package com.dufther.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dufther.domain.Authority;
import com.dufther.repository.AuthorityRepository;

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

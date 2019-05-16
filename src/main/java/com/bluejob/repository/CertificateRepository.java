package com.dufther.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dufther.domain.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate,Long>{

}

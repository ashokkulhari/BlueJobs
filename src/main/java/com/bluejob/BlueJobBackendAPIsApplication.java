package com.dufther;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//(exclude = {ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class})

//@EnableJpaRepositories(basePackages ="com.dufther.repository")
//@EnableElasticsearchRepositories(basePackages = "com.dufther.searchrepository")

public class DuftherBackendAPIsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuftherBackendAPIsApplication.class, args);
	}

}

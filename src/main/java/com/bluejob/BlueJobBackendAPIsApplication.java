package com.bluejob;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//(exclude = {ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class})

//@EnableJpaRepositories(basePackages ="com.bluejob.repository")
//@EnableElasticsearchRepositories(basePackages = "com.bluejob.searchrepository")

public class BlueJobBackendAPIsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlueJobBackendAPIsApplication.class, args);
	}

}

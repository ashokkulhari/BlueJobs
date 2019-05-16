package com.bluejob.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.net.InetAddress;

@Configuration
@EnableJpaRepositories(basePackages = "com.bluejob.repository")
@EnableElasticsearchRepositories(basePackages = "com.bluejob.searchrepository")
public class ElasticConfiguration {


//    @Bean
//    public NodeBuilder nodeBuilder() {
//        return new NodeBuilder();
//    }
//
//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() throws IOException {
//        File tmpDir = File.createTempFile("elastic", Long.toString(System.nanoTime()));
//        System.out.println("Temp directory: " + tmpDir.getAbsolutePath());
////        Settings.Builder elasticsearchSettings =
////                Settings.settingsBuilder()
////                        .put("http.enabled", "true") // 1
////                        .put("index.number_of_shards", "1")
////                        .put("path.data", new File(tmpDir, "data").getAbsolutePath()) // 2
////                        .put("path.logs", new File(tmpDir, "logs").getAbsolutePath()) // 2
////                        .put("path.work", new File(tmpDir, "work").getAbsolutePath()) // 2
////                        .put("path.home", tmpDir); // 3
//
//        Settings.Builder elasticsearchSettings =
//                Settings.builder()
//                        .put("http.enabled", "true") 
//                        .put("index.number_of_shards", "1")
//                        .put("path.data", new File(tmpDir, "data").getAbsolutePath()) 
//                        .put("path.logs", new File(tmpDir, "logs").getAbsolutePath()) 
//                        .put("path.work", new File(tmpDir, "work").getAbsolutePath()) 
//                        .put("path.home", tmpDir);
//
//        return new ElasticsearchTemplate(nodeBuilder()
//                .local(true)
//                .settings(elasticsearchSettings.build())
//                .node()
//                .client());
//    }
    
    @Value("${elasticsearch.host}")
    private String EsHost;

    @Value("${elasticsearch.port}")
    private int EsPort;

    @Value("${elasticsearch.clustername}")
    private String EsClusterName;

    @Bean
    public Client client() throws Exception {

        Settings esSettings = Settings.builder()
                .put("cluster.name", EsClusterName)
                .build();

        //https://www.elastic.co/guide/en/elasticsearch/guide/current/_transport_client_versus_node_client.html
        TransportClient client = new PreBuiltTransportClient(esSettings);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(EsHost), EsPort));
        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }

    //Embedded Elasticsearch Server
    /*@Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(nodeBuilder().local(true).node().client());
    }*/

}

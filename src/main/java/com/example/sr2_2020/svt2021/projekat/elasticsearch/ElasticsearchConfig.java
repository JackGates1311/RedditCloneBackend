package com.example.sr2_2020.svt2021.projekat.elasticsearch;

import com.example.sr2_2020.svt2021.projekat.elasticsearch.repository.CommunitySearchingRepositoryQuery;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.repository.PostSearchingRepositoryQuery;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.sr2_2020.svt2021.projekat")
@ComponentScan(basePackages = { "com.example.sr2_2020.svt2021.projekat.service" })
public class ElasticsearchConfig {
    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private int port;

    @Bean
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchRestTemplate elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }

    @Bean
    public CommunitySearchingRepositoryQuery communitySearchingRepositoryQuery(
            ElasticsearchOperations elasticsearchOperations, ElasticsearchRestTemplate elasticsearchTemplate) {
        return new CommunitySearchingRepositoryQuery(elasticsearchOperations, elasticsearchTemplate);
    }

    @Bean
    public PostSearchingRepositoryQuery postSearchingRepositoryQuery(
            ElasticsearchOperations elasticsearchOperations, ElasticsearchRestTemplate elasticsearchTemplate) {
        return new PostSearchingRepositoryQuery(elasticsearchOperations, elasticsearchTemplate);
    }
}

package com.fsoft.quizsystem.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import reactor.util.annotation.NonNull;


@Configuration
public class ElasticRestClientConfig extends AbstractElasticsearchConfiguration {

    @Value("${spring.data.elasticsearch.elastic-password}")
    private String elasticPassword;

    @NonNull
    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration configuration =
                ClientConfiguration.builder()
                                   .connectedTo("localhost:9200")
                                   .withBasicAuth("elastic", elasticPassword)
                                   .build();

        return RestClients.create(configuration).rest();
    }


}

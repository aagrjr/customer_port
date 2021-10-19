package br.com.amilton.portfolio.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "br.com.amilton.portfolio.repository")
@ComponentScan(basePackages = {"br.com.amilton.portfolio"})
public class ElasticConfig extends AbstractElasticsearchConfiguration {

    private final String hostname;
    private final int port;
    private final String scheme;

    public ElasticConfig(@Value("${elasticsearch.rest.hostname}") String hostname,
            @Value("${elasticsearch.rest.port}") int port,
            @Value("${elasticsearch.rest.scheme}") String scheme) {
        this.hostname = hostname;
        this.port = port;
        this.scheme = scheme;
    }

    @Override
    public RestHighLevelClient elasticsearchClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(hostname, port, scheme)));
    }


}

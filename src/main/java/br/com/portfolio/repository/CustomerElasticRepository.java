package br.com.portfolio.repository;

import br.com.portfolio.domain.CustomerElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerElasticRepository extends ElasticsearchRepository<CustomerElastic, String> {

}

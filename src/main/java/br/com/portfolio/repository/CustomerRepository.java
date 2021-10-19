package br.com.portfolio.repository;

import br.com.portfolio.domain.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, ObjectId> {
    boolean existsByDocumentNumber(String documentNumber);
}

package br.com.amilton.portfolio.repository;

import br.com.amilton.portfolio.domain.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, ObjectId> {
    boolean existsByDocumentNumber(String documentNumber);
}

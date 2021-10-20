package br.com.portfolio.repository;

import br.com.portfolio.domain.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, ObjectId> {

    boolean existsByDocumentNumber(String documentNumber);

    GeoResults<Customer> findByContactNear(Point location, Distance distance);

}

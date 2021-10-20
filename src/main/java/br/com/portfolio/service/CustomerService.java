package br.com.portfolio.service;

import static net.logstash.logback.argument.StructuredArguments.kv;

import br.com.portfolio.domain.Contact;
import br.com.portfolio.domain.Customer;
import br.com.portfolio.domain.payload.CreateCustomerPayload;
import br.com.portfolio.domain.payload.UpdateCustomerPayload;
import br.com.portfolio.domain.response.CustomerResponse;
import br.com.portfolio.domain.search.CustomerSearchParams;
import br.com.portfolio.exception.CustomerAlreadyExistsException;
import br.com.portfolio.exception.CustomerNotFoundException;
import br.com.portfolio.repository.CustomerRepository;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository repository;
    private final GeoLocationService geolocationService;

    public CustomerResponse create(@Valid CreateCustomerPayload payload) {
        log.info("Create customer", kv("CreateCustomerPayload", payload));
        if (repository.existsByDocumentNumber(payload.getDocumentNumber())) {
            throw new CustomerAlreadyExistsException();
        }
        var latLong = geolocationService.getLatLongByAddress(payload.getAddress());
        return new CustomerResponse(repository.save(createModel(payload, latLong)));
    }


    public CustomerResponse update(ObjectId id, @Valid UpdateCustomerPayload payload) {
        log.info("Update customer{} {}", kv("UpdateCustomerPayload", payload), kv("Id", id));

        var latLong = geolocationService.getLatLongByAddress(payload.getAddress());
        return repository.findById(id).map(customer -> {
                            return repository.save(updateModel(payload, customer, latLong));
                        }
                ).map(CustomerResponse::new)
                .orElseThrow(CustomerNotFoundException::new);
    }

    public CustomerResponse findById(ObjectId id) {
        return repository.findById(id)
                .map(CustomerResponse::new)
                .orElseThrow(CustomerNotFoundException::new);
    }

    public void delete(ObjectId id) {
        log.info("Delete customer", kv("Id", id));

        final var customer = repository.findById(id).orElseThrow(CustomerNotFoundException::new);

        repository.delete(customer);
    }

    public Page<CustomerResponse> findAll(Pageable pageable, CustomerSearchParams search) {
        return repository.findAll(example(search), pageable).map(CustomerResponse::new);
    }

    private Customer createModel(CreateCustomerPayload payload, List<Double> latLong) {
        return Customer.builder()
                .name(payload.getName())
                .gender(payload.getGender())
                .birthDate(payload.getBirthDate())
                .documentNumber(payload.getDocumentNumber())
                .nickname(payload.getNickname())
                .email(payload.getEmail())
                .contact(Contact.builder().coordinates(latLong).address(payload.getAddress()).build())
                .build();
    }

    private Customer updateModel(UpdateCustomerPayload payload, Customer model, List<Double> latLong) {
        model.setName(payload.getName());
        model.setGender(payload.getGender());
        model.setNickname(payload.getNickname());
        model.setEmail(payload.getEmail());
        model.setContact(Contact.builder().coordinates(latLong).address(payload.getAddress()).build());
        return model;
    }

    private Customer filters(final CustomerSearchParams search) {
        return Customer.builder().name(search.getName()).documentNumber(search.getDocumentNumber())
                .build();
    }

    private Example<Customer> example(final CustomerSearchParams search) {
        return Example.of(filters(search));
    }


}

package br.com.amilton.portfolio.service;

import static net.logstash.logback.argument.StructuredArguments.kv;

import br.com.amilton.portfolio.domain.Customer;
import br.com.amilton.portfolio.domain.payload.CreateCustomerPayload;
import br.com.amilton.portfolio.domain.payload.UpdateCustomerPayload;
import br.com.amilton.portfolio.domain.response.CustomerResponse;
import br.com.amilton.portfolio.domain.search.CustomerSearchParams;
import br.com.amilton.portfolio.exception.CustomerAlreadyExistsException;
import br.com.amilton.portfolio.exception.CustomerNotFoundException;
import br.com.amilton.portfolio.repository.CustomerRepository;
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

    public CustomerResponse create(@Valid CreateCustomerPayload payload) {
        log.info("Create customer", kv("CreateCustomerPayload", payload));

        if (repository.existsByDocumentNumber(payload.getDocumentNumber())) {
            throw new CustomerAlreadyExistsException();
        }
        return new CustomerResponse(repository.save(createModel(payload)));
    }


    public CustomerResponse update(ObjectId id, @Valid UpdateCustomerPayload payload) {
        log.info("Update price", kv("UpdatePricePayload", payload), kv("Id", id));
        return repository.findById(id).map(customer -> {
                            return repository.save(updateModel(payload, customer));
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

        repository.delete(customer);;
    }

    public Page<CustomerResponse> findAll(Pageable pageable, CustomerSearchParams search) {
        return repository.findAll(example(search), pageable).map(CustomerResponse::new);
    }

    private Customer createModel(CreateCustomerPayload payload) {
        return Customer.builder()
                .name(payload.getName())
                .gender(payload.getGender())
                .birthDate(payload.getBirthDate())
                .documentNumber(payload.getDocumentNumber())
                .nickname(payload.getNickname())
                .email(payload.getEmail())
                .build();
    }
    private Customer updateModel(UpdateCustomerPayload payload, Customer model) {
        model.setName(payload.getName());
        model.setGender(payload.getGender());
        model.setNickname(payload.getNickname());
        model.setEmail(payload.getEmail());
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

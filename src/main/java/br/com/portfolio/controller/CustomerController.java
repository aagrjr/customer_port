package br.com.portfolio.controller;

import static org.springframework.http.HttpStatus.OK;

import br.com.portfolio.api.CustomerApi;
import br.com.portfolio.domain.payload.CreateCustomerPayload;
import br.com.portfolio.domain.payload.UpdateCustomerPayload;
import br.com.portfolio.domain.response.CustomerResponse;
import br.com.portfolio.domain.search.CustomerSearchParams;
import br.com.portfolio.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController implements CustomerApi {

    private final CustomerService service;

    @Override
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CustomerResponse create(@RequestBody CreateCustomerPayload payload) {
        return service.create(payload);
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public CustomerResponse update(@PathVariable ObjectId id, @RequestBody UpdateCustomerPayload payload) {
        return service.update(id, payload);
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CustomerResponse findById(@PathVariable ObjectId id) {
        return service.findById(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void delete(@PathVariable ObjectId id) {
        service.delete(id);
    }

    @Override
    @ResponseStatus(code = OK)
    @GetMapping
    public Page<CustomerResponse> findAll(Pageable pageable, CustomerSearchParams search) {
        return service.findAll(pageable, search);
    }

}

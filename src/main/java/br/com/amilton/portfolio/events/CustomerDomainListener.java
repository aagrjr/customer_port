package br.com.amilton.portfolio.events;

import br.com.amilton.portfolio.domain.Customer;
import br.com.amilton.portfolio.domain.CustomerElastic;
import br.com.amilton.portfolio.repository.CustomerElasticRepository;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerDomainListener extends AbstractMongoEventListener<Customer> {

    private final CustomerElasticRepository repository;

    @Override
    public void onAfterSave(AfterSaveEvent<Customer> event) {
        log.info("Finding item on elastic");
        final var customerElastic = repository.findById(event.getSource().getId().toHexString());

        repository.save(buildCustomerElastic(event.getSource()));
    }

    private CustomerElastic buildCustomerElastic(Customer customer) {
        return CustomerElastic.builder()
                .id(customer.getId().toHexString())
                .name(customer.getName())
                .gender(customer.getGender())
                .birthDate(customer.getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .nickname(customer.getNickname())
                .email(customer.getEmail())
                .documentNumber(customer.getDocumentNumber())
                .build();
    }
}

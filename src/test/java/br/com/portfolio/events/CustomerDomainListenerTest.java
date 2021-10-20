package br.com.portfolio.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.portfolio.domain.Customer;
import br.com.portfolio.domain.CustomerElastic;
import br.com.portfolio.helper.MockGenerator;
import br.com.portfolio.repository.CustomerElasticRepository;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerDomainListenerTest {

    @RegisterExtension
    static MockGenerator mockGenerator = MockGenerator.instance();
    @Mock
    CustomerElasticRepository repository;
    @InjectMocks
    private CustomerDomainListener listener;
    private Customer customer;

    private CustomerElastic customerElastic;

    @BeforeEach
    public void beforeEach() throws Exception {

        customer = mockGenerator.generateFromJson("customer").as(Customer.class);
        customerElastic = buildCustomerElastic(customer);
        reset(repository);
    }

    @Test
    void onAfterSaveSuccess() {
        when(repository.save(customerElastic)).thenReturn(customerElastic);

        AfterSaveEvent<Customer> event = new AfterSaveEvent<Customer>(customer, null, null);
        listener.onAfterSave(event);
        final var captor = ArgumentCaptor.forClass(CustomerElastic.class);

        verify(repository).save(captor.capture());
        assertResult(captor.getValue());
        assertNotNull(event);
    }

    public void assertResult(CustomerElastic result) {
        assertNotNull(result);
        assertEquals(result.getId(), customer.getId().toHexString());
        assertEquals(result.getName(), customer.getName());
        assertEquals(result.getGender(), customer.getGender());
        assertEquals(result.getBirthDate(), customer.getBirthDate().toString());
        assertEquals(result.getNickname(), customer.getNickname());
        assertEquals(result.getEmail(), customer.getEmail());
        assertEquals(result.getDocumentNumber(), customer.getDocumentNumber());
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
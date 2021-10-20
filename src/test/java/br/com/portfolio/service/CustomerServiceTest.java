package br.com.portfolio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.portfolio.domain.Customer;
import br.com.portfolio.domain.payload.CreateCustomerPayload;
import br.com.portfolio.domain.payload.UpdateCustomerPayload;
import br.com.portfolio.domain.response.CustomerDistanceResponse;
import br.com.portfolio.domain.response.CustomerResponse;
import br.com.portfolio.domain.search.CustomerSearchParams;
import br.com.portfolio.exception.CustomerAlreadyExistsException;
import br.com.portfolio.exception.CustomerNotFoundException;
import br.com.portfolio.helper.MockGenerator;
import br.com.portfolio.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceTest {

    @RegisterExtension
    static MockGenerator mockGenerator = MockGenerator.instance();
    private final Distance distance = new Distance(2.5, Metrics.KILOMETERS);
    private final ObjectId id = new ObjectId("61701da494bdec3eec35d8ff");
    @Mock
    CustomerRepository repository;
    @Mock
    GeoLocationService geoLocationService;
    @InjectMocks
    private CustomerService service;
    private CreateCustomerPayload createCustomerPayload;
    private UpdateCustomerPayload updateCustomerPayload;
    private Customer customer;

    @BeforeEach
    public void beforeEach() {
        createCustomerPayload = mockGenerator.generateFromJson("createPayload").as(CreateCustomerPayload.class);
        updateCustomerPayload = mockGenerator.generateFromJson("updatePayload").as(UpdateCustomerPayload.class);

        customer = mockGenerator.generateFromJson("customer").as(Customer.class);

        reset(repository, geoLocationService);
    }

    @Test
    void createWithSuccess() {
        when(repository.existsByDocumentNumber(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(customer);

        assertResult(service.create(createCustomerPayload));

        verify(repository).existsByDocumentNumber(any());
        verify(repository).save(any());
    }

    @Test
    void createWithCustomerAlreadyExistsException() {
        when(repository.existsByDocumentNumber(any())).thenReturn(true);

        assertThrows(CustomerAlreadyExistsException.class, () -> service.create(createCustomerPayload));

        verify(repository).existsByDocumentNumber(any());
        verify(repository, never()).save(any());
    }

    @Test
    void updateWithSuccess() {
        when(repository.findById(id)).thenReturn(Optional.of(customer));
        when(repository.save(any())).thenReturn(customer);

        assertResult(service.update(id, updateCustomerPayload));
        verify(repository).findById(id);
        verify(repository).save(any());
    }

    @Test
    void updateWithCustomerNotFoundException() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> service.update(id, updateCustomerPayload));

        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void findByIdWithSuccess() {
        when(repository.findById(id)).thenReturn(Optional.of(customer));

        assertResult(service.findById(id));

        verify(repository).findById(id);
    }

    @Test
    void FindByIdWithCustomerNotFoundException() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> service.findById(id));

        verify(repository).findById(id);
    }

    @Test
    void deleteWithSuccess() {
        when(repository.findById(id)).thenReturn(Optional.of(customer));

        final var captor = ArgumentCaptor.forClass(Customer.class);

        service.delete(id);

        verify(repository).findById(id);
        verify(repository).delete(captor.capture());

        assertEquals(captor.getValue().getId(), id);
    }

    @Test
    void deleteWithCustomerNotFoundException() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> service.delete(id));

        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void findAllWithSuccess() {
        Pageable page = PageRequest.of(0, 1);
        CustomerSearchParams params = new CustomerSearchParams();
        params.setName(customer.getName());

        Page<Customer> customerPage = new PageImpl<>(List.of(customer), page,
                Integer.MAX_VALUE);

        when(repository.findAll(any(), any(Pageable.class))).thenReturn(customerPage);
        var response = service.findAll(page, params);

        assertNotNull(response);
        assertResult(response.getContent().get(0));
        assertEquals(response.getContent().size(), page.getPageSize());
    }

    @Test
    void findByLocationNear() {
        GeoResult<Customer> result = new GeoResult<>(customer, distance);
        when(repository.findById(id)).thenReturn(Optional.of(customer));
        when(repository.findByContactNear(any(), any())).thenReturn(new GeoResults<>(List.of(result)));

        assertResult(service.findByLocationNear(200, id));

        verify(repository).findById(id);
        verify(repository).findByContactNear(any(), any());
    }

    private void assertResult(List<CustomerDistanceResponse> result) {
        assertNotNull(result);
        var response = result.get(0);
        assertEquals(response.getId(), customer.getId().toHexString());
        assertEquals(response.getName(), customer.getName());
        assertEquals(response.getGender(), customer.getGender());
        assertEquals(response.getBirthDate(), customer.getBirthDate());
        assertEquals(response.getNickname(), customer.getNickname());
        assertEquals(response.getEmail(), customer.getEmail());
        assertEquals(response.getDocumentNumber(), customer.getDocumentNumber());
        assertEquals("2.5", response.getDistance());

    }

    public void assertResult(CustomerResponse result) {
        assertNotNull(result);
        assertEquals(result.getId(), id.toHexString());
        assertEquals(result.getName(), customer.getName());
        assertEquals(result.getGender(), customer.getGender());
        assertEquals(result.getBirthDate(), customer.getBirthDate());
        assertEquals(result.getNickname(), customer.getNickname());
        assertEquals(result.getEmail(), customer.getEmail());
        assertEquals(result.getDocumentNumber(), customer.getDocumentNumber());
    }
}
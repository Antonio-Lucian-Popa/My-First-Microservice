package com.asusoftware.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRegistrationRequest.firstName())
                .lastName(customerRegistrationRequest.lastName())
                .email(customerRegistrationRequest.email())
                .build();
        // TODO: Check if email valid
        // TODO: Check if email not taken
        // TODO: Store customer in db
        customerRepository.saveAndFlush(customer);
        // TODO: Check if fraudster
        // Va a chiamare l'altro microservice via Http, e va a passare il tipo di risposta che
        // si aspetta e passa l'id del customer
        FraudCheckResponse fraudCheckReponse = restTemplate.getForObject(
                "http://localhost:8081/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );
        if(fraudCheckReponse.isFraudster()) {
            throw new IllegalStateException("Fraudsteer");
        }
        // TODO: Send Notification
    }
}

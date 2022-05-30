package uy.lstabile.paymentchain.customerapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uy.lstabile.paymentchain.customerapi.entities.Customer;
import uy.lstabile.paymentchain.customerapi.exception.CustomerNotFoundException;
import uy.lstabile.paymentchain.customerapi.repository.CustomerRepository;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping()
    public ResponseEntity<List<Customer>> list() {
        List<Customer> customerList = customerRepository.findAll();

        return new ResponseEntity<List<Customer>>(customerList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> get(@PathVariable("id") long id) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Customer> save(@RequestBody Customer input) {
        Customer customer = customerRepository.save(input);

        return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer input)
            throws CustomerNotFoundException {
        Customer editedCustomer = customerRepository.findById(id)
                .map(customer -> {
                    customer.setName(input.getName());
                    customer.setPhone(input.getPhone());

                    return customer;
                })
                .orElseThrow(
                        () -> new CustomerNotFoundException(id));

        editedCustomer = customerRepository.save(editedCustomer);

        return new ResponseEntity<Customer>(editedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteEmployee(@PathVariable Long id)
            throws CustomerNotFoundException {

        try {
            customerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new CustomerNotFoundException(id);
        }

        return ResponseEntity.noContent().build();
    }

}

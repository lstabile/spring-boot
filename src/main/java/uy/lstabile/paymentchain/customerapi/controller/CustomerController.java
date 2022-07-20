package uy.lstabile.paymentchain.customerapi.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
import uy.lstabile.paymentchain.customerapi.api.model.CustomerModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerModelAssembler assembler;

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<Customer>>> list() {
        List<EntityModel<Customer>> customerList = customerRepository.findAll().stream()
                .map(assembler::toModel).collect(Collectors.toList());

        CollectionModel<EntityModel<Customer>> collectionModel = CollectionModel.of(customerList,
                linkTo(methodOn(CustomerController.class).list()).withSelfRel());

        return new ResponseEntity<CollectionModel<EntityModel<Customer>>>(collectionModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<Customer>> get(@PathVariable("id") long id) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        EntityModel<Customer> entityModel = assembler.toModel(customer);

        return new ResponseEntity<EntityModel<Customer>>(entityModel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Customer>> save(@RequestBody Customer input) {
        Customer customer = customerRepository.save(input);

        EntityModel<Customer> entityModel = assembler.toModel(customer);

        return new ResponseEntity<EntityModel<Customer>>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EntityModel<Customer>> update(@PathVariable Long id, @RequestBody Customer input)
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

        EntityModel<Customer> entityModel = assembler.toModel(editedCustomer);

        return new ResponseEntity<EntityModel<Customer>>(entityModel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<Customer>> deleteEmployee(@PathVariable Long id)
            throws CustomerNotFoundException {

        try {
            customerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new CustomerNotFoundException(id);
        }

        return ResponseEntity.noContent().build();
    }

}

package uy.lstabile.paymentchain.customerapi.api.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import uy.lstabile.paymentchain.customerapi.controller.CustomerController;
import uy.lstabile.paymentchain.customerapi.entities.Customer;
import uy.lstabile.paymentchain.customerapi.exception.CustomerNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<Customer, EntityModel<Customer>> {

    @Override
    public EntityModel<Customer> toModel(Customer customer) {

        ResponseEntity<EntityModel<Customer>> selfLink = null;
        try {
            selfLink = methodOn(CustomerController.class).get(customer.getId());
        } catch (CustomerNotFoundException e) {
            e.printStackTrace();

            selfLink = null;
        }

        EntityModel<Customer> entityModel = null;

        if (selfLink != null) {
            entityModel = EntityModel.of(customer,
                    linkTo(selfLink).withSelfRel(),
                    linkTo(methodOn(CustomerController.class).list()).withRel("customers"));
        } else {
            entityModel = EntityModel.of(customer,
                    linkTo(methodOn(CustomerController.class).list()).withRel("customers"));
        }

        return entityModel;
    }
}

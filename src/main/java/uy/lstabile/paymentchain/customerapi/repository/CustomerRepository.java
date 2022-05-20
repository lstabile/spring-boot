package uy.lstabile.paymentchain.customerapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uy.lstabile.paymentchain.customerapi.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByName(String name);
}

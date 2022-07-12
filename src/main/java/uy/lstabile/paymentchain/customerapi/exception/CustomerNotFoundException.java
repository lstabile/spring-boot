package uy.lstabile.paymentchain.customerapi.exception;

public class CustomerNotFoundException extends Exception {

    public CustomerNotFoundException(Long id) {
        super("Could not find customer " + id);
    }
}

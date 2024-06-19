package com.payroll.exceptions;



public class TaxServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TaxServiceException(String message) {
        super(message);
    }

}
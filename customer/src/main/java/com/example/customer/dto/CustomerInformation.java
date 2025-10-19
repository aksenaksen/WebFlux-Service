package com.example.customer.dto;

import com.example.customer.domain.Customer;

import java.util.List;

public record CustomerInformation(
        Integer id,
        String name,
        Integer balance,
        List<Holding> holding
) {
    public static CustomerInformation of(Customer customer, List<Holding> holding){
        return new CustomerInformation(customer.getId(), customer.getName(), customer.getBalance(), holding);
    }
}

package com.example.customer.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public class Customer {

    @Id
    private Integer id;
    private Integer balance;
    private String name;

    public boolean canBuy(Integer price){
        return this.balance >= price;
    }

    public void decreaseBalance(Integer price){
        this.balance -= price;
    }
}

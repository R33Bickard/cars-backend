package com.example.dto;

public class CarRequest {

    public String name;
    public String description;
    
    public CarRequest() {
    }
    
    public CarRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
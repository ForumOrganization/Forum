package com.example.forum.models.dtos;

public class PhoneNumberDto {

    private String phoneNumber;

    public PhoneNumberDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneNumberDto() {
        
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
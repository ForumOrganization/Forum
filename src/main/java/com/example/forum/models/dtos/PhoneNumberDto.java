package com.example.forum.models.dtos;

import jakarta.validation.constraints.Pattern;

public class PhoneNumberDto {


    @Pattern(regexp = "^\\+[0-9-()\\s]{9,}+$",
            message = "Invalid phone number format")
    private String phoneNumber;

    public PhoneNumberDto() {

    }

    public PhoneNumberDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
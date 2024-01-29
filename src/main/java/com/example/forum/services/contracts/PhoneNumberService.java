package com.example.forum.services.contracts;

import com.example.forum.models.PhoneNumber;
import com.example.forum.models.User;

import java.util.Optional;

public interface PhoneNumberService {

    PhoneNumber getPhoneNumberByUserId(int userId);

    void addPhoneNumberToAdmin(User admin, PhoneNumber phoneNumber);

    void updatePhoneNumber(PhoneNumber phoneNumber);

    void deletePhoneNumber(int userId);
}
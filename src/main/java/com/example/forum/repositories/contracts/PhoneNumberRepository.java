package com.example.forum.repositories.contracts;

import com.example.forum.models.PhoneNumber;
import com.example.forum.models.User;

import java.util.List;

public interface PhoneNumberRepository {

    PhoneNumber getPhoneNumberByUserId(int userId);

    void addPhoneNumberToAdmin(User admin, PhoneNumber phoneNumber);


    void updatePhoneNumber(PhoneNumber phoneNumber);


    void deletePhoneNumber(int userId);
}
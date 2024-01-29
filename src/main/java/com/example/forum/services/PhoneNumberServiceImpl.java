package com.example.forum.services;

import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.PhoneNumber;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.PhoneNumberRepository;
import com.example.forum.services.contracts.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.forum.utils.CheckPermission.checkAccessPermissionsAdmin;
import static com.example.forum.utils.Messages.UPDATE_PHONENUMBER_ERROR_MESSAGE;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final PhoneNumberRepository phoneNumberRepository;

    @Autowired
    public PhoneNumberServiceImpl(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @Override
    public PhoneNumber getPhoneNumberByUserId(int userId) {
        return phoneNumberRepository.getPhoneNumberByUserId(userId);
    }

    @Override
    public void addPhoneNumberToAdmin(User admin, PhoneNumber phoneNumber) {
        checkAccessPermissionsAdmin(admin, UPDATE_PHONENUMBER_ERROR_MESSAGE);

        PhoneNumber phoneNumberAdmin = phoneNumberRepository.getPhoneNumberByUserId(admin.getId());

        if (phoneNumber != null && !phoneNumber.getPhoneNumber().isEmpty()) {
            if (phoneNumber.getPhoneNumber().equals(phoneNumberAdmin.getPhoneNumber())) {
                throw new DuplicateEntityException("Admin", "phone number", phoneNumber.getPhoneNumber());
            }

            phoneNumberAdmin.setPhoneNumber(phoneNumber.getPhoneNumber());

            phoneNumberRepository.addPhoneNumberToAdmin(admin, phoneNumberAdmin);
        } else {
            throw new EntityNotFoundException("Admin", "phone number");
        }
    }

    @Override
    public void updatePhoneNumber(PhoneNumber phoneNumber) {
        phoneNumberRepository.updatePhoneNumber(phoneNumber);
    }

    @Override
    public void deletePhoneNumber(int userId) {
        phoneNumberRepository.deletePhoneNumber(userId);
    }
}
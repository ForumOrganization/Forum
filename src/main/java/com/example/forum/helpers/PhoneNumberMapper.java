package com.example.forum.helpers;

import com.example.forum.models.PhoneNumber;
import com.example.forum.models.User;
import com.example.forum.models.dtos.PhoneNumberDto;
import com.example.forum.models.dtos.UserDto;
import com.example.forum.services.contracts.PhoneNumberService;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberMapper {

    private final PhoneNumberService phoneNumberService;

    public PhoneNumberMapper(PhoneNumberService phoneNumberService) {
        this.phoneNumberService = phoneNumberService;
    }

    public PhoneNumber fromDto(int id, PhoneNumberDto dto) {
        PhoneNumber phoneNumber = fromDto(dto);
        phoneNumber.setId(id);
        PhoneNumber phoneNumberRepository = phoneNumberService.getPhoneNumberByUserId(id);
        phoneNumber.setUser(phoneNumberRepository.getUser());

        return phoneNumber;
    }

    public PhoneNumber fromDto(PhoneNumberDto dto) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber(dto.getPhoneNumber());

        return phoneNumber;
    }
}
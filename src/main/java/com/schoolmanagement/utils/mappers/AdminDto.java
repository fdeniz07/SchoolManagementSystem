package com.schoolmanagement.utils.mappers;

import com.schoolmanagement.entity.concretes.Admin;
import com.schoolmanagement.payload.request.AdminRequest;
import com.schoolmanagement.payload.response.AdminResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component //DI icin eklendi
@RequiredArgsConstructor
public class AdminDto {

    public Admin createAdminForSave(AdminRequest request) { //DTO --> POJO

        return Admin.builder()
                .username(request.getUsername())
                .name(request.getName())
                .surname(request.getSurname())
                .password(request.getPassword())
                .ssn(request.getSsn())
                .birthDay(request.getBirthDay())
                .birthPlace(request.getBirthPlace())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .build();
    }

    public AdminResponse createResponse(Admin admin) { //POJO --> DTO

        return AdminResponse.builder()
                .userId(admin.getId())
                .username(admin.getUsername())
                .name(admin.getName())
                .surname(admin.getSurname())
                .phoneNumber(admin.getPhoneNumber())
                .birthDay(admin.getBirthDay())
                .birthPlace(admin.getBirthPlace())
                .gender(admin.getGender())
                .ssn(admin.getSsn())
                .build();
    }
}

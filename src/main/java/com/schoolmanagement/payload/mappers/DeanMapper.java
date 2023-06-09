package com.schoolmanagement.payload.mappers;

import com.schoolmanagement.entity.concretes.Dean;
import com.schoolmanagement.entity.concretes.UserRole;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.payload.request.DeanRequest;
import com.schoolmanagement.payload.response.DeanResponse;
import lombok.Data;

@Data
//@Component //annotatoion olmadan atama yapmak istersek asagidaki gibi yazariz.
public class DeanMapper {

    //Dto - POJO
    public Dean dtoDean(DeanRequest deanRequest){ //builder annotation olmadan uzunca asagidaki gibi yazmak zorunda kaliriz
        return Dean.builder()
                .username(deanRequest.getUsername())
                .name(deanRequest.getName())
                .surname(deanRequest.getSurname())
                .password(deanRequest.getPassword())
                .ssn(deanRequest.getSsn())
                .birthDay(deanRequest.getBirthDay())
                .birthPlace(deanRequest.getBirthPlace())
                .phoneNumber(deanRequest.getPhoneNumber())
                .gender(deanRequest.getGender())
                .build();
    }

    //POJO --> DTO Dönüsümü
    public DeanResponse createDeanResponse(Dean dean) {

        return DeanResponse.builder()  //injection yapmadan / builder ile (2.Yol - uzun)
                .userId(dean.getId())
                .username(dean.getUsername())
                .name(dean.getName())
                .surname(dean.getSurname())
                .birthDay(dean.getBirthDay())
                .birthPlace(dean.getBirthPlace())
                .phoneNumber(dean.getPhoneNumber())
                .gender(dean.getGender())
                .ssn(dean.getSsn())
                .build();
    }

    //DTO --> POJO Dönüsümü
    public Dean createUpdatedDean(DeanRequest deanRequest, Long managerId, UserRole userRole) {

        return Dean.builder()
                .id(managerId)
                .username(deanRequest.getUsername())
                .ssn(deanRequest.getSsn())
                .name(deanRequest.getName())
                .surname(deanRequest.getSurname())
                .birthDay(deanRequest.getBirthDay())
                .birthPlace(deanRequest.getBirthPlace())
                .phoneNumber(deanRequest.getPhoneNumber())
                .gender(deanRequest.getGender())
                .userRole(userRole)  //.userRole(userRoleService.getUserRole(RoleType.MANAGER))
                .build();
    }

    //DTO --> POJO Dönüsümü
    public Dean createDtoForDean(DeanRequest deanRequest) { //createDtoToPOJO

        return dtoDean(deanRequest); //Injection yaparak (1.Yol - kisa) --> Tüm bean ler bir klasörde toplanmali(config-->CreateObjectBean)
    }
}

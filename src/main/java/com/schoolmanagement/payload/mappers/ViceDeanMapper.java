package com.schoolmanagement.payload.mappers;

import com.schoolmanagement.entity.concretes.ViceDean;
import com.schoolmanagement.payload.request.ViceDeanRequest;
import com.schoolmanagement.payload.response.ViceDeanResponse;
import lombok.Data;

@Data
//@Component
public class ViceDeanMapper {

    //DTO --> POJO Dönüsümü
    public ViceDean dtoViceBean(ViceDeanRequest viceDeanRequest){
        return ViceDean.builder()
                .birthDay(viceDeanRequest.getBirthDay())
                .username(viceDeanRequest.getUsername())
                .name(viceDeanRequest.getName())
                .surname(viceDeanRequest.getSurname())
                .ssn(viceDeanRequest.getSsn())
                .birthPlace(viceDeanRequest.getBirthPlace())
                .phoneNumber(viceDeanRequest.getPhoneNumber())
                .password(viceDeanRequest.getPassword())
                .gender(viceDeanRequest.getGender())
                .build();
    }

    //DTO --> POJO
    public ViceDean createUpdatedViceDean(ViceDeanRequest viceDeanRequest, Long managerId) {
        return ViceDean.builder()
                .id(managerId)
                .username(viceDeanRequest.getUsername())
                .ssn(viceDeanRequest.getSsn())
                .name(viceDeanRequest.getName())
                .surname(viceDeanRequest.getSurname())
                .birthPlace(viceDeanRequest.getBirthPlace())
                .birthDay(viceDeanRequest.getBirthDay())
                .phoneNumber(viceDeanRequest.getPhoneNumber())
                .gender(viceDeanRequest.getGender())
                .build();
    }

    //DTO --> POJO
    public ViceDean createPojoFromDTO(ViceDeanRequest viceDeanRequest) {
        return dtoViceBean(viceDeanRequest);
    }

    //POJO --> DTO
    public ViceDeanResponse createViceDeanResponse(ViceDean viceDean) {
        return ViceDeanResponse.builder()
                .userId(viceDean.getId())
                .username(viceDean.getUsername())
                .name(viceDean.getName())
                .surname(viceDean.getSurname())
                .ssn(viceDean.getSsn())
                .birthDay(viceDean.getBirthDay())
                .birthPlace(viceDean.getBirthPlace())
                .gender(viceDean.getGender())
                .phoneNumber(viceDean.getPhoneNumber())
                .build();
    }
}

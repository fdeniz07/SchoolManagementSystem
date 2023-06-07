package com.schoolmanagement.payload.mappers;

import com.schoolmanagement.entity.concretes.EducationTerm;
import com.schoolmanagement.payload.request.EducationTermRequest;
import com.schoolmanagement.payload.response.EducationTermResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//@Component //-->Biz IOC container'i bean lerle biz yönetecegiz. CreateObjectBean'e bu classi ekliyoruz
@Data
@RequiredArgsConstructor
public class EducationTermMapper {

    // Dto to Pojo
    public EducationTerm createEducationTerm(EducationTermRequest request) {

        return EducationTerm.builder()
                .term(request.getTerm())
                .startDate(request.getStartDate())
                .endDate(request.getLastRegistrationDate())
                .lastRegistrationDate(request.getLastRegistrationDate())
                .build();
    }

    // Pojo to Dto
    public EducationTermResponse createEducationTermResponse(EducationTerm response) {
        return EducationTermResponse.builder()
                .id(response.getId())
                .term(response.getTerm())
                .startDate(response.getStartDate())
                .endDate(response.getEndDate())
                .lastRegistrationDate(response.getLastRegistrationDate())
                .build();
    }

    //DTO --> POJO
    public EducationTerm createUpdatedEducationTerm(EducationTermRequest request, Long id) {
        return EducationTerm.builder()
                .id(id)
                .term(request.getTerm())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .lastRegistrationDate(request.getLastRegistrationDate())
                .build();
    }
}

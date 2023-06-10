package com.schoolmanagement.payload.mappers;

import com.schoolmanagement.entity.concretes.AdvisorTeacher;
import com.schoolmanagement.payload.response.AdvisorTeacherResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AdvisorTeacherMapper {

    // POJO --> DTO
    public AdvisorTeacherResponse createResponseObject(AdvisorTeacher advisorTeacher){
        return AdvisorTeacherResponse.builder()
                .advisorTeacherId(advisorTeacher.getId())
                .teacherName(advisorTeacher.getTeacher().getName())
                .teacherSurname(advisorTeacher.getTeacher().getSurname())
                .teacherSsn(advisorTeacher.getTeacher().getSsn())
                .build();
    }
}

package com.schoolmanagement.payload.mappers;

import com.schoolmanagement.entity.concretes.AdvisorTeacher;
import com.schoolmanagement.entity.concretes.Student;
import com.schoolmanagement.entity.concretes.UserRole;
import com.schoolmanagement.payload.request.StudentRequest;
import com.schoolmanagement.payload.response.StudentResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;

//@Component
@Data
@RequiredArgsConstructor
public class StudentMapper {

    //!!! DTO - POJO dönüsümü
    public Student studentRequestToDto(StudentRequest studentRequest) {
        return Student.builder()
                .fatherName(studentRequest.getFatherName())
                .motherName(studentRequest.getMotherName())
                .birthDay(studentRequest.getBirthDay())
                .birthPlace(studentRequest.getBirthPlace())
                .name(studentRequest.getName())
                .surname(studentRequest.getSurname())
                .password(studentRequest.getPassword())
                .username(studentRequest.getUsername())
                .ssn(studentRequest.getSsn())
                .email(studentRequest.getEmail())
                .phoneNumber(studentRequest.getPhoneNumber())
                .gender(studentRequest.getGender())
                .build();
    }

    //!!! DTO --> POJO dönüsümü
    public Student createUpdatedStudent(StudentRequest studentRequest, Long userId, UserRole userRole, AdvisorTeacher advisorTeacher){
        return Student.builder()
                .id(userId)
                .username(studentRequest.getUsername())
                .name(studentRequest.getName())
                .surname(studentRequest.getSurname())
                .birthDay(studentRequest.getBirthDay())
                .birthPlace(studentRequest.getBirthPlace())
                .gender(studentRequest.getGender())
                .phoneNumber(studentRequest.getPhoneNumber())
                .ssn(studentRequest.getSsn())
                .email(studentRequest.getEmail())
                .fatherName(studentRequest.getFatherName())
                .motherName(studentRequest.getMotherName())
                .build();
    }

}









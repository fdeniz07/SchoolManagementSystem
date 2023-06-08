package com.schoolmanagement.payload.mappers;

import com.schoolmanagement.entity.concretes.Teacher;
import com.schoolmanagement.payload.request.TeacherRequest;
import com.schoolmanagement.payload.response.TeacherResponse;
import lombok.Data;

@Data
public class TeacherMapper {

   //DTO --> POJO dönüsümü
    public Teacher dtoTeacher(TeacherRequest teacherRequest){

        return Teacher.builder()
                .name(teacherRequest.getName())
                .surname(teacherRequest.getSurname())
                .ssn(teacherRequest.getSsn())
                .username(teacherRequest.getUsername())
                .birthDay(teacherRequest.getBirthDay())
                .birthPlace(teacherRequest.getBirthPlace())
                .password(teacherRequest.getPassword())
                .phoneNumber(teacherRequest.getPhoneNumber())
                .email(teacherRequest.getEmail())
                .isAdvisor(teacherRequest.getIsAdvisorTeacher())
                .gender(teacherRequest.getGender())
                .build();
    }

    //POJO-->DTO dönüsümü
    public TeacherResponse createTeacherResponse(Teacher teacher){
        return TeacherResponse.builder()
                .userId(teacher.getId())
                .username(teacher.getUsername())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .birthDay(teacher.getBirthDay())
                .birthPlace(teacher.getBirthPlace())
                .ssn(teacher.getSsn())
                .phoneNumber(teacher.getPhoneNumber())
                .gender(teacher.getGender())
                .email(teacher.getEmail())
                .build();
    }
}














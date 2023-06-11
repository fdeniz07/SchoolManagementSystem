package com.schoolmanagement.payload.mappers;

import com.schoolmanagement.entity.concretes.Teacher;
import com.schoolmanagement.entity.concretes.UserRole;
import com.schoolmanagement.payload.request.TeacherRequest;
import com.schoolmanagement.payload.response.TeacherResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TeacherMapper {

    //private final UserRoleService userRoleService; //UserRole enjekte islemini parametreye ceviriyoruz

    //DTO --> POJO dönüsümü
    public Teacher dtoTeacher(TeacherRequest teacherRequest) {

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
                .isAdvisor(teacherRequest.isAdvisorTeacher())
                .gender(teacherRequest.getGender())
                .build();
    }

    //POJO-->DTO dönüsümü
    public  TeacherResponse createTeacherResponse(Teacher teacher) {
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

    //DTO --> POJO dönüsümü
    public Teacher createUpdatedTeacher(TeacherRequest request, Long id, UserRole userRole) {
        return Teacher.builder()
                .id(id)
                .username(request.getUsername())
                .name(request.getName())
                .surname(request.getSurname())
                .ssn(request.getSsn())
                .birthDay(request.getBirthDay())
                .birthPlace(request.getBirthPlace())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .isAdvisor(request.isAdvisorTeacher())
                .userRole(userRole)  //.userRole(userRoleService.getUserRole(RoleType.TEACHER)) yazimi parametredeki UserRole birakildi
                .email(request.getEmail())
                .build();
    }
}














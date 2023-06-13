package com.schoolmanagement.payload.mappers;

import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.entity.concretes.Student;
import com.schoolmanagement.entity.concretes.Teacher;
import com.schoolmanagement.payload.response.LessonProgramResponse;
import com.schoolmanagement.payload.response.StudentResponse;
import com.schoolmanagement.payload.response.TeacherResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

//@Component
@Data
@RequiredArgsConstructor
public class LessonProgramTeacherStudentMapper {

    // POJO --> DTO Dönüsümü
    public LessonProgramResponse createLessonProgramResponse(LessonProgram lessonProgram) {
        return LessonProgramResponse.builder()
                .day(lessonProgram.getDay())
                .startTime(lessonProgram.getStartTime())
                .stopTime(lessonProgram.getStopTime())
                .lessonProgramId(lessonProgram.getId())
                .lessonName(lessonProgram.getLesson())
                //!!! Teacher yazilinca eklendi
                .teachers(lessonProgram.getTeachers().stream().map(this::createTeacherResponse).collect(Collectors.toSet()))
                //TODO Student yazilinca buraya ekleme yapilacak
                .students(lessonProgram.getStudents()
                        .stream()
                        .map(this::createStudentResponse)
                        .collect(Collectors.toSet()))
                .build();
    }

    // POJO --DTO dönüsümü
    public LessonProgramResponse createLessonProgramResponseForStudent(LessonProgram lessonProgram) {
        return LessonProgramResponse.builder()
                .day(lessonProgram.getDay())
                .startTime(lessonProgram.getStartTime())
                .stopTime(lessonProgram.getStopTime())
                .lessonProgramId(lessonProgram.getId())
                .lessonName(lessonProgram.getLesson())
                //!!! Teacher yazilinca eklendi
                .teachers(lessonProgram.getTeachers().stream().map(this::createTeacherResponse).collect(Collectors.toSet()))
                .build();
    }

    //POJO-->DTO dönüsümü
    public TeacherResponse createTeacherResponse(Teacher teacher) {
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

    // POJO --> DTO dönüsümü
    public LessonProgramResponse createLessonProgramResponseForTeacher(LessonProgram lessonProgram) {
        return LessonProgramResponse.builder()
                .day(lessonProgram.getDay())
                .startTime(lessonProgram.getStartTime())
                .stopTime(lessonProgram.getStopTime())
                .lessonProgramId(lessonProgram.getId())
                .lessonName(lessonProgram.getLesson())
                //TODO Student yazilinca buraya ekleme yapilacak
                .students(lessonProgram.getStudents()
                        .stream()
                        .map(this::createStudentResponse)
                        .collect(Collectors.toSet()))
                .build();
    }

    //!!! POJO - DTO dönüsümü
    public StudentResponse createStudentResponse(Student student) {

        return StudentResponse.builder()
                .userId(student.getId())
                .username(student.getUsername())
                .name(student.getName())
                .surname(student.getSurname())
                .birthDay(student.getBirthDay())
                .birthPlace(student.getBirthPlace())
                .phoneNumber(student.getPhoneNumber())
                .gender(student.getGender())
                .email(student.getEmail())
                .fatherName(student.getFatherName())
                .motherName(student.getMotherName())
                .studentNumber(student.getStudentNumber())
                .isActive(student.isActive())
                .build();
    }
}













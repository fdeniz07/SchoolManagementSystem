package com.schoolmanagement.payload.mappers;

import com.schoolmanagement.entity.concretes.Student;
import com.schoolmanagement.entity.concretes.StudentInfo;
import com.schoolmanagement.entity.enums.Note;
import com.schoolmanagement.payload.request.StudentInfoRequestWithoutTeacherId;
import com.schoolmanagement.payload.response.StudentInfoResponse;
import com.schoolmanagement.payload.response.StudentResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;

//@Component
@Data
@RequiredArgsConstructor
public class StudentInfoMapper {

    public StudentInfo createDto(StudentInfoRequestWithoutTeacherId request, Note note, Double average) {
        return StudentInfo.builder()
                .infoNote(request.getInfoNote())
                .absentee(request.getAbsentee())
                .midtermExam(request.getMidtermExam())
                .finalExam(request.getFinalExam())
                .examAverage(average)
                .letterGrade(note)
                .build();
    }

    public StudentInfoResponse createResponse(StudentInfo studentInfo){
        return  StudentInfoResponse.builder()
                .lessonName(studentInfo.getLesson().getLessonName())
                .creditScore(studentInfo.getLesson().getCreditScore())
                .isCompulsory(studentInfo.getLesson().getIsCompulsory())
                .educationTerm(studentInfo.getEducationTerm().getTerm())
                .id(studentInfo.getId())
                .absentee(studentInfo.getAbsentee())
                .midtermExam(studentInfo.getMidtermExam())
                .finalExam(studentInfo.getFinalExam())
                .infoNote(studentInfo.getInfoNote())
                .average(studentInfo.getExamAverage())
                .studentResponse(createStudentResponse(studentInfo.getStudent()))
                .build();
    }

    public StudentResponse createStudentResponse(Student student){
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
                .motherName(student.getMotherName())
                .fatherName(student.getFatherName())
                .studentNumber(student.getStudentNumber())
                .isActive(student.isActive())
                .build();
    }
}












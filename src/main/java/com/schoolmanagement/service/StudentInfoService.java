package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.*;
import com.schoolmanagement.entity.enums.Note;
import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.mappers.StudentInfoMapper;
import com.schoolmanagement.payload.request.StudentInfoRequestWithoutTeacherId;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.StudentInfoResponse;
import com.schoolmanagement.repository.StudentInfoRepository;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class StudentInfoService implements Serializable {

    private final StudentInfoRepository studentInfoRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final LessonService lessonService;
    private final EducationTermService educationTermService;
    private final StudentInfoMapper studentInfoMapper;

    @Value("${midterm.exam.impact.percentage}") //bu datalari resources-->application.properties altindan cek diyoruz
    private Double midtermExamPercentage;

    @Value("${final.exam.impact.percentage}")
    private Double finalExamPercentage;

    //Not: save() *******************************************************************************************************************************
    public ResponseMessage<StudentInfoResponse> save(String username, StudentInfoRequestWithoutTeacherId request) {

        //DTO ve Request'den gelen Student,Teacher,Lesson ve EducationTerm getiriliyor --> Id verecegiz geriye POJO dönmeli
        Student student = studentService.getStudentByIdForResponse(request.getStudentId());
        Teacher teacher = teacherService.getTeacherByUsername(username);
        Lesson lesson = lessonService.getLessonById(request.getLessonId());
        EducationTerm educationTerm = educationTermService.getById(request.getEducationTermId());

        //!!! lesson cakisma var mi kontrolü
        if (checkSameLesson(request.getStudentId(), lesson.getLessonName())) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_LESSON_MESSAGE, lesson.getLessonName()));
        }

        //!!! Ders notu ortalamasi aliniyor
        Double noteAverage = calculateExamAverage(request.getMidtermExam(), request.getFinalExam());

        //!!! Ders notu alfabetik olarak hesaplaniyor
        Note note = checkLetterGrade(noteAverage);

        //!!! DTO --> POJO Dönüsümü
        StudentInfo studentInfo = studentInfoMapper.createDto(request, note, noteAverage);

        //!!! DTO da olmayan fieldler setleniyor
        studentInfo.setStudent(student);
        studentInfo.setEducationTerm(educationTerm);
        studentInfo.setTeacher(teacher);
        studentInfo.setLesson(lesson);

        //!!! DB'ye kayit islemi
        StudentInfo savedStudentInfo = studentInfoRepository.save(studentInfo);

        //!!! Response objesi olsuturuluyor
        return ResponseMessage.<StudentInfoResponse>builder()
                .message("Student Info Saved Successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(studentInfoMapper.createResponse(savedStudentInfo))
                .build();
    }

    private boolean checkSameLesson(Long studentId, String lessonName) {

        return studentInfoRepository.getAllByStudentId_Id(studentId)
                .stream()
                .anyMatch((e) -> e.getLesson().getLessonName().equalsIgnoreCase(lessonName));
    }

    private Double calculateExamAverage(Double midtermExam, Double finalExam) {

        return ((midtermExam * midtermExamPercentage) + (finalExam * finalExamPercentage));
    }

    private Note checkLetterGrade(Double average) {
        if (average < 50.0) {
            return Note.FF;
        } else if (average > 50.0 && average < 55.0) {
            return Note.DD;
        } else if (average > 55.0 && average < 60.0) {
            return Note.DC;
        } else if (average > 60.0 && average < 65.0) {
            return Note.CC;
        } else if (average > 65.0 && average < 70.0) {
            return Note.CB;
        } else if (average > 70.0 && average < 75.0) {
            return Note.BB;
        } else if (average > 75.0 && average < 80.0) {
            return Note.BA;
        } else {
            return Note.AA;
        }
    }

    // Not: delete()*****************************************************************************************************************************
    public ResponseMessage<?> deleteStudentInfo(Long studentInfoId) {

        if(!studentInfoRepository.existsByIdEquals(studentInfoId)) {
            throw new ResourceNotFoundException(String.format(Messages.STUDENT_INFO_NOT_FOUND, studentInfoId));
        }
        studentInfoRepository.deleteById(studentInfoId);

        return ResponseMessage.builder()
                .message("Student Info deleted successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }
}















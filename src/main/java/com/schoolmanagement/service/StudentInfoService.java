package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.*;
import com.schoolmanagement.entity.enums.Note;
import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.mappers.StudentInfoMapper;
import com.schoolmanagement.payload.request.StudentInfoRequestWithoutTeacherId;
import com.schoolmanagement.payload.request.UpdateStudentInfoRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.StudentInfoResponse;
import com.schoolmanagement.payload.response.StudentResponse;
import com.schoolmanagement.repository.StudentInfoRepository;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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

        if (!studentInfoRepository.existsByIdEquals(studentInfoId)) {
            throw new ResourceNotFoundException(String.format(Messages.STUDENT_INFO_NOT_FOUND, studentInfoId));
        }
        studentInfoRepository.deleteById(studentInfoId);

        return ResponseMessage.builder()
                .message("Student Info deleted successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Not: update()*****************************************************************************************************************************
    public ResponseMessage<StudentInfoResponse> update(UpdateStudentInfoRequest studentInfoRequest, Long studentInfoId) {

        //!!! Parametreden gelen datalar ile nesneler elde ediliyor
        Lesson lesson = lessonService.getLessonById(studentInfoRequest.getLessonId()); //DB'ye kayit islemi olacagi icin POJO dönen bir metot ariyoruz
        StudentInfo getStudentInfo = getStudentInfoById(studentInfoId);
        EducationTerm educationTerm = educationTermService.getById(studentInfoRequest.getEducationTermId());

        //!!! Dersnotu ortalamasi hesaplaniyor
        Double notAverage = calculateExamAverage(studentInfoRequest.getMidtermExam(), studentInfoRequest.getFinalExam());

        //!!! AlfabetikNot belirlenecek
        Note note = checkLetterGrade(notAverage);

        //!!! DTO-->POJO dönüsümü
        StudentInfo studentInfo = studentInfoMapper.createUpdatedStudent(studentInfoRequest, studentInfoId, lesson, educationTerm, note, notAverage);
        studentInfo.setStudent(getStudentInfo.getStudent()); //PutMapping yaptigimiz icin degisiklik yapmasak da bu alanlari burada aynen setlemeliyiz
        studentInfo.setTeacher(getStudentInfo.getTeacher()); //PutMapping yaptigimiz icin degisiklik yapmasak da bu alanlari burada aynen setlemeliyiz

        //!!! DB'ye kayit islemi
        StudentInfo updatedStudentInfo = studentInfoRepository.save(studentInfo);

        //!!! Response nesnesi olusturuluyor
        return ResponseMessage.<StudentInfoResponse>builder()
                .message("Student Info Updated Successfully")
                .httpStatus(HttpStatus.OK)
                .object(studentInfoMapper.createResponse(updatedStudentInfo))
                .build();
    }

    private StudentInfo getStudentInfoById(Long studentInfoId) {

        if (!studentInfoRepository.existsByIdEquals(studentInfoId)) {
            throw new ResourceNotFoundException(String.format(Messages.STUDENT_INFO_NOT_FOUND, studentInfoId));
        }
        return studentInfoRepository.findByIdEquals(studentInfoId);
    }

    // Not: getStudentInfoById() *****************************************************************************************************************
    public Page<StudentInfoResponse> getAllForAdmin(Pageable pageable) {

        return studentInfoRepository.findAll(pageable).map(studentInfoMapper::createResponse);
    }

    // Not: getAllForTeacher() ******************************************************************************************************************
    public Page<StudentInfoResponse> getAllTeacher(Pageable pageable, String username) {

        //ÖDEV : alttaki metotdaki ya yoksa metodunu burayada yapalim

        return studentInfoRepository.findByTeacherId_UsernameEquals(username, pageable).map(studentInfoMapper::createResponse);
    }

    // Not: getAllForStudent() ******************************************************************************************************************
    public Page<StudentInfoResponse> getAllStudentInfoByStudent(String username, Pageable pageable) {

        boolean student = studentService.existByUsername(username);

        if (!student) throw new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE);
        return studentInfoRepository.findByStudentId_UsernameEquals(username, pageable).map(studentInfoMapper::createResponse);
    }

    // Not: getStudentInfoByStudentId() ***********************************************************************************************************
    public List<StudentInfoResponse> getStudentInfoByStudentId(Long studentId) {

        if (!studentService.existById(studentId)) {
            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, studentId));
        }

        if (!studentInfoRepository.existsByStudent_IdEquals(studentId)) {
            throw new ResourceNotFoundException(String.format(Messages.STUDENT_INFO_NOT_FOUND_BY_STUDENT_ID, studentId));
        }

        return studentInfoRepository.findByStudent_IdEquals(studentId)
                .stream()
                .map(studentInfoMapper::createResponse)
                .collect(Collectors.toList());
    }

    // Not: getStudentInfoById() ********************************************************************************************************************
    public StudentInfoResponse findStudentInfoById(Long id) {

        if (!studentInfoRepository.existsByIdEquals(id)) {
            throw new ResourceNotFoundException(String.format(Messages.STUDENT_INFO_NOT_FOUND, id));
        }

        return studentInfoMapper.createResponse(studentInfoRepository.findByIdEquals(id));
    }
}





























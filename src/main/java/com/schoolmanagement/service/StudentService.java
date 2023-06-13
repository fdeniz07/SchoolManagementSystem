package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.AdvisorTeacher;
import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.entity.concretes.Student;
import com.schoolmanagement.entity.concretes.UserRole;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.mappers.LessonProgramTeacherStudentMapper;
import com.schoolmanagement.payload.mappers.StudentMapper;
import com.schoolmanagement.payload.request.ChooseLessonProgramWithId;
import com.schoolmanagement.payload.request.StudentRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.StudentResponse;
import com.schoolmanagement.repository.StudentRepository;
import com.schoolmanagement.utils.CheckSameLessonProgram;
import com.schoolmanagement.utils.CheckUniqueFields;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService implements Serializable {

    private final StudentRepository studentRepository;
    private final AdvisorTeacherService advisorTeacherService;
    private final CheckUniqueFields checkUniqueFields;
    private final StudentMapper studentMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final LessonProgramService lessonProgramService;
    private final LessonProgramTeacherStudentMapper lessonProgramTeacherStudentMapper;

    //Not: save() *************************************************************************************************************************************
    public ResponseMessage<StudentResponse> save(StudentRequest studentRequest) {

        //!!! AdvisorTeacher kontrolü
        AdvisorTeacher advisorTeacher = advisorTeacherService.getAdvisorTeacherGetById(studentRequest.getAdvisorTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Messages.NOT_FOUND_ADVISOR_MESSAGE,
                        studentRequest.getAdvisorTeacherId())));

        //!!! Dublicate Kontrolü
        checkUniqueFields.checkDuplicate(studentRequest.getUsername(),
                studentRequest.getSsn(),
                studentRequest.getPhoneNumber(),
                studentRequest.getEmail());

        //!!! Student DTO - POJO
        Student student = studentMapper.studentRequestToDto(studentRequest);

        //!!! Student nesnesindeki eksik datalari setliyoruz
        student.setStudentNumber(lastNumber());
        student.setAdvisorTeacher(advisorTeacher);
        student.setUserRole(userRoleService.getUserRole(RoleType.STUDENT));
        student.setActive(true);
        student.setPassword(passwordEncoder.encode(studentRequest.getPassword()));

        //studentRepository.save(student); //Bunu da yapabiliriz ama biz asagidaki gibi cesitlendiriyoruz
        //!!! Response nesnesi olusturuluyor
        return ResponseMessage.<StudentResponse>builder()
                .object(lessonProgramTeacherStudentMapper.createStudentResponse(studentRepository.save(student)))
                .message("Student save successfully")
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public int lastNumber() {
        if (studentRepository.findStudent()) {
            return 1000;
        }
        return studentRepository.getLastStudentNumber() + 1;
    }

    //Not: changeActiveStatus() ***********************************************************************************************************************
    public ResponseMessage<?> changeStatus(Long id, boolean status) {

        //!!! id kontrolü
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE));

        student.setActive(status);

        studentRepository.save(student);

        return ResponseMessage.builder()
                .message("Student is " + (status ? "active" : "passive"))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    //Not: getAllStudents () ***************************************************************************************************************************
    public List<StudentResponse> getAllStudent() {

        return studentRepository.findAll()
                .stream()
                .map(lessonProgramTeacherStudentMapper::createStudentResponse)
                .collect(Collectors.toList());
    }

    //Not: updateStudent() *******************************************************************************************************************************
    public ResponseMessage<StudentResponse> updateStudent(Long userId, StudentRequest studentRequest) {

        //!!! Student var mi kontrolü
        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE));

        //!!! AdvisorTeacher kontrolü
        AdvisorTeacher advisorTeacher = advisorTeacherService.getAdvisorTeacherGetById(studentRequest.getAdvisorTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Messages.NOT_FOUND_ADVISOR_MESSAGE, studentRequest.getAdvisorTeacherId())));

        //!!! Duplicate kontrolü
        checkUniqueFields.checkDuplicate(studentRequest.getUsername(), studentRequest.getSsn(), studentRequest.getPhoneNumber(), student.getEmail());

        //!!! DTO --> POJO dönüsümü
        UserRole userRole = userRoleService.getUserRole(RoleType.STUDENT);
        Student updatedStudent = studentMapper.createUpdatedStudent(studentRequest, userId, userRole, advisorTeacher);
        updatedStudent.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        //updatedStudent.setAdvisorTeacher(advisorTeacher); //biz bu kismi yukarida parametre olarak DTO-POJO ya parametre olarak gectik
        updatedStudent.setStudentNumber(student.getStudentNumber());
        updatedStudent.setActive(true);

        studentRepository.save(updatedStudent);

        return ResponseMessage.<StudentResponse>builder()
                .object(lessonProgramTeacherStudentMapper.createStudentResponse(updatedStudent))
                .message("Student updated Successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    //Not: deleteStudent() *******************************************************************************************************************************
    public ResponseMessage<?> deleteStudent(Long studentId) {

        //!!! id var mi kontrolü
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE));

        studentRepository.deleteById(studentId);

        return ResponseMessage.builder()
                .message("Student is deleted successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    //Not: getStudentByName() ***************************************************************************************************************************
    public List<StudentResponse> getStudentByName(String studentName) {

        return studentRepository.getStudentByNameContaining(studentName)
                .stream()
                .map(lessonProgramTeacherStudentMapper::createStudentResponse)
                .collect(Collectors.toList());
    }

    // Not: getStudentById() ***************************************************************************************************************************
    public Student getStudentByIdForResponse(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE));

    }

    // Not: getAllStudentWithPage() ********************************************************************************************************************
    public Page<StudentResponse> search(int page, int size, String sort, String type) {

        // Pageable pageable = PageRequest.of(page,size, Sort.by(type,sort)); //--> Asagideki kodun kisaltmasi
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")) {
            PageRequest.of(page, size, Sort.by(sort).descending());
        }

        return studentRepository.findAll(pageable).map(lessonProgramTeacherStudentMapper::createStudentResponse);
    }

    // Not: chooseLessonProgramById() ******************************************************************************************************************
    public ResponseMessage<StudentResponse> chooseLesson(String username, ChooseLessonProgramWithId chooseLessonProgramRequest) {

        //!!! Student ve LessonProgram kontrolü
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE));

        //!!! Talep edilen LessonProgram getiriliyor
        Set<LessonProgram> lessonPrograms = lessonProgramService.getLessonProgramById(chooseLessonProgramRequest.getLessonProgramId());

        if (lessonPrograms.size() == 0) {
            throw new ResourceNotFoundException(Messages.LESSON_PROGRAM_NOT_FOUND_MESSAGE);
        }

        //!!! Ögrencinin mevcut LessonProgram'ini getiriyoruz
        Set<LessonProgram> studentOldLessonProgram = student.getLessonsProgramList();

        //!!! lesson icin dublicate kontrolü
        CheckSameLessonProgram.checkLessonPrograms(studentOldLessonProgram, lessonPrograms);

        studentOldLessonProgram.addAll(lessonPrograms);
        student.setLessonsProgramList(studentOldLessonProgram);
        Student savedStudent = studentRepository.save(student);

        return ResponseMessage.<StudentResponse>builder()
                .message("Lessons added to Student")
                .object(lessonProgramTeacherStudentMapper.createStudentResponse(savedStudent))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    // Not: getAllStudentByAdvisorUsername() **************************************************************************************************************
    public List<StudentResponse> getAllStudentByTeacher_Username(String username) {

        return studentRepository.getStudentByAdvisorTeacher_Username(username)
                .stream()
                .map(lessonProgramTeacherStudentMapper::createStudentResponse)
                .collect(Collectors.toList());
    }
}





























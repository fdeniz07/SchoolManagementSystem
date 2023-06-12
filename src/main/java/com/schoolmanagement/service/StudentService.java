package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.AdvisorTeacher;
import com.schoolmanagement.entity.concretes.Student;
import com.schoolmanagement.entity.concretes.UserRole;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.mappers.StudentMapper;
import com.schoolmanagement.payload.request.StudentRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.StudentResponse;
import com.schoolmanagement.repository.StudentRepository;
import com.schoolmanagement.utils.CheckUniqueFields;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
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
                .object(studentMapper.createStudentResponse(studentRepository.save(student)))
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
                .map(studentMapper::createStudentResponse)
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
                .object(studentMapper.createStudentResponse(updatedStudent))
                .message("Student updated Successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }
}



























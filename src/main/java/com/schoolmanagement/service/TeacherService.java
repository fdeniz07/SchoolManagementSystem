package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.entity.concretes.Teacher;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.mappers.TeacherMapper;
import com.schoolmanagement.payload.request.TeacherRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.TeacherResponse;
import com.schoolmanagement.repository.TeacherRepository;
import com.schoolmanagement.utils.CheckParameterUpdateMethod;
import com.schoolmanagement.utils.CheckUniqueFields;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService implements Serializable {

    private final TeacherRepository teacherRepository;
    private final LessonProgramService lessonProgramService;
    private final CheckUniqueFields checkUniqueFields;
    private final PasswordEncoder passwordEncoder;
    private final TeacherMapper teacherMapper;
    private final UserRoleService userRoleService;

    //Not: save() *************************************************************************************************************************************
    public ResponseMessage<TeacherResponse> save(TeacherRequest request) {

        Set<LessonProgram> lessons = lessonProgramService.getLessonProgramById(request.getLessonsIdList());

        if (lessons.size() == 0) {
            throw new BadRequestException(Messages.LESSON_PROGRAM_NOT_FOUND_MESSAGE);
            // !!! Dublicate kontrolu
        } else {
            checkUniqueFields.checkDuplicate(request.getUsername(), request.getSsn(), request.getPhoneNumber(), request.getEmail());
        }

        //!!! Dto --> Pojo dönüsümü
        Teacher teacher = teacherMapper.dtoTeacher(request);

        // !!! Rol bilgisi setleniyor
        teacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));

        // !!! dersProgrami ekleniyor
        teacher.setLessonsProgramList(lessons);

        // !!! sifre encode ediliyor
        teacher.setPassword(passwordEncoder.encode(request.getPassword()));

        // !!! Db ye kayit islemi
        Teacher savedTeacher = teacherRepository.save(teacher);

        //TODO AdvisorTeacher yazilinca ekleme yapilacak

        return ResponseMessage.<TeacherResponse>builder()
                .message("Teacher saved successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(teacherMapper.createTeacherResponse(savedTeacher))
                .build();
    }

    //Not: getAllTeacher() ****************************************************************************************************************************
    public List<TeacherResponse> getAllTeacher() {

        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::createTeacherResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<TeacherResponse> updateTeacher(TeacherRequest newTeacher, Long userId) {

        //!!! Id üzerinden teacher nesnesi getiriliyor
        Optional<Teacher> teacher = teacherRepository.findById(userId);

        //Dto üzerinden eklenecek lesson'lar getiriliyor
        Set<LessonProgram> lessons = lessonProgramService.getLessonProgramById(newTeacher.getLessonsIdList());

        //Gelen listenin ici bos mu dolu mu kontrolü (OrElseThrow yapanlarin bunu kullanmasina gerek yok)
        if (!teacher.isPresent()) {
            throw new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE);
        } else if (lessons.size() == 0) {
            throw new BadRequestException(Messages.LESSON_PROGRAM_NOT_FOUND_MESSAGE);
        } else if (!CheckParameterUpdateMethod.checkParameter(teacher.get(), newTeacher)) { //TODO email kontrol
            checkUniqueFields.checkDuplicate(newTeacher.getUsername(),
                    newTeacher.getSsn(),
                    newTeacher.getPhoneNumber(),
                    newTeacher.getEmail());
        }

        Teacher updatedTeacher = teacherMapper.createUpdatedTeacher(newTeacher, userId);

        //!!! Password encode ediliyor
        updatedTeacher.setPassword(passwordEncoder.encode(newTeacher.getPassword()));

        //!!! LessonProgram set ediliyor
        updatedTeacher.setLessonsProgramList(lessons); //TODO buraya bakilacak
        Teacher savedTeacher = teacherRepository.save(updatedTeacher);

        //TODO: AdvisorTeacher eklenince yazilacak

        return ResponseMessage.<TeacherResponse>builder()
                .object(teacherMapper.createTeacherResponse(savedTeacher))
                .message("Teacher Updated Successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Not: getTeacherByName() ************************************************************************************************************************
    public List<TeacherResponse> getTeacherByName(String teacherName) {

        return teacherRepository.getTeacherByNameContaining(teacherName)
                .stream()
                .map(teacherMapper::createTeacherResponse)
                .collect(Collectors.toList());
    }

    // Not: deleteTeacher() ***************************************************************************************************************************
    public ResponseMessage<?> deleteTeacher(Long id) {

        teacherRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE);
        });

        // LessonProgram tabloasunda teacher kaldirilacak
        teacherRepository.deleteById(id);

        return ResponseMessage.builder()
                .message("Teacher is Deleted")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Not: getTeacherById() ****************************************************************************************************************************
    public ResponseMessage<TeacherResponse> getSavedTeacherById(Long id) {

        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException(Messages.NOT_FOUND_USER_MESSAGE);
        });

        return ResponseMessage.<TeacherResponse>builder()
                .object(teacherMapper.createTeacherResponse(teacher))
                .message("Teacher Successfully found")
                .httpStatus(HttpStatus.OK)
                .build();

    }
}






























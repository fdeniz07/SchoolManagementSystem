package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.entity.concretes.Teacher;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.payload.mappers.TeacherMapper;
import com.schoolmanagement.payload.request.TeacherRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.TeacherResponse;
import com.schoolmanagement.repository.TeacherRepository;
import com.schoolmanagement.utils.CheckUniqueFields;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;

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

        Set<LessonProgram> lessons = lessonProgramService.getLessonProgramById(request.getLessonIdList());

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
}






























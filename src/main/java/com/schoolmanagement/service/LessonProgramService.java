package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.EducationTerm;
import com.schoolmanagement.entity.concretes.Lesson;
import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.mappers.LessonProgramMapper;
import com.schoolmanagement.payload.mappers.LessonProgramTeacherStudentMapper;
import com.schoolmanagement.payload.request.LessonProgramRequest;
import com.schoolmanagement.payload.response.LessonProgramResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.LessonProgramRepository;
import com.schoolmanagement.utils.Messages;
import com.schoolmanagement.utils.TimeControl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonProgramService {

    private final LessonProgramRepository lessonProgramRepository;
    private final LessonService lessonService;
    private final EducationTermService educationTermService;
    private final LessonProgramMapper lessonProgramMapper;
    private final LessonProgramTeacherStudentMapper lessonProgramTeacherStudentMapper;

    //Not: save() **************************************************************************************************************************************
    public ResponseMessage<LessonProgramResponse> save(LessonProgramRequest request) {

        //!!! Lesson programda olacak dersleri LessonService üzerinden getiriyorum
        Set<Lesson> lessons = lessonService.getLessonByLessonIdList(request.getLessonIdList());

        //!!! EducationTerm id ile getiriliyor
        EducationTerm educationTerm = educationTermService.getById(request.getEducationTermId());

        // !!! yukarda gelen lessons ici bos degilse zaman kontrolu yapiliyor :
        if (lessons.size() == 0) {
            throw new ResourceNotFoundException(Messages.NOT_FOUND_LESSON_IN_LIST);
        } else if (TimeControl.check(request.getStartTime(), request.getStopTime())) {
            throw new BadRequestException(Messages.TIME_NOT_VALID_MESSAGE);
        }

        // !!! DTO-POJO donusumu
        LessonProgram lessonProgram = lessonProgramRequestToDto(request, lessons);
        // !!! lessonProgram da educationTerm bilgisi setleniyor
        lessonProgram.setEducationTerm(educationTerm);
        //!!! lessonProgram DB ye kaydediliyor
        LessonProgram savedLessonProgram = lessonProgramRepository.save(lessonProgram);
        // !!! ResponseMessage objesi olusturuluyor
        return ResponseMessage.<LessonProgramResponse>builder()
                .message("Lesson Program is Created")
                .httpStatus(HttpStatus.CREATED)
                .object(lessonProgramMapper.createLessonProgramResponseForSaveMethod(savedLessonProgram))
                .build();
    }

    private LessonProgram lessonProgramRequestToDto(LessonProgramRequest lessonProgramRequest, Set<Lesson> lessons) {
        return lessonProgramMapper.dtoLessonProgram(lessonProgramRequest, lessons);
    }

    // NOT : getAll() *********************************************************************************************************************************
    public List<LessonProgramResponse> getAllLessonProgram() {

        return lessonProgramRepository.findAll()
                .stream()
                .map(lessonProgramTeacherStudentMapper::createLessonProgramResponse)
                .collect(Collectors.toList());
    }

    // Not :  getById() ********************************************************************************************************************************
    public LessonProgramResponse getByLessonProgramId(Long id) {

        LessonProgram lessonProgram = lessonProgramRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_LESSON_MESSAGE, id));
        });

        // return lessonProgramRepository.findById(id).map(this::createLessonProgramResponse).get();
        return lessonProgramTeacherStudentMapper.createLessonProgramResponse(lessonProgram);
    }

    // Not :  getAllLessonProgramUnassigned() ************************************************************************************************************
    public List<LessonProgramResponse> getAllLessonProgramUnassigned() {

        return lessonProgramRepository.findByTeachers_IdNull()
                .stream()
                .map(lessonProgramTeacherStudentMapper::createLessonProgramResponse)
                .collect(Collectors.toList());
    }

    // Not :  getAllLessonProgramAssigned() ***************************************************************************************************************
    public List<LessonProgramResponse> getAllLessonProgramAssigned() {

        return lessonProgramRepository.findByTeachers_IdNotNull()
                .stream()
                .map(lessonProgramTeacherStudentMapper::createLessonProgramResponse)
                .collect(Collectors.toList());
    }

    // Not :  Delete() *************************************************************************************************************************************
    public ResponseMessage deleteLessonProgram(Long id) {

        // !!! id kontrolu
        lessonProgramRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(Messages.NOT_FOUND_LESSON_MESSAGE, id)));

        lessonProgramRepository.deleteById(id);

        return ResponseMessage.builder()
                .message("Lesson Program is deleted Successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Not :  getLessonProgramByTeacher() *******************************************************************************************************************
    public Set<LessonProgramResponse> getLessonProgramByTeacher(String username) {
        return lessonProgramRepository.getLessonProgramByTeacherUsername(username)
                .stream()
                .map(lessonProgramTeacherStudentMapper::createLessonProgramResponseForTeacher)
                .collect(Collectors.toSet());
    }

    // Not :  getLessonProgramByStudent() *******************************************************************************************************************
    public Set<LessonProgramResponse> getLessonProgramByStudent(String username) {
        return lessonProgramRepository.getLessonProgramByStudentUsername(username)
                .stream()
                .map(lessonProgramTeacherStudentMapper::createLessonProgramResponseForStudent)
                .collect(Collectors.toSet());
    }

    // Not :  getAllWithPage() *******************************************************************************************************************************
    public Page<LessonProgramResponse> search(int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        return lessonProgramRepository.findAll(pageable)
                .map(lessonProgramTeacherStudentMapper::createLessonProgramResponse);
    }

    // Not :  getLessonProgramById() ***************************************************************************************************************************
    public Set<LessonProgram> getLessonProgramById(Set<Long> lessonIdList) {

        return  lessonProgramRepository.getLessonProgramByLessonProgramIdList(lessonIdList);
    }
}




























package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.EducationTerm;
import com.schoolmanagement.entity.concretes.Lesson;
import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.mappers.LessonProgramMapper;
import com.schoolmanagement.payload.request.LessonProgramRequest;
import com.schoolmanagement.payload.response.LessonProgramResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.LessonProgramRepository;
import com.schoolmanagement.utils.Messages;
import com.schoolmanagement.utils.TimeControl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonProgramService {

    private final LessonProgramRepository lessonProgramRepository;
    private final LessonService lessonService;
    private final EducationTermService educationTermService;
    private final LessonProgramMapper lessonProgramMapper;

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
                .object(createLessonProgramResponseForSaveMethod(savedLessonProgram))
                .build();
    }

    private LessonProgram lessonProgramRequestToDto(LessonProgramRequest lessonProgramRequest, Set<Lesson> lessons) {
        return lessonProgramMapper.dtoLessonProgram(lessonProgramRequest, lessons);
    }

    private LessonProgramResponse createLessonProgramResponseForSaveMethod(LessonProgram lessonProgram) {
        return LessonProgramResponse.builder()
                .day(lessonProgram.getDay())
                .startTime(lessonProgram.getStartTime())
                .stopTime(lessonProgram.getStopTime())
                .lessonProgramId(lessonProgram.getId())
                .lessonName(lessonProgram.getLesson())
                .build();
    }

    // NOT : getAll() *********************************************************************************************************************************
    public List<LessonProgramResponse> getAllLessonProgram() {

        return lessonProgramRepository.findAll()
                .stream()
                .map(this::createLessonProgramResponse)
                .collect(Collectors.toList());
    }

    public LessonProgramResponse createLessonProgramResponse(LessonProgram lessonProgram) {
        return LessonProgramResponse.builder()
                .day(lessonProgram.getDay())
                .startTime(lessonProgram.getStartTime())
                .stopTime(lessonProgram.getStopTime())
                .lessonProgramId(lessonProgram.getId())
                .lessonName(lessonProgram.getLesson())
                //TODO Teacher ve Student yazilinca buraya ekleme yapilacak
                .build();
    }


    // Not :  getById() ********************************************************************************************************************************
    public LessonProgramResponse getByLessonProgramId(Long id) {

        LessonProgram lessonProgram = lessonProgramRepository.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_LESSON_MESSAGE, id));
        });

        // return lessonProgramRepository.findById(id).map(this::createLessonProgramResponse).get();
        return createLessonProgramResponse(lessonProgram);
    }

    // Not :  getAllLessonProgramUnassigned() ************************************************************************************************************
    public List<LessonProgramResponse> getAllLessonProgramUnassigned() {

        return lessonProgramRepository.findByTeachers_IdNull()
                .stream()
                .map(this::createLessonProgramResponse)
                .collect(Collectors.toList());
    }

    // Not :  getAllLessonProgramAssigned() ***************************************************************************************************************
    public List<LessonProgramResponse> getAllLessonProgramAssigned() {

        return lessonProgramRepository.findByTeachers_IdNotNull()
                .stream()
                .map(this::createLessonProgramResponse)
                .collect(Collectors.toList());

    }

    // Not :  Delete() *************************************************************************************************************************************
    public ResponseMessage deleteLessonProgram(Long id) {

        // !!! id kontrolu
        lessonProgramRepository.findById(id).orElseThrow(()->{
            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_LESSON_MESSAGE,id));
        });

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
                .map(this::createLessonProgramResponseForTeacher)
                .collect(Collectors.toSet());
    }

    public LessonProgramResponse createLessonProgramResponseForTeacher(LessonProgram lessonProgram) {
        return LessonProgramResponse.builder()
                .day(lessonProgram.getDay())
                .startTime(lessonProgram.getStartTime())
                .stopTime(lessonProgram.getStopTime())
                .lessonProgramId(lessonProgram.getId())
                .lessonName(lessonProgram.getLesson())
                //TODO Student yazilinca buraya ekleme yapilacak
                .build();
    }
}



























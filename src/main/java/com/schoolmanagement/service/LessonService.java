package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.Lesson;
import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.request.LessonRequest;
import com.schoolmanagement.payload.response.LessonProgramResponse;
import com.schoolmanagement.payload.response.LessonResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.LessonRepository;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService implements Serializable {

    private final LessonRepository lessonRepository;

    //Not: save() **************************************************************************************************************************************
    public ResponseMessage<LessonResponse> save(LessonRequest lessonRequest) {

        //!!! conflict kontrolü
        if (existsLessonByLessonName(lessonRequest.getLessonName())) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_LESSON_MESSAGE, lessonRequest.getLessonName()));
        }
        Lesson lesson = createLessonObject(lessonRequest);
        return ResponseMessage.<LessonResponse>builder()
                .object(createLessonResponse(lessonRepository.save(lesson)))
                .httpStatus(HttpStatus.CREATED)
                .message("Lesson Created Successfully")
                .build();
    }

    private boolean existsLessonByLessonName(String lessonName) {
        return lessonRepository.existsLessonByLessonNameEqualsIgnoreCase(lessonName);
    }

    private Lesson createLessonObject(LessonRequest request) {
        return Lesson.builder()
                .lessonName(request.getLessonName())
                .creditScore(request.getCreditScore())
                .isCompulsory(request.getIsCompulsory()) //isCompulsory negatif kontrolü eklenecek
                .build();
    }

    private LessonResponse createLessonResponse(Lesson lesson) {
        return LessonResponse.builder()
                .lessonId(lesson.getLessonId())
                .lessonName(lesson.getLessonName())
                .creditScore(lesson.getCreditScore())
                .isCompulsory(lesson.getIsCompulsory())
                .build();
    }

    // Not :  delete() ************************************************************************************************************************************
    public ResponseMessage deleteLesson(Long id) {

        lessonRepository.findById(id).orElseThrow(() -> {
            return new ResourceNotFoundException(String.format(Messages.NOT_FOUND_LESSON_MESSAGE, id));
        });

        lessonRepository.deleteById(id);

        return ResponseMessage.builder()
                .message("Lesson is deleted successfuly")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    //Not: getLessonByName() ******************************************************************************************************************************
    public ResponseMessage<LessonResponse> getLessonByLessonName(String lessonName) {

        Lesson lesson = lessonRepository.getLessonByLessonName(lessonName).orElseThrow(() -> {
            return new ResourceNotFoundException(String.format(Messages.NOT_FOUND_LESSON_MESSAGE, lessonName));
        });

        return ResponseMessage.<LessonResponse>builder()
                .message("Lesson Successfuly Found")
                .httpStatus(HttpStatus.OK)
                .object(createLessonResponse(lesson))
                .build();
    }

    //Not: getAllLesson() **********************************************************************************************************************************
    public List<LessonResponse> getAllLesson() {

        return lessonRepository.findAll()
                .stream()
                .map(this::createLessonResponse)
                .collect(Collectors.toList());
    }

    //Not: getAllWithPage() ********************************************************************************************************************************
    public Page<LessonResponse> search(int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        return lessonRepository.findAll(pageable).map(this::createLessonResponse);
    }

    //Not: getAllLessonByLessonIds() *************************************************************************************************************************
    public Set<Lesson> getLessonByLessonIdList(Set<Long> lessons) {

        return lessonRepository.getLessonByLessonIdList(lessons);
    }
}

























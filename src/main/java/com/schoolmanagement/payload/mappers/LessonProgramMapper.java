package com.schoolmanagement.payload.mappers;

import com.schoolmanagement.entity.concretes.Lesson;
import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.payload.request.LessonProgramRequest;
import com.schoolmanagement.payload.response.LessonProgramResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class LessonProgramMapper {

    private final TeacherMapper teacherMapper;

    //DTO --> POJO dönüsümü

    public LessonProgram dtoLessonProgram(LessonProgramRequest lessonProgramRequest, Set<Lesson> lessons){

        return LessonProgram.builder()
                .startTime(lessonProgramRequest.getStartTime())
                .stopTime(lessonProgramRequest.getStopTime())
                .day(lessonProgramRequest.getDay())
                .lesson(lessons)
                .build();
    }

    // POJO --> DTO Dönüsümü
    public LessonProgramResponse createLessonProgramResponse(LessonProgram lessonProgram) {
        return LessonProgramResponse.builder()
                .day(lessonProgram.getDay())
                .startTime(lessonProgram.getStartTime())
                .stopTime(lessonProgram.getStopTime())
                .lessonProgramId(lessonProgram.getId())
                .lessonName(lessonProgram.getLesson())
                //!!! Teacher yazilinca eklendi
                .teachers(lessonProgram.getTeachers().stream().map(teacherMapper::createTeacherResponse).collect(Collectors.toSet()))
                //TODO Student yazilinca buraya ekleme yapilacak
                .build();
    }

    // POJO --> DTO dönüsümü
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

    // POJO --> DTO dönüsümü
    public LessonProgramResponse createLessonProgramResponseForSaveMethod(LessonProgram lessonProgram) {
        return LessonProgramResponse.builder()
                .day(lessonProgram.getDay())
                .startTime(lessonProgram.getStartTime())
                .stopTime(lessonProgram.getStopTime())
                .lessonProgramId(lessonProgram.getId())
                .lessonName(lessonProgram.getLesson())
                .build();
    }

    // POJO --DTO dönüsümü
    public LessonProgramResponse createLessonProgramResponseForStudent(LessonProgram lessonProgram) {
        return LessonProgramResponse.builder()
                .day(lessonProgram.getDay())
                .startTime(lessonProgram.getStartTime())
                .stopTime(lessonProgram.getStopTime())
                .lessonProgramId(lessonProgram.getId())
                .lessonName(lessonProgram.getLesson())
                //!!! Teacher yazilinca eklendi
                .teachers(lessonProgram.getTeachers().stream().map(teacherMapper::createTeacherResponse).collect(Collectors.toSet()))
                .build();
    }
}






























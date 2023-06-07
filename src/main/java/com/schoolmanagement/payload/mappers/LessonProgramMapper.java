package com.schoolmanagement.payload.mappers;

import com.schoolmanagement.entity.concretes.Lesson;
import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.payload.request.LessonProgramRequest;
import lombok.Data;

import java.util.Set;

@Data
public class LessonProgramMapper {

    //DTO --> POJO dönüsümü

    public LessonProgram dtoLessonProgram(LessonProgramRequest lessonProgramRequest, Set<Lesson> lessons){

        return LessonProgram.builder()
                .startTime(lessonProgramRequest.getStartTime())
                .stopTime(lessonProgramRequest.getStopTime())
                .day(lessonProgramRequest.getDay())
                .lesson(lessons)
                .build();
    }
}






























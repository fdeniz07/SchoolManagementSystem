package com.schoolmanagement.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.payload.response.abstracts.BaseUserResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherResponse extends BaseUserResponse {

    private Set<LessonProgram> lessonPrograms;
    private String email;
}

























package com.schoolmanagement.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LessonRequest {

    @NotNull(message = "Please enter Lesson name")
    @Size(min = 2, max = 16, message = "Lesson name should be at least 2 characters")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+" ,message="Lesson name must consist of the characters .")
    private String lessonName;

    @NotNull(message = "Please enter credit score")
    private Integer creditScore;

    @NotNull(message = "Please enter isCompulsory")
    private Boolean isCompulsory;
}




















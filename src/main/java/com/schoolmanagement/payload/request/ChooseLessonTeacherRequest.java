package com.schoolmanagement.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChooseLessonTeacherRequest implements Serializable {

    @NotNull
    @Size(min = 1, message = "Lessons must not be empty")
    private Set<Long> lessonProgramId;

    @NotNull(message = "Please select teacher")
    private Long teacherId;
}



















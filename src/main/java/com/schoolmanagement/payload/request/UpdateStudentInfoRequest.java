package com.schoolmanagement.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateStudentInfoRequest implements Serializable {

    @NotNull(message = "please select education term")
    private Long educationTermId;

    @DecimalMax("100.0")
    @DecimalMin("0.0")
    @NotNull(message = "Please enter midterm exam")
    private Double midtermExam;

    @DecimalMax("100.0")
    @DecimalMin("0.0")
    @NotNull(message = "Please enter final exam")
    private Double finalExam;

    @NotNull(message = "Please enter absentee")
    private Integer absentee;

    @NotNull(message = "Please enter info")
    @Size(min = 10, max=200, message = "Info should be at least 10 chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+" ,message="Info must consist of the characters .")
    private String infoNote;

    @NotNull(message = "Please select lesson")
    private Long lessonId;
}







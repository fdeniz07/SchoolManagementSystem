package com.schoolmanagement.payload.response;

import com.schoolmanagement.entity.concretes.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MeetResponse implements Serializable {

    private Long id;
    private String description;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime stopTime;
    private Long advisorTeacherId;
    private String teacherName;
    private String teacherSsn;
    private String username;
    private List<Student> students;
}
















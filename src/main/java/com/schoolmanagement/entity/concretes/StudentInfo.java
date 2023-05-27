package com.schoolmanagement.entity.concretes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.schoolmanagement.entity.enums.Note;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class StudentInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer absentee;

    private Double midtermExam;
    private Double finalExam;
    private Double examAverage;

    private String infoNote;


    @ManyToOne
    @JsonIgnoreProperties("teacher")
    private Teacher teacher;

    @ManyToOne
    private Student student;

    @Enumerated(EnumType.STRING)
    private Note letterGrade;

    //!!! Lesson - EducationTerm
    @ManyToOne
    @JsonIgnoreProperties("lesson")
    private Lesson lesson;

    @OneToOne
    private EducationTerm educationTerm;

}

package com.schoolmanagement.entity.concretes;

import com.schoolmanagement.entity.abstracts.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Student extends User {

    private String motherName;

    private String fatherName;

    private int studentNumber;

    private boolean isActive;

    @Column(unique = true)
    private String email;

    //AdvisorTeacher, StudentInfo, LessonProgram, Meet --> iliskili tablo olacagindan yarin devam edilecektir.

}

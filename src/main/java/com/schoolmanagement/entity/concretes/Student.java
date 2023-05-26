package com.schoolmanagement.entity.concretes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.schoolmanagement.entity.abstracts.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder //Türeyen ya da türetilen class ise SuperBuilder yazilmali(hem user hem de student e yazilmali). Ancak single(tek) olan sinifa @Builder yeterlidir.
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true) //ayni nesneden türetilen iki objeyu hash leyip ona göre hizlice karsilastirir
                                                                    //ikinci parametre ise base'den gelen field larida dahil eder
@ToString(callSuper = true) // normalde toString metodu ilgili sinifin field'larini yazririr, callsuper ile basedeki fieldlari da yazdirmaya yarar
public class Student extends User {

    private String motherName;

    private String fatherName;

    private int studentNumber;

    private boolean isActive;

    @Column(unique = true)
    private String email;

    //AdvisorTeacher, StudentInfo, LessonProgram, Meet

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore //coklu iliskilerde tablonun birinde bu annotation kullanilir, aksi durumda sout yapildiginda sonsuz döngüye girer!
    private AdvisorTeacher advisorTeacher;

    @JsonIgnore
    @OneToMany(mappedBy = "student",cascade = CascadeType.REMOVE)
    private List<StudentInfo> studentInfos;

    @ManyToMany
    @JoinTable(
            name = "student_lessonprogramm",
            joinColumns = @JoinColumn(name="student_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_program_id")
    )
    private Set<LessonProgram> lessonProgramList;


    @ManyToMany
    @JoinTable(
            name = "meet_student_table",
            joinColumns = @JoinColumn(name="student_id"),
            inverseJoinColumns = @JoinColumn(name = "meet_id")
    )
    private Set<Meet> meetList; //Unique yapi bizim icin önemli ise Set yapisi(RoleTypes), önemli degil ise List de kullanilabilir.

}

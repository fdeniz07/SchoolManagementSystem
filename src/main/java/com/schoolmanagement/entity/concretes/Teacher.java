package com.schoolmanagement.entity.concretes;

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
@SuperBuilder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString
public class Teacher extends User {

    @OneToOne(mappedBy = "teacher", cascade = CascadeType.PERSIST,orphanRemoval = true)
    //cascade = CascadeType.PERSIST --> biri kaydedilince, digeri de otomatik DB ye kaydediliyor
    //orphanRemoval = true --> biri silinince digerini de sil
    private AdvisorTeacher advisorTeacher;

    @Column(name = "isAdvisor")
    private Boolean isAdvisor;

    @Column(unique = true)
    private String email;

    //!!! StudentInfo, LessonProgram
    @OneToMany(mappedBy = "teacher",cascade = CascadeType.REMOVE)
    private List<StudentInfo> studentInfos;

    @ManyToMany
    @JoinTable(
            name = "teacher_lesson_program",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_program_id")
    )
    private Set<LessonProgram> lessonsProgramList;
}

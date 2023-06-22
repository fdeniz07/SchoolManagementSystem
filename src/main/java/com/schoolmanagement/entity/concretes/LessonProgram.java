package com.schoolmanagement.entity.concretes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.schoolmanagement.entity.enums.Day;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LessonProgram implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Day day;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime stopTime;

    @JsonIgnore //Recursive yapiya girmesini önlemek icin yazdik
    @ManyToMany
    @JoinTable(
            name = "lesson_program_lesson",
            joinColumns = @JoinColumn(name = "lesson_program_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private Set<Lesson> lesson;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private EducationTerm educationTerm;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany(mappedBy = "lessonsProgramList", fetch = FetchType.EAGER)
    private Set<Teacher> teachers;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany(mappedBy = "lessonsProgramList", fetch = FetchType.EAGER)
    private Set<Student> students;

    //!!! @PreRemove yazilacak
    @PreRemove
    //Bir islem yapmadan önce yapilacaklar - Kayit silindiginde, Student'deki ve Teacher'deki LessonProgram iliskisini ve recordunu siliyoruz.
    private void removeLessonProgramFromStudent() {
        teachers.forEach((t) -> {
            t.getLessonsProgramList().remove(this);
        });

        students.forEach((s) -> {
            s.getLessonsProgramList().remove(this);
        });
    }
}

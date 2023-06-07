package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.LessonProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface LessonProgramRepository extends JpaRepository<LessonProgram,Long> {

    List<LessonProgram> findByTeachers_IdNull();

    List<LessonProgram> findByTeachers_IdNotNull();

    @Query("SELECT l FROM LessonProgram l INNER JOIN l.teachers teachers WHERE teachers.username =?1") // LessonProgramdaki TeacherId yi alip, Teacher tablosuna gidip,
        //ilgili teacherId ye ait username'i aliyoruz
    Set<LessonProgram> getLessonProgramByTeacherUsername(String username);
}

























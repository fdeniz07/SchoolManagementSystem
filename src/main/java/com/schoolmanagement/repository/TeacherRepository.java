package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TeacherRepository  extends JpaRepository<Teacher,Long> {

    boolean existsByUsername(String usernname);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    boolean existsByEmail(String email);

    Teacher findByUsernameEquals(String username);

    //@Query("select t from Teacher t where t.name like concat('%', ?1, '%')") //asagidaki hazir spring komutu(Containing) bu sorguyu yapiyor
    List<Teacher> getTeacherByNameContaining(String teacherName);

    Teacher getTeacherByUsername(String username);

    @Query(value = "SELECT t FROM Teacher t WHERE t.id IN :id") //LessonProgramService icin olusturuldu
    Set<Teacher> findByIdsEquals(List<Long> id);
}




























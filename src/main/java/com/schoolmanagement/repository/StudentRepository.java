package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.Student;
import com.schoolmanagement.payload.response.StudentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByUsername(String usernname);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    boolean existsByEmail(String email);

    Student findByUsernameEquals(String username);

    @Query(value = "SELECT (count(s)>0) FROM Student s")
        //Student tablosunda bir ögrenci var mi
    boolean findStudent();

    @Query(value = "SELECT MAX(s.studentNumber) FROM Student s")
        // Student tablosundaki en yüksek numarali numarayi aliyoruz
    int getLastStudentNumber();

    List<Student> getStudentByNameContaining(String studentName);

    Optional<Student> findByUsername(String username); //OrElseThrow icin null kontrolü yapmak icin Optional ekledik

    @Query(value = "SELECT s FROM Student s WHERE s.advisorTeacher.teacher.username =:username") //ilgili ögrencinin advisorTeacher tablosuna git,
    //oradaki teacherId ile teacher tablosuna git ve ilgili teacher'in username'ini getir diyerek 3 tablo arasinda join kurmus oluyoruz.--> JPQL ile
    //@Query(value="SELECT s FROM Student s JOIN s.advisorTeacher at JOIN at.teacher t WHERE t.username=:username") --> SQL dilinde yazimi
    List<Student> getStudentByAdvisorTeacher_Username(String username);
}


























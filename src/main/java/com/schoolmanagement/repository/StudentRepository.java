package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student,Long> {

    boolean existsByUsername(String usernname);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    boolean existsByEmail(String email);

    Student findByUsernameEquals(String username);

    @Query(value = "SELECT (count(s)>0) FROM Student s") //Student tablosunda bir ögrenci var mi
    boolean findStudent();

    @Query(value = "SELECT MAX(s.studentNumber) FROM Student s") // Student tablosundaki en yüksek numarali numarayi aliyoruz
    int getLastStudentNumber();
}














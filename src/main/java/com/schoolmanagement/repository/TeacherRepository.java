package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository  extends JpaRepository<Teacher,Long> {

    boolean existsByUsername(String usernname);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    Teacher findByUsernameEquals(String username);
}

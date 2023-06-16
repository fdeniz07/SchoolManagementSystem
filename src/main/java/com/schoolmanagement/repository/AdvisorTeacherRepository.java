package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.AdvisorTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvisorTeacherRepository extends JpaRepository<AdvisorTeacher, Long> {

    Optional<AdvisorTeacher> getAdvisorTeacherByTeacher_Id(Long advisorTeacherId);

    Optional<AdvisorTeacher> findByTeacher_UsernameEquals(String username);
}




































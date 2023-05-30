package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByUsername(String usernname);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    Admin findByUsernameEquals(String username);
}

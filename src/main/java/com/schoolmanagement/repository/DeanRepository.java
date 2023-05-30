package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.Dean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeanRepository extends JpaRepository<Dean,Long> {
    boolean existsByUsername(String usernname);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    Dean findByUsernameEquals(String username);
}

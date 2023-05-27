package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.GuestUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestUserRepository  extends JpaRepository<GuestUser,Long> {

    boolean existsByUsername(String usernname);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);
}

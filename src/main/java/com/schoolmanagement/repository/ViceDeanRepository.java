package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.ViceDean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViceDeanRepository extends JpaRepository<ViceDean,Long> {

    boolean existsByUsername(String usernname);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    ViceDean findByUsernameEquals(String username);
}

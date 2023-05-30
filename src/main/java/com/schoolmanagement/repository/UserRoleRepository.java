package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.UserRole;
import com.schoolmanagement.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    @Query("select r from UserRole r where r.roleType = ?1") //PHQL olarak  sorgu olusturuldu (?1 ilk parametreyi getir (roleType))
    Optional<UserRole> findByERoleEquals(RoleType roleType);


    @Query("select (count(r)>0) from UserRole r where r.roleType=?1")
    boolean existsByERoleEquals(RoleType roleType);

}

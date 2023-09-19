package com.privacydashboard.application.data.service;

import com.privacydashboard.application.data.entity.User;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByName(String username);

    User findByMail(String mail);

    @Modifying
    @Query("UPDATE User SET hashedPassword=:pass WHERE id=:id")
    @Transactional
    void changePasswordByUserID(@Param("id") UUID id, @Param("pass") String pass);

    @Modifying
    @Query("UPDATE User SET mail=:mail WHERE id=:id")
    @Transactional
    void changeMailByUserID(@Param("id") UUID id, @Param("mail") String mail);

    @Modifying
    @Query("UPDATE User SET name=:name, hashedPassword=:hashedPassword, mail=:mail WHERE id=:id")
    @Transactional
    void changeValues(@Param("id") UUID id, @Param("name") String name, @Param("hashedPassword") String hashedPassword, @Param("mail") String mail);
}
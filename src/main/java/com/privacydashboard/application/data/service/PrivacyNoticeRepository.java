package com.privacydashboard.application.data.service;

import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.PrivacyNotice;
import com.privacydashboard.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface PrivacyNoticeRepository extends JpaRepository<PrivacyNotice, UUID> {
    PrivacyNotice findByApp(IoTApp app);

    @Modifying
    @Query("UPDATE PrivacyNotice SET text=:newText WHERE id=:id")
    @Transactional
    void changeText(@Param("id") UUID id, @Param("newText") String newText);

    @Query("SELECT DISTINCT pn FROM PrivacyNotice pn WHERE " +
            "(pn.app in (SELECT uar.app FROM UserAppRelation uar WHERE uar.user=:user))")
    List<PrivacyNotice> getAllPrivacyNoticeFromUser(@Param("user") User user);

    @Query("SELECT DISTINCT pn FROM PrivacyNotice pn WHERE " +
            "(pn.app in (SELECT uar.app FROM UserAppRelation uar WHERE " +
            "(uar.user=:user AND LOWER(uar.app.name) like concat('%', LOWER(:name), '%')))) ")
    List<PrivacyNotice> getPrivacyNoticeFromUserFilterByAppName(@Param("user") User user, @Param("name") String name);
}

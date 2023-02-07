package com.privacydashboard.application.data.service;

import com.privacydashboard.application.data.entity.Notification;
import com.privacydashboard.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findAllByReceiver(User user);
    List<Notification> findAllByReceiverAndIsRead(User user, Boolean isRead);

    @Modifying
    @Query("UPDATE Notification SET isRead=:isRead WHERE id=:id")
    @Transactional
    void changeIsReadNotificationById(@Param("id") UUID id, @Param("isRead") Boolean isRead);
}

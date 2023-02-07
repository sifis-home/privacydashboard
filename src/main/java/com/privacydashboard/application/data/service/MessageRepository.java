package com.privacydashboard.application.data.service;

import com.privacydashboard.application.data.entity.Message;
import com.privacydashboard.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface MessageRepository  extends JpaRepository<Message, UUID> {
    @Query("SELECT m FROM Message m " +
            "WHERE (sender=:user1 AND receiver=:user2) OR (sender=:user2 AND receiver=:user1)" +
            "ORDER BY time")
    List<Message> getConversationFromUsers(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT DISTINCT u FROM User u WHERE u in " +
            "(SELECT m.receiver FROM Message m WHERE m.sender=:user) OR u in " +
            "(SELECT m.sender FROM Message m WHERE m.receiver=:user)")
    List<User> getUserConversationFromUser(@Param("user") User user);

    @Query("SELECT DISTINCT u FROM User u WHERE " +
            "(u in (SELECT m.receiver FROM Message m WHERE m.sender=:user) OR u in (SELECT m.sender FROM Message m WHERE m.receiver=:user)) " +
            " AND LOWER(u.name) like concat('%', LOWER(:name), '%')")
    List<User> getUserConversationFromUserFilterByName(@Param("user") User user, @Param("name") String name);
}

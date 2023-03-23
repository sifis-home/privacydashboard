package com.privacydashboard.application.data.service;

import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.entity.UserAppRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface UserAppRelationRepository extends JpaRepository<UserAppRelation, UUID> {

    @Query("SELECT uar.app from UserAppRelation uar WHERE uar.user=:user")
    List<IoTApp> getIoTAppsFromUser(@Param("user") User user);

    @Query("SELECT uar.app from UserAppRelation uar WHERE uar.user=:user AND LOWER(uar.app.name) like concat('%', LOWER(:filter), '%') ")
    List<IoTApp>getIoTAppsFromUserFilterByName(@Param("user") User user, @Param("filter") String filter);

    UserAppRelation findByUserAndApp(User user, IoTApp app);

    @Query("SELECT uar.user from UserAppRelation uar WHERE uar.app=:app AND uar.role=:role")
    List<User> getUsersFromAppFilterByRole(@Param("app") IoTApp app, @Param("role") Role role);

    @Query("SELECT uar.user from UserAppRelation uar WHERE uar.app=:app")
    List<User> getUsersFromApp(@Param("app") IoTApp app);

    @Query("SELECT DISTINCT i from IoTApp i WHERE " +
            "i in (SELECT app from UserAppRelation WHERE user=:user1) AND " +
            "i in (SELECT app from UserAppRelation WHERE user=:user2)")
    List<IoTApp> getAppsFrom2Users(@Param("user1") User user1, @Param("user2") User user2);

    // Prende i contatti per i Data Subject (gli altri data subject sono esclusi)
    @Query("SELECT DISTINCT uar.user from UserAppRelation uar WHERE " +
            "(uar.app in (SELECT app from UserAppRelation WHERE user=:user)) AND " +
            "(uar.role=:role1 OR uar.role=:role2) " +
            "ORDER BY uar.user.name")
    List<User> getAllContactsFilterBy2Roles(@Param("user") User user, @Param("role1") Role role1, @Param("role2") Role role2);

    // Prende i contatti per i Data Controller e DPO
    @Query("SELECT DISTINCT uar.user from UserAppRelation uar WHERE " +
            "(uar.app in (SELECT app from UserAppRelation WHERE user=:user)) AND (uar.user<>:user) " +
            "ORDER BY uar.user.name")
    List<User> getAllDPOOrControllerContacts(@Param("user") User user);

    // Prende i contatti per i Data Subject (gli altri data subject sono esclusi)
    @Query("SELECT DISTINCT uar.user from UserAppRelation uar WHERE " +
            "(uar.app in (SELECT app from UserAppRelation WHERE user=:user)) AND " +
            "(uar.role=:role1 OR uar.role=:role2) AND " +
            "LOWER(uar.user.name) like concat('%', LOWER(:name), '%')" +
            "ORDER BY uar.user.name")
    List<User> getAllContactsFilterBy2RolesFilterByName(@Param("user") User user, @Param("role1") Role role1, @Param("role2") Role role2, @Param("name") String name);

    // Prende i contatti per i Data Controller e DPO
    @Query("SELECT DISTINCT uar.user from UserAppRelation uar WHERE " +
            "(uar.app in (SELECT app from UserAppRelation WHERE user=:user)) AND (uar.user<>:user) AND " +
            "LOWER(uar.user.name) like concat('%', LOWER(:name), '%')" +
            "ORDER BY uar.user.name")
    List<User> getAllDPOOrControllerContactsFilterByName(@Param("user") User user, @Param("name") String name);

    @Modifying
    @Query("UPDATE UserAppRelation SET consenses=:consenses WHERE id=:id")
    @Transactional
    void updateConsenses(@Param("id") UUID id, @Param("consenses") String[] consenses);

}

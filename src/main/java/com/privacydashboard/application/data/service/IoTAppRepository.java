package com.privacydashboard.application.data.service;

import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.entity.IoTApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Hashtable;
import java.util.UUID;

public interface IoTAppRepository extends JpaRepository<IoTApp, UUID> {
    IoTApp findByName(String name);

    @Modifying
    @Query("UPDATE IoTApp SET name=:name, description=:description WHERE id=:id")
    @Transactional
    void changeNameAndDescription(@Param("id") UUID id, @Param("name") String name, @Param("description") String description);


    @Modifying
    @Query("UPDATE IoTApp SET questionnaireVote=:vote, detailVote=:detailVote, optionalAnswers=:optionalAnswers WHERE id=:id")
    @Transactional
    void changeQuestionnaire(@Param("id") UUID id, @Param("vote") QuestionnaireVote vote, @Param("detailVote") String[] detailVote, @Param("optionalAnswers") Hashtable<Integer, String> optionalAnswers);

}

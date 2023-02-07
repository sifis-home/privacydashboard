package com.privacydashboard.application.data.service;

import com.privacydashboard.application.data.entity.RightRequest;
import com.privacydashboard.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface RightRequestRepository extends JpaRepository<RightRequest, UUID> {
    List<RightRequest> findAllByReceiverOrderByTimeDesc(User user);
    List<RightRequest> findAllByReceiverAndHandledOrderByTimeDesc(User user, Boolean handled);
    List<RightRequest> findAllBySenderOrderByTimeDesc(User user);
    List<RightRequest> findAllBySenderAndHandledOrderByTimeDesc(User user, Boolean handled);

    @Modifying
    @Query("UPDATE RightRequest SET handled=:newHandled, response=:newResponse WHERE id=:id")
    @Transactional
    void changeRequest(@Param("id") UUID id, @Param("newHandled") Boolean newHandled, @Param("newResponse") String newResponse);

    @Modifying
    @Query("UPDATE RightRequest SET other=:other, details=:details WHERE id=:id")
    @Transactional
    void changeValuesOfRequest(@Param("id") UUID id, @Param("other") String other, @Param("details") String details);
}

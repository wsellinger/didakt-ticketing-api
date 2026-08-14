package com.didakt.ticketing.didaktticketingapi.repository;

import com.didakt.ticketing.didaktticketingapi.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.util.List;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
    //List<TicketType> findByEventId(Long eventId);
}

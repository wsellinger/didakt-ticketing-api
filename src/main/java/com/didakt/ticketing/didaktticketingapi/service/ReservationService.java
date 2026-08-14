package com.didakt.ticketing.didaktticketingapi.service;

import com.didakt.ticketing.didaktticketingapi.entity.TicketType;
import com.didakt.ticketing.didaktticketingapi.repository.TicketTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReservationService {

    private final TicketTypeRepository ticketTypeRepository;

    public ReservationService(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Transactional
    public TicketType reserveTickets(Long ticketTypeId, int quantity) {

        //Validate Quantity
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        //Get Ticket type
        TicketType ticketType = ticketTypeRepository.findById(ticketTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket type not found"));

        //Validate Availability
        if (ticketType.getAvailableQuantity() < quantity) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Not enough tickets available");
        }

        //Decrement Available Quantity
        ticketType.setAvailableQuantity(ticketType.getAvailableQuantity() - quantity);

        //Try Reserve
        try {
            return ticketTypeRepository.save(ticketType);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ticket type was updated concurrently, please retry");
        }
    }
}

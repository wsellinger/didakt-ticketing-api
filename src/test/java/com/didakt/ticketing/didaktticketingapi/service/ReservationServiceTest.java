package com.didakt.ticketing.didaktticketingapi.service;

import com.didakt.ticketing.didaktticketingapi.entity.TicketType;
import com.didakt.ticketing.didaktticketingapi.repository.TicketTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void reserveTickets_decrementsAvailableQuantity_whenEnoughAvailable() {
        //Arrange
        long id = 1L;
        int availableQuantity = 10;
        int reservedQuantity = 3;
        Integer expectedQuantity = availableQuantity - reservedQuantity;

        TicketType ticketType = new TicketType();
        ticketType.setId(id);
        ticketType.setAvailableQuantity(availableQuantity);

        when(ticketTypeRepository.findById(id)).thenReturn(Optional.of(ticketType));
        when(ticketTypeRepository.save(ticketType)).thenReturn(ticketType);

        //Act
        TicketType result = reservationService.reserveTickets(id, reservedQuantity);

        //Assert
        assertThat(result.getAvailableQuantity()).isEqualTo(expectedQuantity);
        verify(ticketTypeRepository).save(ticketType);
    }

    @Test
    void reserveTickets_throwsNotFound_whenNoTicketFound() {
        //Arrange
        long id = 1L;

        when(ticketTypeRepository.findById(id)).thenReturn(Optional.empty());

        //Act//Assert
        assertThatThrownBy(() -> reservationService.reserveTickets(id, 1))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(ex -> ((ResponseStatusException) ex).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void reserveTickets_throwsConflict_whenNotEnoughAvailable() {
        //Arrange
        long id = 1L;
        int availableQuantity = 2;
        int reservedQuantity = 3;

        TicketType ticketType = new TicketType();
        ticketType.setId(id);
        ticketType.setAvailableQuantity(availableQuantity);

        when(ticketTypeRepository.findById(id)).thenReturn(Optional.of(ticketType));

        //Act//Assert
        assertThatThrownBy(() -> reservationService.reserveTickets(id, reservedQuantity))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(ex -> ((ResponseStatusException) ex).getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void reserveTickets_throwsIllegalArgument_whenQuantityNotPositive() {
        //Arrange//Act//Assert
        assertThatThrownBy(() -> reservationService.reserveTickets(1L, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

package com.credibanco.cards.adapters.input.web;

import com.credibanco.cards.domain.exception.GeneralException;
import com.credibanco.cards.domain.usecase.CardService;
import com.credibanco.cards.model.dto.CardDto;
import com.credibanco.cards.model.dto.RechargeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNewCard() {
        CardDto request = new CardDto();
        doNothing().when(cardService).createNewCard(request);
        ResponseEntity<Void> response = cardController.createNewCard(request);
        assertEquals(200, response.getStatusCode().value());
        verify(cardService, times(1)).createNewCard(request);
    }

    @Test
    void assignNumberToCard() throws GeneralException {
        String productId = "123456";
        CardDto cardDto = new CardDto();
        when(cardService.assignNumberToCard(productId)).thenReturn(cardDto);
        ResponseEntity<CardDto> response = cardController.createNewCard(productId);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cardDto, response.getBody());
        verify(cardService, times(1)).assignNumberToCard(productId);
    }

    @Test
    void blockCard() throws GeneralException {
        String cardId = "1234567890123456";
        doNothing().when(cardService).blockCard(cardId);
        ResponseEntity<Void> response = cardController.blockCard(cardId);
        assertEquals(200, response.getStatusCode().value());
        verify(cardService, times(1)).blockCard(cardId);
    }

    @Test
    void rechargeCard() throws GeneralException {
        RechargeDto request = new RechargeDto();
        when(cardService.rechargeCard(request)).thenReturn(request);
        ResponseEntity<RechargeDto> response = cardController.rechargeCard(request);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(request, response.getBody());
        verify(cardService, times(1)).rechargeCard(request);
    }

    @Test
    void getCardBalance() throws GeneralException {
        String cardId = "1234567890123456";
        RechargeDto rechargeDto = new RechargeDto();
        when(cardService.getBalance(cardId)).thenReturn(rechargeDto);
        ResponseEntity<RechargeDto> response = cardController.getCardBalance(cardId);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(rechargeDto, response.getBody());
        verify(cardService, times(1)).getBalance(cardId);
    }


}
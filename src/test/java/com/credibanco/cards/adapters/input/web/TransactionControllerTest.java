package com.credibanco.cards.adapters.input.web;

import com.credibanco.cards.domain.exception.GeneralException;
import com.credibanco.cards.domain.usecase.TransactionService;
import com.credibanco.cards.model.dto.TransactionAnnulDto;
import com.credibanco.cards.model.dto.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void purchaseShouldReturnTransactionDto() throws GeneralException {
        TransactionDto request = new TransactionDto();
        when(transactionService.createTransaction(request)).thenReturn(request);
        ResponseEntity<TransactionDto> response = transactionController.purchase(request);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(request, response.getBody());
        verify(transactionService, times(1)).createTransaction(request);
    }

    @Test
    void annulationShouldReturnTransactionAnnulDto() throws GeneralException {
        TransactionAnnulDto request = new TransactionAnnulDto();
        when(transactionService.annulTransaction(request)).thenReturn(request);
        ResponseEntity<TransactionAnnulDto> response = transactionController.annulation(request);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(request, response.getBody());
        verify(transactionService, times(1)).annulTransaction(request);
    }
}
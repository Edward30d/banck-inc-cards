package com.credibanco.cards.domain.usecase;

import com.credibanco.cards.domain.exception.GeneralException;
import com.credibanco.cards.domain.port.CardPort;
import com.credibanco.cards.domain.port.RechargePort;
import com.credibanco.cards.domain.port.TransactionPort;
import com.credibanco.cards.model.cons.StateCardEnum;
import com.credibanco.cards.model.cons.StateTransaction;
import com.credibanco.cards.model.dto.TransactionAnnulDto;
import com.credibanco.cards.model.dto.TransactionDto;
import com.credibanco.cards.model.entity.Card;
import com.credibanco.cards.model.entity.Recharge;
import com.credibanco.cards.model.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @Mock
    private CardPort cardPort;

    @Mock
    private RechargePort rechargePort;

    @Mock
    private TransactionPort transactionPort;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransactionShouldThrowGeneralExceptionWhenCardNotFound() {
        TransactionDto request = new TransactionDto();
        request.setCardId("1234567890123456");
        when(cardPort.findByCardNumber(request.getCardId())).thenReturn(Optional.empty());
        assertThrows(GeneralException.class, () -> transactionService.createTransaction(request));
        verify(cardPort, times(1)).findByCardNumber(request.getCardId());
    }

    @Test
    void createTransactionShouldThrowGeneralExceptionWhenCardIsBlocked() {
        TransactionDto request = new TransactionDto();
        request.setCardId("1234567890123456");
        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setState(StateCardEnum.BLOCKED.getState());
        when(cardPort.findByCardNumber(request.getCardId())).thenReturn(Optional.of(card));
        assertThrows(GeneralException.class, () -> transactionService.createTransaction(request));
        verify(cardPort, times(1)).findByCardNumber(request.getCardId());
    }

    @Test
    void createTransactionShouldThrowGeneralExceptionWhenCardHasNoBalance() {
        TransactionDto request = new TransactionDto();
        request.setCardId("1234567890123456");
        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setExpirationDate(LocalDate.now().plusYears(1));
        card.setState(StateCardEnum.ACTIVE.getState());
        when(cardPort.findByCardNumber(request.getCardId())).thenReturn(Optional.of(card));
        when(rechargePort.findFirstByCardIdOrderByIdDesc(any())).thenReturn(Optional.empty());
        assertThrows(GeneralException.class, () -> transactionService.createTransaction(request));
        verify(cardPort, times(1)).findByCardNumber(request.getCardId());
        verify(rechargePort, times(1)).findFirstByCardIdOrderByIdDesc(any());
    }

    @Test
    void createTransactionShouldReturnTransactionDtoWhenCardHasSufficientBalance() {
        TransactionDto request = new TransactionDto();
        request.setCardId("1234567890123456");
        request.setPrice(new BigDecimal("50"));
        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setExpirationDate(LocalDate.now().plusYears(1));
        card.setState(StateCardEnum.ACTIVE.getState());
        when(cardPort.findByCardNumber(request.getCardId())).thenReturn(Optional.of(card));
        when(rechargePort.findFirstByCardIdOrderByIdDesc(any())).thenReturn(Optional.of(Recharge.builder()
                .newBalance(new BigDecimal("100"))
                .card(card)
                .build()));
        TransactionDto response = transactionService.createTransaction(request);
        assertEquals(request.getCardId(), response.getCardId());
        verify(cardPort, times(1)).findByCardNumber(request.getCardId());
        verify(rechargePort, times(1)).findFirstByCardIdOrderByIdDesc(any());
        verify(rechargePort, times(1)).save(any(Recharge.class));
        verify(transactionPort, times(1)).save(any());
    }

    @Test
    void createTransactionShouldThrowGeneralExceptionWhenCardIsExpired() {
        TransactionDto request = new TransactionDto();
        request.setCardId("1234567890123456");
        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setState(StateCardEnum.ACTIVE.getState());
        card.setExpirationDate(LocalDate.now().minusDays(1));
        when(cardPort.findByCardNumber(request.getCardId())).thenReturn(Optional.of(card));
        assertThrows(GeneralException.class, () -> transactionService.createTransaction(request));
        verify(cardPort, times(1)).findByCardNumber(request.getCardId());
    }

    @Test
    void createTransactionShouldThrowGeneralExceptionWhenInsufficientFunds() {
        TransactionDto request = new TransactionDto();
        request.setCardId("1234567890123456");
        request.setPrice(new BigDecimal("100"));
        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setExpirationDate(LocalDate.now().plusYears(1));
        card.setState(StateCardEnum.ACTIVE.getState());
        when(cardPort.findByCardNumber(request.getCardId())).thenReturn(Optional.of(card));
        when(rechargePort.findFirstByCardIdOrderByIdDesc(any())).thenReturn(Optional.of(Recharge.builder()
                .newBalance(new BigDecimal("50"))
                .card(card)
                .build()));
        assertThrows(GeneralException.class, () -> transactionService.createTransaction(request));
        verify(cardPort, times(1)).findByCardNumber(request.getCardId());
        verify(rechargePort, times(1)).findFirstByCardIdOrderByIdDesc(any());
    }

    @Test
    void annulTransactionShouldThrowGeneralExceptionWhenTransactionIdIsNull() {
        TransactionAnnulDto request = new TransactionAnnulDto();
        assertThrows(GeneralException.class, () -> transactionService.annulTransaction(request));
    }

    @Test
    void annulTransactionShouldThrowGeneralExceptionWhenTransactionNotFound() {
        TransactionAnnulDto request = new TransactionAnnulDto();
        request.setTransactionId(1);
        when(transactionPort.findById(request.getTransactionId())).thenReturn(Optional.empty());
        assertThrows(GeneralException.class, () -> transactionService.annulTransaction(request));
        verify(transactionPort, times(1)).findById(request.getTransactionId());
    }

    @Test
    void annulTransactionShouldThrowGeneralExceptionWhenTransactionCannotBeAnnulled() {
        TransactionAnnulDto request = new TransactionAnnulDto();
        request.setTransactionId(1);
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDateTime.now().minusDays(2));
        transaction.setState(StateTransaction.ACTIVE.getState());
        transaction.setAmount(new BigDecimal("50"));
        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setExpirationDate(LocalDate.now().plusYears(1));
        card.setState(StateCardEnum.ACTIVE.getState());
        transaction.setCard(card);
        when(cardPort.findByCardNumber(request.getCardId())).thenReturn(Optional.of(card));
        when(transactionPort.findById(request.getTransactionId())).thenReturn(Optional.of(transaction));
        assertThrows(GeneralException.class, () -> transactionService.annulTransaction(request));
        verify(transactionPort, times(1)).findById(request.getTransactionId());
    }

    @Test
    void annulTransactionShouldThrowGeneralExceptionWhenTransactionIsAlreadyAnnulled() {
        TransactionAnnulDto request = new TransactionAnnulDto();
        request.setTransactionId(1);
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDateTime.now().minusDays(2));
        transaction.setState(StateTransaction.ANNULLED.getState());
        when(transactionPort.findById(request.getTransactionId())).thenReturn(Optional.of(transaction));
        assertThrows(GeneralException.class, () -> transactionService.annulTransaction(request));
        verify(transactionPort, times(1)).findById(request.getTransactionId());
    }

    @Test
    void annulTransactionShouldReturnTransactionAnnulDtoWhenTransactionCanBeAnnulled() {
        TransactionAnnulDto request = new TransactionAnnulDto();
        request.setTransactionId(1);
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDateTime.now().minusHours(1));
        transaction.setState(StateTransaction.ACTIVE.getState());
        transaction.setAmount(new BigDecimal("50"));
        Card card = new Card();
        card.setId(1);
        transaction.setCard(card);
        when(transactionPort.findById(request.getTransactionId())).thenReturn(Optional.of(transaction));
        when(rechargePort.findFirstByCardIdOrderByIdDesc(any())).thenReturn(Optional.of(Recharge.builder()
                .newBalance(new BigDecimal("100"))
                .card(card)
                .build()));
        TransactionAnnulDto response = transactionService.annulTransaction(request);
        assertEquals(request.getTransactionId(), response.getTransactionId());
        verify(transactionPort, times(1)).findById(request.getTransactionId());
        verify(rechargePort, times(1)).findFirstByCardIdOrderByIdDesc(any());
        verify(rechargePort, times(1)).save(any(Recharge.class));
        verify(transactionPort, times(1)).save(any());
    }
}
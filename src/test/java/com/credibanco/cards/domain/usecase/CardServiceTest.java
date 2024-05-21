package com.credibanco.cards.domain.usecase;

import com.credibanco.cards.domain.exception.GeneralException;
import com.credibanco.cards.domain.port.CardPort;
import com.credibanco.cards.domain.port.RechargePort;
import com.credibanco.cards.model.cons.StateCardEnum;
import com.credibanco.cards.model.dto.CardDto;
import com.credibanco.cards.model.dto.RechargeDto;
import com.credibanco.cards.model.entity.Card;
import com.credibanco.cards.model.entity.Recharge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardServiceTest {

    @Mock
    private CardPort cardPort;

    @Mock
    private RechargePort rechargePort;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNewCardShouldSaveCard() {
        CardDto request = new CardDto();
        cardService.createNewCard(request);
        verify(cardPort, times(1)).save(any(Card.class));
    }

    @Test
    void assignNumberToCardShouldReturnCardDto() throws GeneralException {
        String productId = "123456";
        Card card = Card.builder()
                .id(1)
                .cardHolderName("John Doe")
                .productId(productId)
                .build();
        when(cardPort.findByProductId(productId)).thenReturn(Optional.of(card));
        when(cardPort.save(any())).thenReturn(card);
        CardDto response = cardService.assignNumberToCard(productId);
        assertEquals(productId, response.getProductId());
        verify(cardPort, times(1)).findByProductId(productId);
        verify(cardPort, times(1)).save(any(Card.class));
    }

    @Test
    void assignNumberToCardShouldThrowGeneralExceptionWhenCardNotFound() {
        String productId = "123456";
        when(cardPort.findByProductId(productId)).thenReturn(Optional.empty());
        assertThrows(GeneralException.class, () -> cardService.assignNumberToCard(productId));
        verify(cardPort, times(1)).findByProductId(productId);
    }

    @Test
    void assignNumberToCardShouldThrowGeneralExceptionWhenCardAlreadyHasNumber() throws GeneralException {
        String productId = "123456";
        Card card = new Card();
        card.setCardNumber("1234567890123456");
        when(cardPort.findByProductId(productId)).thenReturn(Optional.of(card));
        assertThrows(GeneralException.class, () -> cardService.assignNumberToCard(productId));
        verify(cardPort, times(1)).findByProductId(productId);
    }

    @Test
    void blockCardShouldBlockCard() throws GeneralException {
        String cardId = "1234567890123456";
        when(cardPort.findByCardNumber(cardId)).thenReturn(Optional.of(new Card()));
        cardService.blockCard(cardId);
        verify(cardPort, times(1)).findByCardNumber(cardId);
        verify(cardPort, times(1)).save(any(Card.class));
    }

    @Test
    void blockCardShouldThrowGeneralExceptionWhenCardNotFound() {
        String cardId = "1234567890123456";
        when(cardPort.findByCardNumber(cardId)).thenReturn(Optional.empty());
        assertThrows(GeneralException.class, () -> cardService.blockCard(cardId));
        verify(cardPort, times(1)).findByCardNumber(cardId);
    }



    @Test
    void rechargeCardShouldReturnRechargeDto() throws GeneralException {
        RechargeDto request = new RechargeDto();
        request.setCardId("1234567890123456");
        request.setBalance(new BigDecimal("100"));
        Card card = Card.builder()
                .id(1)
                .cardNumber("1234567890123456")
                .state("ACTIVE")
                .build();
        when(cardPort.findByCardNumber(request.getCardId())).thenReturn(Optional.of(card));
        RechargeDto response = cardService.rechargeCard(request);
        assertEquals(request.getCardId(), response.getCardId());
        verify(cardPort, times(1)).findByCardNumber(request.getCardId());
        verify(rechargePort, times(1)).save(any(Recharge.class));
    }

    @Test
    void rechargeCardShouldThrowGeneralExceptionWhenCardNotFound() {
        RechargeDto request = new RechargeDto();
        request.setCardId("1234567890123456");
        when(cardPort.findByCardNumber(request.getCardId())).thenReturn(Optional.empty());
        assertThrows(GeneralException.class, () -> cardService.rechargeCard(request));
        verify(cardPort, times(1)).findByCardNumber(request.getCardId());
    }

    @Test
    void getBalanceShouldReturnRechargeDto() throws GeneralException {
        String cardId = "1234567890123456";
        Card card = Card.builder()
                .id(1)
                .cardNumber("1234567890123456")
                .state("ACTIVE")
                .build();
        when(cardPort.findByCardNumber(cardId)).thenReturn(Optional.of(new Card()));
        when(rechargePort.findFirstByCardIdOrderByIdDesc(any())).thenReturn(Optional.of(Recharge.builder()
                .card(card).build()));
        RechargeDto response = cardService.getBalance(cardId);
        assertEquals(cardId, response.getCardId());
        verify(cardPort, times(1)).findByCardNumber(cardId);
        verify(rechargePort, times(1)).findFirstByCardIdOrderByIdDesc(any());
    }

    @Test
    void getBalanceShouldThrowGeneralExceptionWhenCardNotFound() {
        String cardId = "1234567890123456";
        when(cardPort.findByCardNumber(cardId)).thenReturn(Optional.empty());
        assertThrows(GeneralException.class, () -> cardService.getBalance(cardId));
        verify(cardPort, times(1)).findByCardNumber(cardId);
    }

    @Test
    void getBalanceShouldThrowGeneralExceptionWhenCardHasNoBalance() throws GeneralException {
        String cardId = "1234567890123456";
        Card card = new Card();
        card.setCardNumber("1234567890123456");
        when(cardPort.findByCardNumber(cardId)).thenReturn(Optional.of(card));
        when(rechargePort.findFirstByCardIdOrderByIdDesc(any())).thenReturn(Optional.empty());
        assertThrows(GeneralException.class, () -> cardService.getBalance(cardId));
        verify(cardPort, times(1)).findByCardNumber(cardId);
        verify(rechargePort, times(1)).findFirstByCardIdOrderByIdDesc(any());
    }

    @Test
    void rechargeCardShouldThrowGeneralExceptionWhenCardIsBlocked() throws GeneralException {
        RechargeDto request = new RechargeDto();
        request.setCardId("1234567890123456");
        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setState(StateCardEnum.BLOCKED.getState());
        when(cardPort.findByCardNumber(request.getCardId())).thenReturn(Optional.of(card));
        assertThrows(GeneralException.class, () -> cardService.rechargeCard(request));
        verify(cardPort, times(1)).findByCardNumber(request.getCardId());
    }

    @Test
    void rechargeCardShouldUpdateCurrentBalanceWhenLastRechargeIsPresent() throws GeneralException {
        RechargeDto request = new RechargeDto();
        request.setCardId("1234567890123456");
        request.setBalance(new BigDecimal("100"));
        Card card = Card.builder()
                .id(1)
                .cardNumber("1234567890123456")
                .state("ACTIVE")
                .build();
        when(cardPort.findByCardNumber(request.getCardId())).thenReturn(Optional.of(card));
        when(rechargePort.findFirstByCardIdOrderByIdDesc(any())).thenReturn(Optional.of(Recharge.builder()
                .newBalance(new BigDecimal("50"))
                .card(card)
                .build()));
        RechargeDto response = cardService.rechargeCard(request);
        assertEquals(request.getCardId(), response.getCardId());
        assertEquals(new BigDecimal("150"), response.getBalance());
        verify(cardPort, times(1)).findByCardNumber(request.getCardId());
        verify(rechargePort, times(1)).findFirstByCardIdOrderByIdDesc(any());
        verify(rechargePort, times(1)).save(any(Recharge.class));
    }

}
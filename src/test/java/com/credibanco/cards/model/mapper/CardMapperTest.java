package com.credibanco.cards.model.mapper;

import com.credibanco.cards.model.cons.StateCardEnum;
import com.credibanco.cards.model.dto.CardDto;
import com.credibanco.cards.model.entity.Card;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardMapperTest {

    @Test
    void toCardDtoShouldReturnCorrectCardDto() {
        Card card = Card.builder()
                .id(1)
                .cardNumber("1234567890123456")
                .productId("123456")
                .cardHolderName("John Doe")
                .expirationDate(LocalDate.of(2025, 12, 31))
                .creationDate(LocalDateTime.now())
                .state("ACTIVE")
                .build();

        CardDto cardDto = CardMapper.toCardDto(card);

        assertEquals(1, cardDto.getId());
        assertEquals("1234567890123456", cardDto.getCardNumber());
        assertEquals("123456", cardDto.getProductId());
        assertEquals("John Doe", cardDto.getCardHolderName());
        assertEquals(LocalDate.of(2025, 12, 31), cardDto.getExpirationDate());
        assertEquals(StateCardEnum.ACTIVE, cardDto.getState());
    }

    @Test
    void toCardShouldReturnCorrectCard() {
        CardDto cardDto = CardDto.builder()
                .id(1)
                .cardNumber("1234567890123456")
                .productId("123456")
                .cardHolderName("John Doe")
                .expirationDate(LocalDate.of(2025, 12, 31))
                .creationDate(LocalDateTime.now())
                .state(StateCardEnum.ACTIVE)
                .build();

        Card card = CardMapper.toCard(cardDto);

        assertEquals(1, card.getId());
        assertEquals("1234567890123456", card.getCardNumber());
        assertEquals("123456", card.getProductId());
        assertEquals("John Doe", card.getCardHolderName());
        assertEquals(LocalDate.of(2025, 12, 31), card.getExpirationDate());
        assertEquals("ACTIVE", card.getState());
    }
}
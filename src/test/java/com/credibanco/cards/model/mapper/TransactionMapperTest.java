package com.credibanco.cards.model.mapper;

import com.credibanco.cards.model.dto.TransactionDto;
import com.credibanco.cards.model.entity.Card;
import com.credibanco.cards.model.entity.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionMapperTest {

    @Test
    void toTransactionDtoShouldReturnCorrectTransactionDto() {
        Card card = new Card();
        card.setCardNumber("1234567890123456");

        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setAmount(new BigDecimal("100.0"));

        TransactionDto transactionDto = TransactionMapper.toTransactionDto(transaction);

        assertEquals("1234567890123456", transactionDto.getCardId());
        assertEquals(new BigDecimal("100.0"), transactionDto.getPrice());
    }
}
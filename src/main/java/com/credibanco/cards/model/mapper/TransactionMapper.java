package com.credibanco.cards.model.mapper;

import com.credibanco.cards.model.dto.TransactionDto;
import com.credibanco.cards.model.entity.Transaction;

public class TransactionMapper {

    public static TransactionDto toTransactionDto(Transaction transaction) {
        return TransactionDto.builder()
                .cardId(transaction.getCard().getCardNumber())
                .price(transaction.getAmount())
                .build();
    }

}

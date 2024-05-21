package com.credibanco.cards.model.mapper;

import com.credibanco.cards.model.cons.StateCardEnum;
import com.credibanco.cards.model.dto.CardDto;
import com.credibanco.cards.model.entity.Card;

public class CardMapper {

    public static CardDto toCardDto(Card card) {
        return CardDto.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .productId(card.getProductId())
                .cardHolderName(card.getCardHolderName())
                .expirationDate(card.getExpirationDate())
                .creationDate(card.getCreationDate())
                .state(StateCardEnum.valueOf(card.getState()))
                .build();
    }

    public static Card toCard(CardDto cardDto) {
        return Card.builder()
                .id(cardDto.getId())
                .cardNumber(cardDto.getCardNumber())
                .productId(cardDto.getProductId())
                .cardHolderName(cardDto.getCardHolderName())
                .expirationDate(cardDto.getExpirationDate())
                .creationDate(cardDto.getCreationDate())
                .state(cardDto.getState().getState())
                .build();
    }

}

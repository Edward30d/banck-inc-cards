package com.credibanco.cards.domain.port;

import com.credibanco.cards.model.entity.Card;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface CardPort {


    Optional<Card> findByCardNumber(String cardNumber);

    Optional<Card> findByProductId(String productId);
    @Transactional
    Card save(Card card);

}

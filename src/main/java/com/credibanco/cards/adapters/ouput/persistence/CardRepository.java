package com.credibanco.cards.adapters.ouput.persistence;

import com.credibanco.cards.domain.port.CardPort;
import com.credibanco.cards.model.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends CardPort, JpaRepository<Card, Integer> {

    Optional<Card> findByProductId(String productId);

    Optional<Card> findByCardNumber(String cardNumber);
}

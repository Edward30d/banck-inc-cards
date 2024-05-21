package com.credibanco.cards.domain.port;

import com.credibanco.cards.model.entity.Transaction;
import jakarta.transaction.Transactional;

import java.util.Optional;


public interface TransactionPort {

    @Transactional
    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Integer id);

}

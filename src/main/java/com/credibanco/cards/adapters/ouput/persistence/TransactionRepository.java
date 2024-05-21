package com.credibanco.cards.adapters.ouput.persistence;

import com.credibanco.cards.domain.port.TransactionPort;
import com.credibanco.cards.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends TransactionPort, JpaRepository<Transaction, Integer> {

    Optional<Transaction> findById(Integer id);

}

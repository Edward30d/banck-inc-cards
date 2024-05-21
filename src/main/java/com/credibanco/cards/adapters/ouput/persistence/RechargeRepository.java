package com.credibanco.cards.adapters.ouput.persistence;

import com.credibanco.cards.domain.port.RechargePort;
import com.credibanco.cards.model.entity.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RechargeRepository extends RechargePort, JpaRepository<Recharge, Integer> {

    Optional<Recharge> findFirstByCardIdOrderByIdDesc(Integer cardId);

}

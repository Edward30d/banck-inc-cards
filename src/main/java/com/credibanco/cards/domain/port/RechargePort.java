package com.credibanco.cards.domain.port;

import com.credibanco.cards.model.entity.Recharge;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface RechargePort {

    @Transactional
    Recharge save(Recharge recharge);

    Optional<Recharge> findFirstByCardIdOrderByIdDesc(Integer cardId);
}


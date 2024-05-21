package com.credibanco.cards.model.mapper;

import com.credibanco.cards.model.dto.RechargeDto;
import com.credibanco.cards.model.entity.Recharge;

public class RechargeMapper {

    public static RechargeDto toRechargeDto(Recharge recharge) {
        return RechargeDto.builder()
                .cardId(recharge.getCard().getCardNumber())
                .balance(recharge.getNewBalance())
                .build();
    }

}

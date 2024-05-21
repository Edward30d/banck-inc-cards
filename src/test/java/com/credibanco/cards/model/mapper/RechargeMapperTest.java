package com.credibanco.cards.model.mapper;

import com.credibanco.cards.model.dto.RechargeDto;
import com.credibanco.cards.model.entity.Card;
import com.credibanco.cards.model.entity.Recharge;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RechargeMapperTest {

    @Test
    void toRechargeDtoShouldReturnCorrectRechargeDto() {
        Card card = new Card();
        card.setCardNumber("1234567890123456");

        Recharge recharge = new Recharge();
        recharge.setCard(card);
        recharge.setNewBalance(new BigDecimal("100.0"));

        RechargeDto rechargeDto = RechargeMapper.toRechargeDto(recharge);

        assertEquals("1234567890123456", rechargeDto.getCardId());
        assertEquals(new BigDecimal("100.0"), rechargeDto.getBalance());
    }
}
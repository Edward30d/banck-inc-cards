package com.credibanco.cards.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "recharge")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "previous_balance", precision = 10, scale = 2)
    private BigDecimal previousBalance;

    @Column(name = "new_balance", precision = 10, scale = 2)
    private BigDecimal newBalance;

    @Column(name = "recharge_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate rechargeDate;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

}
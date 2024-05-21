package com.credibanco.cards.domain.usecase;

import com.credibanco.cards.domain.exception.GeneralException;
import com.credibanco.cards.domain.port.CardPort;
import com.credibanco.cards.domain.port.RechargePort;
import com.credibanco.cards.domain.port.TransactionPort;
import com.credibanco.cards.model.cons.StateCardEnum;
import com.credibanco.cards.model.cons.StateTransaction;
import com.credibanco.cards.model.dto.TransactionAnnulDto;
import com.credibanco.cards.model.dto.TransactionDto;
import com.credibanco.cards.model.entity.Card;
import com.credibanco.cards.model.entity.Recharge;
import com.credibanco.cards.model.entity.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionService {

    private final CardPort cardPort;
    private final RechargePort rechargePort;
    private final TransactionPort transactionPort;

    public TransactionService(CardPort cardPort
            , RechargePort rechargePort
            , TransactionPort transactionPort) {
        this.cardPort = cardPort;
        this.rechargePort = rechargePort;
        this.transactionPort = transactionPort;
    }

    @Transactional
    public TransactionDto createTransaction(TransactionDto request) {
        Optional<Card> result = cardPort.findByCardNumber(request.getCardId());
        if (result.isEmpty()) {
            throw GeneralException.builder()
                    .message("Card not found")
                    .status(404)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
        Card card = result.get();
        if (card.getState().equals(StateCardEnum.BLOCKED.getState())) {
            throw GeneralException.builder()
                    .message("Card is blocked")
                    .status(400)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if (card.getExpirationDate().isBefore(LocalDate.now())) {
            throw GeneralException.builder()
                    .message("Card is expired")
                    .status(400)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        BigDecimal currentBalance;
        Optional<Recharge> lastRecharge = rechargePort.findFirstByCardIdOrderByIdDesc(card.getId());
        if (lastRecharge.isEmpty()) {
            throw GeneralException.builder()
                    .message("Card has no balance")
                    .status(400)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        currentBalance = lastRecharge.get().getNewBalance();
        if (currentBalance.compareTo(request.getPrice()) < 0) {
            throw GeneralException.builder()
                    .message("Insufficient funds")
                    .status(400)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Recharge recharge = Recharge.builder()
                .newBalance(currentBalance.subtract(request.getPrice()))
                .previousBalance(currentBalance)
                .rechargeDate(LocalDate.now())
                .card(card)
                .build();
        rechargePort.save(recharge);
        transactionPort.save(Transaction.builder()
                .transactionDate(LocalDateTime.now())
                .amount(request.getPrice())
                .card(card)
                .state(StateTransaction.ACTIVE.getState())
                .build());
        return request;
    }

    @Transactional
    public TransactionAnnulDto annulTransaction(TransactionAnnulDto request) {
        if(Objects.isNull(request.getTransactionId())) {
            throw GeneralException.builder()
                    .message("Transaction id is required")
                    .status(400)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Optional<Transaction> result = transactionPort.findById(request.getTransactionId());
        if (result.isEmpty()) {
            throw GeneralException.builder()
                    .message("Transaction not found")
                    .status(404)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
        Transaction transaction = result.get();
        if(isLessThan24HoursAgo(transaction.getTransactionDate())) {
            throw GeneralException.builder()
                    .message("Transaction cannot be annulled")
                    .status(400)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if(transaction.getState().equals(StateTransaction.ANNULLED.getState())) {
            throw GeneralException.builder()
                    .message("Transaction is already annulled")
                    .status(400)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        BigDecimal currentBalance = BigDecimal.ZERO;
        transaction.setState(StateTransaction.ANNULLED.getState());
        Optional<Recharge> resultRecharge = rechargePort.findFirstByCardIdOrderByIdDesc(transaction.getCard().getId());
        if (resultRecharge.isPresent()) {
            currentBalance = resultRecharge.get().getNewBalance();
        }
        transactionPort.save(transaction);
        rechargePort.save(Recharge.builder()
                .card(transaction.getCard())
                .rechargeDate(LocalDate.now())
                .previousBalance(currentBalance)
                .newBalance(currentBalance.add(transaction.getAmount()))
                .build());
        return request;
    }


    public boolean isLessThan24HoursAgo(LocalDateTime dateTime) {
        LocalDateTime twentyFourHoursAgo = dateTime.plusHours(24);
        return !LocalDateTime.now().isBefore(twentyFourHoursAgo);
    }

}

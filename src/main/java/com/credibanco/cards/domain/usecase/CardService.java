package com.credibanco.cards.domain.usecase;

import com.credibanco.cards.domain.exception.GeneralException;
import com.credibanco.cards.domain.port.CardPort;
import com.credibanco.cards.domain.port.RechargePort;
import com.credibanco.cards.domain.port.TransactionPort;
import com.credibanco.cards.model.cons.StateCardEnum;
import com.credibanco.cards.model.dto.CardDto;
import com.credibanco.cards.model.dto.RechargeDto;
import com.credibanco.cards.model.dto.TransactionDto;
import com.credibanco.cards.model.entity.Card;
import com.credibanco.cards.model.entity.Recharge;
import com.credibanco.cards.model.entity.Transaction;
import com.credibanco.cards.model.mapper.CardMapper;
import com.credibanco.cards.model.mapper.RechargeMapper;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;
import java.util.Random;

@Service
public class CardService {
    private final CardPort cardPort;
    private final RechargePort rechargePort;
    private final TransactionPort transactionPort;

    public CardService(CardPort cardPort
            , RechargePort rechargePort
            , TransactionPort transactionPort) {
        this.cardPort = cardPort;
        this.rechargePort = rechargePort;
        this.transactionPort = transactionPort;
    }

    @Transactional
    public void createNewCard(CardDto request) {
        request.setCreationDate(LocalDateTime.now());
        request.setState(StateCardEnum.INACTIVE);
        LocalDate currentDate = LocalDate.now();
        LocalDate dateInThreeYears = currentDate.plusYears(3);
        request.setExpirationDate(YearMonth.from(dateInThreeYears).atDay(1));
        cardPort.save(CardMapper.toCard(request));
    }

    @Transactional
    public CardDto assignNumberToCard(String request) throws GeneralException {
        Optional<Card> result = cardPort.findByProductId(request);
        if (result.isEmpty()) {
            throw GeneralException.builder()
                    .message("Product ID not found")
                    .status(404)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
        Card card = result.get();
        if (Strings.isNotBlank(card.getCardNumber())) {
            throw GeneralException.builder()
                    .message("Card already has a number")
                    .status(400)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        card.setCardNumber(generateCardNumber(request));
        card.setState(StateCardEnum.ACTIVE.getState());
        rechargePort.save(Recharge.builder()
                .newBalance(BigDecimal.ZERO)
                .previousBalance(BigDecimal.ZERO)
                .rechargeDate(LocalDate.now())
                .card(card)
                .build());
        return CardMapper.toCardDto(cardPort.save(card));
    }

    @Transactional
    public void blockCard(String cardId) throws GeneralException {
        Optional<Card> result = cardPort.findByCardNumber(cardId);
        if (result.isEmpty()) {
            throw GeneralException.builder()
                    .message("Card not found")
                    .status(404)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
        Card card = result.get();
        card.setState(StateCardEnum.BLOCKED.getState());
        cardPort.save(card);
    }

    private String generateCardNumber(String productId) {
        Random random = new Random();
        int firstDigit = random.nextInt(9) + 1;
        int remainingDigits = random.nextInt(1_000_000_000);
        long complement = firstDigit * 1_000_000_000L + remainingDigits;
        return productId.concat(String.valueOf(complement));
    }

    @Transactional
    public RechargeDto rechargeCard(RechargeDto request) throws GeneralException {
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
        BigDecimal currentBalance = BigDecimal.ZERO;
        Optional<Recharge> lastRecharge = rechargePort.findFirstByCardIdOrderByIdDesc(card.getId());
        if (lastRecharge.isPresent()) {
            currentBalance = lastRecharge.get().getNewBalance();
        }
        Recharge recharge = Recharge.builder()
                .newBalance(currentBalance.add(request.getBalance()))
                .previousBalance(currentBalance)
                .rechargeDate(LocalDate.now())
                .card(card)
                .build();
        rechargePort.save(recharge);
        return RechargeMapper.toRechargeDto(recharge);
    }

    public RechargeDto getBalance(String cardId) throws GeneralException {
        Optional<Card> result = cardPort.findByCardNumber(cardId);
        if (result.isEmpty()) {
            throw GeneralException.builder()
                    .message("Card not found")
                    .status(404)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
        Card card = result.get();
        Optional<Recharge> lastRecharge = rechargePort.findFirstByCardIdOrderByIdDesc(card.getId());
        if (lastRecharge.isEmpty()) {
            throw GeneralException.builder()
                    .message("Card has no balance")
                    .status(400)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return RechargeMapper.toRechargeDto(lastRecharge.get());
    }

}

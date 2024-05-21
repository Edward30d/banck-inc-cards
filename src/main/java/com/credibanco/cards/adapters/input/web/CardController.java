package com.credibanco.cards.adapters.input.web;

import com.credibanco.cards.domain.exception.GeneralException;
import com.credibanco.cards.domain.usecase.CardService;
import com.credibanco.cards.model.dto.CardDto;
import com.credibanco.cards.model.dto.RechargeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/card")
    public ResponseEntity<Void> createNewCard(@RequestBody @Valid CardDto request) {
        cardService.createNewCard(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/card/{productId}/number")
    public ResponseEntity<CardDto> createNewCard(
            @PathVariable @Pattern(regexp = "\\d{6}", message = "productId must be a 6-digit number") String productId
    ) throws GeneralException {
        CardDto response = cardService.assignNumberToCard(productId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/card/{cardId}")
    public ResponseEntity<Void> blockCard(
            @PathVariable @Pattern(regexp = "\\d+", message = "cardId must be a numeric value") String cardId
    ) throws GeneralException {
        cardService.blockCard(cardId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/card/balance")
    public ResponseEntity<RechargeDto> rechargeCard(@RequestBody @Valid RechargeDto request)
            throws GeneralException {
        RechargeDto response = cardService.rechargeCard(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/card/balance/{cardId}")
    public ResponseEntity<RechargeDto> getCardBalance(
            @PathVariable @Pattern(regexp = "\\d{16}+", message = "cardId must be must be a 16-digit number") String cardId
    ) throws GeneralException {
        RechargeDto response = cardService.getBalance(cardId);
        return ResponseEntity.ok(response);
    }

}

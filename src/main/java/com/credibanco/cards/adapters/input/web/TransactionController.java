package com.credibanco.cards.adapters.input.web;

import com.credibanco.cards.domain.exception.GeneralException;
import com.credibanco.cards.domain.usecase.TransactionService;
import com.credibanco.cards.model.dto.TransactionAnnulDto;
import com.credibanco.cards.model.dto.TransactionDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction/purchase")
    public ResponseEntity<TransactionDto> purchase(@RequestBody @Valid TransactionDto request)
            throws GeneralException {
        TransactionDto response = transactionService.createTransaction(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transaction/anulation")
    public ResponseEntity<TransactionAnnulDto> annulation(@RequestBody @Valid TransactionAnnulDto request)
            throws GeneralException {
        TransactionAnnulDto response = transactionService.annulTransaction(request);
        return ResponseEntity.ok(response);
    }

}

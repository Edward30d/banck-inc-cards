package com.credibanco.cards.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAnnulDto {


    @NotBlank(message = "Card ID is required")
    @Pattern(regexp = "\\d{16}", message = "cardId must be a 16-digit numeric")
    private String cardId;

    @NotNull(message = "transactionId is required")
    @Min(value = 1, message = "transactionId must be greater than 0")
    private Integer transactionId;
}

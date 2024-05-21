package com.credibanco.cards.model.dto;

import com.credibanco.cards.model.cons.StateCardEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {

    private Integer id;
    private String cardNumber;

    @NotBlank(message = "Product ID is required")
    @Pattern(regexp = "\\d{6}", message = "Product ID must be a 6-digit number")
    private String productId;

    @NotBlank(message = "Card holder name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]{1,100}$", message = "Card holder name must be between 1 and 100 characters and can only contain letters and spaces")
    private String cardHolderName;
    private LocalDate expirationDate;
    private LocalDateTime creationDate;
    private StateCardEnum state;

}
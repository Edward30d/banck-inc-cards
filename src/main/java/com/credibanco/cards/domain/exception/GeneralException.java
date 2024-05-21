package com.credibanco.cards.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GeneralException extends RuntimeException {

    private String message;
    private int status;
    private HttpStatus httpStatus;

}

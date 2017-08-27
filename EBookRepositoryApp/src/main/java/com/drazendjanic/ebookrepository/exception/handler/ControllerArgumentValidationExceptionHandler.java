package com.drazendjanic.ebookrepository.exception.handler;

import com.drazendjanic.ebookrepository.dto.MessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerArgumentValidationExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<MessageDto> handleException(MethodArgumentNotValidException exception) {
        ResponseEntity<MessageDto> responseEntity = null;
        MessageDto messageDto = new MessageDto("Invalid request body");     // TODO: Enable returning appropriate message

        responseEntity = new ResponseEntity<MessageDto>(messageDto, HttpStatus.BAD_REQUEST);

        return responseEntity;
    }

}

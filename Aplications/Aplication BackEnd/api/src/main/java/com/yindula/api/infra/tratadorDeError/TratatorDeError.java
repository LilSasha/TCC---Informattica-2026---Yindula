package com.yindula.api.infra.tratadorDeError;

import com.yindula.api.infra.error.ErroRespostaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Esta anotação diz ao Spring para monitorizar todos os Controllers
@RestControllerAdvice
public class TratatorDeError {

    // Este metodo captura especificamente o RuntimeException que lançamos no Service
    @ExceptionHandler(RuntimeException.class)

    public ResponseEntity tratarErroRegraDeNegocio(RuntimeException ex) {

        // Criamos o nosso DTO com a mensagem que escreveste no throw new RuntimeException
        var erro = new ErroRespostaDTO("Algo Correu mal", ex.getMessage());

        return ResponseEntity.badRequest().body(erro);
    }

    // Este metodo captura erros de validação (campos @NotBlank vazios, etc)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {

        var erros = ex.getFieldErrors().stream()
                .map(erro -> new ErroRespostaDTO(erro.getField(), erro.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(erros);
    }
}


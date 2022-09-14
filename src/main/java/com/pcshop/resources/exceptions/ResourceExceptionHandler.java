package com.pcshop.resources.exceptions;

import com.pcshop.services.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest; //Contem as informações da requisição
import java.time.Instant;

//Essa classe é responsável por customizar a mensagem de erro lançada pelo categoryService utilizando como molde a Classe StandarError

@ControllerAdvice // Permite que essa classe intercept alguma exceção que acontecer lá na camada de Resource(controller) e trata a exceção
public class ResourceExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class) //Essa anotattion verifica se o erro lançado em qualquer rota é do tipo passado como argumento
    public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest request){

        //Instacia da Classe modelo que irá substituir o erro padrão
        StandardError err = new StandardError();

        //Passo os valores que quero para esse objeto
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value()); //Gera um cod 404 que converto com o .value() par apegar o npumero inteiro
        err.setError("Resource not found"); //Msg de erro
        err.setMessage(e.getMessage()); //Pego a msg definida no momento que instaciei o EntityNotFoundException no CategoryService (PASSO 2)
        err.setPath(request.getRequestURI()); //Pega o caminho da requisição feita

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);//Retorno o obj
    }
}

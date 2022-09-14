package com.pcshop.services.exceptions;


//Essa Classe contém a mensagem customizada de erro que passamos no Category Service
public class DataBaseException extends  RuntimeException  {
    private static final long serialVersionUID = 1L;

    public DataBaseException(String msg) {
        super(msg); //Repassa a msg para o construtor do RunTimeException
    }
}

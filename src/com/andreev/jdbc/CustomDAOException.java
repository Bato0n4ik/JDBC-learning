package com.andreev.jdbc;

public class CustomDAOException extends RuntimeException{

    public CustomDAOException(Throwable throwable){
        super(throwable);
    }
}

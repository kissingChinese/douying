package com.example.exception;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class AuthorityException extends Exception{

    private int code;

    private String msg;

    public AuthorityException(String msg){
        super(msg);
    }
}

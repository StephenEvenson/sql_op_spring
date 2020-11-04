package me.stephenj.sqlope.Exception;

public class TokenExpiredException extends Exception{
    public TokenExpiredException() {
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}

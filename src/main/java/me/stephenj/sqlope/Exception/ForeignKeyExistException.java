package me.stephenj.sqlope.Exception;

public class ForeignKeyExistException extends Exception{


    public ForeignKeyExistException() {
    }

    public ForeignKeyExistException(String message) {
        super(message);
    }

    public ForeignKeyExistException(String tbName, String dtName) {
        super(String.format("存在外键由`%s`表的`%s`字段", tbName, dtName));
    }

}

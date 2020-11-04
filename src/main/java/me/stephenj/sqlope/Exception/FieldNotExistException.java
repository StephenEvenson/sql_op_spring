package me.stephenj.sqlope.Exception;

/**
 * @ClassName FieldNotExistException.java
 * @Description
 * @author 张润天
 * @Time 2020/11/5 00:36
 * @Field :
 */
public class FieldNotExistException extends Exception{
    public FieldNotExistException() {
    }

    public FieldNotExistException(String message) {
        super(message);
    }
}

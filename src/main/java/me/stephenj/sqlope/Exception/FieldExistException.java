package me.stephenj.sqlope.Exception;

/**
 * @ClassName DataNotExistException.java
 * @Description
 * @author 张润天
 * @Time 2020/10/20 12:52
 * @Field :
 */
public class FieldExistException extends Exception{
    public FieldExistException() {
    }

    public FieldExistException(String message) {
        super(message);
    }
}

package me.stephenj.sqlope.Exception;

/**
 * @ClassName RowNotExistException.java
 * @Description
 * @author 张润天
 * @Time 2020/11/5 00:36
 * @Field :
 */
public class RowNotExistException extends Exception{
    public RowNotExistException() {
    }

    public RowNotExistException(String message) {
        super(message);
    }
}

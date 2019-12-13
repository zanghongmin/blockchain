package top.zanghongmin.blockchain.core;

/**
 * 自定义异常
 */
public class MyException extends RuntimeException {

    private String message;
    public MyException() {
    }
    public MyException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

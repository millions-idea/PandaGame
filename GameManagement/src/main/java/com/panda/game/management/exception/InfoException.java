/***
 * @pName proback
 * @name FinanceException
 * @user HongWei
 * @date 2018/8/5
 * @desc
 */
package com.panda.game.management.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/***
 * 信息异常类
 */
public class InfoException extends RuntimeException {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String msg;

    public Logger getLogger() {
        return logger;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * Constructs a new runtime exception with the specified cause and a
     * detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    public InfoException(Throwable cause, String msg) {
        super(cause);
        this.msg = msg;
        String causeStr = "empty";
        if(getCause() != null){
            causeStr = getCause().toString();
        }
        String body = "FinanceException-Msg-[" + msg + "]-Cause[" + causeStr + "]";
        logger.error(body);
        System.err.println("普通：捕捉到系统异常日志, 具体原因：" + body);
    }

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public InfoException(String msg) {
        this.msg = msg;
        String causeStr = "empty";
        if(getCause() != null){
            causeStr = getCause().toString();
        }
        String body = "FinanceException-Msg-[" + msg + "]-Cause[" + causeStr + "]";
        logger.error(body);
        System.err.println("普通：捕捉到系统异常日志, 具体原因：" + body);
    }

}

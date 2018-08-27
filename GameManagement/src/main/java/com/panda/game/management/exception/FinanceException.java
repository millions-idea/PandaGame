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
 * 财务模块专用异常类
 */
public class FinanceException extends RuntimeException {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Errors code;
    private String msg;

    public Logger getLogger() {
        return logger;
    }

    public Errors getCode() {
        return code;
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
    public FinanceException(Throwable cause, Errors code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
        String causeStr = "empty";
        if(getCause() != null){
            causeStr = getCause().toString();
        }
        String body = "FinanceException-Code[" + code.getCode() + "]-Msg-[" + msg + "]-Cause[" + causeStr + "]";
        logger.error(body);
        System.err.println("严重：捕捉到财务异常日志, 具体原因：" + body);
    }

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public FinanceException(Errors code, String msg) {
        this.code = code;
        this.msg = msg;
        String causeStr = "empty";
        if(getCause() != null){
            causeStr = getCause().toString();
        }
        String body = "FinanceException-Code[" + code.getCode() + "]-Msg-[" + msg + "]-Cause[" + causeStr + "]";
        logger.error(body);
        System.err.println("严重：捕捉到财务异常日志, 具体原因：" + body);
    }

    public enum Errors{
        /**
         * 签名错误
         */
        SIGN_ERROR("E0001"),
        /**
         * 生成流水失败
         */
        CREATE_TRANSACTION("E0002"),
        /**
         * 增加余额失败
         */
        WALLET_ADD_ERROR("E0003"),
        /**
         * 扣减余额失败
         */
        WALLET_REDUCE_ERROR("E0004"),
        /**
         * 资金变化更新失败
         */
        WALLET_BALANCE_LOG("E0005"),

        /**
         * 找不到用户
         */
        NOT_FOUND_USER("E0006");


        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        Errors(String code) {

            this.code = code;
        }
    }
}

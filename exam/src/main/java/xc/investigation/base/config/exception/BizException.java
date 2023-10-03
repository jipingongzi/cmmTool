package xc.investigation.base.config.exception;

/**
 * 业务异常
 * @author ibm
 */
public class BizException extends RuntimeException{
    public BizException(String message) {
        super(message);
    }
}

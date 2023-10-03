package xc.investigation.base.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xc.investigation.base.api.vo.ResponseVo;
import xc.investigation.base.constant.ApiStatusEnum;
import xc.investigation.base.constant.SystemStatusEnum;

/**
 * @author ibm
 */
@RestControllerAdvice
@Slf4j
public class XcExceptionHandler {

    @ExceptionHandler(SystemException.class)
    @ResponseBody
    public ResponseVo systemExceptionHandler(Exception e) {
        log.error(e.getMessage(),e);
        return ResponseVo.systemError(SystemStatusEnum.SYS_EXCEPTION,null,e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseVo exceptionHandler(Exception e) {
        log.error(e.getMessage(),e);
        return ResponseVo.apiError(ApiStatusEnum.BIZ_EXCEPTION,null,e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(),e);
        return new ResponseVo(ApiStatusEnum.BIZ_EXCEPTION.getValue(),SystemStatusEnum.NORMAL.getValue(),null,
                e.getBindingResult().getFieldError().getDefaultMessage());
    }
}

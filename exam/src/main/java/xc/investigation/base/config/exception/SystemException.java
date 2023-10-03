package xc.investigation.base.config.exception;

/**
 * 系统异常
 * @author ibm
 */
public class SystemException extends RuntimeException{
    public SystemException(String message) {
        super(message);
    }

    public static SystemException adminNotLogin(){
        return new SystemException("管理员未登录");
    }

    public static SystemException adminNotExist(){
        return new SystemException("管理员不存在");
    }

    public static SystemException userNotLogin(){
        return new SystemException("用户未登录");
    }

    public static SystemException userNotExist(){
        return new SystemException("用户不存在");
    }

    public static SystemException authFail(){return new SystemException("无权访问");}

}

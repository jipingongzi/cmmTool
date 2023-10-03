package xc.investigation.base.api.vo;

import lombok.Data;
import xc.investigation.base.constant.ApiStatusEnum;
import xc.investigation.base.constant.SystemStatusEnum;

/**
 * @author ibm
 */
@Data
public class ResponseVo<T> {
    /**
     * 接口状态返回值，正常为0
    */
    private Integer apiStatus;
    /**
     * 账号状态返回值，正常为0
     */
    private Integer sysStatus;
    /**
     * 接口返回对象值
     */
    private T data;

    /**
     * 接口返回提示信息
     */
    private String info;

    private long timestamp = System.currentTimeMillis();

    public ResponseVo(Integer apiStatus, Integer sysStatus, T data, String info) {
        this.apiStatus = apiStatus;
        this.sysStatus = sysStatus;
        this.data = data;
        this.info = info;
    }

    public static <T> ResponseVo<T> of(T data){
        return new ResponseVo<>(ApiStatusEnum.NORMAL.getValue(), SystemStatusEnum.NORMAL.getValue(),data,"");
    }

    public static <T> ResponseVo<T> of(T data,String info){
        return new ResponseVo<>(ApiStatusEnum.NORMAL.getValue(), SystemStatusEnum.NORMAL.getValue(),data,info);
    }

    public static <T> ResponseVo<T> apiError(ApiStatusEnum apiStatusEnum,T data,Exception ex){
        return new ResponseVo<>(apiStatusEnum.getValue(), SystemStatusEnum.NORMAL.getValue(),data,ex.getMessage());
    }

    public static <T> ResponseVo<T> systemError(SystemStatusEnum systemStatusEnum,T data,Exception ex){
        return new ResponseVo<>(ApiStatusEnum.NORMAL.getValue(), systemStatusEnum.getValue(),data,ex.getMessage());
    }
}

package com.dogbody.spring.framework.web.common.response;

import com.dogbody.spring.framework.web.common.util.TraceIdUtil;
import lombok.Data;


/**
 * @author zhangdd on 2023/4/12
 */
@Data
public class ResponseModule<T> {


    /**
     * 请求成功失败，状态码
     */
    private Integer code;

    /**
     * 成功/失败 信息
     */
    private String message;


    private String traceId = TraceIdUtil.get();

    /**
     * 数据
     */
    private T data;

    /**
     * 成功
     */
    private static final int SUCCESS = 200;

    /**
     * 服务器错误，空指针、数组越界等非业务代码抛出异常
     */
    private static final int SERVER_ERROR = -1;

    public ResponseModule() {

    }

    public ResponseModule(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.traceId = null;
        this.data = data;
    }

    public static <T> ResponseModule<T> success(T data) {
        ResponseModule<T> response = new ResponseModule<>();
        response.setCode(SUCCESS);
        response.setData(data);
        response.setMessage(null);
        return response;
    }

    public static <T> ResponseModule<T> fail() {
        return fail(SERVER_ERROR, null);
    }

    public static <T> ResponseModule<T> fail(String message) {
        return fail(SERVER_ERROR, message);
    }
//    public static <T> ResponseModule<T> fail(BaseException exception) {
//        return fail(exception.getStatus(), exception.getMessage());
//    }

    public static <T> ResponseModule<T> fail(Integer status, String message) {
        ResponseModule<T> response = new ResponseModule<>();
        response.setCode(status);
        response.setData(null);
        response.setMessage(message);
        return response;
    }
}

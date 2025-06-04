package cn.wanfeng.sp.interceptor;


import cn.wanfeng.sp.exception.SimpleExceptionCode;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.model.ResponseEntity;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * SimpleGlobalExceptionHandler: 全局异常处理器 .
 *
 * @date: 2025-05-01 23:54
 * @author: luozh.wanfeng
 */
@RestControllerAdvice(basePackages = "cn.wanfeng.sp.web.controller")
public class GlobalExceptionHandler implements Ordered {

    /**
     * 异常拦截处理
     * @param exception 异常
     * @return 响应体
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity handleException(Exception exception){
        if(exception instanceof SpException spException){
            String finalMessage = handleMessageFormat(spException.getMessage(), spException.getArgs());
            return ResponseEntity.fail(spException.getCode(), finalMessage);
        }
        return ResponseEntity.fail(SimpleExceptionCode.UNKNOWN_EXCEPTION.getCode(), exception.getMessage());
    }

    /**
     * 异常信息格式化
     * @param message 信息
     * @param args 填充参数
     * @return 处理后的信息
     */
    private String handleMessageFormat(String message, Object... args) {
        return String.format(message, args);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}

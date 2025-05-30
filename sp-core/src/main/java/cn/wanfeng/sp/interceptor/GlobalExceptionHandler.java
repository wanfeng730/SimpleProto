package cn.wanfeng.sp.interceptor;


import cn.wanfeng.sp.exception.SimpleExceptionCode;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.model.ResponseEntity;
import jakarta.annotation.Resource;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * SimpleGlobalExceptionHandler: 全局异常处理器 .
 *
 * @date: 2025-05-01 23:54
 * @author: luozh.wanfeng
 */
@RestControllerAdvice
public class GlobalExceptionHandler implements Ordered {

    @Resource
    private MessageSource messageSource;

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
     * 处理国际化信息
     * @param message 信息
     * @param args 填充参数
     * @return 处理后的信息
     */
    private String handleMessageFormat(String message, Object... args) {
        return String.format(message, args);

        // 处理国际化信息方式 使用i18n
        // try {
        //     String finalMessage = messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
        //     if(ArrayUtils.isNotEmpty(args)){
        //         finalMessage = String.format(finalMessage, args);
        //     }
        //     return finalMessage;
        // } catch (NoSuchMessageException e) {
        //     LogUtil.error("No Message For Exception Code: {}. Please Check file messages.properties", message);
        //     return message;
        // }
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}

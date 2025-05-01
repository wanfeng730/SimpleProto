package cn.wanfeng.sp.exception;

import lombok.Getter;

/**
 * ExceptionCode: 异常编码.
 *
 * @date: 2025-05-01 13:50
 * @author: luozh.wanfeng
 */
@Getter
public enum ExceptionCode {


    /**
     * 测试
     */
    TEST("00000", "TEST_MESSAGE_CODE"),
    /**
     * 未知异常，请根据日志排查原因
     */
    UNKNOWN_EXCEPTION("99999", "UNKNOWN_EXCEPTION");

    private final String code;

    private final String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

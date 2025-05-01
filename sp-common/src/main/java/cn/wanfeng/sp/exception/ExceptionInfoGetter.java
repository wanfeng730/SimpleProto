package cn.wanfeng.sp.exception;

/**
 * ExceptionCode: 异常信息获取接口，用于构造异常.
 *
 * @date: 2025-05-02 00:25
 * @author: luozh.wanfeng
 */
public interface ExceptionInfoGetter {

    String getCode();

    String getMessage();

}

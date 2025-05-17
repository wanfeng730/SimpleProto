package cn.wanfeng.sp.model;


import cn.wanfeng.sp.util.DateUtils;

/**
 * ResponseEntity: 通用接口响应体实体类.
 *
 * @date: 2025-05-02 00:01
 * @author: luozh.wanfeng
 */
public class ResponseEntity {
    /**
     * 响应码
     */
    private String code;
    /**
     * 响应信息
     */
    private String message;
    /**
     * 响应时间
     */
    private String time;
    /**
     * 响应数据
     */
    private Object data;


    public ResponseEntity() {
    }

    /**
     * 成功响应体
     * @param data 响应数据
     * @return 响应体
     */
    public static ResponseEntity success(Object data){
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setCode("200");
        responseEntity.setMessage("成功");
        responseEntity.setData(data);
        responseEntity.setTime(DateUtils.currentDateTimeMillis());
        return responseEntity;
    }

    /**
     * 失败响应体
     * @param code 响应码
     * @param message 响应信息
     * @return 响应体
     */
    public static ResponseEntity fail(String code, String message){
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setCode(code);
        responseEntity.setMessage(message);
        responseEntity.setData(null);
        responseEntity.setTime(DateUtils.currentDateTimeMillis());
        return responseEntity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

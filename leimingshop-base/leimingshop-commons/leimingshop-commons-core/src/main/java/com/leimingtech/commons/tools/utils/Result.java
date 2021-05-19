/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils;

import com.leimingtech.commons.tools.exception.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应数据
 *
 * @since 1.0.0
 */
@Data
@ApiModel(description = "Result")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 编码：0表示成功，其他值表示失败
     */
    @ApiModelProperty(value = "编码：200表示成功，其他值表示失败")
    private int code = 200;
    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String msg = "success";

    /**
     * 编码：0表示成功，其他值表示失败
     */
    @ApiModelProperty(value = "编码：200表示成功，其他值表示失败")
    private String errorCode;
    /**
     * 响应数据
     */
    @ApiModelProperty(value = "响应数据")
    private T data;

    public Result() {
    }

    public Result<T> ok(T data) {
        this.setData(data);
        return this;
    }

    public Result<T> ok(T data, String message) {
        this.setData(data);
        this.setMsg(message);
        return this;
    }

    public boolean success() {
        return code == 200 ? true : false;
    }

    public Result<T> error() {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public Result<T> error(int code) {
        this.code = code;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public Result<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }


    public Result<T> error(String errorCode, String msg) {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
        this.errorCode = errorCode;
        return this;
    }

    public Result<T> error(int code, String errorCode, String msg) {
        this.code = code;
        this.msg = msg;
        this.errorCode = errorCode;
        return this;
    }


    public Result<T> error(String msg) {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
        return this;
    }
}

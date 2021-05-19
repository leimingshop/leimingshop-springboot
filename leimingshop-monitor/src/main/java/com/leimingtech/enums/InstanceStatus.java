/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enums;

import lombok.Getter;

@Getter
public enum InstanceStatus {

    /**
     * 健康检查没通过
     **/
    DOWN("DOWN", "健康检查不通过"),

    /**
     * 已离线
     **/
    OFFLINE("OFFLINE", "已离线"),

    /**
     * 已上线
     **/
    UP("UP", "已上线"),

    /**
     * 未知状态
     **/
    UNKNOWN("UNKNOWN", "未知");

    private String code;
    private String value;

    InstanceStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }
}

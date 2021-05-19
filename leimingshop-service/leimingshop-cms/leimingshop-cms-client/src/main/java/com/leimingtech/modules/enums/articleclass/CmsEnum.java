/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.articleclass;

/**
 * @Author: yuhaifeng
 * @Description:cmsCode枚举
 * @Date: 2020/03/24 11:46
 * @Version: V1.0
 */
public enum CmsEnum {

    //圈子类型标志
    QZCODE(1),

    //资讯类型标志
    ZXCODE(2),

    //服务指南类型标志
    FWZNCODE(3),

    //文章帖
    wzCOODE(4);

    private Integer value;

    CmsEnum(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }

}

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.complain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单投诉表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-09
 */
@Data
public class OrderComplainExcel {
    @ExcelProperty(value = "主键ID")
    private Long id;
    @ExcelProperty(value = "订单ID")
    private Long orderId;
    @ExcelProperty(value = "订单编号")
    private Long orderSn;
    @ExcelProperty(value = "订单金额")
    private BigDecimal orderAmount;

    @ExcelProperty(value = "会员账号")
    private String account;
    @ExcelProperty(value = "手机号")
    private String phone;

    @ExcelProperty(value = "店铺名称")
    private String storeName;
    @ExcelProperty(value = "店铺账号")
    private String storeAccount;

    @ExcelProperty(value = "投诉类型1:服务相关,2:物流相关,3:售后相关,4:商品相关5:其他")
    private String type;
    @ExcelProperty(value = "投诉来源 0:pc,1:h5,2:android,3:ios,4:微信,5:小程序 ")
    private String source;
    @ExcelProperty(value = "投诉状态 10：待处理 20：已处理")
    private String status;
    @ExcelProperty(value = "处理人")
    private String disposePerson;
    @ExcelProperty(value = "处理时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date disposeDate;
    @ExcelProperty(value = "投诉判定 1：有效投诉，2：重点问题，3：无效投诉")
    private String verdict;
    @ExcelProperty(value = "投诉时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ExcelProperty(value = "满意度")
    private Integer customerSatisfaction;

    public void setType(Integer type) {
        if (type == null) {
            this.type = "";
            return;
        }
        if (type == 1) {
            this.type = "服务相关";
        } else if (type == 2) {
            this.type = "物流相关";
        } else if (type == 3) {
            this.type = "售后相关";
        } else if (type == 4) {
            this.type = "商品相关";
        } else if (type == 5) {
            this.type = "其他";
        }

    }

    public void setSource(Integer source) {
        //0:pc,1:h5,2:android,3:ios,4:微信,5:小程序
        if (source == null) {
            this.source = "";
            return;
        }
        if (source == 0) {
            this.source = "pc";
        } else if (source == 1) {
            this.source = "h5";
        } else if (source == 2) {
            this.source = "android";
        } else if (source == 3) {
            this.source = "ios";
        } else if (source == 4) {
            this.source = "微信";
        } else if (source == 5) {
            this.source = "小程序";
        }
    }

    public void setStatus(Integer status) {
        //投诉状态 10：待处理 20：已处理

        if (status == null) {
            this.status = "";
            return;
        }
        if (status == 10) {
            this.status = "待处理";
        } else if (status == 20) {
            this.status = "已处理";
        }

    }

    public void setVerdict(Integer verdict) {
        //投诉判定 1：有效投诉，2：重点问题，3：无效投诉
        if (verdict == null) {
            this.verdict = "";
            return;
        }
        if (verdict == 1) {
            this.verdict = "有效投诉";
        } else if (verdict == 2) {
            this.verdict = "重点问题";
        } else if (verdict == 3) {
            this.verdict = "无效投诉";
        }

    }
}

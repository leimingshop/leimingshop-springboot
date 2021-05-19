/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.excel.feedback;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 意见反馈信息表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-13
 */
@Data
public class FeedbackExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "会员账号")
    private String memberName;
    @ExcelProperty(value = "用户ID")
    private Long memberId;
    @ExcelProperty(value = "手机号码")
    private String mobileNumber;
    @ExcelProperty(value = "反馈类型（默认1：功能异常、2：优化建议、3：其他反馈）")
    private String feedbackType;
    @ExcelProperty(value = "反馈来源（默认0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）")
    private String feedbackSource;
    @ExcelProperty(value = "反馈判定（0:无效反馈、1:有效反馈、2:重点问题、)")
    private String decideType;
    @ExcelProperty(value = "处理状态(默认0:未处理、1：已处理)")
    private String disposeStatus;
    @ExcelProperty(value = "处理时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date disposeDate;
    @ExcelProperty(value = "反馈时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ExcelProperty(value = "处理人")
    private String operator;


    public void setFeedbackType(Integer feedbackType) {

        if (feedbackType == null) {
            this.feedbackType = "";
            return;
        }
        //1：功能异常、2：优化建议、3：其他反馈
        if (feedbackType == 1) {
            this.feedbackType = "功能异常";
        } else if (feedbackType == 2) {
            this.feedbackType = "优化建议";
        } else if (feedbackType == 3) {
            this.feedbackType = "其他反馈";
        }

    }

    public void setFeedbackSource(Integer feedbackSource) {

        // 0:pc,1:h5,2:android,3:ios,4:微信,5:小程序

        if (feedbackSource == null) {
            this.feedbackSource = "";
            return;
        }
        if (feedbackSource == 0) {
            this.feedbackSource = "PC";
        } else if (feedbackSource == 1) {
            this.feedbackSource = "H5";
        } else if (feedbackSource == 2) {
            this.feedbackSource = "android";
        } else if (feedbackSource == 3) {
            this.feedbackSource = "ios";
        } else if (feedbackSource == 4) {
            this.feedbackSource = "微信";
        } else if (feedbackSource == 5) {
            this.feedbackSource = "小程序";
        }
    }

    public void setDecideType(Integer decideType) {
        //0:无效反馈、1:有效反馈、2:重点问题
        if (decideType == null) {
            this.decideType = "";
            return;
        }
        if (decideType == 0) {
            this.decideType = "无效反馈";
        } else if (decideType == 1) {
            this.decideType = "有效反馈";
        } else if (decideType == 2) {
            this.decideType = "重点问题";
        }
    }

    public void setDisposeStatus(Integer disposeStatus) {
        if (disposeStatus == 0) {
            this.disposeStatus = "未处理";
        } else if (disposeStatus == 1) {
            this.disposeStatus = "已处理";
        }
    }
}

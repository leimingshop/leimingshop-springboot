/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.report;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hecheng 347861695@qq.com
 * @since v1.0.0 2020-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_cms_report")
public class CmsReportEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long reportCreaterId;
    /**
     * 举报信息
     */
    private String reportContent;
    /**
     * 举报区分id(文章id)
     */
    private Long reportFlagId;
    /**
     * 举报区分标识（1：文章；）
     */
    private Integer reportFlag;
    /**
     * 举报处理状态(1：待处理，2：通过，3未通过)
     */
    private Integer reportStatus;
    /**
     * 举报结果
     */
    private String reportResult;
}
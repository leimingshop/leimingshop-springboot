/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.updatelog;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 版本更新日志
 *
 * @author huangjianeng huangjianeng@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_update_log")
public class UpdateLogEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 更新日志标题
     */
    private String logTitle;
    /**
     * 版本号
     */
    private String logVersion;
    /**
     * 更新日志类型（0优化迭代；1新增功能）
     */
    private Integer logType;
    /**
     * 日志更新内容
     */
    private String logDec;
    /**
     * 发版日期
     */
    private Date saveDate;
}
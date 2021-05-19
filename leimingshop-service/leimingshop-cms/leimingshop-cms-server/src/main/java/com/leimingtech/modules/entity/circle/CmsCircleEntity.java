/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.circle;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 圈子话题管理 CmsCircleEntity
 *
 * @author pixiaoyong@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_cms_circle")
public class CmsCircleEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 话题名称
     */
    private String topicName;
    /**
     * 所属分类ID
     */
    private Long acId;
    /**
     * 话题创建人ID
     */
    private Long createrId;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 话题修改人ID
     */
    private Long updateId;
    /**
     * 话题描述
     */
    private String topicAbstract;
    /**
     * 话题logo
     */
    private String topicLogo;
    /**
     * 话题底图
     */
    private String topicPicture;
    /**
     * 文字统计数
     */
    private Integer textNum;
    /**
     * 图说统计数
     */
    private Integer pictureNum;
    /**
     * 视频统计数
     */
    private Integer videoNum;
    /**
     * 文章统计数
     */
    private Integer articleNum;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 超话标识（0：普话（默认），1：超话）
     */
    private Integer superTopicFlag;
    /**
     * 状态标识（0：停用 1：启用（默认））
     */
    private Integer status;
}

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.articleclass;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 分类管理 CmsClassEntity
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_cms_class")
public class CmsClassEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 分类名
     */
    private String acName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 父级ID
     */
    private Long acParentId;
    /**
     * 分类层级
     */
    private Integer acLevel;
    /**
     * 分类idpaths
     */
    private String acIdpaths;
    /**
     * 图片URL
     */
    private String imageUrl;
    /**
     * 分类icon
     */
    private String acIcon;
    /**
     * 关注数
     */
    private Integer attentionNum;
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
     * 状态标识（0：停用 1：启用（默认））
     */
    private Integer status;
    /**
     * 分类区分标识（1：圈子2：资讯3：服务指南）
     */
    private Integer acCode;

}

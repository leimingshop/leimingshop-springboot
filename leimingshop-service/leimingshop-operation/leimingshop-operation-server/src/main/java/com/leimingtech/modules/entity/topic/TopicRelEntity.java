/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.topic;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 专题页商品关联表
 *
 * @author kuangweiguo kuangweiguo
 * @since v1.0.0 2020-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_topic_rel")
public class TopicRelEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 专题页id
     */
    private Long topicId;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 排序 （0 到255 数字越小越靠前）
     */
    private Integer sort;
}
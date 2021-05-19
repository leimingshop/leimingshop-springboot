/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.topic;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 专题页
 *
 * @author kuangweiguo kuangweiguo
 * @since v1.0.0 2020-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_topic")
public class TopicEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 专题页名称
     */
    private String topicName;
    /**
     * pc专题页图片
     */
    private String topicPicturePc;
    /**
     * h5专题页图片
     */
    private String topicPictureH5;
}
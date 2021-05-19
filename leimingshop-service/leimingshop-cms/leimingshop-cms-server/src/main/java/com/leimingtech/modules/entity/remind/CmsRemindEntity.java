/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.remind;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hecheng 347861695@qq.com
 * @since v1.0.0 2020-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_cms_remind")
public class CmsRemindEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * @人的id
     */
    private Long creatorId;
    /**
     * 被@人id
     */
    private Long memberId;
    /**
     * @类型(1图文2评论)
     */
    private Integer remindType;
    /**
     * 图文id,评论id
     */
    private Long remindTypeId;

    /**
     * 阅读标识（默认为0未读，1已读）
     */
    private Integer readFlag;
}